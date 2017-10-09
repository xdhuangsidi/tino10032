package com.tino.utils;

import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCounter extends CountDownTimer {
    private Button button;

    public TimeCounter(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    public void onTick(long millisUntilFinished) {
        this.button.setClickable(false);
        this.button.setText((millisUntilFinished / 1000) + "秒");
    }

    public void onFinish() {
        this.button.setText("重新验证");
        this.button.setClickable(true);
    }
}
