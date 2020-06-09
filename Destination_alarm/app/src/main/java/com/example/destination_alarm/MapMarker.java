package com.example.destination_alarm;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMarker extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private Marker marakr = null, ClickMarker = null, LongClickMarker = null;
    private CameraUpdate cameraUpdate;
    private LatLng Defaultposition = new LatLng(37.56, 126.97), Clickposition = null, LongClickposition = null, saveLatLng = null;
    private MarkerOptions markerOptions = new MarkerOptions();
    private MarkerOptions ClickarkerOptions = new MarkerOptions(), LongClickOptions = new MarkerOptions();
    private Context context;


    public static final  String Ip = "192.168.0.2"; //아이피 호
    public static final int Port = 8800; //폰트 번호
    Socket_data chk_data = new Socket_data();
    Socket_ socket_;


    public MapMarker(Context context){
        this.context = context;
    }
    public MapMarker(Context context, LatLng savelatlng){this.context = context; saveLatLng = savelatlng;}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markerOptions.position(Defaultposition);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        marakr = mMap.addMarker(markerOptions);
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(Defaultposition, 10);
        mMap.moveCamera(cameraUpdate);

        if(saveLatLng != null){
            OnMapLongClickListener(saveLatLng, PreferenceManager.getInt(context, "Marker_chk"));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                OnMapClickListener(latLng);
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) { OnMapLongClickListener(latLng, PreferenceManager.getInt(context, "Marker_chk"));
            }
        });
    }

    //짧게 터치시 마커 생성
    private void OnMapClickListener(LatLng latLng){
        if(ClickMarker != null){
            ClickMarker.remove();
        }
        Clickposition = latLng;
        ClickarkerOptions.position(Clickposition);
        ClickarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        ClickMarker = mMap.addMarker(ClickarkerOptions);
    }

    //길게 터치시 마커 생성
    public void OnMapLongClickListener(LatLng latLng , int setMarker){
        if(LongClickMarker != null){
            LongClickMarker.remove();
        }
        LongClickposition = latLng;
        LongClickOptions.position(LongClickposition);
        if(setMarker == 0) {
            LongClickOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }
        if(setMarker == 1){
            LongClickOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }

        saveLongClikcPosition(LongClickposition);

        LongClickMarker = mMap.addMarker(LongClickOptions);
    }

    public void ChangeLongMarkerColor(int color){
        if(color == 0) {
            LongClickOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }
        if(color == 1){
            LongClickOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }

        LongClickMarker = mMap.addMarker(LongClickOptions);
    }


    //마커가 생성된곳에 카메라 무빙
    public void settingCamraMarakr(double lat, double lng){
        marakr.remove();
        MarakrHandler(lat,lng,17,true);
    }

    //카메라가 안움직이고 마커를 생성
    public void settingNonCamerMarakr(double lat, double lng){
        marakr.remove();
        MarakrHandler(lat,lng,15,false);
    }

    //마커 추가시 부르는 함수(위치,위치, 줌배율, 카매라가 움직일것인가)
    public void MarakrHandler(double lat , double lng,float zoom, boolean cameraset){
        Defaultposition = new LatLng(lat, lng);
        markerOptions.position(Defaultposition);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        marakr = mMap.addMarker(markerOptions);
        if(cameraset){
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(Defaultposition, zoom);
            mMap.animateCamera(cameraUpdate);}
    }

    public LatLng getMarkerLatLng(){
        return Defaultposition;
    }

    public LatLng getClickMarkerLatLng(){
        if (Clickposition != null){
            return Clickposition;
        }
        return null;
    }

    public LatLng getLongClickhMarkerLatLng() {
        if (LongClickposition != null){
            return LongClickposition;
        }
        return null;
    }

    public void saveLongClikcPosition(LatLng latLng){
        String Lat = Double.toString(latLng.latitude);
        String Lng = Double.toString(latLng.longitude);

        PreferenceManager.clear(context);
        PreferenceManager.setString(context, "Save_Long_Clikc_Position_Lng", Lng);
        PreferenceManager.setString(context, "Save_Long_Clikc_Position_Lat", Lat);
        PreferenceManager.setInt(context, "Marker_chk", chk_data.getOnOff());
    }

}