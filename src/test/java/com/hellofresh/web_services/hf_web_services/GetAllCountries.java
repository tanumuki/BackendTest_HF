package com.hellofresh.web_services.hf_web_services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.Assert;
import resources.GetConnection;
import resources.Utilities;

public class GetAllCountries {
	
	/*
	 * Log4J will collect all the logs in the log file under logs in the project directory
	 */

	public static Logger log = LogManager.getLogger(GetAllCountries.class.getName());
	
	@Test(priority = 1)
	public void testCountries() throws IOException {
		/*
		 * Calling the GetConnection class and invoking method getConnectionDetails for
		 * parsing the json response from the url
		 */
		String url = "http://services.groupkt.com/country/get/all";
		String response = GetConnection.getConnectionDetails(url);
		// System.out.println(response);
		/*
		 * Validating the response per the test case -Get all countries and validate
		 * that US, DE and GB were returned in the response Fetching the countries from
		 * the JSON and validating it through regex
		 */

		JsonObject jsonResp = Utilities.getInstance().getGson().fromJson(response, JsonObject.class);
		ArrayList<String> countryList = new ArrayList<String>();
		JsonArray responseList = jsonResp.get("RestResponse").getAsJsonObject().get("result").getAsJsonArray();
		// Traversing through the array
		for (int i = 0; i < responseList.size(); i++) {
			JsonObject result = responseList.get(i).getAsJsonObject();
			// Fetching the name for countries
			String country = result.get("alpha2_code").getAsString();
			// Adding all the country names to the arrayList we created
			countryList.add(country);
		}
		// checking if these countries are present in the list - US, DE and GB
		Assert.assertTrue(countryList.contains("US"));
		Assert.assertTrue(countryList.contains("DE"));
		Assert.assertTrue(countryList.contains("GB"));
		log.info("All assertions done for US,DE and GB");
		
		
	}

	@Test(priority = 2)
	public void testIndividualCountry() throws IOException {

		// based upon the test case, here the request is being looped with the country
		// codes
		String countries[] = { "US", "DE", "GB", "qeefmcdc" };
		for (int i = 0; i < countries.length; i++) {
			validateCountryResponse(countries[i]);
		}		
		log.info("Individual contries tested!!");

	}

	@Test(priority = 3)
	public void testNewCountryAddition() throws ClientProtocolException, IOException {
		/*
		 * Post Methods can be executed using third party wrappers like apache. So,
		 * httpClient is being used here However it can also be done by getting an
		 * instance of java.net.HttpURLConnection object to send a HTTP request to the
		 * server and receive a HTTP response from the server but apache client is
		 * widely used and even its easier with adding params in body, say adding some
		 * json in body becomes a lot easier.
		 * Here we are adding the parameters in body via an arraylist with nameValuePair as an argument. Multiple params can be added like this
		 * 
		 */

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost;
		ArrayList<NameValuePair> postParameters;
		httpPost = new HttpPost("http://services.groupkt.com/country/post/");
		postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("name", "Test Country"));
		postParameters.add(new BasicNameValuePair("alpha2_code", "TC"));
		postParameters.add(new BasicNameValuePair("alpha3_code", "TCY"));
		httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
		HttpResponse objResponse = httpClient.execute(httpPost);
		System.out.println("URL is " + objResponse);
		HttpEntity entity = objResponse.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		System.out.println(responseString);
		JsonObject resp = Utilities.getInstance().getGson().fromJson(responseString, JsonObject.class);
		System.out.println(resp);

	}

	public void validateCountryResponse(String countryCode) throws IOException {
		System.out.println(countryCode);
		/*
		 * Calling the GetConnection class and invoking method getConnectionDetails for
		 * parsing the json response from the url
		 */
		String url = "http://services.groupkt.com/country/get/iso2code/" + countryCode;
		String response = GetConnection.getConnectionDetails(url);
		/*
		 * Validating the response per the test case -Get all countries and validate
		 * that US, DE and GB were returned in the response Fetching the countries from
		 * the JSON and validating it through regex
		 */

		JsonObject jsonResp = Utilities.getInstance().getGson().fromJson(response, JsonObject.class);
		JsonObject respObj = jsonResp.get("RestResponse").getAsJsonObject();
		Utilities.validateCountryResponse(respObj);

	}

}
