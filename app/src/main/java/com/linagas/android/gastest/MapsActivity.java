package com.linagas.android.gastest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        View.OnClickListener {

    LatLng myLocation;
    boolean isButtonClicked = false;

    JSONArray recoreds = new JSONArray();

    static SQLiteDatabase db = null;
    SqlDB sql;

    int countLocation = 0;
    int id = 0;
    String name = "Hamza";

    Button save, zoom, getLocation;


    public static Location mLastLocation;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        save = (Button) findViewById(R.id.btnSave);
        zoom = (Button) findViewById(R.id.btnZoom);
        getLocation = (Button) findViewById(R.id.btnGetLoc);

        save.setOnClickListener(this);
        zoom.setOnClickListener(this);
        getLocation.setOnClickListener(this);

        sql = new SqlDB();

        creat_DB();


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

//        myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (mLastLocation != null) {

            // mMap.clear();
            // Add a marker in Sydney, Australia, and move the camera.
            LatLng myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


    }

    @Override
    protected void onStart() {
        if (mapFragment != null) {

            if (isButtonClicked == false) {
                AsyncTaskTable asyncTaskTable = new AsyncTaskTable();
                asyncTaskTable.execute();
            }


        } else {
            Toast.makeText(MapsActivity.this, "save your location first", Toast.LENGTH_LONG).show();

        }
        mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View view) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        myLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());



        if (view.getId() == R.id.btnSave)
        {

            if (mLastLocation != null) {




                    sql.insert_myLocation( String.valueOf(id), name, String.valueOf(mLastLocation.getLatitude()) ,
                            String.valueOf(mLastLocation.getLongitude()),
                            "My location" + countLocation);

                    // Add a marker in Sydney, Australia, and move the camera.
                    mMap.addMarker(new MarkerOptions()
                            .position(myLocation)
                            .title("My Location"));




                    id =  sql.setMaxID(id);
                    id++;
                    countLocation++;







            }

        }

        if (view.getId() == R.id.btnZoom)
        {


            if (mLastLocation != null) {

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(myLocation)      // Sets the center of the map to LatLng (refer to previous snippet)
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Add a marker in Sydney, Australia, and move the camera.
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));

            }

        }

        if (view.getId() == R.id.btnGetLoc)
        {


            if (mLastLocation != null) {
                isButtonClicked = true;
                AsyncTaskTable asyncTaskTable = new AsyncTaskTable();
                asyncTaskTable.execute();


            }


        }
    }


    public void getAllLocations(){
        int i = 0;
        LatLng mySqlLocation = null;
        while (i < recoreds.length()) {
            sql.get_myLocation(String.valueOf(id));


                double d1 = Double.parseDouble(sql.sqlLat);
                double d2 = Double.parseDouble(sql.sqlLng);
                 mySqlLocation = new LatLng(d1,
                        d2);


            // Add a marker in Sydney, Australia, and move the camera.
            mMap.addMarker(new MarkerOptions()
                    .position(mySqlLocation)
                    .title("My Location" + id));


            id++;
            i++;



        }



    }

    public void creat_DB()
    {
        //create the sqlit database with the tables
        try{
            db=openOrCreateDatabase("linagas",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS myLocation(id VARCHAR,name VARCHAR,lat VARCHAR,lng VARCHAR, description VARCHAR);");


        }catch(Exception ex){ex.printStackTrace();}
    }




    class AsyncTaskTable extends AsyncTask {



        Bundle send= new Bundle();

        @Override
        protected Object doInBackground(Object[] objects) {

            recoreds = sql.get_allData(recoreds);

            for (int j = 0 ; j < recoreds.length() ; j ++)
                try {
                    send.putString("jsondata"+j, recoreds.getString(j));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);










                if (isButtonClicked) {
                    isButtonClicked = false;

                    Intent i = new Intent(MapsActivity.this, MyLocations.class);
                    i.putExtras(send);
                    startActivity(i);
                }
            else
                {
                    getAllLocations();
                }

        }
    }


   

}
