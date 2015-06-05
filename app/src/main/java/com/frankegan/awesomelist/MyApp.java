package com.frankegan.awesomelist;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * @author frankegan created on 6/2/15.
 */
public class MyApp extends android.app.Application {

    private static MyApp instance;
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    public MyApp() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        //TODO this is mad sus
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapCache(getContext()));
    }

    public static Context getContext() {
        return instance;
    }

    public static RequestQueue getVolleyRequestQueue(){
        return Volley.newRequestQueue(getContext());
    }

    public static ImageLoader getImageLoader(){
        return mImageLoader;
    }

}