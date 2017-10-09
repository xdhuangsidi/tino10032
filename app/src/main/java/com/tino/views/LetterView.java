package com.tino.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LetterView extends LinearLayout {
    LetterListener letterListener;

    public interface LetterListener {
        void onChanged(char c);
    }

    public LetterView(Context context) {
        super(context);
        setOrientation(1);
        updateLetters();
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(1);
        updateLetters();
    }

    private void updateLetters() {
        setLetters(getSortLetters());
    }

    public void setLetters(List<Character> letters) {
        removeAllViews();
        for (Character content : letters) {
            TextView view = new TextView(getContext());
            view.setText(content.toString());
            addView(view);
        }
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int x = Math.round(event.getX());
                int y = Math.round(event.getY());
                for (int i = 0; i < LetterView.this.getChildCount(); i++) {
                    TextView child = (TextView) LetterView.this.getChildAt(i);
                    if (y > child.getTop() && y < child.getBottom() && LetterView.this.letterListener != null) {
                        LetterView.this.letterListener.onChanged(child.getText().toString().charAt(0));
                    }
                }
                return true;
            }
        });
    }

    private List<Character> getSortLetters() {
        List<Character> letterList = new ArrayList();
        for (char c = 'A'; c <= 'Z'; c = (char) (c + 1)) {
            letterList.add(Character.valueOf(c));
        }
        return letterList;
    }

    public void setLetterListener(LetterListener listener) {
        this.letterListener = listener;
    }
}
