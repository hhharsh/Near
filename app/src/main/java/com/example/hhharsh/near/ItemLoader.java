package com.example.hhharsh.near;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by hhharsh on 4/1/18.
 */

public class ItemLoader extends AsyncTaskLoader<List<Item>> {
    private String url;
    public ItemLoader(Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    public List<Item> loadInBackground() {
        if(this.url==null){
            return null;
        }
        List<Item> result = QueryUtils.fetchData(url);
        return result;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
