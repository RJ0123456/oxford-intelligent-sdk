package com.microsoft.projectoxford.text.rest;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class WebServiceRequest {
    private static final String headerKey = "ocp-apim-subscription-key";
    private HttpClient client = new DefaultHttpClient();
    private String subscriptionKey;
    private Gson gson = new Gson();

    public WebServiceRequest(String key) {
        this.subscriptionKey = key;
    }

    public Object request(String url, Map<String, Object> data, String contentType) throws TextServiceException {
        return post(url, data, contentType);
    }

    private Object post(String url, Map<String, Object> data, String contentType) throws TextServiceException {
        HttpPost request = new HttpPost(url);

        boolean isStream = false;
        String strContentType = "application/json";

        /*Set header*/
        if (contentType != null && !contentType.isEmpty()) {
            request.setHeader(HTTP.CONTENT_TYPE, contentType);
            strContentType = contentType;
            if (contentType.toLowerCase().contains("octet-stream")) {
                isStream = true;
            }
        } else {
            request.setHeader("Content-Type", strContentType);
        }

        request.setHeader(headerKey, this.subscriptionKey);

        try {
            if (!isStream) {
                if (strContentType.toLowerCase().contains("application/json")) {
                    String json = this.gson.toJson(data).toString();
                    StringEntity entity = new StringEntity(json);
                    request.setEntity(entity);
                } else if (strContentType.toLowerCase().contains("application/x-www-form-urlencoded")) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(this.convertMapToNameValuePair(data), "UTF-8");
                    request.setEntity(entity);
                }
            } else {
                request.setEntity(new ByteArrayEntity((byte[]) data.get("data")));
            }

            HttpResponse response = this.client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200 || statusCode == 201) {
                return readInput(response.getEntity().getContent());
            } else {
                throw new Exception("Error executing POST request! Received error code: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new TextServiceException(e.getMessage());
        }
    }

    private String readInput(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuffer json = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            json.append(line);
        }

        return json.toString();
    }

    private List<NameValuePair> convertMapToNameValuePair(Map<String, Object> data) {
        List<NameValuePair> result = new ArrayList<NameValuePair>();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                result.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
        }

        return result;
    }
}
