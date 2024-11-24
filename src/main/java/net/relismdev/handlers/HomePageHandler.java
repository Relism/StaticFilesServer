package net.relismdev.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HomePageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = """
            <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        text-align: center;
                        padding-top: 50px;
                    }
                    h1 {
                        font-size: 36px;
                        color: #333;
                    }
                    p {
                        font-size: 18px;
                    }
                    a {
                        color: #0066cc;
                        text-decoration: none;
                    }
                    a:hover {
                        text-decoration: underline;
                    }
                </style>
                <title>Static Files Hosting</title>
            </head>
            <body>
                <h1>Welcome to the Static File Server</h1>
                <p>Visit <a href="/static">/static</a> to browse and download files from the configured directory.</p>
            </body>
            </html>
        """;

        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseHeaders().set("Content-Type", "text/html");

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
