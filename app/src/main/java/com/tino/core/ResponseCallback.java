package com.tino.core;


import com.squareup.okhttp.Request;
import com.tino.core.api.ApiResponse;
import com.tino.core.net.ResultCallback;

import java.lang.reflect.Type;

public class ResponseCallback<T> extends ResultCallback<ApiResponse<T>> {
    ActionCallback<T> callback;

    public ResponseCallback(ActionCallback<T> listener) {
        this.callback = listener;
    }

    public ResponseCallback(final ActionCallback<T> callback, Type type) {
        this.mType = type;
        this.callback = callback;

    }

    public void onError(Request request, Exception e) {
        this.callback.onError();
        System.out.println("ResonseCall back fail"+e.toString());
    }



    public void onResponse(ApiResponse response) {
        if (response == null) {
            System.out.println("ResonseCall  null object response");
            return;
        }
        if(response.getCode()!=0){
            onFailure(""+response.getCode(),"");
        }
       if (response.isListOfT()) {
            onSuccess((T)response.getObjList());

        } else  {
            onSuccess((T)response.getObj());

        }
    }

    public void onSuccess(T result) {
        this.callback.onSuccess(result);
//        System.out.println("ResonseCall onSuccess obj"+result.toString());
    }

    public void onFailure(String errCode, String msg) {
        this.callback.onFailure(errCode, msg);
    }

    public void onBefore(Request request) {
        this.callback.onBefore();
    }

    public void onAfter() {
        this.callback.onAfter();
    }

    public void inProgress(float progress) {
        this.callback.inProgress(progress);
    }
}
