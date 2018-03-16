package com.example.jelleoue.hhcgeobot.services;

import android.location.Location;
import android.util.Log;

import com.example.jelleoue.hhcgeobot.MainActivity;
import com.polestar.naosdk.api.external.NAOERRORCODE;
import com.polestar.naosdk.api.external.NAOLocationHandle;
import com.polestar.naosdk.api.external.NAOLocationListener;
import com.polestar.naosdk.api.external.NAOSensorsListener;
import com.polestar.naosdk.api.external.NAOSyncListener;
import com.polestar.naosdk.api.external.TNAOFIXSTATUS;

import io.indoorlocation.core.IndoorLocation;

// NAO LocationListener : positioning events
// NAO SensorsListener : sensors events (recalibrate device, activate Bluetooth etc...)
// NAO SyncListener : synchronization events (with cloud engine)
public class LocationClient implements NAOLocationListener, NAOSensorsListener, NAOSyncListener {

    protected NAOLocationHandle handle; // generic service handle

    protected String API_KEY = "lAvlCoX7VSMyekJtDPNGw@eu";
    protected MainActivity main;
    protected IndoorProvider _mappingIndoorProvider;

    public LocationClient(MainActivity pMainActivity, IndoorProvider mappingIndoorProvider)
    {
        main = pMainActivity;
        _mappingIndoorProvider = mappingIndoorProvider;
        // instanciate
        handle = new NAOLocationHandle(pMainActivity, LBSService.class, API_KEY, this, this);
        handle.synchronizeData(this);
        handle.start();
    }


    @Override
    // on position event (every 1 sec)
    public void onLocationChanged(Location location) {
        Log.i("LocationClient", "on new location");
        // use the IndoorProvider to push the position on the map.
        _mappingIndoorProvider.setIndoorLocation(new IndoorLocation(location, location.getAltitude()));
    }

    @Override
    public void onLocationStatusChanged(TNAOFIXSTATUS tnaofixstatus) {
        Log.i("LocationClient", "on new location status");
    }

    @Override
    public void onEnterSite(String s) {

    }

    @Override
    public void onExitSite(String s) {

    }

    @Override
    public void onError(NAOERRORCODE naoerrorcode, String s) {

    }

    @Override
    public void requiresCompassCalibration() {

    }

    @Override
    public void requiresWifiOn() {

    }

    @Override
    public void requiresBLEOn() {

    }

    @Override
    public void requiresLocationOn() {

    }

    @Override
    public void onSynchronizationSuccess() {
        Log.i("LocationClient", "on synchro OK");

    }

    @Override
    public void onSynchronizationFailure(NAOERRORCODE naoerrorcode, String s) {
        Log.i("LocationClient", "on synchro KO");
    }


}
