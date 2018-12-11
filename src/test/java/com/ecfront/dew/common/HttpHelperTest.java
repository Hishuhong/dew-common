package com.ecfront.dew.common;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHelperTest {

    @Test
    public void testHttp() throws Exception {
        // get
        String result = $.http.get("https://httpbin.org/get");
        Assert.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        result = $.http.get("https://httpbin.org/get", new HashMap<String, String>() {{
            put("Customer-A", "AAA");
            put("Accept", "json");
        }});
        Assert.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assert.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        result = $.http.get("https://httpbin.org/get", new HashMap<String, String>() {{
            put("Customer-A", "AAA");
            put("Accept", "json");
        }}, "application/json; charset=utf-8", "utf-8", 5000,5000);
        Assert.assertEquals("AAA", $.json.toJson(result).get("headers").get("Customer-A").asText());
        Assert.assertEquals("json", $.json.toJson(result).get("headers").get("Accept").asText());
        Assert.assertEquals("application/json; charset=utf-8", $.json.toJson(result).get("headers").get("Content-Type").asText());
        // delete
        result = $.http.delete("https://httpbin.org/delete");
        Assert.assertEquals("httpbin.org", $.json.toJson(result).get("headers").get("Host").asText());
        // post - data
        result = $.http.post("https://httpbin.org/post", "some data");
        Assert.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // post - form
        result = $.http.post("https://httpbin.org/post", new HashMap<String, Object>() {{
            put("a", "1");
        }}, "application/x-www-form-urlencoded");
        result = $.http.post("https://httpbin.org/post", "custname=%E5%8C%BF%E5%90%8D&size=small&topping=cheese&topping=onion", "application/x-www-form-urlencoded");
        Assert.assertEquals("匿名", $.json.toJson(result).get("form").get("custname").asText());
        Assert.assertEquals("small", $.json.toJson(result).get("form").get("size").asText());
        Assert.assertEquals("onion", $.json.toJson(result).get("form").get("topping").get(1).asText());
        // post - file
        result = $.http.post("https://httpbin.org/post", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        // put - data
        result = $.http.put("https://httpbin.org/put", "some data");
        Assert.assertEquals("some data", $.json.toJson(result).get("data").asText());
        // put - form
        result = $.http.put("https://httpbin.org/put", new HashMap<String, Object>() {{
            put("a", "1");
        }}, "application/x-www-form-urlencoded");
        Assert.assertEquals("1", $.json.toJson(result).get("form").get("a").asText());
        // put - file
        result = $.http.put("https://httpbin.org/put", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        // put with head
        HttpHelper.ResponseWrap responseWrap = $.http.putWrap("https://httpbin.org/put", new File(this.getClass().getResource("/").getPath() + "conf1.json"));
        Assert.assertEquals("1", $.json.toJson($.json.toJson(result).get("files").get("conf1.json").asText()).get("a").asText());
        Assert.assertEquals("application/json", responseWrap.head.get("Content-Type").get(0));
        // head
        Map<String, List<String>> head = $.http.head("https://httpbin.org/get");
        Assert.assertEquals("application/json", head.get("Content-Type").get(0));
        // options
        head = $.http.options("https://httpbin.org/get");
        Assert.assertTrue(head.get("Allow").get(0).contains("GET"));
    }

}