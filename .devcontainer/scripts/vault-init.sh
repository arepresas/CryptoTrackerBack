#!/bin/sh
set -eu

INIT_FILE="/vault/init/init.json"
APP_TOKEN_FILE="/vault/token/app-token"

echo "[vault-init] Waiting for Vault API..."
until vault status >/dev/null 2>&1; do
  sleep 1
done

if [ ! -f "$INIT_FILE" ]; then
  echo "[vault-init] Initializing Vault..."
  vault operator init -key-shares=1 -key-threshold=1 -format=json > "$INIT_FILE"
fi

UNSEAL_KEY="$(sed -n 's/.*"unseal_keys_b64"[[:space:]]*:[[:space:]]*\[[[:space:]]*"\([^"]*\)".*/\1/p' "$INIT_FILE" | head -n 1)"
ROOT_TOKEN="$(sed -n 's/.*"root_token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p' "$INIT_FILE" | head -n 1)"

if [ -z "$UNSEAL_KEY" ] || [ -z "$ROOT_TOKEN" ]; then
  echo "[vault-init] Could not extract unseal key or root token"
  exit 1
fi

if vault status | grep -q "Sealed.*true"; then
  echo "[vault-init] Unsealing Vault..."
  vault operator unseal "$UNSEAL_KEY" >/dev/null
fi

export VAULT_TOKEN="$ROOT_TOKEN"

echo "[vault-init] Ensuring KV v2 backend at secret/ ..."
vault secrets list -format=json | grep -q '"secret/"' || vault secrets enable -path=secret kv-v2 >/dev/null

echo "[vault-init] Writing application policy..."
cat >/tmp/crypto-tracker-app.hcl <<'EOF'
path "secret/data/crypto-tracker" {
  capabilities = ["read"]
}

path "secret/metadata/crypto-tracker" {
  capabilities = ["read"]
}
EOF

vault policy write crypto-tracker-app /tmp/crypto-tracker-app.hcl >/dev/null

if [ -z "${CRYPTO_API_KEY:-}" ]; then
  echo "[vault-init] CRYPTO_API_KEY is empty"
  exit 1
fi

echo "[vault-init] Seeding secret/data/crypto-tracker..."
vault kv put secret/crypto-tracker \
  crypto-api.url="${CRYPTO_API_URL}" \
  crypto-api.key="${CRYPTO_API_KEY}" >/dev/null

echo "[vault-init] Creating app token..."
APP_TOKEN="$(vault token create -policy=crypto-tracker-app -orphan -ttl=72h -format=json | sed -n 's/.*"client_token"[[:space:]]*:[[:space:]]*"\([^"]*\)".*/\1/p' | head -n 1)"

if [ -z "$APP_TOKEN" ]; then
  echo "[vault-init] Could not create app token"
  exit 1
fi

printf "%s" "$APP_TOKEN" > "$APP_TOKEN_FILE"

echo "[vault-init] Vault bootstrap completed"
