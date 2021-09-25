package org.sampler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * <h1>Takes care of report creation</h1>
 * Implements functions which manipulate the processed response of each endpoint and appends to a report.
 *
 * @author  Raghav
 */
public class ReportCreator {

    File outputFile;
    FileWriter fw;
    BufferedWriter bfw;

    ReportCreator(String outputFileName){
        outputFile = new File(outputFileName);
        try {
            if (outputFile.exists()) {
                System.out.println("File Exists");
            } else {
                if (outputFile.createNewFile()) {
                    System.out.println("File creation successfull");
                    prepareOutputHeader(outputFileName);
                } else {
                    System.out.println("File creation failed");
                }
            }
            fw = new FileWriter(outputFile, true);
            bfw = new BufferedWriter(fw);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Puts header into the newly created outputfile
     *
     * @param fileName output filename
     */
    void prepareOutputHeader(String fileName){
        try{
            FileWriter writeHeader = new FileWriter(fileName, true);
            writeHeader.write("EndpointFile1,StatusCode,EndPointFile2,StatusCode,TestStatus,Add_Info"+"\n");
            writeHeader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write header into the report file");
        }
    }
    /**
     * This method is used format and append output to the file.
     *
     * @param file1Response processed response object for endpoint from file1
     * @param file2Response processed response object for endpoint from file2
     *
     */
    void appendToOutPutFile(HashMap<String,String> file1Response, HashMap<String,String> file2Response){
        String result = file1Response.get("Endpoint") +","+file1Response.get("StatusCode");
        result += ","+ file2Response.get("Endpoint") +","+file2Response.get("StatusCode");
        result += ","+file1Response.get("TestStatus");
        if(file1Response.get("TestStatus").toString().equals("Error")){
            result += ","+file1Response.get("Error");
        }
        try {
            bfw.write(result+"\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to write into output "+result);
        }
    }

    /**
     * This method is used to safely close the file once all threads are terminated.
     *
     */
    void closeFile(){
        try {
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to close ");
        }
    }




}
