package org.sampler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

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

    void closeFile(){
        try {
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to close ");
        }
    }




}
