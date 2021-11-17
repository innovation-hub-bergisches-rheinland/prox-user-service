# Prox User Service

The purpose of this service is to provide a service that can handle user-related tasks. One major task is the association of users to orgs.

## Installation

After a `git clone` or download the project the following command must be executed once to initialize the projects.

Windows (CMD/PowerShell)

```posh
# Switch to project folder
cd .\prox-user-service\
# Execute initial build for git hooks...
.\mvnw.cmd clean test
```

Linux/MacOS (Bash/Terminal)

```bash
# Switch to project folder
cd prox-user-service/
# Execute initial build for git hooks...
./mvnw clean test
```

Executes the [Maven default lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) up to the `test` phase. During the `package` phase, an executable JAR and the Docker image are created.

## Local usage with docker

A Docker network named `prox` is required for the communication between services:

```bash
docker network create prox
```

Starts a Docker container based on the compose file and the image.

Powershell

```posh
$env:IMAGE='prox-user-service'; `
$env:TAG='latest'; `
docker-compose -f ./src/main/docker/docker-compose.yml up
```

Bash/Shell

```bash
export IMAGE="prox-user-service" &&
export TAG="latest" &&
docker-compose -f ./src/main/docker/docker-compose.yml up
```
