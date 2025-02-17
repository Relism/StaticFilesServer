package net.relismdev.handlers.api;

import com.pixelservices.flash.components.RequestHandler;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import com.pixelservices.flash.models.HttpMethod;
import com.pixelservices.flash.models.RouteInfo;
import com.pixelservices.flash.shaded.org.json.JSONArray;
import com.pixelservices.flash.shaded.org.json.JSONObject;
import net.relismdev.FilesServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Handles API requests for file information.
 * <p>
 * If the request path is "/api/files" or "/api/files/", a JSON representation
 * of all files in the static directory is returned. If a specific file is requested
 * and it doesn't exist, a 404 is returned.
 * </p>
 */
@RouteInfo(endpoint = "/files/*", method = HttpMethod.GET)
public class FilesApiHandler extends RequestHandler {
    private final Path staticDir = FilesServer.getStaticDirPath();

    public FilesApiHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    public Object handle() {
        try {
            // Remove the API prefix "/api/files" (with or without trailing slash)
            String path = req.path();
            String prefix = "/api/files";
            String requestPath = "";
            if (path.startsWith(prefix)) {
                requestPath = path.substring(prefix.length());
                if (requestPath.startsWith("/")) {
                    requestPath = requestPath.substring(1);
                }
            }

            // If requestPath is empty, we list the static directory.
            File requestedFile = new File(staticDir.resolve(requestPath).toString());

            if (!requestedFile.exists()) {
                res.status(404);
                res.type("application/json");
                return new JSONObject().put("error", "Not found").toString();
            }

            if (requestedFile.isDirectory()) {
                File[] files = requestedFile.listFiles();
                if (files == null) {
                    res.status(500);
                    res.type("application/json");
                    return new JSONObject().put("error", "Unable to list directory").toString();
                }

                JSONArray fileList = new JSONArray();
                Arrays.sort(files, Comparator
                        .comparing((File file) -> !file.isDirectory()) // directories first
                        .thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER)); // then by name

                for (File file : files) {
                    JSONObject fileInfo = new JSONObject();
                    fileInfo.put("name", file.getName());
                    fileInfo.put("type", file.isDirectory() ? "directory" : "file");
                    fileInfo.put("size", file.isDirectory() ? JSONObject.NULL : file.length());
                    String relativePath = (requestPath.isEmpty() ? "" : requestPath + "/") + file.getName();
                    fileInfo.put("path", relativePath);
                    fileList.put(fileInfo);
                }
                res.type("application/json");
                return fileList.toString();
            } else {
                JSONObject fileInfo = new JSONObject();
                fileInfo.put("name", requestedFile.getName());
                fileInfo.put("type", "file");
                fileInfo.put("size", requestedFile.length());
                fileInfo.put("contentType", Files.probeContentType(requestedFile.toPath()));
                res.type("application/json");
                return fileInfo.toString();
            }
        } catch (IOException e) {
            res.status(500);
            res.type("application/json");
            return new JSONObject().put("error", "Internal Server Error").toString();
        }
    }
}
