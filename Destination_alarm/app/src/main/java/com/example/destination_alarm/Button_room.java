package com.example.destination_alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Button_room extends AppCompatActivity {

    Button bulb_1,bulb_2;
    Switch Map_Button_Switch;
    TextView Map_Text, Button_Text;
    Socket_data S_data;
    PreferenceManager preferenceManager;
    private IP_Port ip_port = new IP_Port();
    public final  String MainIp = ip_port.getIp(); //아이피 호
    public final int MainPort = ip_port.getPort(); //폰트 번호
    private Socket_ socket_;
    int Priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_room);

        S_data = new Socket_data();
        GetExtra();
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
                                map_intent.putExtra("SocketData", S_data.getOnOff());
                                map_intent.putExtra("Priority", Priority);
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
        boolean checked = Priority == 1 ? true : false;
        Log.d("button","checked : " + Boolean.toString(checked));
        Log.d("btuuon","Priority : " +Integer.toString(Priority));
        Map_Button_Switch.setChecked(checked);
        if(checked){
            Map_Text.setTextColor(Color.parseColor("#283142"));
            Button_Text.setTextColor(Color.parseColor("#F48024"));
        }else{
            Map_Text.setTextColor(Color.parseColor("#F48024"));
            Button_Text.setTextColor(Color.parseColor("#283142"));
        }
    }

    void GetExtra(){
        Bundle extras = getIntent().getExtras();
        S_data.setOnOff(extras.getInt("SocketData"));
        Priority = extras.getInt("Priority");
    }

    void SettingId(){

        bulb_1 = findViewById(R.id.bulb_1);
        if(S_data.getOnOff()==1){
            bulb_1.setBackgroundColor(Color.YELLOW);
        }else{
            bulb_1.setBackgroundColor(Color.GRAY);
        }
        bulb_2 = findViewById(R.id.bulb_2);
        Map_Button_Switch = findViewById(R.id.Btton_map);
        Map_Text = findViewById(R.id.Map_text);
        Button_Text = findViewById(R.id.Button_text);
        preferenceManager = new PreferenceManager();
    }

    void SettingClickListener(){
        bulb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(S_data.getOnOff() == 0){
                    Log.d("buttom","bubl_1 on");
                    S_data.setOnOff(1);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff_data());
                    socket_.start();
                    bulb_1.setBackgroundColor(Color.YELLOW);
                }
                else if(S_data.getOnOff() == 1){
                    Log.d("buttom","bubl_1 off");
                    S_data.setOnOff(0);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff_data());
                    socket_.start();
                    bulb_1.setBackgroundColor(Color.GRAY);
                }
            }
        });
        bulb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(S_data.getOnOff2() == 0){
                    Log.d("buttom","bubl_2 on");
                    S_data.setOnOff2(1);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff2_data());
                    socket_.start();
                    bulb_2.setBackgroundColor(Color.YELLOW);
                }
                else if(S_data.getOnOff2() == 1){
                    Log.d("buttom","bubl_2 off");
                    S_data.setOnOff2(0);
                    socket_ = new Socket_(MainIp, MainPort, S_data.getOnOff2_data());
                    socket_.start();
                    bulb_2.setBackgroundColor(Color.GRAY);
                }
            }
        });
        Map_Button_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Map_Text.setTextColor(Color.parseColor("#283142"));
                    Button_Text.setTextColor(Color.parseColor("#F48024"));
                    Priority = 1;
                    preferenceManager.setInt(Button_room.this, "Priority_MapOrButton", Priority);
                }else{
                    Map_Text.setTextColor(Color.parseColor("#F48024"));
                    Button_Text.setTextColor(Color.parseColor("#283142"));
                    Priority = 0;
                    preferenceManager.setInt(Button_room.this, "Priority_MapOrButton", Priority);
                }
            }
        });
    }
}
