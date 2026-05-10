#!/usr/bin/env bash
set -euo pipefail

TOKEN_FILE="/tmp/dev-vault/app-token"

if [[ ! -f "$TOKEN_FILE" ]]; then
  echo "[devcontainer] Vault app token not found at $TOKEN_FILE"
  echo "[devcontainer] Ensure vault-init service completed successfully"
  exit 0
fi

TOKEN_CONTENT="$(<"$TOKEN_FILE")"

if [[ -z "$TOKEN_CONTENT" ]]; then
  echo "[devcontainer] Vault app token file is empty"
  exit 0
fi

mkdir -p /home/vscode
printf "%s" "$TOKEN_CONTENT" > /home/vscode/.vault-token
chmod 600 /home/vscode/.vault-token

echo "[devcontainer] Synced app Vault token to /home/vscode/.vault-token"
