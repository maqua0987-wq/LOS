package com.j2os.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends Activity {
    private WebView web;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(android.graphics.Color.rgb(13, 16, 28));
        getWindow().setNavigationBarColor(android.graphics.Color.rgb(13, 16, 28));
        web = new WebView(this);
        web.setBackgroundColor(android.graphics.Color.rgb(13, 16, 28));
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.addJavascriptInterface(new LauncherBridge(), "J2OS");
        setContentView(web);
        web.loadUrl("file:///android_asset/index.html");
    }

    private class LauncherBridge {
        @JavascriptInterface public String getApps() {
            JSONArray result = new JSONArray();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
            Collections.sort(apps, new Comparator<ResolveInfo>() {
                public int compare(ResolveInfo a, ResolveInfo b) {
                    return a.loadLabel(pm).toString().compareToIgnoreCase(b.loadLabel(pm).toString());
                }
            });
            for (ResolveInfo info : apps) {
                try {
                    JSONObject item = new JSONObject();
                    item.put("name", info.loadLabel(pm).toString());
                    item.put("packageName", info.activityInfo.packageName);
                    item.put("icon", drawableToBase64(info.loadIcon(pm)));
                    result.put(item);
                } catch (Exception ignored) {}
            }
            return result.toString();
        }

        @JavascriptInterface public void launchApp(String packageName) {
            try {
                Intent launch = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launch != null) startActivity(launch);
            } catch (Exception ignored) {}
        }

        private String drawableToBase64(Drawable drawable) {
            int size = 96;
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, size, size);
            drawable.draw(canvas);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            return "data:image/png;base64," + Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
        }
    }

    @Override public void onBackPressed() {
        if (web != null) web.evaluateJavascript("closeDrawer()", null);
        else super.onBackPressed();
    }
}
