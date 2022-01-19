package com.dpetrunov.weatherapp.utils;

import com.dpetrunov.weatherapp.config.AppConfiguration;

public class WebClientUtil {

    public static Boolean authQueryParamExists(String queryString) {

        return queryString.contains(AppConfiguration.QUERY_PARAM_API_KEY);
    }

    public static String queryStringWithAuthentication(String queryString, String apiKey) {

        String authQueryString = String.format("%s=%s", AppConfiguration.QUERY_PARAM_API_KEY, apiKey);

        String newQuery = queryString;

        if (newQuery == null || newQuery.trim().isEmpty()) {

            newQuery = authQueryString;

        } else {

            newQuery += "&" + authQueryString;
        }

        return newQuery;
    }
}