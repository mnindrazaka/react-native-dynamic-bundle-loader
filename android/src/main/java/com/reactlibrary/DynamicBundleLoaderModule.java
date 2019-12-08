package com.reactlibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.WritableMap;

import java.io.File;

@ReactModule(name = "DynamicBundleLoader")
public class DynamicBundleLoaderModule extends ReactContextBaseJavaModule {

    public interface OnReloadRequestedListener {
        void onReloadRequested();
    }

    private final ReactApplicationContext reactContext;
    private final SharedPreferences bundlePrefs;
    private final SharedPreferences extraPrefs;
    private OnReloadRequestedListener listener;

    public DynamicBundleLoaderModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.bundlePrefs = reactContext.getSharedPreferences("_bundles", Context.MODE_PRIVATE);
        this.extraPrefs = reactContext.getSharedPreferences("_extra", Context.MODE_PRIVATE);
    }

    public OnReloadRequestedListener getListener() {
        return this.listener;
    }

    public void setListener(OnReloadRequestedListener listener) {
        this.listener = listener;
    }

    @Override
    public String getName() {
        return "DynamicBundleLoader";
    }

    @ReactMethod
    public void reloadBundle() {
        if (this.listener != null) {
            this.listener.onReloadRequested();
        }
    }

    @ReactMethod
    public void setActiveBundle(String bundleId) {
        SharedPreferences.Editor editor = this.extraPrefs.edit();
        editor.putString("activeBundle", bundleId);
        editor.commit();
    }

    @ReactMethod
    public void registerBundle(String bundleId, String relativePath) {
        File absolutePath = new File(this.reactContext.getFilesDir(), relativePath);

        SharedPreferences.Editor editor = this.bundlePrefs.edit();
        editor.putString(bundleId, absolutePath.getAbsolutePath());
        editor.commit();
    }

    @ReactMethod
    public void unregisterBundle(String bundleId) {
        SharedPreferences.Editor editor = this.bundlePrefs.edit();
        editor.remove(bundleId);
        editor.commit();
    }

    @ReactMethod
    public void getBundles(Promise promise) {
        WritableMap bundles = Arguments.createMap();
        for (String bundleId : this.bundlePrefs.getAll().keySet()) {
            String path = this.bundlePrefs.getString(bundleId, null);
            Uri url = Uri.fromFile(new File(path));

            bundles.putString(bundleId, url.toString());
        }

        promise.resolve(bundles);
    }

    @ReactMethod
    public void getActiveBundle(Promise promise) {
        promise.resolve(this.extraPrefs.getString("activeBundle", null));
    }

    public String getActiveBundlePath() {
        String activeBundle = this.extraPrefs.getString("activeBundle", null);
        return this.bundlePrefs.getString(activeBundle, null);
    }

    public static String getActiveBundlePath(Context context) {
        SharedPreferences bundlePrefs = context.getSharedPreferences("_bundles", Context.MODE_PRIVATE);
        SharedPreferences extraPrefs = context.getSharedPreferences("_extra", Context.MODE_PRIVATE);

        String activeBundle = extraPrefs.getString("activeBundle", null);
        return bundlePrefs.getString(activeBundle, null);
    }
}
