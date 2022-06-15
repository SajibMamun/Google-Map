package com.example.googlemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    ImageView searchimagebtn;
    EditText searchet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiazlize();




        checkpermissions();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initiazlize() {
        searchet=findViewById(R.id.SearchETid);
        searchimagebtn=findViewById(R.id.SearchImageiconid);
    }













    private void checkpermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },0);
                return;
            }
        }
    }











    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;


        LatLng BD = new LatLng(23.7807777, 90.3492858);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(BD));

      //  googleMap.addMarker(new MarkerOptions().position(BD).title("Dhaka").snippet("The capital of Bangladesh"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BD, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkpermissions();
        }
        googleMap.setMyLocationEnabled(true);


        //this is for custom button
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        SearchLocation();



    }

    private void SearchLocation() {

        searchet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                String search=searchet.getText().toString().trim();
                if(i== EditorInfo.IME_ACTION_SEARCH
                ||i==EditorInfo.IME_ACTION_DONE
                ||keyEvent.getAction()==KeyEvent.ACTION_DOWN
                ||keyEvent.getAction()==KeyEvent.KEYCODE_ENTER)
                {

                    Geocoder geocoder=new Geocoder(MainActivity.this);


                    List<Address> addressList=new ArrayList<>();

                    try {
                        addressList=geocoder.getFromLocationName(search,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(addressList.size()>0)
                    {
                        Address address=addressList.get(0);


                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)).snippet(" Courntry Name: " +address.getCountryName()+

                                "Locality: "+address.getLocality()+"Zip Code: "+address.getPostalCode()));

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    }


                }







                return false;
            }
        });



    }

    private void currentlocation() {

        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkpermissions();
        }
        Task location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Location currentllocation = (Location) task.getResult();

                map.addMarker(new MarkerOptions().position(new LatLng(currentllocation.getLatitude(),currentllocation.getLongitude())));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentllocation.getLatitude(),currentllocation.getLongitude()),7));


            }
        });
    }

    public void myLocation(View view) {

        currentlocation();

    }
}