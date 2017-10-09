package com.tino.core.api;

public class ApiResponse<T> {
    private int code;
    private T obj;
    private T objLists;

    public ApiResponse(int code, T obj, T objLists) {
        this.code = code;
        this.obj = obj;
        this.objLists = objLists;
    }

    public ApiResponse() {
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }



    public boolean isListOfT() {
        return this.objLists != null;
    }


    public T getObj() {
        return this.obj;
    }

    public T getObjList() {
        return this.objLists;
    }
}
