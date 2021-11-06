package com.youtube.search.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpClient {

    private final CloseableHttpClient client;

    public HttpClient(CloseableHttpClient client) {

        this.client = client;
    }

    private String execute(String url, Map<String, String> headers, HttpRequestBase httpRequest) throws HttpException, IOException {

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpRequest.addHeader(entry.getKey(), entry.getValue());
        }

        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpRequest);
            String contentJson = EntityUtils.toString(response.getEntity());
            if(response == null || response.getStatusLine() == null){
                throw new HttpException("Something went wrong");
            }
            return contentJson;
        } catch (Exception e) {
            log.error("Failed to execute {} on {} {}", httpRequest.getMethod(), url, e.getMessage());
            throw new HttpException(e.getMessage() + "status code:" + response.getStatusLine().getStatusCode() ,e);
        } finally {
            httpRequest.releaseConnection();
            if (response != null)
                response.close();
        }
    }

    public String post(String url, String json) throws Exception {
        return post(url, json, Collections.emptyMap());
    }

    public String post(String url, String json, Map<String, String> headers) throws URISyntaxException, IOException, HttpException {
        URI uri = new URI(url);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        return execute(url, headers, httpPost);
    }

    public String get(String url, Map<String, String> queryParams, Map<String, String> headers) throws URISyntaxException, IOException, HttpException {
        URIBuilder uriBuilder = new URIBuilder(url);

        for (Map.Entry<String, String> queryParam : queryParams.entrySet()) {
            uriBuilder.addParameter(queryParam.getKey(), queryParam.getValue());
        }

        URI uri = uriBuilder.build();
        HttpGet httpGet = new HttpGet(uri);
        return execute(url, headers, httpGet);
    }

    public static Map<String, String> headers() {

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json; charset=UTF-8");
        return header;
    }
}
