package com.example.testalimap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static MapsFragment INSTANCE = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double longi, lati;
    String cityName;
    ViewGroup view;
    GoogleMap gMap;
    SearchView search = null;
    static ArrayList<TestClass> eventSpawn = new ArrayList<TestClass>(){{
        add(new TestClass("Party", 56.04646740, 12.69451210, "This is some random info"));
        add(new TestClass("Sport", 56.16, 13.77, "This is a sport event "));
        add(new TestClass("Party", 55.61, 13, "This is a party event"));
        add(new TestClass("Sport", 59.04646740, 11, "hello how are you"));
    }};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_maps, null);
        search = (SearchView) view.findViewById(R.id.searchLocation);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ImageView createEvent = getActivity().findViewById(R.id.createEventButton);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* LatLng sydney=new LatLng(-35,151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("I am here "));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        */

        getClientCurrentLocation();
        spawnNearbyEventsOnMap(googleMap);
        searchLocation();
        LatLng sydney = new LatLng(lati, longi);
        gMap = googleMap;
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title=marker.getTitle();
                final AlertDialog.Builder eventPopup = new AlertDialog.Builder(getActivity()); //Creates dialog
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View dialogView = inflater.inflate(R.layout.popup,null);
                final AlertDialog dialog;
                eventPopup.setView(dialogView);
                eventPopup.setTitle("Event");
                eventPopup.setIcon(R.drawable.party);
                dialog = eventPopup.create();
                dialog.show();

                TextView tvInfo=dialogView.findViewById(R.id.info);
                tvInfo.setText(title);

                return true;
            }
        });
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {




                MarkerOptions markerOptions = new MarkerOptions();
                try {

                    addNameToMarkerOnMap(latLng.latitude, latLng.longitude);
                } catch (IOException e) {
                    System.out.println("NO AREA FOUND !");
                }
                markerOptions.position(latLng).title(cityName);
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                gMap.clear();
                gMap.addMarker(markerOptions);

            }
        });
        //Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.drawable.sport);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("You").snippet("Snippet ...").icon(BitmapDescriptorFactory.fromBitmap(customizeImageToBitMap(R.drawable.sport))));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    public void getClientCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null){
                        lati = location.getLatitude();
                        longi = location.getLongitude();
                    }else {
                        System.out.println("Couldn't access the location!!!");
                    }
                }
            });



        }


    }

    public void addNameToMarkerOnMap(double lati, double longi) throws IOException {
        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(lati, longi, 1);
        if (addresses.size() > 0) {
            cityName = addresses.get(0).getLocality();
        }
        else {
            System.out.println("No area found!");
        }
    }

    public void searchLocation(){
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = search.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    assert addressList != null;
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    gMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    gMap.clear();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void spawnNearbyEventsOnMap(GoogleMap googleMap){
        //Load from firebase data
        //once loaded, create if statements to check what the event is
        //Spawn the event as markers with description (only near current location, not the whole world)
        int event = 0;
        for (int i = 0; i < eventSpawn.size(); i++) {
            if (eventSpawn.get(i).getEventType().equals("Sport")){
                event = R.drawable.sport;
            }else if (eventSpawn.get(i).getEventType().equals("Party")){ // add more icons later
                event = R.drawable.party;
            }

            gMap = googleMap;
            LatLng latLng = new LatLng(eventSpawn.get(i).getLatitude(), eventSpawn.get(i).getLongitude());
            gMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(eventSpawn.get(i).getEventType())
                    .icon(BitmapDescriptorFactory.fromBitmap(customizeImageToBitMap(event))));
        }



    }



    public Bitmap customizeImageToBitMap(int resourcePath){ //Example input: "R.id.sport"
        int height = 50; //Default
        int width = 50; //Default
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(resourcePath); //Change
        Bitmap b = bitmapdraw.getBitmap();

        return Bitmap.createScaledBitmap(b, width, height, false);
    }

    public void createEvent(){

        final AlertDialog.Builder eventPopup = new AlertDialog.Builder(getActivity()); //Creates dialog
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.new_test,null); //Inflate the actual test.xml file
        final AlertDialog dialog;
        eventPopup.setView(dialogView);
        dialog = eventPopup.create();
        dialog.show();
    }

}
