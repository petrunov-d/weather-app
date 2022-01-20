package com.dpetrunov.weatherapp.utils;

import com.dpetrunov.weatherapp.config.AppConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WebClientUtilTest {

    @Test
    @DisplayName("Test Contains Auth Header")
    public void testContainsAuthHeader() {

        var toTest = WebClientUtil.authQueryParamExists("q?=safd" + AppConfiguration.QUERY_PARAM_API_KEY + "=asfd");

        assertTrue(toTest);
    }

    @Test
    @DisplayName("Test Does Not Contain Auth Header")
    public void testDoesNotContainAuthHeader() {

        var toTest = WebClientUtil.authQueryParamExists("q?=safd=asfd");

        assertFalse(toTest);
    }

    @Test
    @DisplayName("testAddsAuthentication")
    public void testAddsAuthentication() {

        var toTest = "?q=SFDasd";

        var result = WebClientUtil.queryStringWithAuthentication(toTest, "test");

        assertEquals("?q=SFDasd&appid=test", result);
    }

    @Test
    @DisplayName("testAddsAuthentication")
    public void testAddsAuthenticationOnEmptyQueryString() {

        var result = WebClientUtil.queryStringWithAuthentication(null, "test");

        assertEquals(result, AppConfiguration.QUERY_PARAM_API_KEY + "=test");
    }
}
