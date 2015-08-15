package com.example.AppArt.thaliapp.Settings.Backend;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.Calendar.Activities.Information;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.R;

import static com.example.AppArt.thaliapp.R.color.thaliapink;

/**
 * Fixes that the user gets the notification for the event on time
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Builds the notification for the event
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        ThaliaEvent nextEvent = intent.getParcelableExtra("nextThaliaEvent");

        Intent intentToStart = new Intent(context, Information.class);
        intentToStart.putExtra("event", nextEvent);
        PendingIntent pending = PendingIntent.getActivity(context, 0,
                intentToStart, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setColor(context.getResources()
                .getColor(thaliapink));
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(nextEvent.getCatIcon());
        mBuilder.setContentTitle(nextEvent.getSummary());
        mBuilder.setContentText(nextEvent.getDescription());
        mBuilder.setTicker("Thalia");
        mBuilder.setContentIntent(pending);
        mBuilder.setWhen(System.currentTimeMillis());

        NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NM.notify(0, mBuilder.build());
    }

}