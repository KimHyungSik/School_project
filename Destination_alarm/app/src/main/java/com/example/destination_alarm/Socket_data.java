package com.example.destination_alarm;

public class Socket_data {
    private int OnOff = 0;
    private int OnOff2 = 0;

    public int getOnOff() {
        return OnOff;
    }
    public String getOnOff_data() {
        if (OnOff == 0) {
            return "전구 오프";
        }
        if(OnOff == 1){
            return "전구 온";
        }
        return "실패";
    }

    public void setOnOff(int onOff) {
        OnOff = onOff;
    }

    public int getOnOff2() {
        return OnOff2;
    }
    public String getOnOff2_data() {
        if (OnOff2 == 0) {
            return "전구2 오프";
        }
        if(OnOff2 == 1){
            return "전구2 온";
        }
        return "실패";
    }
    public void setOnOff2(int onOff) {
        OnOff2 = onOff;
    }

}
