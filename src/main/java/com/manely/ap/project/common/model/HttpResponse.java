package com.manely.ap.project.common.model;


public record HttpResponse<T>(int STATUS_CODE, String MESSAGE, boolean SUCCESS, T CONTENT) {

    public boolean isSuccess() {
        return SUCCESS;
    }

    public int getStatus() {
        return STATUS_CODE;
    }

    public String getMessage() {
        return MESSAGE;
    }

    public T getContent() {
        return CONTENT;
    }
}
