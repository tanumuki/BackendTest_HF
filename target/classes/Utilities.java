package resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import junit.framework.Assert;

public class Utilities {

	/*
	 * Gson is a singleton pattern. It will only have one instance running
	 */
	
	private Gson gson = new Gson();
	SoftAssert softAssertion = new SoftAssert();
	public static Logger log = LogManager.getLogger(Utilities.class.getName());

	static Utilities helper;

	public static Utilities getInstance() {
		if (helper == null)
			helper = new Utilities();
		return helper;
	}

	public Gson getGson() {
		return gson;
	}

	public static void validateCountryResponse(JsonObject response) {

		// Checking for JsonNull
		if (response.isJsonNull()) {
			log.fatal("There is no response body");
			Assert.fail("There is no response body!!");
		}

		String message = response.get("messages").getAsString();
		// Checking is message is not null
		Assert.assertTrue(message != null || message != "");
		if (response.has("result")) {
			String name = response.get("result").getAsJsonObject().get("name").getAsString();
			String alpha2Code = response.get("result").getAsJsonObject().get("alpha2_code").getAsString();
			String alpha3Code = response.get("result").getAsJsonObject().get("alpha3_code").getAsString();

			switch (alpha2Code) {

			case "GB":
				Assert.assertTrue(name.matches("United Kingdom of Great Britain and Northern Ireland"));
				Assert.assertTrue(alpha2Code.matches("GB"));
				Assert.assertTrue(alpha3Code.matches("GBR"));
				log.info("GB validated!");
				break;
			case "DE":
				Assert.assertTrue(name.matches("Germany"));
				Assert.assertTrue(alpha2Code.matches("DE"));
				Assert.assertTrue(alpha3Code.matches("DEU"));
				log.info("DE validated!");
				break;
			case "US":
				Assert.assertTrue(name.matches("United States of America"));
				Assert.assertTrue(alpha2Code.matches("US"));
				Assert.assertTrue(alpha3Code.matches("USA"));
				log.info("US validated!");
				break;
			}

		} else {
			new SoftAssert().fail("No results found for the particular code!");
			log.info("No results found for the particular code!");
		}

	}

}
