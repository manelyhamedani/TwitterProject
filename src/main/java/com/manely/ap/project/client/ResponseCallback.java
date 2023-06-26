package com.manely.ap.project.client;

import com.manely.ap.project.common.model.HttpResponse;

public interface ResponseCallback<T> {

    void onResponse(HttpResponse<T> content);
}