package com.jwc.geo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final String CONTENT_JSON = "application/json";
    private static final String CONTENT_FORM_URLENCODED = "application/x-www-form-urlencoded";
//    private static final String 
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int DEFAULT_REQUEST_TIMEOUT = 30000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 8000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 30000;

    private static HttpClientBuilder clientBuilder;
    private static RequestConfig DEFAULT_CONFIG = null;

    static {
        clientBuilder = HttpClientBuilder.create();
        clientBuilder.setMaxConnTotal(200);
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                return executionCount > 1 ? false : true;
            }
        };
        clientBuilder.setRetryHandler(retryHandler);
        // request 基本配置信息
        DEFAULT_CONFIG = RequestConfig.custom().setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).build();
    }

    public static String getXMLParams(HttpServletRequest request) {
        InputStream is = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEFAULT_CHARSET));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            LOGGER.error("解析请求XML数据失败", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输入流失败", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("关闭读取器失败", e);
                }
            }
        }
        return sb.toString();
    }

    public static String sendGetRequest(String url) {
        try {
            CloseableHttpClient client = clientBuilder.build();
            HttpGet get = new HttpGet(url);
            get.setConfig(DEFAULT_CONFIG);
            CloseableHttpResponse response = client.execute(get);
            return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String sendGetRequest(String url, Map<String, String> params) {
        try {
            return sendGetRequest(appendParam2Url(url, params));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String appendParam2Url(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        if (url.indexOf("?") < 0) {
            builder.append("?");
        }
        try {
            for (Entry<String, String> entry : params.entrySet()) {
                builder.append(entry.getKey()).append("=");
                builder.append(URLEncoder.encode(entry.getValue(), DEFAULT_CHARSET)).append("&");
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url参数编码错误:{}=>{}", url, JSON.toJSONString(params));
        }
        return builder.toString();
    }

    public static String downLoad(String url, String fname) {
        CloseableHttpClient client = clientBuilder.build();
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);
            FileOutputStream fos = new FileOutputStream(fname);
            response.getEntity().writeTo(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String postFile(String url, String filePath) {
        return postFile(url, filePath, Collections.emptyMap(), Collections.emptyMap());
    }

    public static String postFile(String url, String filePath, Map<String, String> params) {
        return postFile(url, filePath, params, Collections.emptyMap());
    }

    public static String postFile(String url, String filePath, Map<String, String> params,
            Map<String, String> headers) {
        CloseableHttpClient httpClient = clientBuilder.build();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(DEFAULT_CONFIG);
            // body 参数
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            File file = new File(filePath);
            multipartEntityBuilder.addBinaryBody("audio", file);
            if (BaseUtils.mapNotEmpty(params)) {
                for (String key : params.keySet()) {
                    multipartEntityBuilder.addTextBody(key, params.get(key));
                }
            }

            HttpEntity httpEntity = multipartEntityBuilder.build();

            httpPost.setEntity(httpEntity);
            // header 参数
            for (String header : headers.keySet()) {
                httpPost.addHeader(header, headers.get(header));
            }
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity(), DEFAULT_CHARSET);
            }
            System.out.println(EntityUtils.toString(httpResponse.getEntity(), DEFAULT_CHARSET));
            // EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
            closeResponse(httpResponse);
        }
        return null;
    }

    public static String postFile(String url, Map<String, String> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = clientBuilder.build();
        CloseableHttpResponse httpResponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(DEFAULT_CONFIG);
            // body 参数
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            if (BaseUtils.mapNotEmpty(params)) {
                for (String key : params.keySet()) {
                    multipartEntityBuilder.addTextBody(key, params.get(key));
                }
            }
            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);
            // header 参数
            for (String header : headers.keySet()) {
                httpPost.addHeader(header, headers.get(header));
            }

            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity(), DEFAULT_CHARSET);
            }
            System.out.println(EntityUtils.toString(httpResponse.getEntity(), DEFAULT_CHARSET));
            // EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttpClient(httpClient);
            closeResponse(httpResponse);
        }
        return null;
    }

    public static String sendPostRequest(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient client = clientBuilder.build();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(DEFAULT_CONFIG);

            List<NameValuePair> paramPairs = genNameValuePairs(params);
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPairs, DEFAULT_CHARSET);
            entity.setContentType(CONTENT_FORM_URLENCODED);
            httpPost.setEntity(entity);
            if (BaseUtils.mapNotEmpty(headers)) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }

            response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            LOGGER.error("httppost请求出现IO异常", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                LOGGER.error("关闭 CloseableHttpClient 出现异常", e);
            }
        }
        return "";
    }

    public static String sendPostRequest(String url, Map<String, String> params) {
        return sendPostRequest(url, Collections.emptyMap(), params);
    }

    public static String sendPostRequest(String url) {
        return sendPostRequest(url, Collections.emptyMap());
    }

    public static String sendPostRequest(String url, String params) {
        return sendPostRequest(url, Collections.emptyMap(), params);
    }

    public static String sendPostRequest(String url, Map<String, String> headers, String params) {
        CloseableHttpResponse response = null;
        CloseableHttpClient client = clientBuilder.build();
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(DEFAULT_CONFIG);
            // add header
            if (BaseUtils.mapNotEmpty(headers)) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // add params
            StringEntity entity = new StringEntity(params, DEFAULT_CHARSET);
            entity.setContentType(CONTENT_JSON);
            post.setEntity(entity);

            response = client.execute(post);
            return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } catch (Exception e) {
            LOGGER.error("httppost请求出现IO异常", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                LOGGER.error("关闭 CloseableHttpClient 出现异常", e);
            }
        }
        return "";
    }

    private static void closeResponse(CloseableHttpResponse response) {
        try {
            if (null != response) {
                response.close();
            }
        } catch (Exception e) {
            LOGGER.error("关闭 CloseableResponse 出现异常", e);
        }
    }

    private static void closeHttpClient(CloseableHttpClient client) {
        try {
            if (null != client) {
                client.close();
            }
        } catch (IOException e) {
            LOGGER.error("关闭 CloseableHttpClient 出现异常", e);
        }
    }

    private static List<NameValuePair> genNameValuePairs(Map<String, String> paramMap) {
        if (BaseUtils.mapEmpty(paramMap)) {
            return Collections.emptyList();
        }
        List<NameValuePair> list = new ArrayList<>();
        for (Entry<String, String> entry : paramMap.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
