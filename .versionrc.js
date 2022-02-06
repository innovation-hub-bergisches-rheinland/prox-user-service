module.exports = {
  bumpFiles: [
    {
      filename: "version.txt",
      type: "plain-text",
    },
    {
      filename: "./deploy/charts/prox-user-service/Chart.yaml",
      updater:
          "./node_modules/@map-colonies/standard-version-update-helm-version/src/index.js",
    },
    {
      filename: "package.json",
      type: "json",
    },
  ],
};
