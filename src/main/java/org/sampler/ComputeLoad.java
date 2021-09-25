package org.sampler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>Compute load to be assigned to threads</h1>
 * The Compute load provides functions to read a file and determine the load to be assigned to each thread.
 * Each load is a pair of integers representing startLineNumber,EndLinenumber
 *
 * @author  Raghav
 */
public class ComputeLoad {

    List<Object> loadHolder;

    ComputeLoad(){
        loadHolder = new ArrayList<>();
    }
    /**
     * This method is used compute load based on totalnumber of lines in file, threadcount and loadperthread configured.
     *
     * @param allowedLoadPerThread represents the max number of lines a thread can process
     * @param inputFilePath File for which the computation is required
     */
    public void computeLoadPerThread(int allowedLoadPerThread, String inputFilePath){
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

    /**
     * This method is used count number of lines in a file.
     * @param filePath accepts path of the file
     * @return long returns number of lines in the file. Exits the program if its found empty.
     */
        long getCountOfLines(String filePath){
        Path path = Paths.get(filePath);
        long lines = 0;
        try{
            lines = Files.lines(path).count();
            if(lines==0){
                System.out.println("Empty file passed as input >> File is "+filePath);
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read file and get linecount >> File is "+filePath);
        }
        return lines;
    }
}
