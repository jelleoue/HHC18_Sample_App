package com.example.jelleoue.hhcgeobot;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jelleoue.hhcgeobot.services.IndoorProvider;
import com.example.jelleoue.hhcgeobot.services.LocationClient;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import io.indoorlocation.core.IndoorLocation;
import io.mapwize.mapwizeformapbox.AccountManager;
import io.mapwize.mapwizeformapbox.MapOptions;
import io.mapwize.mapwizeformapbox.MapwizePlugin;
import io.mapwize.mapwizeformapbox.api.Api;
import io.mapwize.mapwizeformapbox.api.ApiCallback;
import io.mapwize.mapwizeformapbox.model.Direction;
import io.mapwize.mapwizeformapbox.model.LatLngFloor;
import io.mapwize.mapwizeformapbox.model.Place;
import io.mapwize.mapwizeformapbox.model.Venue;

public class MainActivity extends AppCompatActivity {

    private static final String MPBX_API_KEY = "pk.eyJ1IjoidmF5bW9uaW4iLCJhIjoiY2pjdWlncWt3M2U1ajMzcGdxYTBqeGh3diJ9.dyMNTJktwr2RujuRf4n3GA";
    private static final String MWZ_API_KEY = "98d7bc53090ecc4da62e09269332fe5b";
    protected MapboxMap mapboxMapp;
    protected MapwizePlugin mapwizePlugin;
    protected MapView mapView;
    protected IndoorProvider indoor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Request Location permission etc...
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_CONTACTS}, 0);


        // Instanciate Indoor Provider
        indoor = new IndoorProvider();

        // Stellar LBS Positioning SDK
        LocationClient loc = new LocationClient(this, indoor);

        setContentView(R.layout.activity_main);
        Mapbox.getInstance(this, MPBX_API_KEY); // instanciate mapbox with MAPBOX api key

        AccountManager.start(getApplication(), MWZ_API_KEY); // instanciate mapwize with MAPWIZE api key

        mapView = (MapView) findViewById(R.id.mymap);
        mapView.onCreate(savedInstanceState);         // create the view of the MAPBOX map defined in activity_main.xml
        mapView.getMapAsync(new OnMapReadyCallback() { // load the map
            @Override
            public void onMapReady(MapboxMap mapboxMap) { // when map is ready
                mapboxMapp = mapboxMap;
                MapOptions options = new MapOptions.Builder().build();
                mapwizePlugin = new MapwizePlugin(mapView, mapboxMap, options); // instanciate MAPWIZE plugin on top of MAPBOX
                mapwizePlugin.setLocationProvider(indoor); // associate the Stellar LBS location provider to MAPWIZE
            }
        });
    }

    // Wayfinding code +
    private void letsgo(final Direction dir){   // need to run in UI thread.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mapwizePlugin.setDirection(dir);    // draw the wayfinding
            }
        });
    }
    public void wayfinding(View v){
        Api.getVenueWithName("alebrest office", new ApiCallback<Venue>() { // harcdcoded to run on brest map
            @Override
            public void onSuccess(Venue venue) {
                Api.getPlaceWithAlias("pause", venue, new ApiCallback<Place>() { // harcoded final place = pause
                    @Override
                    public void onSuccess(Place place) {
                        Api.getDirection(new LatLngFloor(mapwizePlugin.getUserPosition().getLatitude(), mapwizePlugin.getUserPosition().getLongitude(), mapwizePlugin.getUserPosition().getFloor()), place, true, new ApiCallback<Direction>() {
                            @Override
                            public void onSuccess(Direction direction) {
                                letsgo(direction);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Log.i("MainActivity", "error getDirection");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("MainActivity", "error getPlaceWithAlias");
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i("MainActivity", "error GetVenueWithName");
            }
        });
    }

    // Wayfinding code -

    // Intent code +
    @Override
    // Opening an API for the demo app to be launched directly on the wayfinding to a given POI
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        checkIntent(intent);
    }

    private void checkIntent(Intent intent) {

        Intent lIntent;
        if (intent != null) {
            lIntent = intent;
        }
        else
        {
            lIntent = getIntent();
        }

        Uri lUri = lIntent.getData();
        String lMethod;
        String lWhereToGo;
        if (lUri != null) {
            lMethod = lUri.getQueryParameter("action");
            if (lMethod !=null) {
                if (lMethod.equalsIgnoreCase("wayfinding")) {
                    lWhereToGo = lUri.getQueryParameter("poi");
                    if (lWhereToGo != null)
                    {
                        wayfinding(mapView);
                    }
                }
            }
        }
        else
        {
            lMethod = getIntent().getStringExtra("action");
            if (lMethod !=null) {
                if (lMethod.equalsIgnoreCase("wayfinding")) {
                    lWhereToGo = getIntent().getStringExtra("poi");
                    if (lWhereToGo != null)
                    {
                        wayfinding(mapView);
                    }
                }
            }
        }
    }
    // Intent code -




}
