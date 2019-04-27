package com.lukas.srkandroid;

public enum API {

    PROD_URL("http://rybarskyklub.sk/rest"),
    TEST_URL("http://192.168.0.106:8080/SRK/rest");

    public static String URL = TEST_URL.toString();

    private String url;

    API(String envUrl) {
        this.url = envUrl;
    }

    public String toString() {
        return url;
    }


}
