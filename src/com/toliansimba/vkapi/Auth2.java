package com.toliansimba.vkapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Auth2 {
	public static void main(String[] args){
		SSLSocketFactory f = 
		         (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket s = null;
		try {
			s = (SSLSocket) f.createSocket("oauth.vk.com", 443);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintStream ps = null;
		try {
			ps = new PrintStream(s.getOutputStream(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader reader = null;
		Scanner sc = null;
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream(), "windows-1251"));
			sc = new Scanner(s.getInputStream(), "windows-1251");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ps.println("GET /token?grant_type=password&client_id=5214150&client_secret=0KfBnJbvZI6SqorFCW9a&username=380967130271&password=levi21$%13&v=5.42 HTTP/1.1");
		ps.println("Host: oauth.vk.com");		
		ps.println("Connection: close");
		ps.println("Accept: */*");
		//ps.println("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
		ps.println();
		ps.println();
		//URL url = new URL("https://oauth.vk.com/token?grant_type=password&client_id=3864223&client_secret=tjEYvRIGOHlMypbf89W1&username=380967130271&password=levi21$%13&v=5.42");
		//HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		//connection.connect();
		//Scanner s = new Scanner(connection.getInputStream());
		
		String str;
		try{
		while((str = reader.readLine())!=null)
			System.out.println(str);
		}catch(Exception e){
			
		}
	}
}
