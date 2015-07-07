package com.example.AppArt.thaliapp.Settings.Activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.AppArt.thaliapp.Calendar.Backend.GetiCal;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.AlarmReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Notifications extends ActionBarActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private EditText minutesBefore;
    private int minutes = 60;
    public boolean borrel = true;
    public boolean alv = true;
    public boolean party = true;
    public boolean workshop = true;
    public boolean lezing = true;
    public boolean overig = true;
    private CheckBox bbox, abox, pbox, wbox, lbox, obox;
    SharedPreferences sharedpreferences;

    private ArrayList<ThaliaEvent> allEvents;
    private ArrayList<ThaliaEvent> interestedEvents = new ArrayList<>();
    private ThaliaEvent nextEventToWarn = new ThaliaEvent("Thalia", "mail", "20150611T110000Z", "20150616T113000Z", "loc", "Thalia borrel descr", "borrel");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        minutesBefore = (EditText) findViewById(R.id.time);
        bbox = (CheckBox) findViewById(R.id.Borrelcheck);
        abox = (CheckBox) findViewById(R.id.ALVcheck);
        pbox = (CheckBox) findViewById(R.id.Feestcheck);
        wbox = (CheckBox) findViewById(R.id.Workshopcheck);
        lbox = (CheckBox) findViewById(R.id.Lezingcheck);
        obox = (CheckBox) findViewById(R.id.Overigcheck);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        borrel = sharedpreferences.getBoolean("borrel", true);
        alv = sharedpreferences.getBoolean("alv", true);
        party = sharedpreferences.getBoolean("party", true);
        workshop = sharedpreferences.getBoolean("workshop", true);
        lezing = sharedpreferences.getBoolean("lezing", true);
        overig = sharedpreferences.getBoolean("overig", true);
        minutes = sharedpreferences.getInt("tijd", 60);
        bbox.setChecked(borrel);
        abox.setChecked(alv);
        pbox.setChecked(party);
        wbox.setChecked(workshop);
        lbox.setChecked(lezing);
        obox.setChecked(overig);
        getData();
    }

    /**
     * Save the state of the checkboxes after change and updates the
     * interestedEvents list
     */
    private void savePreferences() {
        borrel = bbox.isChecked();
        alv = abox.isChecked();
        workshop = wbox.isChecked();
        party = pbox.isChecked();
        lezing = lbox.isChecked();
        overig = obox.isChecked();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("borrel", borrel);
        editor.putBoolean("alv", alv);
        editor.putBoolean("workshop", workshop);
        editor.putBoolean("party", party);
        editor.putBoolean("lezing", lezing);
        editor.putBoolean("overig", overig);
        editor.putInt("tijd", minutes);
        editor.commit();
        select();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (minutesBefore.getText() == null) {
            minutes = 60;
        } else {
            minutes = Integer.parseInt(minutesBefore.getText().toString());
        }
        savePreferences();
        createNotification();
        Intent i = new Intent(this, Settings.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (minutesBefore.getText() == null || minutesBefore.getText().equals("")) {
                minutes = 60;
            } else {
                minutes = Integer.parseInt(minutesBefore.getText().toString());
            }
            createNotification();
            savePreferences();
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification() {
        minutes = Integer.parseInt(minutesBefore.getText().toString());

        GregorianCalendar eventStart = nextEventToWarn.getGregCalFormat(nextEventToWarn.getStartDate());
        System.out.println("eventStart" + eventStart);
        GregorianCalendar now = new GregorianCalendar();
        System.out.println("now" + now);
        int negMinutes = minutes * (-1);
        System.out.println("minutes: " + negMinutes);
        eventStart.add(java.util.Calendar.MINUTE, negMinutes);

        long miliseconds = eventStart.getTimeInMillis() - now.getTimeInMillis();

        System.out.println("milisec " + miliseconds);
        // Give the intent the values so that we can populate the notification
        // in the receiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("nextThaliaEvent", nextEventToWarn);

        // Schedule the alarm
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 36954, intent,
                0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + miliseconds, alarmIntent);
        System.out.println("To be notified: " + nextEventToWarn);
    }


    /**
     * Initialises the allEvents list and makes a first interestedEvents list
     */
    public void getData() {
        Log.d("Notifications.getData", "started");
        GetiCal getiCal = new GetiCal();
        Log.d("Notifications.getData", "created GetiCal");
        getiCal.execute();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allEvents = (ArrayList<ThaliaEvent>) getiCal.getNewEvents();
        Collections.sort(allEvents);
        select();
        if (allEvents.size() != 0) removeLast();
    }

    /**
     * Removes all events that have already passed the current date
     */
    private void removeLast() {
        Date nu = new Date();
        for (int i = 0; i < allEvents.size(); i++) {
            if (allEvents.get(i).getGregCalFormat(allEvents.get(i).getStartDate()).getTime().compareTo(nu) < 0) {
                allEvents.remove(i);
            }
        }
    }

    /**
     * Selects the interested events out of the allEvents list of events and puts them in the interestedEvents list
     */
    private void select() {
        if (allEvents != null) {
            for (int i = 0; i < allEvents.size(); i++) {

                switch (allEvents.get(i).getCategory()) {
                    case BORREL:
                        if (borrel) {
                            interestedEvents.add(allEvents.get(i));
                        }
                        break;
                    case LECTURE:
                        if (lezing) {
                            interestedEvents.add(allEvents.get(i));
                        }
                        break;
                    case ALV:
                        if (alv) {
                            interestedEvents.add(allEvents.get(i));
                        }
                        break;
                    case PARTY:
                        if (party) {
                            interestedEvents.add(allEvents.get(i));
                        }
                        break;
                    case WORKSHOP:
                        if (workshop) {
                            interestedEvents.add(allEvents.get(i));
                        }
                        break;
                    case DEFAULT:
                        if (overig) {
                            interestedEvents.add(allEvents.get(i));
                        }
                        break;
                    default:
                        break;
                }
            }
            // Onderaan notifications, in de select de regel met 'nextEventToWarn' vervangen
            Collections.sort(interestedEvents);
            if (interestedEvents.size() != 0) {
                nextEventToWarn = new ThaliaEvent("Thalia", "info@thalia.nl",
                        "20370609T170000Z", "20420607T170000Z", "Nergens",
                        "Een hacky oplossing, maar toch best mooi gevonden",
                        "Dummy date");
            } else {
                nextEventToWarn = interestedEvents.get(0);
            }
        }
    }

}

