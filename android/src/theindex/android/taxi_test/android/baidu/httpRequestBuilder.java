package theindex.android.taxi_test.android.baidu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class httpRequestBuilder {
	String passageNameString = "某先生/女士";
	int passagePhoneNumber = 96106;
	String startStreetName = "起点";
	String destStreetName = "终点";
	String timeToUsed = "now";
	
	int    passageNumber = 1;
	int    carType = 0;
	boolean VipPassage = false;
	
	//Int64 orderId = 13911650018 + getsystemtime(); 
	public httpRequestBuilder()
	{
	
	}
	// Fast Implementation
	static public StringBuilder inputStreamToString(InputStream is)  {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    
	    // Wrap a BufferedReader around the InputStream
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	    // Read response until the end
	    try { 
	    	while ((line = rd.readLine()) != null) { 
	        total.append(line); 
	    }		
		} catch (Exception e) {
			// TODO: handle exception
		}
	   
	    
	    // Return full string
	    return total;
	}

	public HttpResponse postOrderData(httpRequestBuilder requestBuilder) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://50.57.84.117/taxi/order.php");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("name", "12345"));
			nameValuePairs.add(new BasicNameValuePair("phone_num","AndDev is Cool!"));
			nameValuePairs.add(new BasicNameValuePair("start_point", "12345"));
			nameValuePairs.add(new BasicNameValuePair("end_point","AndDev is Cool!"));
			nameValuePairs.add(new BasicNameValuePair("time", "12345"));
			nameValuePairs.add(new BasicNameValuePair("count","AndDev is Cool!"));
			nameValuePairs.add(new BasicNameValuePair("carType", "12345"));
			nameValuePairs.add(new BasicNameValuePair("vip","AndDev is Cool!"));
			nameValuePairs.add(new BasicNameValuePair("order", "12345"));
			nameValuePairs.add(new BasicNameValuePair("getCar","AndDev is Cool!"));
			
			
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			return response;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}

	//int = 13501341544080801
	public HttpResponse RequestTaxiDataByID(String id) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://50.57.84.117/taxi/location.php");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		
			nameValuePairs.add(new BasicNameValuePair("order", "13501341544080801"));
			nameValuePairs.add(new BasicNameValuePair("getLoc",""));
				
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			return response;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
}
