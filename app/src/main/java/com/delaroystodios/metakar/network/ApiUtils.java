package com.delaroystodios.metakar.network;


public class ApiUtils {
    private ApiUtils() {}

    private static final String BASE_URL = "http://metakar.com/";

    public static ApiServices getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiServices.class);
    }
}

