package com.manely.ap.project.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manely.ap.project.common.model.HttpResponse;
import com.manely.ap.project.common.API;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpCall {

    private static final Executor executor = Executors.newCachedThreadPool();

    public static <T> void get(
            String path,
            Map<String, String> query,
            Class<T> contentType,
            ErrorCallback onError,
            ResponseCallback<T> onResponse
    ) {
        execute("GET", path, null, query, onError, onResponse, contentType);
    }

    public static <T> void post(
            String path,
            Object data,
            Class<T> contentType,
            ErrorCallback onError,
            ResponseCallback<T> onResponse
    ) {
        execute("POST", path, data, null, onError, onResponse, contentType);
    }

    static <T> void execute(
            String method,
            String path,
            Object data,
            Map<String, String> query,
            ErrorCallback onError,
            ResponseCallback<T> onResponse,
            Class<T> contentType
    ) {
        executor.execute(() -> {
            try {
                URL url = new URL(API.BASE_URL + path + mapToQuery(query));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                if (data != null && method.equals("POST")) {
                    byte[] bytes = new Gson().toJson(data).getBytes();
                    conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                    conn.getOutputStream().write(bytes);
                }

                HttpResponse<T> response = new Gson().fromJson(
                        new InputStreamReader(conn.getInputStream()),
                        TypeToken.getParameterized(HttpResponse.class, contentType).getType()
                );

                Main.mainThreadQueue.put(() -> {
                    onResponse.onResponse(response);
                });
            }
            catch (Exception e) {
                onError.onError(e);
            }
        });
    }

    private static String mapToQuery(Map<String, String> query) {
        String queryStr = "";

        if (query != null) {
            queryStr = "?";
            for (Map.Entry<String, String> entry : query.entrySet()) {
                String entity = entry.getValue();
                try {
                    entity = URLEncoder.encode(entity, StandardCharsets.UTF_8);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                queryStr += entry.getKey() + "=" + entity + "&";
            }
            queryStr = queryStr.substring(0, queryStr.length() - 1);
        }

        return queryStr;
    }
}