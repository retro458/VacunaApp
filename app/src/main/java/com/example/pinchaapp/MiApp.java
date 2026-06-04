package com.example.pinchaapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.pinchaapp.session.SessionManager;
import com.example.pinchaapp.workers.VacunaReminderWorker;

public class MiApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.init(this);
        crearCanalNotificaciones();
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    VacunaReminderWorker.CHANNEL_ID,
                    "Recordatorio de vacunas",
                    NotificationManager.IMPORTANCE_HIGH);
            canal.setDescription("Recordatorios de próximas dosis de vacunas");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(canal);
        }
    }
}
