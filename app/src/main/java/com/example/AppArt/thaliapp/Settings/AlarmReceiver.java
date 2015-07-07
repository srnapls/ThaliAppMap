package com.example.AppArt.thaliapp.Settings;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.Calendar.Backhand.ThaliaEvent;

/**
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TITLE_KEY = "TITLE_KEY";
    public static final String BODY_KEY = "body_key";
    public static final String SUBJECT_KEY = "subject_key";

    @Override
    public void onReceive(Context context, Intent intent) {
        ThaliaEvent nextEvent = intent.getParcelableExtra("nextThaliaEvent");

        PendingIntent pending = PendingIntent.getActivity(
                context, 0,
                new Intent(context, Calendar.class), 0);

        NotificationCompat.Builder mBuilder
                = new NotificationCompat.Builder(context)
                .setSmallIcon(nextEvent.getCatIcon())
                .setContentTitle(nextEvent.getSummary())
                .setContentText(nextEvent.getDescription())
                .setTicker("Thalia")
                .setContentIntent(pending)
                .setWhen(System.currentTimeMillis());

        NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NM.notify(0, mBuilder.build());
    }

}