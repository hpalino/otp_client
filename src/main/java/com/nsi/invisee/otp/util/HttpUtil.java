/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsi.invisee.otp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 *
 * @author Hatta Palino
 */
public class HttpUtil {
    
    private final Logger logger = Logger.getLogger(this.getClass());
    private final HttpClient httpclient;
    
    public HttpUtil() {
        httpclient = HttpClients.createDefault();
    }
    
    public String body(String url, String json) {
        String response = null;
        try {
            HttpPost httppost = new HttpPost(url);
            StringEntity postingString = new StringEntity(json);
            httppost.setEntity(postingString);
            httppost.setHeader("Content-type", "application/json");
            HttpResponse resp = httpclient.execute(httppost);
            
            if(resp.getStatusLine().getStatusCode() != 200) {
                return response;
            }

            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                response = IOUtils.toString(instream, StandardCharsets.UTF_8.name());
            }
            
        } catch (IOException | UnsupportedOperationException e) {
            logger.error(e);
        }
        return response;
    }
    
    public String post(String url, Map<String, String> map) {
        String response = null;
        try {
            HttpPost httppost = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<>();
            for (String key : map.keySet()) {
                params.add(new BasicNameValuePair(key, map.get(key)));
            }
            httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8.name()));
            
            HttpResponse resp = httpclient.execute(httppost);
            
            if(resp.getStatusLine().getStatusCode() != 200) {
                return response;
            }

            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                response = IOUtils.toString(instream, StandardCharsets.UTF_8.name());
            }
        } catch (IOException | UnsupportedOperationException e) {
            logger.error(e);
        }
        return response;
    }
    
    public String get(String url, Map<String, String> map) {
        String response = null;
        try {
            String qs = "";
            if(map != null && !map.isEmpty()) {
                for (String key : map.keySet()) {
                    qs += "&" + key + "=" + URLEncoder.encode(map.get(key), StandardCharsets.UTF_8.name());
                }
                qs = "?" + qs.substring(1);
            }
            
            System.out.println("qs : " + qs);
            
            HttpGet httppost = new HttpGet(url + qs);
            
            HttpResponse resp = httpclient.execute(httppost);
            
            //int res = resp.getStatusLine().getStatusCode();
            
            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                response = IOUtils.toString(instream, StandardCharsets.UTF_8.name());
            }
        } catch (IOException | UnsupportedOperationException e) {
            logger.error(e);
        }
        return response;
    }
    
}
