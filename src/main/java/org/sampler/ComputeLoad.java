package org.sampler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComputeLoad {

    List<Object> loadHolder;

    ComputeLoad(){
        loadHolder = new ArrayList<>();
    }

    public void computeLoadPerThread(int allowedLoadPerThread, String inputFilePath, int threadCount){
        long startIndex =0;
        long endIndex=allowedLoadPerThread;
        long numberOfLines = getCountOfLines(inputFilePath);
        if(numberOfLines <= allowedLoadPerThread){
            loadHolder.add(Arrays.asList(startIndex,numberOfLines-1));
            return;
        }
        int serialThreadCount = (int) (numberOfLines/allowedLoadPerThread);
        int recidualThreadCount = (int) (numberOfLines - (serialThreadCount*allowedLoadPerThread));
        loadHolder.add(Arrays.asList(startIndex,endIndex));
        for(int i = 2; i <= serialThreadCount;i++){
            startIndex = endIndex+1;
            endIndex = allowedLoadPerThread * i;
            loadHolder.add(Arrays.asList(startIndex,endIndex));
        }
        if(recidualThreadCount != 0 && endIndex!=numberOfLines-1){
            startIndex=endIndex+1;
            endIndex=numberOfLines;
            loadHolder.add(Arrays.asList(startIndex,endIndex));
        }
    }

        long getCountOfLines(String filePath){
        Path path = Paths.get(filePath);
        long lines = 0;
        try{
            lines = Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
