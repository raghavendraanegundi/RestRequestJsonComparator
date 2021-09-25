package org.sampler;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.params.CoreConnectionPNames;

import java.util.HashMap;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

/**
 * <h1>Take care of http calls and response processing</h1>
 * Implements functions related to making http calls and processing output for further consumption.
 *
 * @author  Raghav
 */
public class HTTPHandler {
    /**
     * This method is used to make call to an endpoint.
     *
     * @param requestEndPoint The endpoint to be called
     * @return Processed response map containing information for report creation
     */
    HashMap<String, String> getResponse(String requestEndPoint) {
        HashMap<String, String> presponse = new HashMap<>();
        try {
            RestAssuredConfig config = RestAssured.config()
                    .httpClient(HttpClientConfig.httpClientConfig()
                            .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000)
                            .setParam(CoreConnectionPNames.SO_TIMEOUT, 5000));
            Response response = given()
                    .config(config)
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

    /**
     * This method is used to process the response received and structure it for report creation.
     *
     * @param response response to be processed
     * @return A map with specific keys to help report creation
     */
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
