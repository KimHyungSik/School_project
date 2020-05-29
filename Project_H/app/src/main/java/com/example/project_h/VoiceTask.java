package com.example.project_h;

import android.os.AsyncTask;
import android.util.Log;

public class VoiceTask extends AsyncTask<String, Integer, String> {

    @Override
    protected void onPreExecute() {
        GetVoice getVoice = new GetVoice();
        String str = null;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            getVoice.getVoice();
        } catch (Exception e) {
            Log.d("test","error");
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