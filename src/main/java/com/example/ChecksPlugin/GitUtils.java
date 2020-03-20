package com.example.ChecksPlugin;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

public class GitUtils {


    public static JSONArray pullRequestFiles;

    public static JSONArray getPullRequestFiles() {
        return pullRequestFiles;
    }

    public static void setPullRequestFiles(JSONArray pullRequestFiles) {
        GitUtils.pullRequestFiles = pullRequestFiles;
    }

    public static void populatePullRequestFiles(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get)) {
            String responseString = EntityUtils.toString(response.getEntity());
            setPullRequestFiles(Utils.convertStringtoJSONArray(responseString));
            JSONArray jsonArray = getPullRequestFiles();
            System.out.println(jsonArray.getJSONObject(0).getString("filename"));
        }
    }
}
