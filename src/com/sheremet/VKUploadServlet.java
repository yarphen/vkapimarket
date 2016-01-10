package com.sheremet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;

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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.images.Image;

@SuppressWarnings("serial")
public class VKUploadServlet extends HttpServlet {
	DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
//		PrintStream std_err = System.err;
//		System.setErr(new PrintStream(resp.getOutputStream()));
		try {
			Query q = new Query("user");
			q.addFilter("auth", Query.FilterOperator.EQUAL, req.getCookies()[0].getValue());
			PreparedQuery pq = db.prepare(q);
			String token = (String) pq.asSingleEntity().getProperty("token");
			API api = new API(token);
			URL url = new URL(req.getParameter("image"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			Image image = Utils.getImage(connection.getInputStream());
			int crop_x = (image.getWidth()-image.getHeight())/2;
			crop_x = crop_x<0?0:crop_x;
			int crop_y = (image.getHeight()-image.getWidth())/2;
			crop_y = crop_y<0?0:crop_y;
			int min_w = Math.min(image.getHeight(), image.getWidth())-1;
			JSONObject resp1 = api.photos_getMarketUploadServer(Integer.parseInt(req.getParameter("group")),true, Integer.parseInt(req.getParameter("crop_x")), Integer.parseInt(req.getParameter("crop_y")), Integer.parseInt(req.getParameter("crop_width")));
			System.err.println(resp1.toJSONString());
			String o =  (String) ((JSONObject) resp1.get("response")).get("upload_url");
			System.err.println(o);
			JSONObject resp2 = api.photos_uploadToServer2(new URL(o), new ByteArrayInputStream(image.getImageData()), "photo.jpg");
			System.err.println(resp2.toJSONString());
			JSONObject resp3 = api.photos_saveMarketPhoto(Integer.parseInt(req.getParameter("group")), (String)resp2.get("photo"), (Long)resp2.get("server"), (String)resp2.get("hash"), (String)resp2.get("crop_data"),(String) resp2.get("crop_hash"));
			System.err.println(resp3.toJSONString());
			Object photo_id = ((JSONObject)((JSONArray)resp3.get("response")).get(0)).get("id");
			System.err.println(photo_id);
			JSONObject resp4 = api.market_add(-Integer.parseInt(req.getParameter("group")),req.getParameter("name"), req.getParameter("descr"), Integer.parseInt(req.getParameter("category")), Double.parseDouble(req.getParameter("price")), Boolean.parseBoolean(req.getParameter("deleted")), (Long)photo_id, new LinkedList<Integer>());
			System.err.println(resp4.toJSONString());
			resp.sendRedirect("/");
		} catch (ClientProtocolException | ParseException  e) {
			e.printStackTrace();
//			System.setErr(std_err);
		}
	}
	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("https://pu.vk.com/c633225/upload.php?act=do_add&mid=210823731&aid=-53&gid=54919986&hash=f9b85d32c7d976d083105ca3576980d4&rhash=cb1a5c667f952d94a8280c60be3ffa24&swfupload=1&api=1&market_main_photo=1&_crop=0,0,500");

		URL url = new URL("https://pp.vk.me/c627624/v627624694/127c8/yrtOtALlyDs.jpg");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		builder.addBinaryBody("file", connection.getInputStream(), ContentType.APPLICATION_OCTET_STREAM, "photo"+new Random().nextLong()+".jpg");
		HttpEntity multipart = builder.build();

		uploadFile.setEntity(multipart);
		CloseableHttpResponse response = httpClient.execute(uploadFile);
		HttpEntity responseEntity = response.getEntity();
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new InputStreamReader(responseEntity.getContent()));
			System.err.println(obj.toJSONString());
			
			//url = new URL("https://api.vk.com/method/photos.saveMarketPhoto?group_id=54919986&photo="+obj.get("photo")+"&server="+obj.get("server")+"&hash="+obj.get("hash")+"&crop_data="+obj.get("crop_data")+"&crop_hash="+obj.get("crop_hash")+"&v=5.42&access_token="+token);
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
