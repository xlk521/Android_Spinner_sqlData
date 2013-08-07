package com.example.spinnertext2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class TKYHttpClient {
	public static String connect(String url, String param) {
		try {
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("json", param));
			httpRequest.setHeader("content-type","application/x-www-form-urlencoded; charset=utf-8");
			httpRequest.addHeader("Accept-Charset", "utf-8");
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpParams my_httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(my_httpParams, 3000);
			HttpConnectionParams.setSoTimeout(my_httpParams, 3000);
			DefaultHttpClient mHttpClient = new DefaultHttpClient(my_httpParams);
			BasicHttpResponse httpResponse = (BasicHttpResponse) mHttpClient.execute(httpRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				String re = EntityUtils.toString(httpResponse.getEntity(), "GB2312");
				return re;
			}
		} catch (Exception e) {
		}
		return null;
	}
}
