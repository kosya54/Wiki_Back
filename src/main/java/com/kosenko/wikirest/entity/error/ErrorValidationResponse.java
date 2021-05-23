package com.kosenko.wikirest.entity.error;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ErrorValidationResponse {
    private Map<String, String> error;
    private Date date;

    public ErrorValidationResponse() {
        error = new HashMap<>();
        date = new Date();
    }

    public ErrorValidationResponse(String name, String message) {
        error = new HashMap<>();
        error.put(name, message);
        date = new Date();
    }

    public ErrorValidationResponse(Map<String, String> error) {
        this.error = error;
        date = new Date();
    }

    public void addField(String name, String message) {
        error.put(name, message);
    }

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
