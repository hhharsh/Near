package com.example.hhharsh.near;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhharsh on 4/1/18.
 */

public class MainList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Item>>{
    private String s2;
    private TextView em;
    private String latitude;
    private String longitude;
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private CustomAdapter mAdapter;
    private static final String REQUEST_URL = "https://api.foursquare.com/v2/venues/search?v=20160607&intent=checkin&limit=20&radius=100000&client_id=HCVIWM1P3CR3SNBVL4FRMQWJ2TLKVU55ZLDVOZLWKOAO3ZDY&client_secret=OUAK3RINU4UZNRMS4NBBR3R2O0EB4I10QMEENEWFLWGVJFDG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

       Intent in2= getIntent();
       s2=in2.getStringExtra("search_mess");
       latitude=in2.getStringExtra("lat");
       longitude=in2.getStringExtra("lon");

       Log.v("here",latitude+"---"+longitude);

        ListView lv=(ListView)findViewById(R.id.list_id);
        em=(TextView)findViewById(R.id.empty_state);

        lv.setEmptyView(em);

        mAdapter = new CustomAdapter(this, new ArrayList<Item>());
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
Log.v("rohan","hiii");
                Item ii = mAdapter.getItem(position);
                String uu=ii.getUrl();
                Log.v("uriiiiii",uu);
                Uri itemUri;
                if(ii.getUrl()!="") {
                   /* Uri itemUri = Uri.parse(ii.getUrl());*/

                    if (!uu.startsWith("http://")) {
                        itemUri = Uri.parse("http://" + uu);
                    }

                    else{
                        itemUri = Uri.parse(uu);
                    }


                    Intent launchWeb = new Intent(Intent.ACTION_VIEW, itemUri);
                    startActivity(launchWeb);
                }

            }
        });

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        } else {

            //Hide progress bar in order to display error message
            View progressBar = findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);

            //Update UI to show error message
            em.setText("No Internet Connectoin");
        }
    }

    @Override
    public Loader<List<Item>> onCreateLoader(int id, Bundle args) {

        StringBuilder sb=new StringBuilder(REQUEST_URL);
        sb.append("&query="+s2);
        sb.append("&ll="+latitude+","+longitude);

        Log.v("See",sb.toString());
        return new ItemLoader(this,sb.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {
        mAdapter.clear();

        //Hiding the progress bar once data is retrieved
        View progressBar = (View) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.GONE);

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        em.setText("No Data Found");

    }

    @Override
    public void onLoaderReset(Loader<List<Item>> loader) {

    }
}





