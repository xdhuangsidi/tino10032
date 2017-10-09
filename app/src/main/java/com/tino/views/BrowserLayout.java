package com.tino.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.load.Key;
import com.tino.R;
import com.tino.ui.PhotoViewActivity;


import java.util.ArrayList;

public class BrowserLayout extends LinearLayout {
    private Boolean enableImageClick = Boolean.valueOf(true);
    private int mBarHeight = 5;
    private View mBrowserControllerView = null;
    private Context mContext = null;
    private ImageButton mGoBackBtn = null;
    private ImageButton mGoBrowserBtn = null;
    private ImageButton mGoForwardBtn = null;
    private String mLoadUrl;
    private ProgressBar mProgressBar = null;
    private ImageButton mRefreshBtn = null;
    private WebView mWebView = null;

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            System.out.println(img);
            ArrayList<String> photos = new ArrayList();
            photos.add(img);
            PhotoViewActivity.showWithParams(BrowserLayout.this.mContext, photos, 0, 60, 60, 0, 0);
        }
    }

    public BrowserLayout(Context context) {
        super(context);
        init(context);
    }

    public BrowserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        this.mProgressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
        this.mProgressBar.setMax(100);
        this.mProgressBar.setProgress(0);
        addView(this.mProgressBar, -1, (int) TypedValue.applyDimension(0, (float) this.mBarHeight, getResources().getDisplayMetrics()));
        this.mWebView = new WebView(context);
        this.mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setDefaultTextEncodingName(Key.STRING_CHARSET_NAME);
        this.mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.mWebView.getSettings().setBuiltInZoomControls(false);
        this.mWebView.getSettings().setSupportMultipleWindows(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setSupportZoom(false);
        this.mWebView.getSettings().setPluginState(PluginState.ON);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        addView(this.mWebView, new LayoutParams(-1, 0, 1.0f));
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    BrowserLayout.this.mProgressBar.setVisibility(View.GONE);
                    return;
                }
                BrowserLayout.this.mProgressBar.setVisibility(View.VISIBLE);
                BrowserLayout.this.mProgressBar.setProgress(newProgress);
            }
        });
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                BrowserLayout.this.mLoadUrl = url;
                if (BrowserLayout.this.enableImageClick.booleanValue()) {
                    BrowserLayout.this.addImageClickListner();
                }
            }
        });
        this.mBrowserControllerView = LayoutInflater.from(context).inflate(R.layout.view_browser_controller, null);
        this.mGoBackBtn = (ImageButton) this.mBrowserControllerView.findViewById(R.id.browser_controller_back);
        this.mGoForwardBtn = (ImageButton) this.mBrowserControllerView.findViewById(R.id.browser_controller_forward);
        this.mGoBrowserBtn = (ImageButton) this.mBrowserControllerView.findViewById(R.id.browser_controller_go);
        this.mRefreshBtn = (ImageButton) this.mBrowserControllerView.findViewById(R.id.browser_controller_refresh);
        this.mGoBackBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (BrowserLayout.this.canGoBack()) {
                    BrowserLayout.this.goBack();
                }
            }
        });
        this.mGoForwardBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (BrowserLayout.this.canGoForward()) {
                    BrowserLayout.this.goForward();
                }
            }
        });
        this.mRefreshBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BrowserLayout.this.loadUrl(BrowserLayout.this.mLoadUrl);
            }
        });
        this.mGoBrowserBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.setData(Uri.parse(BrowserLayout.this.mLoadUrl));
                    BrowserLayout.this.mContext.startActivity(intent);

            }
        });
        addView(this.mBrowserControllerView, -1, -2);
    }

    public void loadUrl(String url) {
        this.mWebView.loadUrl(url);
    }

    public boolean canGoBack() {
        return this.mWebView != null ? this.mWebView.canGoBack() : false;
    }

    public boolean canGoForward() {
        return this.mWebView != null ? this.mWebView.canGoForward() : false;
    }

    public void goBack() {
        if (this.mWebView != null) {
            this.mWebView.goBack();
        }
    }

    public void goForward() {
        if (this.mWebView != null) {
            this.mWebView.goForward();
        }
    }

    public WebView getWebView() {
        return this.mWebView != null ? this.mWebView : null;
    }

    public void hideBrowserController() {
        this.mBrowserControllerView.setVisibility(View.GONE);
    }

    public void showBrowserController() {
        this.mBrowserControllerView.setVisibility(View.VISIBLE);
    }

    private void addImageClickListner() {
        this.mWebView.loadUrl("javascript:(function(){var objs = document.getElementsByTagName(\"img\"); for(var i=0;i<objs.length;i++)  {    objs[i].onclick=function()      {          window.imagelistner.openImage(this.src);      }  }})()");
    }

    public void setEnableImageClick(boolean imageClick) {
        this.enableImageClick = Boolean.valueOf(imageClick);
        if (this.enableImageClick.booleanValue()) {
            this.mWebView.addJavascriptInterface(new JavascriptInterface(), "imagelistner");
        }
    }
}
