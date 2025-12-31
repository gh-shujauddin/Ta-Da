package com.qadri.tada.error;

public class IdNotMatchedException extends Exception{

    @Override
    public String getMessage() {
        return "id in request parameter and body doesn't match";
    }
}
