package com.example.hhharsh.near;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by hhharsh on 6/1/18.
 */

public class ItemLoader2  extends AsyncTaskLoader<List<String>> {

    private String url;
    public ItemLoader2(Context context, String url) {
        super(context);
        this.url=url;
    }

    @Override
    public List<String> loadInBackground() {
        if(this.url==null){
            return null;
        }
        List<String> result = QueryUtils.fetchLocation(url);
        return result;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
