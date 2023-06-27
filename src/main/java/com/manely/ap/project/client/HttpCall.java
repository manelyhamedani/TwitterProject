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

    private static final Executor executor = Executors.newSingleThreadExecutor();

    public static <T> void get(
            String path,
            Map<String, String> query,
            Class<T> contentType,
            ResponseCallback<T> onResponse
    ) {
        execute("GET", path, null, query, onResponse, contentType);
    }

    public static <T> void post(
            String path,
            Object data,
            Class<T> contentType,
            ResponseCallback<T> onResponse
    ) {
        execute("POST", path, data, null, onResponse, contentType);
    }

    static <T> void execute(
            String method,
            String path,
            Object data,
            Map<String, String> query,
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

                conn.connect();

                HttpResponse<T> response = new Gson().fromJson(
                        new InputStreamReader(conn.getInputStream()),
                        TypeToken.getParameterized(HttpResponse.class, contentType).getType()
                );

                conn.disconnect();

                onResponse.setResponse(response);

                Main.tasks.execute(onResponse);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

        });

    }

    private static String mapToQuery(Map<String, String> query) {
        StringBuilder queryStr = new StringBuilder();

        if (query != null) {
            queryStr = new StringBuilder("?");
            for (Map.Entry<String, String> entry : query.entrySet()) {
                String entity = entry.getValue();
                try {
                    entity = URLEncoder.encode(entity, StandardCharsets.UTF_8);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                queryStr.append(entry.getKey()).append("=").append(entity).append("&");
            }
            queryStr = new StringBuilder(queryStr.substring(0, queryStr.length() - 1));
        }

        return queryStr.toString();
    }
}