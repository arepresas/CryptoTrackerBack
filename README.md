# CryptoTracker API

Backend API for fetching, storing, and exposing cryptocurrency market data.
The project uses Spring Boot, PostgreSQL, Liquibase, and Vault for secrets management.

## Quick Overview

- Language: Java 25
- Framework: Spring Boot 4
- Database: PostgreSQL
- Migrations: Liquibase
- Secrets: HashiCorp Vault (dev mode for local setup)
- API docs: OpenAPI / Swagger UI

## Architecture

The project follows a modular monolith approach with a package-by-feature structure.

- `features/cryptos`: business logic and endpoints for coins, prices, and quotes
- `external/coinmarket`: HTTP client for CoinMarketCap
- `config`: configuration classes (CORS, scheduler, web client, property logging)
- `src/main/resources/config`: app profiles (`application.yaml`, `application-local.yaml`, etc.)
- `src/main/resources/db`: Liquibase changelogs

## Requirements

- Docker + Docker Compose
- Java 25
- Maven (or use the included wrapper `./mvnw`)

## Local Setup

## Dev Container (Vault closer to production)

You can also work with a devcontainer environment that runs Vault in non-`dev` mode (init/unseal + policy + app token), to better mirror production behavior.

### 1) Variables for Vault bootstrap

Create `.devcontainer/.env` (not versioned) with:

```bash
CRYPTO_API_URL=https://pro-api.coinmarketcap.com
CRYPTO_API_KEY=YOUR_COINMARKET_API_KEY
```

### 2) Open the project in Dev Container

In VS Code:

- `Dev Containers: Reopen in Container`

When starting, `app`, `postgres`, `vault`, and `vault-init` are launched.
`vault-init` is responsible for:

- initializing and unsealing Vault,
- creating the `crypto-tracker-app` policy,
- loading secrets into `secret/crypto-tracker`,
- generating an app token and syncing it to `~/.vault-token` inside the `app` container.

### 3) Run the application inside the container

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local-devcont,debug
```

The app will use Vault at `http://vault:8200`, the local app token, and the Docker-internal datasource (`postgres:5432`) through the `local-devcont` profile.

### 1) Create local environment file

Create a `.env` file at repository root (do not commit it):

```bash
cat > .env <<'EOF'
VAULT_TOKEN=dev-token
CRYPTO_API_URL=https://pro-api.coinmarketcap.com
CRYPTO_API_KEY=YOUR_COINMARKET_API_KEY
EOF
```

Notes:

- `CRYPTO_API_KEY` is required for `vault-init` to load secrets.
- Secrets are written into Vault at `secret/data/crypto-tracker`.

### 2) Start local infrastructure

```bash
docker compose -f src/main/resources/docker/localstack-dev.yaml up -d
```

Services started:

- PostgreSQL: `localhost:6632` (db: `cryptotracker-db`, user: `myuser`, pass: `mysecretpassword`)
- pgAdmin: `http://localhost:6680` (email: `mail@mail.com`, pass: `mysecretpassword`)
- Vault dev: `http://localhost:8200` (default token: `dev-token`)

### 3) Run the application

In your terminal:

```bash
export VAULT_TOKEN=dev-token
./mvnw spring-boot:run -Dspring-boot.run.profiles=local,debug
```

The API is available at `http://localhost:8700`.

## API Documentation

- Swagger UI: `http://localhost:8700/swagger-ui.html`

## Main Endpoints

Local base URL: `http://localhost:8700`

### Internal persisted data

- `GET /v1/crypto/coins/{cryptoId}`
- `GET /v1/crypto/coins`
- `GET /v1/crypto/prices/{cryptoPriceId}`
- `GET /v1/crypto/prices`
- `GET /v1/crypto/quotes/{cryptoQuoteId}`
- `GET /v1/crypto/quotes`

Pagination and sorting (for list endpoints):

- `pageNumber`
- `resultsPerPage`
- `sortBy`
- `sortDirection` (`ASC` or `DESC`)

Filters (depending on resource):

- Coins: `ids`, `symbol`, `name`, `category`, `slug`, `subreddit`, `tags`, etc.
- Prices: `ids`, `cmcRank`, `cryptoCoinIds`, `cryptoCoinSymbols`, etc.
- Quotes: `ids`, `currency`, `lastUpdatedBefore`, `lastUpdatedAfter`, `cryptoPriceIds`

### CoinMarketCap proxy endpoints

- `GET /v1/coinMarketCrypto/info/{cryptoIds}`
- `GET /v1/coinMarketCrypto/lastListing?start=1&limit=200&currency=USD`
- `GET /v1/coinMarketCrypto/quote/{cryptoIds}/{currency}`

Examples:

```bash
curl "http://localhost:8700/v1/coinMarketCrypto/info/1,1027"
curl "http://localhost:8700/v1/coinMarketCrypto/lastListing?start=1&limit=50&currency=USD"
curl "http://localhost:8700/v1/coinMarketCrypto/quote/1,1027/USD"
```

Supported `currency` values: `USD`, `BTC`, `ETH`.

### Manual update task

- `GET /v1/cryptoTask/lastPrices`

Triggers the task that synchronizes cryptos/prices/quotes from CoinMarketCap.

## Database and Migrations

- Master changelog: `src/main/resources/db/changelog-master.yml`
- Initial schema and updates: `src/main/resources/db/init-db.xml`

To evolve the schema, add new Liquibase `changeSet` entries under `db/`.

## Useful Commands

### Run tests

```bash
./mvnw clean test
```

### Build artifact

```bash
./mvnw clean package
```

### Build Docker image

```bash
docker build -f docker/Dockerfile -t crypto-tracker:local .
```

## Stop and Clean Local Environment

Stop containers:

```bash
docker compose -f src/main/resources/docker/localstack-dev.yaml down
```

Stop and remove volumes:

```bash
docker compose -f src/main/resources/docker/localstack-dev.yaml down -v
```

## CI/CD (GitHub Actions)

Workflows under `.github/workflows/` cover:

- build + test + sonar
- docker image build/publish
- develop, pull request, and release flows

## Troubleshooting

- Vault errors on startup:
  - Ensure Vault is running (`http://localhost:8200`) and `VAULT_TOKEN` is exported.
- CoinMarketCap key errors:
  - Check `CRYPTO_API_KEY` in `.env` and recreate `vault-init` by restarting compose.
- PostgreSQL connection issues:
  - Ensure `crypto-tracker-db` container is up and port `6632` is free.
