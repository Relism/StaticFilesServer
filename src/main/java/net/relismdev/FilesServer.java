package net.relismdev;

import com.pixelservices.config.YamlConfig;
import com.pixelservices.flash.components.FlashConfiguration;
import com.pixelservices.flash.components.FlashServer;
import com.pixelservices.flash.components.fileserver.DynamicFileServerConfiguration;
import com.pixelservices.flash.components.fileserver.SourceType;
import com.pixelservices.flash.components.fileserver.StaticFileServerConfiguration;
import com.pixelservices.flash.models.HandlerType;
import net.relismdev.handlers.api.FilesApiHandler;

import java.io.*;
import java.nio.file.Path;

public class FilesServer {

    private static File staticDir;

    public static void main(String[] args) {
        YamlConfig config = new YamlConfig("config.yml");
        config.save();

        if (args.length < 2) {
            System.out.println("Usage: java -jar fileserver.jar <port> <directory>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        staticDir = new File(args[1]);

        System.out.println("Starting server on port " + port + " serving files from " + staticDir.getAbsolutePath());

        if (!staticDir.exists() || !staticDir.isDirectory()) {
            if (staticDir.mkdirs()){
                System.out.println("Directory created successfully");
            } else {
                System.out.println("Error: The specified directory does not exist or is not a directory.");
                System.exit(1);
            }
            System.out.println("Error: The specified directory does not exist or is not a directory.");
            System.exit(1);
        }

        FlashConfiguration flashConfiguration = new FlashConfiguration();
        flashConfiguration.setLoggingPreference(HandlerType.STATIC, false);

        FlashServer server = new FlashServer(port, flashConfiguration);

        server.route("/api")
            .register(FilesApiHandler.class);

        server.serveStatic("/static", new StaticFileServerConfiguration(
                true,
                false,
                "/home/container/static",
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

    public static Path getStaticDirPath(){
        return Path.of(staticDir.getAbsolutePath());
    }

}


