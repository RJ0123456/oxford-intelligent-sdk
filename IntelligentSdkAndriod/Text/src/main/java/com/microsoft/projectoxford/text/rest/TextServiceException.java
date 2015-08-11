package com.microsoft.projectoxford.text.rest;

import com.google.gson.Gson;

public class TextServiceException extends Exception {

    public TextServiceException(String message) {
        super(message);
    }

    public TextServiceException(Gson errorObject) {
        super(errorObject.toString());
    }
}
