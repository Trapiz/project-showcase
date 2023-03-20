package com.saxion.sanderh.androidproject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DataFetcher {
    private static DataFetcher instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    private static String baseURI = "https://ergast.com/api/f1/";

    private DataFetcher(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized DataFetcher getInstance(Context context) {
        if (instance == null) {
            instance = new DataFetcher(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static String formatURL(String query) {
        return baseURI + query + ".json";
    }


}
