package com.example.project_h;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button SttBnt;
    TextView SttText;
    VoiceTask voiceTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SettingItems();
        SetListener();
    }

    private void SettingItems(){
        SttBnt  = findViewById(R.id.sttbutton);
        SttText = findViewById(R.id.sttText);
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


}
