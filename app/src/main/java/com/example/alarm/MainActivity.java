package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button setAlarm; //поле для кнопочки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }

        setAlarm = findViewById(R.id.alarm_button); // достали кнопку по id

        SimpleDateFormat finalSdf = sdf; //для форматирования времени

        setAlarm.setOnClickListener(v -> {   //обработчик нажатия
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Выберите время для будильника")
                    .build();


            materialTimePicker.addOnPositiveButtonClickListener(view -> {  //если пользователь нажал
                Calendar calendar = Calendar.getInstance();  //подсчет времени
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE); //работа с системой

                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());// показывает пользователю будильники

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());//срабатывание
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Toast.makeText(this, "Будильник установлен на " + finalSdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                }

            });

            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");//демонстрация

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private PendingIntent getAlarmInfoPendingIntent() { //класс связывает разные компоненты системы
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//чтобы запустилось неактивное приложение
        return PendingIntent.getActivity(this, 0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT); //выполнение действия от нашего имени
    }

    private PendingIntent getAlarmActionPendingIntent() { // для срабатывания будильника
        Intent intent = new Intent(this, AlarmActivity.class); //звуковой сигнал
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//чтобы запустилось неактивное приложение
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);//интент остается только последний
    }
}
