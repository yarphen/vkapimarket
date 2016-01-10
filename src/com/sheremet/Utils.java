package com.sheremet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;

public class Utils {

	public static URL urlWithParams(String path, HashMap<String, String> params, boolean encode) throws MalformedURLException{
		StringBuilder builder = new StringBuilder(path);
		builder.append("?");
		boolean first = true;
		for(String key:params.keySet()){
			if (first){
				first=false;
			}else{
				builder.append("&");
			}
			if (encode)builder.append(URLEncoder.encode(key));else builder.append(key);
			builder.append("=");
			if (encode)builder.append(URLEncoder.encode(params.get(key)));else builder.append(params.get(key));
		}
		return new URL(builder.toString());
	}
	public static Image getImage(InputStream is) {
		byte[] imageData = null;
		try {
			imageData = getBytes(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image image = ImagesServiceFactory.makeImage(imageData);
		return image;
	}
	public static byte[] getBytes(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int len;
	    byte[] data = new byte[10000];
	    while ((len = is.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, len);
	    }
	    buffer.flush();
	    return buffer.toByteArray();
	}
	public static String toSeparatedList(List photo_ids, String separator) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(Object i:photo_ids){
			if (first){
				first=false;
			}else{
				builder.append(separator);
			}
			builder.append(i);
		}
		return builder.toString();
	}

	public static String md5(String s) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		m.update(s.getBytes(),0,s.length());
		return DatatypeConverter.printHexBinary(m.digest());
	}
}
