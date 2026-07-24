.PHONY: help build clean test dev dev-check dev-cudl-data run-sample run-cudl-data stop

MVN ?= mvn
DOCKER_COMPOSE ?= docker compose

help:
	@echo "CUDL Viewer Makefile targets:"
	@echo "  build         - Clean and build WAR with Maven"
	@echo "  clean         - Clean Maven target directory"
	@echo "  test          - Run Maven tests"
	@echo "  dev           - Build and run hot development (requires ENV_FILE=<path>)"
	@echo "  dev-check     - Check hot-development prerequisites only"
	@echo "  dev-cudl-data - Run dev with ENV_FILE=cudl-data.env"
	@echo "  run-sample    - Run docker compose with sample data"
	@echo "  run-cudl-data - Run docker compose with CUDL data"
	@echo "  stop          - Stop docker compose stack"

build:
	$(MVN) clean package

clean:
	$(MVN) clean

test:
	$(MVN) test

dev:
	@set -eu; \
	UI_VERSION=$$(./scripts/check-dev-prerequisites.sh "$(ENV_FILE)"); \
	echo "Building viewer with cudl-viewer-ui $$UI_VERSION"; \
	$(MVN) -Dcudl-viewer-ui.version="$$UI_VERSION" clean package; \
	$(DOCKER_COMPOSE) --file docker-compose-hot.yml --env-file "$(ENV_FILE)" up

dev-check:
	@UI_VERSION=$$(./scripts/check-dev-prerequisites.sh "$(ENV_FILE)"); \
	echo "Development prerequisites passed for cudl-viewer-ui $$UI_VERSION"

dev-cudl-data:
	$(MAKE) dev ENV_FILE=cudl-data.env

run-sample:
	$(DOCKER_COMPOSE) --env-file sample-data.env up

run-cudl-data:
	$(DOCKER_COMPOSE) --env-file cudl-data.env up

stop:
	$(DOCKER_COMPOSE) down
