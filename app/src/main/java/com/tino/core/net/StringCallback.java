package com.tino.core.net;



import com.squareup.okhttp.Request;
import com.tino.core.ActionCallback;

public class StringCallback<String> extends ResultCallback<String> {
    ActionCallback<java.lang.String> callback;

    public StringCallback(ActionCallback<java.lang.String> listener) {

        this.mType = java.lang.String.class;
        this.callback = listener;
    }



    public void onError(Request request, Exception e) {
        this.callback.onError();
        System.out.println("ResonseCall back fail"+e.toString());
    }

    @Override
    public void onResponse(String string) {
        onSuccess(string);
    }




    public void onSuccess(String result) {
        this.callback.onSuccess((java.lang.String)result);
    }

    public void onFailure(String errCode, String msg) {
        this.callback.onFailure((java.lang.String)errCode, (java.lang.String)msg);
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
