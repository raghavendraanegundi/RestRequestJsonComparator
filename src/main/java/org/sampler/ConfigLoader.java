package org.sampler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigLoader {

    JSONParser jp = new JSONParser();
    JSONObject jsonObject;
    public JSONObject getConfig() throws IOException, ParseException {
        jsonObject=(JSONObject) jp.parse(new FileReader("config.json"));
        return jsonObject;
    }

}
