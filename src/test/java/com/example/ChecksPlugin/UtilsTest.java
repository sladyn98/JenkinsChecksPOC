package com.example.ChecksPlugin;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UtilsTest {

    @Test
    public void testFileToStringConversion() throws Exception{

        String warningsString = "{\n"
            + "  \"_class\":\"io.jenkins.plugins.analysis.core.restapi.ReportApi\",\n"
            + "  \"issues\":[\n"
            + "    {\n"
            + "      \"baseName\":\"AbstractParser.java\",\n"
            + "      \"category\":\"EXPERIMENTAL\",\n"
            + "      \"columnEnd\":0,\n"
            + "      \"columnStart\":0,\n"
            + "      \"description\":\"\",\n"
            + "      \"fileName\":\"/private/tmp/node1/workspace/Full Analysis - Model/src/main/java/edu/hm/hafner/analysis/AbstractParser.java\",\n"
            + "      \"fingerprint\":\"be18f803030f2af690fbeef09eafa5c9\",\n"
            + "      \"lineEnd\":59,\n"
            + "      \"lineStart\":59,\n"
            + "      \"message\":\"edu.hm.hafner.analysis.AbstractParser.parse(File, Charset, Function) may fail to clean up java.io.InputStream\",\n"
            + "      \"moduleName\":\"Static Analysis Model and Parsers\",\n"
            + "      \"origin\":\"spotbugs\",\n"
            + "      \"packageName\":\"edu.hm.hafner.analysis\",\n"
            + "      \"reference\":\"46\",\n"
            + "      \"severity\":\"LOW\",\n"
            + "      \"type\":\"OBL_UNSATISFIED_OBLIGATION\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"baseName\":\"ReportTest.java\",\n"
            + "      \"category\":\"STYLE\",\n"
            + "      \"columnEnd\":0,\n"
            + "      \"columnStart\":0,\n"
            + "      \"description\":\"\",\n"
            + "      \"fileName\":\"/private/tmp/node1/workspace/Full Analysis - Model/src/test/java/edu/hm/hafner/analysis/ReportTest.java\",\n"
            + "      \"fingerprint\":\"331d509297fad027813365ad0fb37e69\",\n"
            + "      \"lineEnd\":621,\n"
            + "      \"lineStart\":621,\n"
            + "      \"message\":\"Return value of Report.get(int) ignored, but method has no side effect\",\n"
            + "      \"moduleName\":\"Static Analysis Model and Parsers\",\n"
            + "      \"origin\":\"spotbugs\",\n"
            + "      \"packageName\":\"edu.hm.hafner.analysis\",\n"
            + "      \"reference\":\"46\",\n"
            + "      \"severity\":\"LOW\",\n"
            + "      \"type\":\"RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"baseName\":\"ReportTest.java\",\n"
            + "      \"category\":\"STYLE\",\n"
            + "      \"columnEnd\":0,\n"
            + "      \"columnStart\":0,\n"
            + "      \"description\":\"\",\n"
            + "      \"fileName\":\"/private/tmp/node1/workspace/Full Analysis - Model/src/test/java/edu/hm/hafner/analysis/ReportTest.java\",\n"
            + "      \"fingerprint\":\"1e641f9c0b35ed97140d639695e8ce18\",\n"
            + "      \"lineEnd\":624,\n"
            + "      \"lineStart\":624,\n"
            + "      \"message\":\"Return value of Report.get(int) ignored, but method has no side effect\",\n"
            + "      \"moduleName\":\"Static Analysis Model and Parsers\",\n"
            + "      \"origin\":\"spotbugs\",\n"
            + "      \"packageName\":\"edu.hm.hafner.analysis\",\n"
            + "      \"reference\":\"46\",\n"
            + "      \"severity\":\"LOW\",\n"
            + "      \"type\":\"RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"threshold\":3\n"
            + "}";
        System.out.println(Utils.readFileIntoString());
        Assert.assertEquals(warningsString, Utils.readFileIntoString());
    }

    @Test
    public void assertIssuesExistInReport() throws Exception {
        JSONObject warningsObject = Utils.convertStringtoJSONObject(Utils.readFileIntoString());
        JSONArray arr = warningsObject.getJSONArray("issues");
        for (int i = 0; i < arr.length(); i++) {
            String message = arr.getJSONObject(i).getString("message");
            System.out.println(message);
        }
    }
}