package org.sampler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunnerClass {

    List<Object> loadHolder;
    ReportCreator rc;


    RunnerClass(List<Object> listOfLoad, String outPutFileName) {
        loadHolder = listOfLoad;
        rc= new ReportCreator(outPutFileName);
    }

    /**
     * This method is used to create a runnable object.
     * Runnable object makes http request for the range of lines, processes the responses, pushes the findings to report
     * This defines what each thread has to perform
     *
     * @param rangeObject container start index line number and end index line number
     * @param filePath1 input file path
     * @param filePath2 input file path
     * @return returns a runnable object to be passed to thread execution
     */
    Runnable createNewRunnable(Object rangeObject, String filePath1, String filePath2) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //take passed range
                System.out.println("New Runnable object creation start");
                ArrayList<String> lineFromFile1;
                ArrayList<String> lineFromFile2;
                HTTPHandler req = new HTTPHandler();
                Iterator file1Iterator;
                Iterator file2Iterator;
                HashMap<String,String> file1Response;
                HashMap<String, String> file2Response;
                String result = "DEFAULT";
                List<Object> lineRanges = (List<Object>) rangeObject;
                System.out.println("Range is " + lineRanges.toString());
                // get lines from file 1
                lineFromFile1 = getLinesFromFile(Integer.parseInt(lineRanges.get(0).toString()), Integer.parseInt(lineRanges.get(1).toString()), filePath1);
                //get line from file 2
                lineFromFile2 = getLinesFromFile(Integer.parseInt(lineRanges.get(0).toString()), Integer.parseInt(lineRanges.get(1).toString()), filePath2);
                if(lineFromFile1.size() <=1 && lineFromFile2.size() <=1){
                    System.out.println("Reached EOF");
                    return;
                }
                System.out.println(lineFromFile1);
                System.out.println(lineFromFile2);
                file1Iterator = lineFromFile1.listIterator();
                file2Iterator = lineFromFile2.listIterator();
                while(file1Iterator.hasNext() && file2Iterator.hasNext()){
                    file1Response=req.getResponse(file1Iterator.next().toString());
                    file2Response=req.getResponse(file2Iterator.next().toString());
                    if(!file1Response.get("Status").toString().equals("Failed") || !file2Response.get("Status").toString().equals("Failed")){
                        result = responseComparison(file1Response.get("ResponseBody"), file2Response.get("ResponseBody"));
                        file1Response.put("TestStatus", result);
                    }else {
                        file1Response.put("TestStatus", "Error");
                    }
                    // write outputfile
                    rc.appendToOutPutFile(file1Response,file2Response);
                }
            }
        };
        return r;
    }
    /**
     * This method is used to manage and spawn thread based on number of thread-count, loadperthread
     * Also manages the pool of thread, instantiates thread until all the runnable objects are executed.
     * Also makes sure the thread count is maintained as configured
     *
     * @param filePath1 path to input file
     * @param filePath2 path to input file
     * @param threadCountStr max allowed thread at any point of time
     */
    public void startThreadSpawn(String filePath1, String filePath2, String threadCountStr) {
        int threadCount = Integer.parseInt(threadCountStr);
        ListIterator<Object> loadIterator = loadHolder.listIterator();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Runnable runnableObj ;
        while (loadIterator.hasNext()) {
                runnableObj = createNewRunnable(loadIterator.next(), filePath1, filePath2);
                executor.execute(runnableObj);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        rc.closeFile();
        System.out.println("Finished all threads");
    }
    /**
     * This method is used to get specific lines from the input file based on start and end index number
     *
     * @param startIndex linenumber of start from
     * @param  endIndex linenumber to stop retrieval
     * @return List of endpoints strings retrieved
     */
    ArrayList<String> getLinesFromFile(int startIndex, int endIndex, String filePath) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader br = Files.newBufferedReader(Paths.get(filePath));
            String currentLine = null;
            for (int i = 0; i < startIndex; i++) {
                br.readLine();
            }
            for (int i = startIndex; i <= endIndex; i++) {
                currentLine = br.readLine();
                if(currentLine != null) {
                    lines.add(currentLine);
                }else{
                    break;
                }
            }
            return lines;
        } catch (Exception e) {
            System.out.println("Failed to read file from path " + filePath + " due to exception");
            e.printStackTrace();
        }
        return null;
    }
    /**
     * This method is used to compare two json responses and return the result.
     *
     * @param expectedResp expected response object one endpoint
     * @param actualResp actual response object from another endpoint
     * @return Return string "PASS"/"FAIL"/"Exception" based on the comparison
     */
    String responseComparison(String expectedResp, String actualResp) {
        try {
            JsonNode expectedNode = new ObjectMapper().readTree(expectedResp);
            JsonNode actualNode = new ObjectMapper().readTree(actualResp);
            if(expectedNode.equals(actualNode)){
                return "EQUALS";
            }
            return "NOT_EQUALS";
        } catch (Exception e) {
            e.printStackTrace();
            return "EXCEPTION";
        }
    }
}
