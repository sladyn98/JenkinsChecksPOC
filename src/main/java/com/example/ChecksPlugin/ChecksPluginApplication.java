package com.example.ChecksPlugin;


import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.kohsuke.github.GHApp;
import org.kohsuke.github.GHAppInstallation;
import org.kohsuke.github.GHAppInstallationToken;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.example.ChecksPlugin.JwtHelper.createJWT;

@RestController
@SpringBootApplication
public class ChecksPluginApplication {

	private static String APP_IDENTIFIER="53528";

	@RequestMapping(value = "/event_handler", method = RequestMethod.POST)
	public void handleEvent(@RequestBody String requestBodyString) throws Exception {
		System.out.println("Received event handler event");
		String token = authenticateApplication();
		JSONObject requestJSON = Utils.convertPayloadToJSON(requestBodyString);
		System.out.println(requestBodyString);

		if (requestJSON.has("check_suite")) {
            if (requestJSON.getString("action").equals("requested") || requestJSON
                .getString("action").equals("rerequested")) {
                createCheckRun(requestJSON, token);
            }

            if (requestJSON.getString("action").equals("created")) {
                System.out.println("Initiating check run because we have received check run event");
            }
        }

		else {
            System.out.println("The GithubChecks Application has been successfully installed");
        }
	}

	private void createCheckRun(JSONObject requestJSON, String accessToken) throws Exception {
		System.out.println("Creating check run");

		JSONObject repository = (JSONObject) requestJSON.get("repository");
		String repositoryName = repository.getString("full_name");
		String url = "https://api.github.com/repos/" + repositoryName +"/check-runs";

		// Obtain Head SHA
		JSONObject checkSuiteObject = (JSONObject) requestJSON.get("check_suite");
		String headSHA = checkSuiteObject.getString("head_sha");

		//Construct JSON Object
		JSONObject jo = new JSONObject();
		jo.put("name", "WarningsPluginCheck");
		jo.put("head_sha", headSHA);

		HttpPost post = new HttpPost(url);
		post.addHeader( "Accept" , "application/vnd.github.antiope-preview+json");
        String token = "token " + accessToken;
		post.addHeader("Authorization", token);

		StringEntity input = new StringEntity(jo.toString());
		input.setContentType("application/json");
		post.setEntity(input);

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(post)) {
			String responseString = EntityUtils.toString(response.getEntity());
			System.out.println(responseString);
			JSONObject responseEntity = Utils.convertPayloadToJSON(responseString);
			String checkRunID = responseEntity.getString("id");
			if(response.getStatusLine().getStatusCode()==201) {
				CheckRuns.initiateCheckRun(requestJSON, checkRunID, token);
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ChecksPluginApplication.class, args);
	}

	@SuppressWarnings("deprecation")
	private static String authenticateApplication() {
		try {
			String jwtToken = createJWT(APP_IDENTIFIER, 600000);
			GitHub gitHubApp = new GitHubBuilder().withEndpoint("https://api.github.com").withJwtToken(jwtToken).build();
			GHApp app = gitHubApp.getApp();
			System.out.println(app.getName());
			System.out.println(app.getOwner());

			List<GHAppInstallation> appInstallations = app.listInstallations().asList();
			if (!appInstallations.isEmpty()) {
				GHAppInstallation appInstallation = appInstallations.get(0);
				GHAppInstallationToken appInstallationToken = appInstallation
					.createToken(appInstallation.getPermissions())
					.create();
				return appInstallationToken.getToken();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

