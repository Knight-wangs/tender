package com.telecom.tender.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class HttpClientUtils {
    private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(50);// 整个连接池最大连接数
            cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
        }
    }

    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(cm).build();
    }
    /**
     * @param url
     * @return
     */
    public static String get(String url) {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }
    /**
     * @param url、params
     * @return
     */
    public static String get(String url, Map<String, String > params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }
    /**
     * @param url、headers、params
     * @return
     */
    public static String get(String url, Map<String, Object> headers, Map<String, String> params)
            throws URISyntaxException {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);

        if (params != null) {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            ub.setParameters(pairs);
        }

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }
    /**
     * @param url
     * @return
     */
    public static String post(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }
    /**
     * @param url、params
     * @return
     */
    public static String post(String url, Map<String, String> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(covertParams2NVPS(params), "utf-8"));//设置表单提交编码

//        httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_FORM_URLENCODED));
        return getResult(httpPost);
    }
    /**
     * @param url、headers、params
     * @return
     */
    public static String post(String url, Map<String, Object> headers, Map<String, String> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        if (params != null) {
            for (Map.Entry<String, Object> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
        }

        httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));

        return getResult(httpPost);
    }
    /**
     * @param method、url
     * @return
     */
    public static String request(String method, String url)
            throws UnsupportedEncodingException {

        RequestBuilder requestBuilder = RequestBuilder.create(method);
        requestBuilder.setUri(url);
        return getResult(requestBuilder);
    }
    /**
     * @param method、url、params
     * @return
     */
    public static String request(String method, String url, Map<String, Object> params)
            throws UnsupportedEncodingException {

        RequestBuilder requestBuilder = RequestBuilder.create(method);
        requestBuilder.setUri(url);

        EntityBuilder entityBuilder = EntityBuilder.create();
        entityBuilder.setContentEncoding(UTF_8);
        entityBuilder.setText(JSON.toJSONString(params));
        entityBuilder.setContentType(ContentType.APPLICATION_FORM_URLENCODED);

        requestBuilder.setEntity(entityBuilder.build());

        return getResult(requestBuilder);
    }
    /**
     * @param method、url、headers、params
     * @return
     */
    public static String request(String method, String url, Map<String, Object> headers, Map<String, String> params)
            throws UnsupportedEncodingException {

        RequestBuilder requestBuilder = RequestBuilder.create(method);
        requestBuilder.setUri(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            requestBuilder.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        EntityBuilder entityBuilder = EntityBuilder.create();
        entityBuilder.setContentEncoding(UTF_8);
        entityBuilder.setText(JSON.toJSONString(params));
        entityBuilder.setContentType(ContentType.APPLICATION_JSON);

        requestBuilder.setEntity(entityBuilder.build());

        return getResult(requestBuilder);
    }
    /**
     * @param url、file、fileName
     * @return
     */
    public static String uploadFile(String url, byte[] file, String fileName) {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept-Encoding", "gzip");
        httpPost.setHeader("charset", "utf-8");

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.setCharset(Charset.forName(UTF_8));
        multipartEntityBuilder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, fileName);

        httpPost.setEntity(multipartEntityBuilder.build());

        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, String> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (param.getValue() != null) {
                pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
            }
        }

        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity, UTF_8);
                response.close();
                // httpClient.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        return EMPTY_STR;
    }

    /**
     * 处理Http请求
     *
     * @param requestBuilder
     * @return
     */
    private static String getResult(RequestBuilder requestBuilder) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(requestBuilder.build());
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity, UTF_8);
                response.close();
                // httpClient.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

        return EMPTY_STR;
    }
    /**
     * post请求上传文件
     */
    public static String httpClientUploadFile(MultipartFile file,String url,Map<String,String> headers) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            String fileName = file.getOriginalFilename();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            builder.addTextBody("filename", fileName);// 类似浏览器表单提交，对应input的name和value
            HttpEntity entity = builder.build();
            for (Map.Entry<String, String> param : headers.entrySet()) {
                httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
            }
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);// 执行提交
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static File saveUrlAs(String url, String filePath, String method){
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        File file=null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {

                filePath += "/";

            }
            String extName = url.substring(url.lastIndexOf("."));
            java.util.GregorianCalendar gcalendar =  new java.util.GregorianCalendar();
            String year = gcalendar.get(Calendar.YEAR)+"";
            String month = gcalendar.get(Calendar.MONTH)+1 + "";
            String day = gcalendar.get(Calendar.DAY_OF_MONTH)+"";
            String newFileName =new java.util.Date().getTime() + "_" + (int)(1000 *Math.random())+ extName;
            String dirPath = year + File.separator +month + File.separator + day + File.separator;
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            //创建不同的文件夹目录
            file=new File(filePath +dirPath);
            //判断文件夹是否存在
            if (!file.exists())
            {
                //如果文件夹不存在，则创建新的的文件夹
                file.mkdirs();
            }
            fileOut = new FileOutputStream(filePath +dirPath +newFileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return file;

    }

    public static ApiResult toApiRequest(String resultStr) {
        return JSONObject.parseObject(resultStr, ApiResult.class);
    }
}