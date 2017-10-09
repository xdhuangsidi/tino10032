package com.tino.views.spannable;

import android.text.SpannableString;
import android.widget.Toast;


public class NameClickListener implements ISpanClick {
    private String userId;
    private SpannableString userName;

    public NameClickListener(SpannableString name, String userid) {
        this.userName = name;
        this.userId = userid;
    }

    public void onClick(int position) {
       // Toast.makeText(BookApplication.getInstance(), this.userName + " &id = " + this.userId, Toast.LENGTH_LONG).show();
    }
}
