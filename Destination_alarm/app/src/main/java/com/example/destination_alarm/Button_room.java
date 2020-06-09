package com.example.destination_alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Button_room extends AppCompatActivity {

    Button bulb_1,bulb_2;
    Socket_data S_data;
    public static final  String MainIp = "192.168.0.5"; //아이피 호
    public static final int MainPort = 9999; //폰트 번호
    private Socket_ socket_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_room);
        SettingId();
        SettingClickListener();

        BottomNavigationView bottomNavigationView = findViewById(R.id.Button_bottom_nav_view);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.GRAY));
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // 어떤 메뉴 아이템이 터치되었는지 확인합니다.
                        switch (item.getItemId()) {
                            case R.id.nav_map:
                                Intent map_intent = new Intent(getApplicationContext(), MainGooglMapActivity.class);
                                startActivity(map_intent);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.nav_button:
                                return true;
                            case R.id.nav_mic:
                                Intent mic_ntent = new Intent(getApplicationContext(), Menu_Activity.class);
                                startActivity(mic_ntent);
                                overridePendingTransition(0, 0);
                                return true;
                        }
                        return false;
                    }
                });
    }

    void SettingId(){
        bulb_1 = findViewById(R.id.bulb_1);
        bulb_2 = findViewById(R.id.bulb_2);
        S_data = new Socket_data();
    }

    void SettingClickListener(){
        bulb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(S_data.getOnOff() == 0){
                    S_data.setOnOff(1);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff_data());
                    socket_.start();
                }
                if(S_data.getOnOff() == 1){
                    S_data.setOnOff(0);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff_data());
                    socket_.start();
                }
            }
        });
        bulb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(S_data.getOnOff2() == 0){
                    S_data.setOnOff2(1);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff2_data());
                    socket_.start();
                }
                if(S_data.getOnOff2() == 1){
                    S_data.setOnOff2(0);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff2_data());
                    socket_.start();
                }
            }
        });
    }
}
