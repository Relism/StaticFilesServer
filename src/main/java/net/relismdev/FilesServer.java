package net.relismdev;

import com.pixelservices.config.YamlConfig;
import com.pixelservices.flash.components.FlashConfiguration;
import com.pixelservices.flash.components.FlashServer;
import com.pixelservices.flash.components.fileserver.DynamicFileServerConfiguration;
import com.pixelservices.flash.components.fileserver.SourceType;
import com.pixelservices.flash.components.fileserver.StaticFileServerConfiguration;
import com.pixelservices.flash.models.HandlerType;
import net.relismdev.handlers.api.ConfigAPIHandler;
import net.relismdev.handlers.api.FilesApiHandler;

import java.io.File;
import java.nio.file.Path;

public class FilesServer {

    private static File filesDir;
    private static final YamlConfig config = new YamlConfig("config.yml");

    public static void main(String[] args) {
        config.save();

        if (!validateConfiguration()) {
            System.err.println("Configuration validation failed. Exiting.");
            System.exit(1);
        }

        int port = config.getInt("serverPort");

        FlashConfiguration flashConfiguration = new FlashConfiguration();
        flashConfiguration.setLoggingPreference(HandlerType.STATIC, false);

        FlashServer server = new FlashServer(port, flashConfiguration);

        server.route("/api")
                .register(FilesApiHandler.class)
                .register(ConfigAPIHandler.class);

        server.serveStatic("/static", new StaticFileServerConfiguration(
                true,
                false,
                filesDir.getAbsolutePath(),
                SourceType.FILESYSTEM
        ));

        server.serveDynamic("/", new DynamicFileServerConfiguration(
                true,
                "frontend",
                "index.html",
                SourceType.RESOURCESTREAM
        ));

        server.start();
    }

    /**
     * Validates the startup configuration.
     * Checks for a valid server port and ensures that the files directory exists and is accessible.
     *
     * @return true if the configuration is valid; false otherwise.
     */
    private static boolean validateConfiguration() {
        int port = config.getInt("serverPort");
        if (port <= 0 || port > 65535) {
            System.err.println("Invalid server port: " + port + ". It must be between 1 and 65535.");
            return false;
        }

        String filesDirPath = config.getString("filesDir");
        if (filesDirPath == null || filesDirPath.isEmpty()) {
            System.err.println("Files directory path is not configured in config.yml.");
            return false;
        }

        filesDir = new File(filesDirPath);
        if (!filesDir.exists()) {
            System.out.println("Files directory does not exist. Attempting to create: " + filesDir.getAbsolutePath());
            if (!filesDir.mkdirs()) {
                System.err.println("Failed to create files directory: " + filesDir.getAbsolutePath());
                return false;
            }
            System.out.println("Files directory created successfully.");
        }

        if (!filesDir.isDirectory() || !filesDir.canRead()) {
            System.err.println("The specified files directory " + filesDir.getAbsolutePath() +
                    " is not a valid directory or is not accessible.");
            return false;
        }

        return true;
    }

    public static Path getStaticDirPath() {
        return Path.of(filesDir.getAbsolutePath());
    }

    public static YamlConfig getConfig() {
        return config;
    }
}
