package net.relismdev.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class StaticFileHandler implements HttpHandler {
    private final File staticDir;
    private final List<String> textFileExtensions = Arrays.asList("yml", "toml", "properties", "ini", "json", "txt");

    public StaticFileHandler(File staticDir) {
        this.staticDir = staticDir;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();

        if (requestPath.equals("/static") || requestPath.equals("/static/")) {
            listFiles(exchange, staticDir, "");
        } else {
            String filePath = requestPath.replace("/static/", "");
            File requestedFile = new File(staticDir, filePath);

            if (requestedFile.isDirectory()) {
                listFiles(exchange, requestedFile, filePath);
            } else {
                serveFile(exchange, filePath);
            }
        }
    }

    private void serveFile(HttpExchange exchange, String requestPath) throws IOException {
        File requestedFile = new File(staticDir, requestPath);

        if (requestedFile.exists() && !requestedFile.isDirectory()) {
            String fileExtension = getFileExtension(requestedFile);
            if (textFileExtensions.contains(fileExtension)) {
                serveTextFile(exchange, requestedFile);
            } else {
                serveBinaryFile(exchange, requestedFile);
            }
        } else {
            send404(exchange);
        }
    }

    private void serveTextFile(HttpExchange exchange, File file) throws IOException {
        String contentType = "text/plain";
        exchange.getResponseHeaders().set("Content-Type", contentType);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        exchange.sendResponseHeaders(200, fileContent.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(fileContent);
        }
    }

    private void serveBinaryFile(HttpExchange exchange, File file) throws IOException {
        String contentType = Files.probeContentType(file.toPath());
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, file.length());

        try (BufferedOutputStream os = new BufferedOutputStream(exchange.getResponseBody());
             BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {

            byte[] buffer = new byte[8192];
            int count;
            while ((count = bis.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
        }
    }

    private void listFiles(HttpExchange exchange, File dir, String pathPrefix) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            StringBuilder response = new StringBuilder();
            response.append("<html><head><meta charset=\"UTF-8\"><style>")
                    .append("body { font-family: Arial, sans-serif; margin: 20px; }")
                    .append("table { width: 100%; border-collapse: collapse; }")
                    .append("th, td { padding: 8px 12px; border: 1px solid #ddd; text-align: left; }")
                    .append("th { background-color: #f4f4f4; }")
                    .append("tr:nth-child(even) { background-color: #f9f9f9; }")
                    .append("a { text-decoration: none; color: #0066cc; }")
                    .append("a:hover { text-decoration: underline; }")
                    .append(".icon { width: 1.5em; }")
                    .append("</style><title>Static Files Hosting</title></head><body>")
                    .append("<h1>File List</h1>");

            // Add back button if not in the root directory
            if (!pathPrefix.isEmpty()) {
                String parentPath = pathPrefix.substring(0, pathPrefix.lastIndexOf('/', pathPrefix.length() - 2) + 1);
                response.append("<a href=\"/static/")
                        .append(parentPath)
                        .append("\">Back</a><br><br>");
            }

            response.append("<table><tr><th>Type</th><th>Filename</th><th>Size</th></tr>");

            Arrays.stream(files)
                    .forEach(file -> {
                        String fileName = file.getName();
                        String fileSize = file.isDirectory() ? "Directory" : readableFileSize(file.length());
                        String fileLink = (pathPrefix.isEmpty() ? "" : pathPrefix.endsWith("/") ? pathPrefix : pathPrefix + "/") + fileName;

                        if (file.isDirectory()) {
                            fileLink += "/";
                        }

                        String fileTypeIcon = file.isDirectory() ? "📁" : "📄";

                        response.append("<tr><td class=\"icon\">")
                                .append(fileTypeIcon)
                                .append("</td><td><a href=\"/static/")
                                .append(fileLink)
                                .append("\">")
                                .append(fileName)
                                .append("</a></td><td>")
                                .append(fileSize)
                                .append("</td></tr>");
                    });

            response.append("</table></body></html>");

            byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } else {
            send404(exchange);
        }
    }


    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1).toLowerCase();
    }

    private String readableFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private void send404(HttpExchange exchange) throws IOException {
        String response = "404 (File Not Found)";
        exchange.sendResponseHeaders(404, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
