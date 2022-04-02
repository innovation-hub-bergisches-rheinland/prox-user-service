# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## [1.5.0](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.4.3...v1.5.0) (2022-04-02)


### Features

* add user profiles ([#5](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/issues/5)) ([c0110b0](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/c0110b0c3f91c288d49c4a7f12929e45eea8276b))


### Bug Fixes

* permission check during update ([4925141](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/49251417c392078ad3047815b4a3c07d5d721fb7))

### [1.4.3](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.4.2...v1.4.3) (2022-04-01)


### Bug Fixes

* also allow blank/null values for headquarter ([4657641](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/4657641d6d614001c864200dc78e2892d709b4e1))

### [1.4.2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.4.1...v1.4.2) (2022-04-01)


### Bug Fixes

* allow null/blank values for quarters ([960af04](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/960af04491c8da06365995e41ead7b9c2965929d))
* members can view memberships now ([9db31bf](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/9db31bf66fdcb94e2d57f474a00df239de474d8f))

### [1.4.1](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.4.0...v1.4.1) (2022-04-01)


### Bug Fixes

* add null-check ([b4d76df](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/b4d76df6e3cf2e323f737a2d3ecf7a01255c33a9))

## [1.4.0](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.7...v1.4.0) (2022-04-01)


### Features

* refactor organization roles and remove owner ([7489aa6](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/7489aa66ebe72ba7d9af36de7efbc1c0484e6b2a))

### [1.3.7](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.6...v1.3.7) (2022-03-17)


### Bug Fixes

* remove validation from quarter ([55bc733](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/55bc73377f3e8e2749ad86c7035b65f8c9e43c6f))

### [1.3.6](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.5...v1.3.6) (2022-03-16)


### Bug Fixes

* fix regex ([839c276](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/839c27669e8cfa5231a75aa0cb8142974f307bb0))

### [1.3.5](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.4...v1.3.5) (2022-03-16)


### Bug Fixes

* regex for social media handles ([9efff99](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/9efff99296b9ef9ad100d011257d4b3734cc10a6))

### [1.3.4](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.3...v1.3.4) (2022-03-16)


### Bug Fixes

* allow null values for mapping branches/quarters ([004b1b7](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/004b1b7b4e74ad4b5248ff8a9b526e051aea192c))

### [1.3.3](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.2...v1.3.3) (2022-03-16)


### Bug Fixes

* change quarters from array type to simple string ([dd29843](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/dd298435d35e956b06290ae2d0c3049112b7875e))

### [1.3.2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.1...v1.3.2) (2022-03-16)


### Bug Fixes

* disable email validation ([fe12b71](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/fe12b71da4ac2825967edd6dcf96c6d65e69e90a))

### [1.3.1](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.3.0...v1.3.1) (2022-03-15)


### Bug Fixes

* mapper component model ([a23449e](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/a23449e9ade8684481744937b81eda725e17de10))

## [1.3.0](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.2.0...v1.3.0) (2022-03-15)


### Features

* add permissions to DTO payload ([ef29b74](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/ef29b74ae2ae0266b978b4fa1036a79d0121da56))

## [1.2.0](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.1.0...v1.2.0) (2022-03-14)


### Features

* add organization profile APIs ([cba5f8b](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/cba5f8b8b32b1644ea7bf0597c04a97fb9a41e76))
* implement S3 object store for avatars ([ff61fa6](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/ff61fa6f906389a19db504d66850b9ad3332f1a3))
* save object key in organization entity ([d8453da](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/d8453da11367cf27b28b77b43244aede6a897555))


### Bug Fixes

* add validation to service ([fe00362](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/fe0036289bf6bfa044c5e7820790cc270f6798c5))
* update api ([1941cb8](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/1941cb8f9a14951fb8c8e45e3b3b13194a5b5069))

## [1.1.0](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.8...v1.1.0) (2022-03-01)


### Features

* add api to query authenticated users organizations ([addeae6](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/addeae651eb35be352bc9411da5019f488652894))

### [1.0.8](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.7...v1.0.8) (2022-02-26)


### Bug Fixes

* add owner to members ([3c52cd8](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/3c52cd80003eade3b8e6fe2be4f683cd7c2f8bb9))

### [1.0.7](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.6...v1.0.7) (2022-02-14)


### Bug Fixes

* keycloak secret environment variable ([59419a8](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/59419a8052cd746d35e888fa9e6405899114cd4d))

### [1.0.6](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.5...v1.0.6) (2022-02-14)


### Bug Fixes

* fix service port in chart values ([109d58d](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/109d58d95c8f47504b6a3821d1a260bf0b406c5a))

### [1.0.5](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.4...v1.0.5) (2022-02-11)


### Bug Fixes

* fix keycloak-client-secret key ([dac088e](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/dac088e6c22f9f0be2e748fc3e71b6817cf4a83f))

### [1.0.4](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.3...v1.0.4) (2022-02-11)


### Bug Fixes

* trigger new release ([db15745](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/db1574527b96ef9af47776960e521eab5ed4b89a))

### [1.0.3](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.2...v1.0.3) (2022-02-11)


### Bug Fixes

* disable kubernetes-config secret querying and use env mapping ([50802a1](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/50802a1b971ebd9f98ca303e979be6ca35b82f6c))
* template syntax ([e92717f](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/e92717f0c54137a8c9bed2a871d60f8be9efb581))

### [1.0.2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.1...v1.0.2) (2022-02-11)


### Bug Fixes

* helm template ([fc70b28](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/fc70b28157e0d1a8c90086f0fa5ca83dc6338744))
* update config map and secret casing ([c201ce6](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/c201ce6faaa5f97da9dfeb1f17e1a0d9b1bec743))

### [1.0.1](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/compare/v1.0.0...v1.0.1) (2022-02-11)


### Bug Fixes

* fix rolebinding template ([966a543](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/966a5431a9a3fa4b652c5853346bcba3c9d045ac))

## 1.0.0 (2022-02-06)


### ⚠ BREAKING CHANGES

* add a basis for the quarkus implementation

### Features

* add api for updating membership ([0b59eb5](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/0b59eb598050ceec6d02b33d73ac2906c77d420e))
* add functionality to add and remove members ([ef75919](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/ef759190cbb7a387286f18176156fd834208d3dc))
* add keycloak admin client bean ([11df4c0](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/11df4c0252671853f11d5a6958fcfa5ff1c05d76))
* add keycloak implementation for a user repo ([79173a4](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/79173a4959371d593f416ae9386d94c11f6c253e))
* add mapstruct and dto mappers ([97b824c](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/97b824c3c8155a8fe3155c33af69779abdbdd1a5))
* create organization ([b19fd46](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/b19fd4634efe02c863b07f24b617ff70e3d876f4))
* implement create organization api ([a7f1f62](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/a7f1f621a82efd40186a3fdb3bee8ce2fcbda8d3))
* validate user id against keycloak ([fb6070b](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/fb6070b93776d59d91cc13fc616d94b491eaa388))


### Bug Fixes

* change to new keycloak deployment ([930dcf2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/930dcf2930e43b35b0779ae0f30843dd0a30ccb6))
* export required javac modules for spotless ([53548e9](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/53548e99bc14c6881e2fdd0e253fabfffde26dad))


### build

* add a basis for the quarkus implementation ([ab5b603](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/ab5b60398ff7750bd9c8b41ba72650b07d92710c))

## 1.0.0 (2021-12-11)

### Bug Fixes

- change to new keycloak deployment ([930dcf2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/930dcf2930e43b35b0779ae0f30843dd0a30ccb6))
- export required javac modules for spotless ([53548e9](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/53548e99bc14c6881e2fdd0e253fabfffde26dad))

## 1.0.0 (2021-12-11)

### Bug Fixes

- change to new keycloak deployment ([930dcf2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/930dcf2930e43b35b0779ae0f30843dd0a30ccb6))
- export required javac modules for spotless ([53548e9](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/53548e99bc14c6881e2fdd0e253fabfffde26dad))

## 1.0.0 (2021-12-11)

### Bug Fixes

- change to new keycloak deployment ([930dcf2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/930dcf2930e43b35b0779ae0f30843dd0a30ccb6))
- export required javac modules for spotless ([53548e9](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/53548e99bc14c6881e2fdd0e253fabfffde26dad))

# 1.0.0 (2021-11-29)

### Bug Fixes

- change to new keycloak deployment ([930dcf2](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/930dcf2930e43b35b0779ae0f30843dd0a30ccb6))
- export required javac modules for spotless ([53548e9](https://github.com/innovation-hub-bergisches-rheinland/prox-user-service/commit/53548e99bc14c6881e2fdd0e253fabfffde26dad))
