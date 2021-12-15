# Prox User Service

The purpose of this service is to provide a service that can handle user-related tasks. One major task is the association of users to orgs.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw quarkus:dev
```
## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Packaging and running the application as a docker iamge

You can create a docker image:
```shell script
./mvnw package -Dquarkus.container-image.build=true
```

If you want to learn more about building container images, please consult https://quarkus.io/guides/container-image.

## Creating a native executable

You can create a native executable using:
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/prox-user-service-0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Development

Please consider our [contributing guidelines](./CONTRIBUTING.md).

### Commit Messages

The commit messages **SHOULD** adhere to the
[Conventional Commits specification](https://conventionalcommits.org/). This
repository is also
[Commitizen](https://github.com/pocommitizen/cz-cli)-friendly. You can use
Commitizen seamless in this repository.

### Perform a release

In general releases are done by pushing a git tag which conforms to
[SemVer](https://semver.org/) specification. We prefix those tags with a `v`, so
the tag itself **MUST** follow the pattern `vMAJOR.MINOR.PATCH`. A label is not
used.

The simplest way to perform a release is by relying on our
[standard-version](https://github.com/conventional-changelog/standard-version)
configuration. If you are ready to perform a release simply call

```shell
$ npx standard-version
# or
$ npm run release
```

This will analyze the last commits since the last release, determine the new
version, generate a changelog and create a git tag for you. Next up you will
need to push the tag and version bumped files, our release pipeline will take
care of the rest.

```shell
$ git push --follow-tags origin main
```
