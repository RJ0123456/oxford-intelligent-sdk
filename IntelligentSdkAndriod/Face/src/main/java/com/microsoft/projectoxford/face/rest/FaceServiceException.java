package com.microsoft.projectoxford.face.rest;

import com.google.gson.Gson;

public class FaceServiceException extends Exception {

    public FaceServiceException(String message) {
        super(message);
    }

    public FaceServiceException(Gson errorObject) {
        super(errorObject.toString());
    }
}
