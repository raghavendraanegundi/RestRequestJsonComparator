package org.sampler;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class AppTest {

    public static void main(String args[]) {
        try {
            ConfigLoader configLoader = new ConfigLoader();
            ComputeLoad computeLoad = new ComputeLoad();
            RunnerClass executor;
            JSONObject config = configLoader.getConfig();
            computeLoad.computeLoadPerThread(Integer.parseInt(config.get("loadPerThread").toString()),
                    config.get("inputFilePath1").toString(),
                    Integer.parseInt(config.get("threadCount").toString()));
            List<Object> loadHolder = computeLoad.loadHolder;
            System.out.println(loadHolder);
            executor = new RunnerClass(loadHolder, config.get("outputFileName").toString());
            executor.startThreadSpawn(config.get("inputFilePath1").toString(),
                    config.get("inputFilePath2").toString(),
                    config.get("outputFileName").toString(),
                    config.get("threadCount").toString());
        }catch(IOException | ParseException e){
            System.out.println("Unhandled scenario/input");
            e.printStackTrace();
        }
    }
}
