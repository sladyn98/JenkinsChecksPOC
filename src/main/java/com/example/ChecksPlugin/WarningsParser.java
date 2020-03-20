package com.example.ChecksPlugin;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.ChecksPlugin.GitUtils.getPullRequestFiles;

public class WarningsParser {


    /*Strings required for creation of annotations object and initiating a CI*/
    private static int startLine;
    private static int endLine;
    private static int startColumn;
    private static int endColumn;
    private static String message;
    private static JSONArray annotationsArray = new JSONArray();
    private static String filePath;

    static public JSONArray createAnnotationsArray(JSONObject warningsReportObject) throws Exception {

        JSONArray issuesArray = warningsReportObject.getJSONArray("issues");
        for (int i = 0; i < issuesArray.length(); i++) {
            /*Create a new annotations object while iterating through every error*/
            JSONObject annotation = new JSONObject();

            message = issuesArray.getJSONObject(i).getString("message");
            startLine = Integer.parseInt(issuesArray.getJSONObject(i).getString("lineStart"));
            endLine = Integer.parseInt(issuesArray.getJSONObject(i).getString("lineEnd"));
            startColumn = Integer.parseInt(issuesArray.getJSONObject(i).getString("columnStart"));
            endColumn = Integer.parseInt(issuesArray.getJSONObject(i).getString("columnEnd"));

            /*For now I have just populated the files from the pull request because of the POC, this will change because
            * the warnings file will give me the correct name.*/
            JSONArray jsonArray = getPullRequestFiles();
            filePath = jsonArray.getJSONObject(0).getString("filename");

            /*Fill the annotations object*/
            annotation.put("path",filePath);
            annotation.put("start_line",startLine);
            annotation.put("end_line",endLine);
            annotation.put("annotation_level","warning");
            annotation.put("start_column",startColumn);
            annotation.put("end_column", endColumn);
            annotation.put("message", message);

            annotationsArray.put(annotation);
        }
        return  annotationsArray;
    }

}
