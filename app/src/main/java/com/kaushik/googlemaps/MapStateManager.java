package com.kaushik.googlemaps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class MapStateManager {

    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    //private static final String ZOOM = "zoom";
    //private static final String BEARING = "bearing";
    //private static final String TILT = "tilt";
    //private static final String MAPTYPE = "MAPTYPE";

    private static final String PREFS_NAME ="mapCameraState";
    private int incrementValue = 0;

    private SharedPreferences mapStatePrefs;

    public MapStateManager(Context context) {
        mapStatePrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveMapState(GoogleMap map,LatLng latLng) {
        SharedPreferences.Editor editor = mapStatePrefs.edit();
        //CameraPosition position = map.getCameraPosition();

        editor.putFloat(LATITUDE + incrementValue, (float) latLng.latitude);
        editor.putFloat(LONGITUDE + incrementValue, (float) latLng.longitude);
        //editor.putFloat(ZOOM, position.zoom);
        //editor.putFloat(TILT, position.tilt);
        //editor.putFloat(BEARING, position.bearing);
        //editor.putInt(MAPTYPE, map.getMapType());
        System.out.println(LATITUDE + incrementValue);
        incrementValue++;
        editor.apply();
    }

    public LatLng getSavedCameraPosition() {
        double[] latitude = new double[incrementValue];
        double[] longitude = new double[incrementValue];
        for(int i = 0 ; i < incrementValue ; i++){

            latitude[i] = mapStatePrefs.getFloat(LATITUDE, 0);
            if (latitude[i] == 0) {
                return null;
            }
            longitude[i] = mapStatePrefs.getFloat(LONGITUDE, 0);
            return (new LatLng(latitude[i], longitude[i]));
        }

        //LatLng target = new LatLng(latitude, longitude);

        /*float zoom = mapStatePrefs.getFloat(ZOOM, 0);
        float bearing = mapStatePrefs.getFloat(BEARING, 0);
        float tilt = mapStatePrefs.getFloat(TILT, 0);*/

        //CameraPosition position = new CameraPosition(target, zoom, tilt, bearing);
        return null;
    }

}
