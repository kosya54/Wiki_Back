package com.kosenko.wikirest.entity.error;

import java.util.Date;

public class ErrorResponse {
    private String error;
    private Date date;

    public ErrorResponse() {
        this.date = new Date();
    }

    public ErrorResponse(String error) {
        this.error = error;
        this.date = new Date();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
