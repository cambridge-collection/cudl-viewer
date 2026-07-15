.PHONY: help build clean test dev-cudl-data run-sample run-cudl-data stop

MVN ?= mvn
DOCKER_COMPOSE ?= docker compose

help:
	@echo "CUDL Viewer Makefile targets:"
	@echo "  build         - Clean and build WAR with Maven"
	@echo "  clean         - Clean Maven target directory"
	@echo "  test          - Run Maven tests"
	@echo "  dev-cudl-data - Build then run CUDL data with hot reloading"
	@echo "  run-sample    - Run docker compose with sample data"
	@echo "  run-cudl-data - Run docker compose with CUDL data"
	@echo "  stop          - Stop docker compose stack"

build:
	$(MVN) clean package

clean:
	$(MVN) clean

test:
	$(MVN) test

dev-cudl-data: build
	$(DOCKER_COMPOSE) --file docker-compose-hot.yml --env-file cudl-data.env up

run-sample:
	$(DOCKER_COMPOSE) --env-file sample-data.env up

run-cudl-data:
	$(DOCKER_COMPOSE) --env-file cudl-data.env up

stop:
	$(DOCKER_COMPOSE) down

