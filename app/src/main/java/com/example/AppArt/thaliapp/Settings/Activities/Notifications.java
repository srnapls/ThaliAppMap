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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.Calendar.Backend.EventCategory;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.AlarmReceiver;
import com.example.AppArt.thaliapp.Settings.Backend.Database;
import com.example.AppArt.thaliapp.ThaliappActivity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Activity that handles the notifications. Through extensive input, a push
 * notification is set for a ThaliaEvent. This can be done per category and a
 * certain amount of time beforehand.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Notifications extends ThaliappActivity {
    public static final String MyPREFERENCES = "Settings";
    private EditText timeBefore;
    private int amountOfTime = 60;
    public boolean checkBorrel = true;
    public boolean checkLecture = true;
    public boolean checkALV = true;
    public boolean checkParty = true;
    public boolean checkWorkshop = true;
    public boolean checkDefault = true;
    private CheckBox bbox, abox, pbox, wbox, lbox, obox;
    private Spinner timespinner;
    public int chosennumber;
    SharedPreferences sharedpreferences;

    private final Database database = Database.getDatabase();
    private List<ThaliaEvent> allEvents = database.getEvents();
    private ThaliaEvent nextEventToWarn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        timeBefore = (EditText) findViewById(R.id.time);
        bbox = (CheckBox) findViewById(R.id.BoxBorrel);
        abox = (CheckBox) findViewById(R.id.BoxALV);
        pbox = (CheckBox) findViewById(R.id.BoxParty);
        wbox = (CheckBox) findViewById(R.id.BoxWorkshop);
        lbox = (CheckBox) findViewById(R.id.BoxLecture);
        obox = (CheckBox) findViewById(R.id.BoxDefault);
        timespinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this,
                R.array.notifications_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timespinner.setAdapter(adapter);

        setBoxes();
    }

    /**
     * Function to read all the stored values for the boxes and set the boxes accordingly.
     */
    private void setBoxes() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        checkBorrel = sharedpreferences.getBoolean("BoxBorrel", true);
        checkALV = sharedpreferences.getBoolean("BoxALV", true);
        checkParty = sharedpreferences.getBoolean("BoxParty", true);
        checkWorkshop = sharedpreferences.getBoolean("BoxWorkshop", true);
        checkLecture = sharedpreferences.getBoolean("BoxLecture", true);
        checkDefault = sharedpreferences.getBoolean("BoxDefault", true);
        amountOfTime = sharedpreferences.getInt("tijd", 60);
        chosennumber = sharedpreferences.getInt("sortTime", 0);
        bbox.setChecked(checkBorrel);
        abox.setChecked(checkALV);
        pbox.setChecked(checkParty);
        wbox.setChecked(checkWorkshop);
        lbox.setChecked(checkLecture);
        obox.setChecked(checkDefault);
        timespinner.setSelection(chosennumber);
        nextEventToWarn = select();
    }

    /**
     * Save the state of the checkboxes after change and updates the
     * interestedEvents list
     */
    private void savePreferences() {
        checkBorrel = bbox.isChecked();
        checkALV = abox.isChecked();
        checkWorkshop = wbox.isChecked();
        checkParty = pbox.isChecked();
        checkLecture = lbox.isChecked();
        checkDefault = obox.isChecked();
        chosennumber = timespinner.getSelectedItemPosition();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("checkBorrel", checkBorrel);
        editor.putBoolean("checkALV", checkALV);
        editor.putBoolean("checkWorkshop", checkWorkshop);
        editor.putBoolean("checkParty", checkParty);
        editor.putBoolean("checkLecture", checkLecture);
        editor.putBoolean("checkDefault", checkDefault);
        editor.putInt("tijd", amountOfTime);
        editor.putInt("sortTime", chosennumber);
        editor.apply();
        nextEventToWarn = select();
    }

    //TODO Serena: Deze functie moet misschien geen notificatie zetten
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timeBefore.getText() == null) {
            amountOfTime = 60;
        } else {
            amountOfTime = Integer.parseInt(timeBefore.getText().toString());
        }
        savePreferences();
        createNotification();
    }


    /**
     * When the setNotification button is clicked, a notification is set
     */
    
    public void onSetNotification(View view) {
        if (timeBefore.getText() == null) {
            amountOfTime = 60;
        } else {
            amountOfTime = Integer.parseInt(timeBefore.getText().toString());
        }
        savePreferences();
        createNotification();
    }

    /**
     * Calculates what the amount of time is in minutes. If the spinner is selected on 0, the time is
     * already displayed in minutes, when 1 the time is in hours, when 2 the time is in days.
     */
    private void calculateTime(){
        int temp = Integer.parseInt(timeBefore.getText().toString());
        switch(timespinner.getSelectedItemPosition()){
            case 0:
                amountOfTime = temp;
                break;
            case 1:
                amountOfTime = temp*60;
                break;
            case 2:
                amountOfTime = temp*60*24;
                break;
        }
    }

    /**
     * Reads the given values and sets a notification through an alarm manager
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createNotification() {
        // There is no event to notify
        if(nextEventToWarn == null){
            Toast.makeText(this, "Er zijn geen activiteiten die voldoen aan de " +
                    "eisen.\n Er is geen notificatie gemaakt.", Toast.LENGTH_SHORT).show();
            return;
        }
        calculateTime();
        GregorianCalendar eventStart = nextEventToWarn.getStartDate();
        System.out.println("eventStart" + eventStart);

        int negMinutes = amountOfTime * (-1);
        System.out.println("amountOfTime: " + negMinutes);

        long msToWarn = eventStart.getTimeInMillis();
        msToWarn += (negMinutes *60*1000);

        // Give the intent the values so that we can populate the notification
        // in the receiver
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("nextThaliaEvent", nextEventToWarn);

        // Schedule the alarm
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 36954, intent, 0);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, msToWarn, alarmIntent);
        System.out.println("To be notified: " + nextEventToWarn);

        Toast.makeText(this, "Er is op " + timeToString(msToWarn) + " voor " +
                nextEventToWarn.getSummary() + " een notificatie gezet.",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Dutch String representation of a time
     *
     * @param time a Long denoting a time
     * @return "DAY om HOUR:MINUTE"
     */
    private String timeToString(Long time){
        GregorianCalendar gregCal
                = new GregorianCalendar(TimeZone.getTimeZone("CET"));
        gregCal.setTimeInMillis(time);
        StringBuilder timeString = new StringBuilder();
        timeString.append(gregCal.getDisplayName(java.util.Calendar.DAY_OF_WEEK,
                java.util.Calendar.LONG, Locale.getDefault()));
        timeString.append(" om ");
        timeString.append(gregCal.get(java.util.Calendar.HOUR_OF_DAY));
        timeString.append(":");
        int minute = gregCal.get(java.util.Calendar.MINUTE);
        if (minute < 10) {
            timeString.append("0" + minute);
        } else {
            timeString.append(minute);
        }
        return timeString.toString();
    }

    /**
     * Selects the interested events out of the allEvents list of events and
     * puts them in the interestedEvents list
     *
     * @return earliest event that meets requirements. Null if no event meets
     * requirements.
     */
    private ThaliaEvent select() {
        List<ThaliaEvent> interestedEvents = new ArrayList<>();
        // Events might not have been parsed yet
        if (allEvents == null) {
            database.updateEvents();
            allEvents = database.getEvents();
        }
        // If it still equals null, there are no events in the calendar
        if (allEvents != null) {
            // Adding all possible ThaliaEvents that meet the requirements
            for (int i = 0; i < allEvents.size(); i++) {
                EventCategory c = allEvents.get(i).getCategory();
                if ((c == EventCategory.BORREL   && checkBorrel)  ||
                    (c == EventCategory.LECTURE  && checkLecture) ||
                    (c == EventCategory.ALV      && checkALV)     ||
                    (c == EventCategory.PARTY    && checkParty)   ||
                    (c == EventCategory.WORKSHOP && checkWorkshop)||
                    (c == EventCategory.DEFAULT  && checkDefault)) {

                    interestedEvents.add(allEvents.get(i));
                }
            }
            Collections.sort(interestedEvents);
        }

        // If there are no ThaliaEvents that meet the requirements, null is returned
        if(interestedEvents.size() == 0){
            return null;
        }else {
            return nextEventToWarn = interestedEvents.get(0);
        }
    }
}

