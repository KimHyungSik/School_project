package com.example.destination_alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Menu_Activity extends AppCompatActivity {

    Button SttBnt;
    VoiceTask voiceTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);
        SettingItems();
        SetListener();

        BottomNavigationView bottomNavigationView = findViewById(R.id.Menu_bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        // 어떤 메뉴 아이템이 터치되었는지 확인합니다.
                        switch (item.getItemId()) {

                            case R.id.nav_map:
                                Intent intent = new Intent(getApplicationContext(), MainGooglMapActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                return true;

                            case R.id.nav_button:

                                return true;

                            case R.id.nav_mic:

                                return true;
                        }
                        return false;
                    }
                });
    }



    private void SettingItems(){
        SttBnt  = findViewById(R.id.sttbutton);
    }

    private void SetListener(){
        SttBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceTask = new VoiceTask();
                voiceTask.execute();
            }
        });
    }


    public class VoiceTask extends AsyncTask<String, Integer, String> {
        String str = null;

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                getVoice();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            try {

            } catch (Exception e) {
                Log.d("onActivityResult", "getImageURL exception");
            }
        }
    }

    private void getVoice() {

        Intent intent = new Intent();
        intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        String language = "ko-KR";

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        startActivityForResult(intent, 2);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String str = results.get(0);
            control(str);
        }
    }
    private void Back_Map_Intent(){
        Intent intent = new Intent(getApplicationContext(), MainGooglMapActivity.class);
        startActivity(intent);
    }

    private void control(String stt){
        if(stt.contains("지도")){
            Back_Map_Intent();
        }
    }
}
