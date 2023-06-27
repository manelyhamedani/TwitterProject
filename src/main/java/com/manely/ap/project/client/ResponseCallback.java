package com.manely.ap.project.client;

import com.manely.ap.project.common.model.HttpResponse;

public abstract class ResponseCallback<T> implements Runnable {
    private HttpResponse<T> response;

    public abstract void run();

    public void setResponse(HttpResponse<T> response) {
        this.response = response;
    }

    public HttpResponse<T> getResponse() {
        return response;
    }
}