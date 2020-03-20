package com.example.ChecksPlugin;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class Utils {

    	static public JSONObject convertPayloadToJSON(String payload) throws Exception{
		return new JSONObject(payload);
		}

		static public String readFileIntoString() throws Exception {
			String fileString = new String(Files.readAllBytes(Paths.get("/home/sladyn/GithubExample/ChecksPlugin/src/main/resources/static/warningsReport.json")), StandardCharsets.UTF_8);
			return fileString;
		}

		static public JSONObject convertStringtoJSONObject(String jsonString) throws Exception {
    		return new JSONObject(jsonString);
		}

		static public JSONArray convertStringtoJSONArray(String jsonString) throws Exception {
    		return new JSONArray(jsonString);
		}
}
