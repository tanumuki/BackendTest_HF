package resources;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetConnection {

	public static String getConnectionDetails(String url) throws IOException {

		/*
		 * Creating the URL connection object and parsing the response. Checking the
		 * response code as well for different HTTP status codes
		 */

		URL urlObj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			assertEquals(200, responseCode);
		}
		if (responseCode == (300 | 303 | 306)) {
			System.out.println("Redirection...");
		}
		if (responseCode == 403) {
			System.out.println("Forbidden...");
		}
		if (responseCode == 400) {
			System.out.println("Bad Request...");
		}
		if (responseCode == 500) {
			System.out.println("Internal Server Error...");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(urlObj.openStream()));

		String inputLine;
		// optimizing with String Buffer
		StringBuffer responseSB = new StringBuffer();
		while ((inputLine = reader.readLine()) != null) {
			responseSB.append(inputLine);
		}
		reader.close();
		return responseSB.toString();

	}

}
