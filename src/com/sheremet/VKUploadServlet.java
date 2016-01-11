
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
	private static final String param_image = "https://pp.vk.me/c629421/v629421731/39b50/5Z99pbcQCQg.jpg";
	private static final int param_group = 54919986;
	private static final String param_name = "name"+(int)(Math.random()*1000);
	private static final String param_descr = "THIS IS DESCRIPTION!";
	private static final boolean param_deleted = true;
	private static final long param_category = 5;
	private static final double param_price = 1.02;
	private static final long crop_x = 0;
	private static final long crop_y = 0;
	private static final long crop_width = 200;
	DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
//		PrintStream std_err = System.err;
//		System.setErr(new PrintStream(resp.getOutputStream()));
		/*try {
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
		}*/
		main(null);
	}
	public static void main(String[] args) throws ClientProtocolException, IOException {
		try {
			String token = "aa57603fe679b18d99056a77ac708a5ca7bdf0fb499e2aa2829199e8fdac0500f0adedaa0ff378639a160";
			API api = new API(token);
			URL url = new URL(param_image);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			Image image = Utils.getImage(connection.getInputStream());
			JSONObject resp1 = api.photos_getMarketUploadServer(param_group,true, crop_x, crop_y,crop_width);
			System.err.println(resp1.toJSONString());
			String o =  (String) ((JSONObject) resp1.get("response")).get("upload_url");
			System.err.println(o);
			JSONObject resp2 = api.photos_uploadToServer2(new URL(o), new ByteArrayInputStream(image.getImageData()), "photo.jpg");
			System.err.println(resp2.toJSONString());
			JSONObject resp3 = api.photos_saveMarketPhoto(param_group, (String)resp2.get("photo"), (Long)resp2.get("server"), (String)resp2.get("hash"), (String)resp2.get("crop_data"),(String) resp2.get("crop_hash"));
			System.err.println(resp3.toJSONString());
			Object photo_id = ((JSONObject)((JSONArray)resp3.get("response")).get(0)).get("id");
			System.err.println(photo_id);
			JSONObject resp4 = api.market_add(-param_group,param_name, param_descr, param_category,param_price, param_deleted, (Long)photo_id, new LinkedList<Integer>());
			System.err.println(resp4.toJSONString());
		} catch (ClientProtocolException | ParseException  e) {
			e.printStackTrace();
//			System.setErr(std_err);
		}
		
	}
	
}