package service;

import DTOs.PassengerRequest;
import config.Config;
import constants.ApiServiceEndpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.RestHelper;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceApi extends RestHelper {
    Logger LOGGER = LogManager.getLogger();

    String loginUrl = Config.getAuthConfig().loginUrl();
    String serviceUrl = Config.getServiceConfig().apiBaseUrl();

    public AuthServiceApi() {
        headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getToken());

        url = serviceUrl;
    }

    @Step("POST dev okta Create authentication token")
    public Response login() {
        url = loginUrl;
        Map<String, String> formParams = new HashMap<>();
        formParams.put("username", Config.getAuthConfig().userName());
        formParams.put("password", Config.getAuthConfig().password());
        formParams.put("scope", Config.getAuthConfig().scope());
        formParams.put("client_id", Config.getAuthConfig().clientId());
        formParams.put("grant_type", Config.getAuthConfig().grantType());

        return postUrlEncoded(ApiServiceEndpoints.GET_TOKEN, formParams);
    }

    @Step("GET v2/airlines - Get all airlines details")
    public Response getAllAirlines() {
        System.out.println("Running getAllAirlines method");
        return get(ApiServiceEndpoints.AIRLINES);
    }

    @Step("GET v2/passenger/{0} - Get passenger details")
    public Response getPassengerInfo(String id) {
        System.out.println("Running getAllAirlines method");
        return get(String.format(ApiServiceEndpoints.PASSENGER_BY_ID, id));
    }

    @Step("POST v2/passenger - Create passenger")
    public Response createPassenger(PassengerRequest passenger) {
        return post(ApiServiceEndpoints.PASSENGER, passenger);
    }

    @Step("PUT v2/passenger/{0} - Change passenger details")
    public Response changePassengerName(String id, PassengerRequest passenger) {
        return put(String.format(ApiServiceEndpoints.PASSENGER_BY_ID, id), passenger);
    }

    @Step("DELETE v2/passenger/{0} - delete passenger completely!")
    public Response deletePassenger(String id) {
        return delete(String.format(ApiServiceEndpoints.PASSENGER_BY_ID, id));
    }

    public String getToken() {
        LOGGER.error("Getting token...");
        return login().then().statusCode(HttpStatus.SC_OK).extract().path("access_token");
    }

}