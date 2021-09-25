package org.sampler;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;


import static io.restassured.RestAssured.given;

public class HTTPHandler {

    HashMap<String, String> getResponse(String requestEndPoint) {
        HashMap<String, String> presponse = new HashMap<>();
        try {
            Response response = given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(requestEndPoint)
                    .then()
                    .extract().response();
            presponse = getProcessedResponse(response);
            presponse.put("Endpoint", requestEndPoint);
        } catch (Exception e){
            presponse.put("Endpoint", requestEndPoint);
            presponse.put("StatusCode","ERR");
            presponse.put("ResponseBody", "");
            presponse.put("Status", "Failed");
            presponse.put("Error", e.getMessage());
        }
        return presponse;
    }

    HashMap<String, String> getProcessedResponse(Response response) {
        HashMap<String, String> processedResponse = new HashMap<>();
        try {
            processedResponse.put("StatusCode", Integer.toString(response.statusCode()));
            processedResponse.put("ResponseBody", response.asPrettyString());
            if (response.statusCode() != 200) {
                processedResponse.put("Status", "Failed");
            }else{
                processedResponse.put("Status", "Passed");
            }
        } catch (Exception e) {
            processedResponse.put("StatusCode", "ERR");
            processedResponse.put("Error", e.getMessage());
            processedResponse.put("Status", "Failed");
        }
        return processedResponse;
    }
}
