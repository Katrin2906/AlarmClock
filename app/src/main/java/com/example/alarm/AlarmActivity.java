package com.example.alarm;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {
    Ringtone ringtone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);//запрашиваем дефолтный рингтон
        ringtone = RingtoneManager.getRingtone(this, notificationUri);
        if (ringtone == null) { //если рингтон не установлен
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);//вытаскиваем стандартный
            ringtone = RingtoneManager.getRingtone(this, notificationUri);
        }
        if (ringtone != null) { //включили рингтон
            ringtone.play();
        }
    }

    @Override
    protected void onDestroy() {//выключили рингтон, когда пользователь выключил
        if (ringtone != null && ringtone.isPlaying()) {//проверили, что есть и проигрывается
            ringtone.stop();
        }
        super.onDestroy();
    }
}
