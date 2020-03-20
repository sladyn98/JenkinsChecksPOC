package com.example.ChecksPlugin;

import java.time.Instant;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.ChecksPlugin.Utils.convertStringtoJSONObject;
import static com.example.ChecksPlugin.Utils.readFileIntoString;


public class CheckRuns {

    static public void initiateCheckRun(JSONObject responseObject, String CheckRunID, String token) throws Exception {

        JSONObject repository = (JSONObject) responseObject.get("repository");
        String repositoryName = repository.getString("full_name");

        String url = "https://api.github.com/repos/" + repositoryName +"/check-runs/" + CheckRunID;

        //Construct JSON Object
        JSONObject jo = new JSONObject();
        jo.put("name", "WarningsPluginCheck");
        jo.put("status", "in_progress");
        jo.put("started_at", Instant.now().toString());
        
        CloseableHttpResponse initiateCheckRunResponse = HttpHelper.httpPatch(url, token, jo);
        if (initiateCheckRunResponse.getStatusLine().getStatusCode() == 200) {
            System.out.println("We are about to initiate CI service............");

            JSONObject responseEntity = Utils.convertPayloadToJSON(HttpHelper.getHttpResponse());
            System.out.println(responseEntity);
            JSONArray pullRequestsArray = responseEntity.getJSONArray("pull_requests");
            String pullRequestURL = pullRequestsArray.getJSONObject(0).getString("url");

            //I have populated files just because this is a POC
            GitUtils.populatePullRequestFiles(pullRequestURL + "/files");

            initiateCI(CheckRunID, repositoryName, token);
        }
    }

    static public void initiateCI(String CheckRunID, String repositoryName, String token) throws Exception {

        /*Strings required for creation of annotations object and initiating a CI*/
        String conclusion;
        JSONArray annotationsArray = new JSONArray();
        String summary;
        String text;

        /*Convert warnings report to json object*/
        JSONObject warningsReportObject = convertStringtoJSONObject(readFileIntoString());

        if (Integer.parseInt(warningsReportObject.getString("threshold")) == 0 ) {
            conclusion = "completed";

        } else {
            conclusion = "neutral";
            /*Create the annotations array*/
            annotationsArray = WarningsParser.createAnnotationsArray(warningsReportObject);
        }

        /*Complete summary and text fields*/
        summary = "This is the result of the Jenkins Checks plugin ";
        text = "Checks Plugin version: 0.0.1";

        String url = "https://api.github.com/repos/" + repositoryName +"/check-runs/" + CheckRunID;

        JSONObject jo = new JSONObject();
        jo.put("name", "WarningsPluginCheck");
        jo.put("status", "completed");
        jo.put("conclusion", conclusion);
        jo.put("completed_at", Instant.now().toString());

        /*Create output object*/
        JSONObject output = new JSONObject();
        output.put("title", "WarningsPluginCheck");
        output.put("summary", summary);
        output.put("text", text);
        output.put("annotations", annotationsArray);

        jo.put("output", output);
        HttpHelper.httpPatch(url, token, jo);
        System.out.println("We have successfully completed Check Run");
    }

}
