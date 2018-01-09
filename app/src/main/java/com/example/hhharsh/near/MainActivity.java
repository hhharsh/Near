package com.example.hhharsh.near;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.os.StrictMode;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>{

    private static final String REQUEST_URL = "https://api.foursquare.com/v2/venues/search?v=20160607&intent=checkin&limit=1&radius=10000&client_id=HCVIWM1P3CR3SNBVL4FRMQWJ2TLKVU55ZLDVOZLWKOAO3ZDY&client_secret=OUAK3RINU4UZNRMS4NBBR3R2O0EB4I10QMEENEWFLWGVJFDG";
    private TextView em2;

    final String TAG = "GPS";
    Location loc;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    boolean isGPS = false;
    boolean isNetwork = false;



    private LocationManager locationManager;
    private LocationListener listener;
    double lat=0,lon=0;

    StringBuilder sb;
    TextView tt;
    ImageButton imb;

    private static final int EARTHQUAKE_LOADER_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }*/

        imb=(ImageButton)findViewById(R.id.reload_id);
        imb.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ImageButton re=(ImageButton)findViewById(R.id.reload_id);

        re.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent in4=new Intent(MainActivity.this,MainActivity.class);
                startActivity(in4);
            }
        });





        ImageButton im1=(ImageButton)findViewById(R.id.im1_id);
        ImageButton im2=(ImageButton)findViewById(R.id.im2_id);
        ImageButton im3=(ImageButton)findViewById(R.id.im3_id);
        ImageButton im4=(ImageButton)findViewById(R.id.im4_id);

        im1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent in2=new Intent(MainActivity.this,MainList.class);
                in2.putExtra("search_mess","food");
                in2.putExtra("lat",String.valueOf(lat));
                in2.putExtra("lon",String.valueOf(lon));
                startActivity(in2);
            }
        });

        im2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent in2=new Intent(MainActivity.this,MainList.class);
                in2.putExtra("search_mess","cinema");
                in2.putExtra("lat",String.valueOf(lat));
                in2.putExtra("lon",String.valueOf(lon));
                startActivity(in2);
            }
        });

        im3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent in2=new Intent(MainActivity.this,MainList.class);
                in2.putExtra("search_mess","shopping");
                in2.putExtra("lat",String.valueOf(lat));
                in2.putExtra("lon",String.valueOf(lon));
                startActivity(in2);
            }
        });

        im4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent in2=new Intent(MainActivity.this,MainList.class);
                in2.putExtra("search_mess","hotel");
                in2.putExtra("lat",String.valueOf(lat));
                in2.putExtra("lon",String.valueOf(lon));
                startActivity(in2);
            }
        });




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lat=location.getLatitude();
                lon=location.getLongitude();


                Log.v("new",lat+"=="+lon);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
               /* showSettingsAlert();*/
            }
        };

       /* final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }*/

        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();

        }
        else {
            getLocation();
            sb=new StringBuilder(REQUEST_URL);
            sb.append("&ll="+lat+","+lon);
        }

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
           /* while(lat==0);*/
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        } else {
            em2=findViewById(R.id.empty_state2);
            View progressBar = findViewById(R.id.loading_spinner2);
            progressBar.setVisibility(View.GONE);
            em2.setText("No Internet Connectoin");

            imb.setVisibility(View.VISIBLE);
        }


        em2=(TextView)findViewById(R.id.empty_state2);

        tt=(TextView)findViewById(R.id.textView3);

        Log.v("loook",sb.toString());

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                getLocation();
                break;
            default:
                break;
        }
    }

    void getLocation() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }

            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
if(isGPS) {
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);

    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    if (loc != null) {
        Log.v("hhh from GPS", loc.getLatitude() + "==" + loc.getLongitude());
        lat=loc.getLatitude();
        lon=loc.getLongitude();



    }
}

else if(isNetwork){
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);

    loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    if (loc != null) {
        Log.v("hhh from network", loc.getLatitude() + "==" + loc.getLongitude());
        lat=loc.getLatitude();
        lon=loc.getLongitude();




    }

}

    }

/*    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d(TAG, "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            Log.v("new",lat+"=="+lon);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d(TAG, "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            Log.v("new",lat+"=="+lon);
                    }
                }
            } else {
                Log.d(TAG, "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }*/

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void Search2(View v){



        EditText ss=( EditText)findViewById(R.id.editText2);
        String gg=ss.getText().toString();

        if(!gg.isEmpty()) {

            Intent in = new Intent(this, MainList.class);
            in.putExtra("search_mess", gg);
            in.putExtra("lat", String.valueOf(lat));
            in.putExtra("lon", String.valueOf(lon));
            startActivity(in);
        }

        else{
            Toast.makeText(this, "Please Enter Place", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new ItemLoader2(this,sb.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> arr2) {
        View progressBar = (View) findViewById(R.id.loading_spinner2);
        progressBar.setVisibility(View.GONE);

        if (arr2 != null && !arr2.isEmpty()) {
            String city=arr2.get(0);
            String state=arr2.get(1);
            String country=arr2.get(2);

            tt.setText(city+","+state+" "+"("+country+")");


            imb.setVisibility(View.GONE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }
}