package com.sheremet;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

@SuppressWarnings("serial")
public class VKUploadServlet extends HttpServlet {
	DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	public static String token=null;
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("https://pu.vk.com/c629421/upload.php?act=do_add&mid=210823731&aid=-53&gid=54919986&hash=f8abcc4244f66a1b9b33265ab0e296f4&rhash=17254ef4c287c9666fc9fae52fa7f74a&swfupload=1&api=1&market_main_photo=1&_crop=0,0,200");

		URL url = new URL("https://pp.vk.me/c627624/v627624694/127f3/-4rLxuMTGw8.jpg");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		builder.addBinaryBody("file", connection.getInputStream(), ContentType.APPLICATION_OCTET_STREAM, "photo11.jpg");
		HttpEntity multipart = builder.build();

		uploadFile.setEntity(multipart);

		CloseableHttpResponse response = httpClient.execute(uploadFile);
		HttpEntity responseEntity = response.getEntity();
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new InputStreamReader(responseEntity.getContent()));
			System.err.println(obj.toJSONString());
			
			url = new URL("https://api.vk.com/method/photos.saveMarketPhoto?group_id=54919986&photo="+obj.get("photo")+"&server="+obj.get("server")+"&hash="+obj.get("hash")+"&crop_data="+obj.get("crop_data")+"&crop_hash="+obj.get("crop_hash")+"&v=5.42&access_token="+token);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
			System.err.println(json.toJSONString());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("https://pu.vk.com/c629421/upload.php?act=do_add&mid=210823731&aid=-53&gid=54919986&hash=f8abcc4244f66a1b9b33265ab0e296f4&rhash=17254ef4c287c9666fc9fae52fa7f74a&swfupload=1&api=1&market_main_photo=1&_crop=0,0,200");

		URL url = new URL("https://pp.vk.me/c627624/v627624694/127f3/-4rLxuMTGw8.jpg");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		builder.addBinaryBody("file", connection.getInputStream(), ContentType.APPLICATION_OCTET_STREAM, "photo11.jpg");
		HttpEntity multipart = builder.build();

		uploadFile.setEntity(multipart);

		CloseableHttpResponse response = httpClient.execute(uploadFile);
		HttpEntity responseEntity = response.getEntity();
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new InputStreamReader(responseEntity.getContent()));
			System.err.println(obj.toJSONString());
			
			url = new URL("https://api.vk.com/method/photos.saveMarketPhoto?group_id=54919986&photo="+obj.get("photo")+"&server="+obj.get("server")+"&hash="+obj.get("hash")+"&crop_data="+obj.get("crop_data")+"&crop_hash="+obj.get("crop_hash")+"&v=5.42&access_token="+token);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
			System.err.println(json.toJSONString());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
}
