package br.com.facom.poo2.voxxx.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


//singleton
public class MongoREST {
	private static final String API_KEY = "apiKey=lRrmytuyKlt3I1v6K-WQsItHLYJdOio";//4
	private static final String BASE_URL = "https://api.mongolab.com/api/1/";
	private static final String DATABASE_NAME = "databases/voxxx/";
	private static String COLLECTION_NAME = "collections/records/";
	
	private static MongoREST self;
	
	private MongoREST() {
	}
	
	public static MongoREST getSelf(){
		if(self == null){
			self = new MongoREST();
		}
		return self;
	}

	private String GET(String url) {
		String result = null;
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");

			int responseCode = con.getResponseCode();
			Log.e("HTTP_GET", "Sending 'GET' request to URL : " + url);
			Log.e("HTTP_GET", "Response Code : " + responseCode);

			result = convertInputStreamToString(con.getInputStream());

		} catch (Exception e) {
			Log.e("FAIL_HTTP_GET",
					(e.getMessage() == null) ? "HTTP GET Failed!" : e
							.getMessage());
		}
		return result;
	}

	private String POST(String url, JSONObject jsonObject) {
		InputStream inputStream = null;
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			StringEntity se = new StringEntity(jsonObject.toString());
			httpPost.setEntity(se);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpclient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);

		} catch (Exception e) {
			Log.e("FAIL_HTTP_POST",
					(e.getMessage() == null) ? "HTTP POST Failed!" : e
							.getMessage());
		}
		return result;
	}

	private String convertInputStreamToString(InputStream inputStream) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			while ((line = bufferedReader.readLine()) != null)
				result.append(line);
			inputStream.close();
		} catch (IOException e) {
			Log.e("FAIL", "Could not convert your InputStream into String \n"
					+ e.getMessage());
		}
		return result.toString();
	}

	public String getDocuments(String query) {
		String FULL_URL = BASE_URL + DATABASE_NAME
				+ COLLECTION_NAME + "?" + API_KEY;
		
		String url = FULL_URL;
		if (query != "") {
			url = url.concat("&q=" + query);
		}
		return GET(url);
	}
	
	public boolean insertScore(VoxxxRecord score) {
		String FULL_URL = BASE_URL + DATABASE_NAME
				+ COLLECTION_NAME + "?" + API_KEY;
		
		String postResult = "";
		try {
			JSONObject params = new JSONObject();
			params.accumulate("filename", score.getFileName());
			params.accumulate("recordDay", score.getRecordDay());
			params.accumulate("starttime", score.getStartTimeAsDate()
					.toString());
			params.accumulate("duration", score.getDuration());
			params.accumulate("peeks", score.getPeeks());
			postResult = POST(FULL_URL, params);
		} catch (Exception e) {
			Log.e("FAIL",
					"Could not mount/send this request...." + e.getMessage());
		}
		return postResult.contains("_id");
	}

	
	public double extractDayAvg(String recordsOfToday) {
		double avg = 0;
		double accumulatedScore = 0;
		try {
			final JSONArray results = new JSONArray(recordsOfToday);
			Log.d("RECORDS", "Lenght: "+results.length());
			for(int i =0; i< results.length(); i++){
				final JSONObject each = results.getJSONObject(i);
				VoxxxRecord result = new VoxxxRecord(each.getString("filename"));
				result.setDuration(each.getDouble("duration"));
				result.setPeeks(each.getInt("peeks"));
				
				accumulatedScore += result.getScore();
			}
			if(results.length() > 0)
				avg = accumulatedScore / results.length();
			
		} catch (JSONException e) {
			Log.e("FAIL",
					"Could not extract day avg from this results...." + e.getMessage());
		}
		Log.d("RECORDS", "Total: "+accumulatedScore+"\n AVG: "+ avg);
		return avg;
	}
	
	public void setCollection(String collection){
		COLLECTION_NAME = "collections/"+collection+"/";
	}
}
