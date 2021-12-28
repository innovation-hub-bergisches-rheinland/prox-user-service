module.exports = {
  // Delegate the whole formatting to spotless
  "*": [
    (resolvedPaths) =>
      resolvedPaths.map(
        (filename) => `./gradlew spotlessApply -PspotlessIdeHook="${filename}"`
      ),
  ],
};
