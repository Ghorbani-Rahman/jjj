package com.delaroystodios.metakar.Model;

public class EditProfile {

    private String result;
    private String message;
    private ErrorsEditProfile error;

    public ErrorsEditProfile getError() {
        return error;
    }

    public void setError(ErrorsEditProfile error) {
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
