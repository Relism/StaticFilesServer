const path = require('path');

module.exports = {
    webpack: {
        configure: (webpackConfig, { paths }) => {
            const buildPath = path.resolve(__dirname, '../src/main/resources/frontend');

            // Update Craco paths
            paths.appBuild = buildPath;

            // Ensure Webpack also outputs to the correct location
            webpackConfig.output.path = buildPath;

            return webpackConfig;
        },
    },
    style: {
        postcss: {
            plugins: [
                require('tailwindcss'),
                require('autoprefixer'),
            ],
        },
    },
};
