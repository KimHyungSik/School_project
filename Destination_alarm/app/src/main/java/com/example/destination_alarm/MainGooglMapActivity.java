package com.example.destination_alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class MainGooglMapActivity extends AppCompatActivity {
    //거리계산 테스트용
    private TextView testText;
    private String testTextString, SaveLat = null, SaveLng = null;

    public static final  String MainIp = "192.168.0.2"; //아이피 호
    public static final int MainPort = 8800; //폰트 번호

    private double mlat, mlng, lat, lng;
    private FloatingActionButton fab;

    private GpsTracker gpsTracker;
    private MarkerData chk = new MarkerData();
    private Socket_ socket_;

    private LatLng myPosition, touchPosition, clickPosition;
    private Context context;

    //거리체크 변수
    private double Ddistance;
    private int Idistance;

    //알림 1번만보내기 체크
    private boolean notification_check = false;

    //런너블 타이머
    Timer timer;

    private FusedLocationProviderClient fusedLocationClient;

    private MapMarker mapMarker;

    private Check_Distance check_distance = new Check_Distance();
    //각종상수들
    private static final int GPS_ENABLE_REQUEST_CODE = 2000;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String CHANNEL_ID = "Destination_channel";
    //저장된 롱 클릭 좌표 저장
    public LatLng LongClick_latLng;

    //체크해야할 퍼미션들
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_googl_map);
        context = this;
        createNotificationChannel();
        SaveLat = PreferenceManager.getString(context, "Save_Long_Clikc_Position_Lat");
        SaveLng = PreferenceManager.getString(context, "Save_Long_Clikc_Position_Lng");
        //저장된 롱 클릭 마커 확인
        if(SaveLng!=null&&SaveLat!=null){
            LongClick_latLng = new LatLng(Double.parseDouble(SaveLat),Double.parseDouble(SaveLng));
            mapMarker = new MapMarker(context, LongClick_latLng);
        }else{
        mapMarker = new MapMarker(context);}


        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().
                findFragmentById(R.id.map);

        //Last knows location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Check Permission
        if (checkLocationServicesStatus()) {
            checkRunTimePermission();
        }

        //fab 사용 함수
        fab = findViewById(R.id.find_location_me);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickFab();
            }
        });
        mapFragment.getMapAsync(mapMarker);

        testText = findViewById(R.id.test_distance);
        //Last knowLocation 위치 설정
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null){

                            mlat = location.getLatitude();
                            mlng = location.getLongitude();

                            mapMarker.settingCamraMarakr(mlat, mlng);
                        }
                    }
                });


        startTimer();

    }


    public void onclickFab(){
         gpsTracker = new GpsTracker(MainGooglMapActivity.this);
        mlat = gpsTracker.getLatitude();
        mlng = gpsTracker.getLongitude();

        mapMarker.settingCamraMarakr(mlat, mlng);
        Intent intent = new Intent(getApplicationContext(), Menu_Activity.class);
        startActivity(intent);
    }

    //prmission check
    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainGooglMapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainGooglMapActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasBackgroundLocationPermission = ContextCompat.checkSelfPermission(MainGooglMapActivity.this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED&&
                hasBackgroundLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainGooglMapActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainGooglMapActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainGooglMapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainGooglMapActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //타이머 설정
    private void startTimer(){
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Timerrun();
            }
        },0, GPS_ENABLE_REQUEST_CODE);
    }

    //런너블 함수 설정
    private void Timerrun(){
        this.runOnUiThread(timerTick);
    }

    private Runnable timerTick = new Runnable() {
        @Override
        public void run() {
            gpsTracker = new GpsTracker(MainGooglMapActivity.this);
            mlat = gpsTracker.getLatitude();
            mlng = gpsTracker.getLongitude();

            mapMarker.settingNonCamerMarakr(mlat, mlng);

            myPosition = mapMarker.getMarkerLatLng();
            clickPosition = mapMarker.getClickMarkerLatLng();
            touchPosition = mapMarker.getLongClickhMarkerLatLng();

            if(touchPosition != null && clickPosition != null){
            Ddistance = check_distance.getDistance(clickPosition, touchPosition);

            Idistance = (int)Math.round(Ddistance);

            testTextString = Double.toString(Idistance);
            testText.setText(testTextString);

            if(Idistance <=40 && notification_check == false){
                    if(chk.getOnOff() == 0){
                        chk.setOnOff(1);
                        socket_ = new Socket_(MainIp, MainPort, chk.getOnOff());
                        socket_.start();
                        mapMarker.ChangeLongMarkerColor(chk.getOnOff());
                    }
                    sendNotification();
                    notification_check = true;
                }
            if(Idistance >= 50 && notification_check == true){
                if(chk.getOnOff() == 1){
                    chk.setOnOff(0);
                    socket_ = new Socket_(MainIp, MainPort, chk.getOnOff());
                    socket_.start();
                    mapMarker.ChangeLongMarkerColor(chk.getOnOff());
                }
                notification_check = false;
                }
            }
        }
    };
    //Creat Notification channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Destination_Cahnnel";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    //sned Notification
    private void sendNotification(){
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Intent push;
        PendingIntent fullScreenPendingIntent;
        push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(this, MainGooglMapActivity.class);
        fullScreenPendingIntent = PendingIntent.getActivity(this, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("성공")
                .setContentText("알람 보내기 성공")
                .setAutoCancel(true)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, builder.build());
        vibrator.vibrate(1500);
    }



}


