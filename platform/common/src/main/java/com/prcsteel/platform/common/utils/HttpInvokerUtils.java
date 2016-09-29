package com.prcsteel.platform.common.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Http请求
 */
public class HttpInvokerUtils {

    /**
     * Post 请求
     *
     * @param requestUrl 请求url
     * @param postPars   post参数
     * @return
     */
    public static String readContentFromPost(String requestUrl, Map<String, String> postPars) {
        try {
            // Post请求的url，与get不同的是不需要带参数
            URL postUrl = new URL(requestUrl);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod("POST");

            // Post 请求不能使用缓存
            connection.setUseCaches(false);

            connection.setInstanceFollowRedirects(true);

            String ENCODING_UTF_8 = "utf-8";
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
            // 进行编码
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());
            // The URL-encoded contend
            // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
            StringBuilder content = new StringBuilder();
            if (postPars != null && postPars.size() > 0) {
                for (String key : postPars.keySet()) {
                    content.append(key + "=" + URLEncoder.encode(postPars.get(key), ENCODING_UTF_8) + "&");
                }
            }
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            out.writeBytes(content.toString());

            out.flush();
            out.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING_UTF_8));
            String line;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
            connection.disconnect();
            return result.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }


}