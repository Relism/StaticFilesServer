package net.relismdev;

import com.sun.net.httpserver.HttpServer;
import net.relismdev.handlers.HomePageHandler;
import net.relismdev.handlers.StaticFileHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilesServer {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java -jar fileserver.jar <port> <directory>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        File staticFilesDir = new File(args[1]);

        if (!staticFilesDir.exists() || !staticFilesDir.isDirectory()) {
            if (staticFilesDir.mkdirs()){
                System.out.println("Directory created successfully");
            } else {
                System.out.println("Error: The specified directory does not exist or is not a directory.");
                System.exit(1);
            }
            System.out.println("Error: The specified directory does not exist or is not a directory.");
            System.exit(1);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HomePageHandler());
        server.createContext("/static", new StaticFileHandler(staticFilesDir));

        // Use a thread pool to handle concurrent requests
        ExecutorService executor = Executors.newFixedThreadPool(10);
        server.setExecutor(executor);

        server.start();

        System.out.println("Server started at http://localhost:" + port);
    }
}


