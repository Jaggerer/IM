package com.example.im.client.http;

import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by ganchenqing on 2018/4/24.
 */

public class CommonRequest {
    /**
     * @param url
     * @param params
     * @return 返回一个创建好post的Request对象
     */
    public static Request createPostRequest(String url, Map params) {

        FormBody.Builder mFormBodybuilder = new FormBody.Builder();
        if (params != null) {
            Iterator it=params.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                // 将请求参数逐一添加到请求体中
                mFormBodybuilder.add((String)entry.getKey(), (String)entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodybuilder.build();
        return new Request.Builder()
                .url(url)
                .post(mFormBody)
                .build();
    }

    /**
     * @param url
     * @param params
     * @return 返回一个创建好get的Request对象
     */
    public static Request createGetRequest(String url, Map params) {

        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            Iterator it=params.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                // 将请求参数逐一添加到请求体中
                urlBuilder.append((String) entry.getKey()).append("=")
                        .append((String)entry.getValue())
                        .append("&");
            }
        }
        return new Request.Builder()
                .url(urlBuilder.substring(0, urlBuilder.length() - 1)) //要把最后的&符号去掉
                .get()
                .build();
    }

//    /**
//     * 文件上传请求
//     *
//     * @return
//     */
//    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");
//
//    public static Request createMultiPostRequest(String url, RequestParams params) {
//
//        MultipartBody.Builder requestBody = new MultipartBody.Builder();
//        requestBody.setType(MultipartBody.FORM);
//        if (params != null) {
//            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
//                if (entry.getValue() instanceof File) {
//                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
//                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
//                } else if (entry.getValue() instanceof String) {
//
//                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
//                            RequestBody.create(null, (String) entry.getValue()));
//                }
//            }
//        }
//        return new Request.Builder()
//                .url(url)
//                .post(requestBody.build())
//                .build();
//    }
}
