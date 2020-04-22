package stepDefinations;

import static org.testng.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.testng.Assert;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONArray;
public class getRequest {

	
	public static String UserToken = "";
	public static String pretty = "1";
	public static String channelName = "";
	public static String channelNewName = channelName + "new";
	public static String baseurl = "https://slack.com";
	public static String RequestURL = "";
	public static Response response;
	public static String channelID = "";
	public static String channelNameFromResponse = "";
	public static String channelCreator = "";
	public static String jsonPart = "";
	
	public int valueFinder(String value, String valueToSearch){
		
		value = value.replace("[", "");
		value = value.replace("]", "");
		value = value.replace(" ", "");
		List<String> list = new ArrayList<String>(Arrays.asList(value.split(",")));
		int count = 0;
		for(String nice : list)
		{
				if(nice.trim().equalsIgnoreCase(valueToSearch.trim()))
				{
					System.out.println(nice);
					System.out.println(count);
					break;
				}
				count ++;
		}
		return list.size() -3;
	}
	
	
	@Given("api URL {string}, ChannelNameRequired {string}, NewChannelNameRequired {string}, ChannelIDRequired {string}")
	public void api_URL_ChannelNameRequired_NewChannelNameRequired_ChannelIDRequired(String URL,
			String ChannelNameRequired, String NewChannelNameRequired, String ChannelIDRequired) {
		
		FileReader reader = null;
		try {
		reader = new FileReader("base.properties");
		Properties p=new Properties();  
	    p.load(reader);
	    UserToken = p.getProperty("UserToken");
		channelName = p.getProperty("ChannelName");
		
		} catch (IOException e) {
		}  
		
		
		if (Boolean.valueOf(ChannelNameRequired) == true && Boolean.valueOf(NewChannelNameRequired) == false
				&& Boolean.valueOf(ChannelIDRequired) == false) {

			// create and Join
			RequestURL = "/api/channels." + URL + "?token=" + UserToken + "&pretty=1" + "&name=" + channelName;

		} else if (Boolean.valueOf(ChannelNameRequired) == false && Boolean.valueOf(NewChannelNameRequired) == true
				&& Boolean.valueOf(ChannelIDRequired) == true) {
			
			// rename
			RequestURL = "/api/channels." + URL + "?token=" + UserToken + "&pretty=1" + "&name=" + channelNewName
					+ "&channel=" + channelID ;
		
		} else if (Boolean.valueOf(ChannelNameRequired) == false && Boolean.valueOf(NewChannelNameRequired) == false
				&& Boolean.valueOf(ChannelIDRequired) == true) {
		
			// archive
			RequestURL = "/api/channels." + URL + "?token=" + UserToken + "&pretty=1" + "&channel=" + channelID;
		
		} else {
			
			//list all
			RequestURL = "/api/channels." + URL + "?token=" + UserToken + "&pretty=1" ;
		}
	}

	@When("User hits the API")
	public void user_hits_the_API() {

		RestAssured.baseURI = baseurl;
		RequestSpecification request = RestAssured.given();
		response = request.post(RequestURL);

		if (RequestURL.contains("create")) {
			channelID = response.jsonPath().get("channel.id");
			channelNameFromResponse = response.jsonPath().get("channel.name");
			channelCreator = response.jsonPath().get("channel.creator");
		}
	}

	@Then("Verify the Success Response Status-Code {int}")
	public void verify_the_Success_Response_Status_Code(Integer int1) {
		Assert.assertEquals(response.getStatusCode(), 200, "Verifying the Success response code should be 200");
	}
	
	@Then("Verify the response received under {int} Second")
	public void verify_the_response_received_under_Second(Integer int1) {
		Assert.assertEquals(response.getTime() < 5000, true, "Verifying the response time should be less than 5 sec");
	}

	@Then("Verify the Schema of the Resp onse for {string}")
	public void verify_the_Schema_of_the_Response_for(String fileName) {
		FileReader json = null;
		try{
			json = new FileReader("src/main/java/resources/"+fileName+".json");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		JSONObject rawSchema = new JSONObject(json);
		Schema schema = SchemaLoader.load(rawSchema);
		try {
			schema.validate(response.getBody());
		} catch (ValidationException exception) {
			assertEquals(true, false,
					"Schema Validation Failed while checking is the Response schema is same or Not");
		}
		
	}

	@Then("Verify the property Channel-name in the response JSON")
	public void verify_the_property_Channel_name_in_the_response_JSON() {
			assertEquals(channelNameFromResponse.equalsIgnoreCase(channelName), true,"Verify the property Channel-name in the response JSON");
	}

	@Then("verify the success response of archive")
	public void verify_the_success_response_of_archive() {
		boolean flag =Boolean.valueOf(response.jsonPath().get("ok").toString()); 
		assertEquals(flag, true,"verify the success response of archive");
	
	}
	
	@Then("Verify the property Already-In-Channel should be true")
	public void verify_the_property_Already_In_Channel_should_be_true() {
		boolean flag =Boolean.valueOf(response.jsonPath().get("already_in_channel").toString()); 
		assertEquals(flag, true,"Verify the property Already-In-Channel should be true");

	}

	@Then("Verify the property Channel-id, Channel-name, Channel-Creator in the response JSON for join")
	public void verify_the_property_Channel_id_Channel_name_Channel_Creator_in_the_response_JSON_for_join() {

		assertEquals(channelID.equalsIgnoreCase(response.jsonPath().get("channel.id").toString()), true);
		assertEquals(channelName.equalsIgnoreCase(response.jsonPath().get("channel.name").toString()), true);
		assertEquals(channelCreator.equalsIgnoreCase(response.jsonPath().get("channel.creator").toString()), true);
	}
	
	@Then("Verify the property Channel-id, Channel-name, Channel-Creator in the response JSON for rename")
	public void verify_the_property_Channel_id_Channel_name_Channel_Creator_in_the_response_JSON_for_rename() {

		assertEquals(channelID.equalsIgnoreCase(response.jsonPath().get("channel.id").toString()), true);
		assertEquals(channelNewName.equalsIgnoreCase(response.jsonPath().get("channel.name").toString()), true);
		assertEquals(channelCreator.equalsIgnoreCase(response.jsonPath().get("channel.creator").toString()), true);
	}
	
		
	@Then("Verify the property Channel-id, Channel-Name, Channel-Creator in the response JSON")
	public void verify_the_property_Channel_id_Channel_Name_Channel_Creator_in_the_response_JSON() {
		
		String Jresponse = response.asString();
		JSONArray errorArray =JsonPath.read(Jresponse,"$.channels[?(@.name=='"+channelNewName+"')]");
		jsonPart = errorArray.get(0).toString();
		assertEquals(jsonPart.contains(channelID), true);
		assertEquals(jsonPart.contains(channelNewName), true);
		assertEquals(jsonPart.contains(channelCreator), true);

	}

	@Then("Verify the new channel which is been created and check the Archived Status which should be false")
	public void verify_the_new_channel_which_is_been_created_and_check_the_Archived_Status_which_should_be_false() {
		boolean flag =Boolean.valueOf(jsonPart.substring(jsonPart.indexOf("is_archived")+12,jsonPart.indexOf("is_general")-2)); 
		assertEquals(flag, false,"Verify the new channel which is been created and check the Archived Status which should be false");
		
	}
	@Then("Verify the new channel which is been created and check the Archived Status which should be true")
	public void verify_the_new_channel_which_is_been_created_and_check_the_Archived_Status_which_should_be_true() {
		boolean flag =Boolean.valueOf(jsonPart.substring(jsonPart.indexOf("is_archived")+12,jsonPart.indexOf("is_general")-2)); 
		assertEquals(flag, true,"Verify the new channel which is been created and check the Archived Status which should be false");
	}
}
