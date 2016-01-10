package com.sheremet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.images.Image;


public class API {

	private final String access_token;
	public API(String access_token) {
		this.access_token=access_token;
	}
	public JSONObject photos_getMarketUploadServer(long group_id, boolean main_photo, long crop_x, long crop_y, long crop_width) throws IOException, ParseException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("group_id", group_id+"");
		map.put("crop_x", crop_x+"");
		map.put("crop_y", crop_y+"");
		map.put("crop_width", crop_width+"");
		map.put("main_photo", (main_photo?1:0)+"");
		map.put("access_token", access_token);
		map.put("v", "5.42");
		URL url = Utils.urlWithParams("https://api.vk.com/method/photos.getMarketUploadServer", map, true);
		HttpURLConnection connection = (HttpURLConnection) url .openConnection();
		connection.connect();
		JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
		return json;
	}

	public JSONObject photos_uploadToServer(URL uploadurl, InputStream is, String filename) throws ClientProtocolException, IOException  {
		JSONObject obj = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost(uploadurl.toString());
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file", is, ContentType.APPLICATION_OCTET_STREAM, "photo"+new Random().nextLong()+".jpg");
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		CloseableHttpResponse response = httpClient.execute(uploadFile);
		HttpEntity responseEntity = response.getEntity();
		try {
			 obj = (JSONObject) new JSONParser().parse(new InputStreamReader(responseEntity.getContent()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	public JSONObject photos_uploadToServer2(URL uploadurl, InputStream is, String filename) throws ClientProtocolException, IOException  {
		JSONObject obj = null;
		CustomMultipart multipart = new CustomMultipart(uploadurl.toString(), "utf-8");
		multipart.addFilePart("file", is, filename);
		InputStream is2 = multipart.finish();
		try {
			 obj = (JSONObject) new JSONParser().parse(new InputStreamReader(is2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public JSONObject photos_saveMarketPhoto(long group_id, String photo, long server, String hash, String crop_data, String crop_hash) throws IOException, ParseException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("group_id", group_id+"");
		map.put("photo", photo);
		map.put("server", server+"");
		map.put("hash", hash);
		map.put("crop_data", crop_data+"");
		map.put("crop_hash", crop_hash);
		map.put("access_token", access_token);
		map.put("v", "5.42");
		URL url = Utils.urlWithParams("https://api.vk.com/method/photos.saveMarketPhoto", map, true);
		HttpURLConnection connection = (HttpURLConnection) url .openConnection();
		connection.connect();
		JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
		return json;
	}
	public JSONObject market_add(long owner_id, String name, String description, long category_id, double price, boolean deleted, long main_photo_id, List<Integer> photo_ids_list) throws IOException, ParseException {
		String photo_ids = Utils.toSeparatedList(photo_ids_list, ",");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("owner_id", owner_id+"");
		map.put("name", name);
		map.put("description", description);
		map.put("category_id", category_id+"");
		map.put("price", ((int)price)+"."+((int)(price*100))%100);
		map.put("deleted", (deleted?1:0)+"");
		map.put("main_photo_id", main_photo_id+"");
		map.put("photo_ids", photo_ids);
		map.put("access_token", access_token);
		map.put("v", "5.42");
		URL url = Utils.urlWithParams("https://api.vk.com/method/market.add", map, true);
		HttpURLConnection connection = (HttpURLConnection) url .openConnection();
		connection.connect();
		JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
		return json;
	} 
	public static void main(String[] args) {
		String token = "371e92efbb275ed41e903029958644a8988a2dd19e9b24b0ec9852f3510a85c98a228ef2f9369300a0f7d";
		API api = new API(token);
		try {
			URL url = new URL("https://pp.vk.me/c627216/v627216694/13403/Yq47bYJeAf4.jpg");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			Image image = Utils.getImage(connection.getInputStream());
			int crop_x = (image.getWidth()-image.getHeight())/2;
			crop_x = crop_x<0?0:crop_x;
			int crop_y = (image.getHeight()-image.getWidth())/2;
			crop_y = crop_y<0?0:crop_y;
			int min_w = Math.min(image.getHeight(), image.getWidth())-1;
			JSONObject resp1 = api.photos_getMarketUploadServer(54919986,true, crop_x, crop_y, 403);
			String o =  (String) ((JSONObject) resp1.get("response")).get("upload_url");
			System.out.println(o);
			JSONObject resp2 = api.photos_uploadToServer2(new URL(o), new ByteArrayInputStream(image.getImageData()), "photo.jpg");
			System.out.println(resp2.toJSONString());
			JSONObject resp3 = api.photos_saveMarketPhoto(54919986, (String)resp2.get("photo"), (Long)resp2.get("server"), (String)resp2.get("hash"), (String)resp2.get("crop_data"),(String) resp2.get("crop_hash"));
			System.out.println(resp3.toJSONString());
			Object photo_id = ((JSONObject)((JSONArray)resp3.get("response")).get(0)).get("id");
			System.out.println(photo_id);
			JSONObject resp4 = api.market_add(-54919986L, "Name"+new Random().nextInt(), "Description"+new Random().nextLong(), 5L, 902.2, true, (Long)photo_id, new LinkedList<Integer>());
			System.out.println(resp4.toJSONString());
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}
