package com.tino.core;

public interface ActionCallback<T> {
    void inProgress(float f);

    void onAfter();

    void onBefore();

    void onError();

    void onFailure(String str, String str2);

    void onSuccess(T t);
}
