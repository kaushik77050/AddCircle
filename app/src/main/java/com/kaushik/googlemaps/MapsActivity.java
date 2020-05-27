 package com.kaushik.googlemaps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.kaushik.googlemaps.LatLng;
import java.util.ArrayList;

 public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference myRef;
    MapStateManager mapStateManager;
    ArrayList<LatLng> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Locations");

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;

        mapFragment.getMapAsync(this);

    }

     @Override
     protected void onPause() {
         super.onPause();


         Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
     }

     @Override
     protected void onDestroy() {
         super.onDestroy();

     }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng pos;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //Creating Marker
                MarkerOptions markerOptions = new MarkerOptions();
                //Set Marker Position
                markerOptions.position(latLng);
                //Set Latitude And Longitude On Marker
                markerOptions.title(latLng.latitude+ " : " + latLng.longitude);
                //Zoom the Marker
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,100));
                //Add marker On Map
                mMap.addMarker(markerOptions);
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(10)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(50,0,0,255)));

                String id = myRef.push().getKey();

                final Locations locations = new Locations(id,latLng.latitude,latLng.longitude);

                //myRef.child(id).setValue(locations.getLl());
                myRef.child(id).setValue(locations);

                //Retrive previously saved state of map
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Toast.makeText(MapsActivity.this, "Location:"+snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                            Locations locations1 = snapshot.getValue(Locations.class);
                            //String id1 = locations1.getId();
                            Double lat = locations1.getLatitude();
                            Double lon = locations1.getLongitude();
                            Toast.makeText(MapsActivity.this,  "latitude:"+lat+" longitude:"+lon, Toast.LENGTH_SHORT).show();
                            //LatLng ltln = locations1.getLl();
                            //Toast.makeText(MapsActivity.this, "Location"+ltln.longitude, Toast.LENGTH_SHORT).show();
                        }
//                        Double lt = dataSnapshot.child("latitude").getValue(Double.class);
//                        String ln = dataSnapshot.child("longitude").getValue(String.class);
                        //Log.i("latitude",ln);
                        //System.out.println(lt);
                        //String id1 = dataSnapshot.getValue().toString();
                        //Toast.makeText(MapsActivity.this, "Latitude:"+lt, Toast.LENGTH_SHORT).show();
//                        LatLng nitte = new LatLng(lt,ln);
//                        mMap.addMarker(new MarkerOptions().position(nitte).title("Marker in Nitte").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
//                        //  LatLng latLng1 = (LatLng) dataSnapshot.child("ll").getValue();
//                        //Creating Marker
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        //Set Marker Position
//                        markerOptions.position(latLng1);
//                        //Set Latitude And Longitude On Marker
//                        markerOptions.title(latLng1.latitude+ " : " + latLng1.longitude);
//                        //Zoom the Marker
//                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,100));
//                        //Add marker On Map
//                        mMap.addMarker(markerOptions);
//                        Circle circle = mMap.addCircle(new CircleOptions()
//                                .center(latLng1)
//                                .radius(10)
//                                .strokeColor(Color.RED)
//                                .fillColor(Color.argb(50,0,0,255)));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//
//                Toast.makeText(MapsActivity.this, "Location Added", Toast.LENGTH_SHORT).show();
//                //mapStateManager.saveMapState(mMap,latLng);
            }
        });

        }
    }