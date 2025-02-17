package net.relismdev.handlers.api;

import com.pixelservices.config.YamlConfig;
import com.pixelservices.flash.components.RequestHandler;
import com.pixelservices.flash.lifecycle.Request;
import com.pixelservices.flash.lifecycle.Response;
import com.pixelservices.flash.models.HttpMethod;
import com.pixelservices.flash.models.RouteInfo;
import com.pixelservices.flash.shaded.org.json.JSONObject;
import net.relismdev.FilesServer;

@RouteInfo(endpoint = "/configuration", method = HttpMethod.GET)
public class ConfigAPIHandler extends RequestHandler {
    private final YamlConfig config = FilesServer.getConfig();

    private static final String DEFAULT_LOGO_URL = "https://static.pixel-services.com/static/assets/wemx/moonlogo_cropped.png";
    private static final String DEFAULT_TITLE = "Static Files Server";

    public ConfigAPIHandler(Request req, Response res) {
        super(req, res);
    }

    @Override
    public Object handle() {
        JSONObject configJson = new JSONObject();

        configJson.put("logoUrl", config.get("logoUrl", DEFAULT_LOGO_URL));
        configJson.put("faviconUrl", config.get("faviconUrl", config.get("logoUrl", DEFAULT_LOGO_URL)));
        configJson.put("title", config.get("title", DEFAULT_TITLE));

        res.type("application/json");
        return configJson.toString();
    }

}

