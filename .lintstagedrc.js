module.exports = {
  // Delegate the whole formatting to spotless
  "*": [
    (resolvedPaths) => {
      return `./gradlew spotlessApply`;
    },
  ],
};
