package org.sampler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

/**
 * <h1>Read config.json and load the configuration</h1>
 *
 * @author  Raghav
 */
public class ConfigLoader {

    JSONParser jp = new JSONParser();
    JSONObject jsonObject;
    /**
     * This method is used read the json file config.json from the current directory.
     *
     * @return jsonobject from which the data can be retrieved using the keyname.
     */
    public JSONObject getConfig() throws IOException, ParseException {
        jsonObject=(JSONObject) jp.parse(new FileReader("config.json"));
        return jsonObject;
    }

}
