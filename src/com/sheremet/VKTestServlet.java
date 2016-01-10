package com.sheremet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class VKTestServlet extends HttpServlet {
	DatastoreService db = DatastoreServiceFactory.getDatastoreService();
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("");
		if (req.getParameter("code")!=null){
			URL url = new URL("https://oauth.vk.com/access_token?client_id=5214831&client_secret=SUFnI4xRqCT0PiN89dv5&redirect_uri=https://vk-uploader.appspot.com/auth&code="+req.getParameter("code"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			resp.getWriter().println(connection.getResponseMessage());
			try {
				JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
				String token = (String) json.get("access_token");
				resp.getWriter().println(token);
				//VKUploadServlet.token=token;
				Long expires_in = (Long) json.get("expires_in");
				resp.getWriter().println(expires_in);
				Long user_id = (Long) json.get("user_id");
				resp.getWriter().println(user_id);
				String id = "";
				String cookie = Utils.md5(req.getParameter("code"));
				resp.addCookie(new Cookie("auth", cookie));
				Query q = new Query("user");
				q.addFilter("id", Query.FilterOperator.EQUAL, user_id);
				PreparedQuery preparedQuery = db.prepare(q);
				for (Entity e:preparedQuery.asIterable()){
					db.delete(e.getKey());
				}
				Entity e = new Entity("user");
				e.setProperty("auth", cookie);
				e.setProperty("token", token);
				e.setProperty("id", user_id);
				db.put(e);
				/*
				url = new URL("https://api.vk.com/method/photos.get?album_id=wall&rev=1&extended=1&v=5.42&access_token="+token);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				json = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()));
				JSONArray jsona = (JSONArray) ((JSONObject)json.get("response")).get("items");
				int i=0;
				for(Object obj:jsona){
					if (((String) ((JSONObject)obj).get("text")).isEmpty()){
						id =  ((JSONObject)obj).get("id").toString();
						System.err.println(id);
						resp.getWriter().println(id);
						url = new URL("https://api.vk.com/method/photos.edit?photo_id="+id+"&caption="+URLEncoder.encode("I'm wonderful")+"&v=5.42&access_token="+token);
						connection = (HttpURLConnection) url.openConnection();
						connection.connect();
						resp.getWriter().println(((JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream()))).toJSONString());
						Thread.sleep(500);
						i++;
					}
					if (i==10){
						break;
					}
				}
				*/
			} catch (ParseException /*| InterruptedException */ e) {
				e.printStackTrace();
			}

		}
		if (req.getParameter("access_token")!=null&&req.getParameter("expires_in")!=null&&req.getParameter("user_id")!=null){
			String token = req.getParameter("access_token");
			resp.getWriter().println(token);
			String expires_in = req.getParameter("expires_in");
			resp.getWriter().println(expires_in);
			String user_id = req.getParameter("user_id");
			resp.getWriter().println(user_id);
		}
		resp.sendRedirect("/");
	}
}