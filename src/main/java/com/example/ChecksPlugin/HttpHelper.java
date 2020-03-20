package com.example.ChecksPlugin;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpHelper {

    public static String httpResponse;

    public static String getHttpResponse() {
        return httpResponse;
    }

    public static void setHttpResponse(String httpResponse) {
        HttpHelper.httpResponse = httpResponse;
    }

    static public CloseableHttpResponse httpPatch(String url, String token, JSONObject jo) throws Exception {

        HttpPatch patch = new HttpPatch(url);
        patch.addHeader("Accept" , "application/vnd.github.antiope-preview+json");
        patch.addHeader("Authorization", token);

        StringEntity input = new StringEntity(jo.toString());
        input.setContentType("application/json");
        patch.setEntity(input);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(patch)) {
            String responseString = EntityUtils.toString(response.getEntity());
            setHttpResponse(responseString);
            return response;
        }
    }
}
