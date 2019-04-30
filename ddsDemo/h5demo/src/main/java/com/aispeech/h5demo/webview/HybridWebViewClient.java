package com.aispeech.h5demo.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aispeech.h5demo.R;
import com.aispeech.h5demo.webview.chrome.CustomTabActivityHelper;
import com.aispeech.h5demo.webview.chrome.WebviewFallback;


public class HybridWebViewClient extends WebViewClient {

    Context context;

    public HybridWebViewClient(Context context) {
        super();
        this.context = context;
    }

    /**
     * If the external link captured, then use Chrome Custom Tab to open it.
     * If Chrome not installed, then use WebViewActivity to open external link.
     *
     * @param webView
     * @param url
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (url.startsWith("file://")) {
            return false;
        } else {
            openCustomChromeTab(url);
            return true;
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.startsWith("file://")) {
            view.clearHistory();
        }
        super.onPageFinished(view, url);
    }

    private void openCustomChromeTab(String url) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(context.getResources().getColor(R.color.speed_normal_text_color));
        intentBuilder.setSecondaryToolbarColor(context.getResources().getColor(R.color.speed_normal_text_color));
        intentBuilder.setShowTitle(true);
        intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_back));
        intentBuilder.enableUrlBarHiding();
        intentBuilder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(context, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        CustomTabActivityHelper.openCustomTab(
                (Activity) context, intentBuilder.build(), Uri.parse(url), new WebviewFallback());
    }
}
