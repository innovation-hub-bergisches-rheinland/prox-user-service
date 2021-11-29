module.exports = {
  branches: ["main"],
  plugins: [
    [
      // Analyze commits and determine the new version
      "@semantic-release/commit-analyzer",
      {
        preset: "conventionalcommits",
      },
    ],
    // Generate Release Notes
    "@semantic-release/release-notes-generator",
    // Generate Changelog
    "@semantic-release/changelog",
    // GitHub integration
    "@semantic-release/github",
    // Gradle release integration bumps version in gradle.properties and
    // publishes maven artifacts
    // TODO: This plugin does not add much value and should be replaced
    // It just executes the publish task and bumps the version
    "gradle-semantic-release-plugin",
    // Custom execution to validate that the Maven POM is correctly formed
    // since otherwise the release cycle won't stop as this will only
    [
      "@semantic-release/exec",
      {
        verifyConditionsCmd:
          "./gradlew generateMetadataFileForGprPublication generatePomFileForGprPublication",
      },
    ],
    // Commit git changes
    [
      "@semantic-release/git",
      {
        assets: ["gradle.properties", "CHANGELOG.md"],
      },
    ],
    // Build and publish the docker image from jib
    // TODO: Might be merged with the maven artifact publication
    [
      "@semantic-release/exec",
      {
        verifyConditionsCmd: "./gradlew jibDockerBuild",
        publishCmd: "./gradlew jib",
      },
    ],
  ],
};
