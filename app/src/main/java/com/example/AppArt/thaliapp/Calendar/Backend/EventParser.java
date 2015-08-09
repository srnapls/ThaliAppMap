package com.example.AppArt.thaliapp.Calendar.Backend;

// TODO Frank: Show progress/show that there actually is activity instead of
// just a boring loadingscreen
// TODO Frank: dealing-with-asynctask-and-screen-orientation

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a list of new events using the online Thalia iCalendar
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */
public class EventParser extends AsyncTask<String, Integer, List<ThaliaEvent>> {

    private List<ThaliaEvent> newEvents;

    //TODO: Bepalen of dit weg kan, Frank?
/*
    @Override
    protected void onPreExecute(){
        super.onPreExecute();

    }
*/

    /**
     * Opens an URL stream with the given iCalendaradress and extracts a list of
     * ThaliaEvents out of it
     *
     * @return complete list of all events
     */
    @Override
    protected List<ThaliaEvent> doInBackground(String... icalAddress) {
        try {
            String resource_location = icalAddress[0];
            URL iCalURL = new URL(resource_location);
            Reader iCalSource = new BufferedReader(
                    new InputStreamReader(iCalURL.openStream()));
            newEvents = this.Parsing(iCalSource);
        } catch (IOException ex) {
            Logger.getLogger(EventParser.class.getName()).log(Level.SEVERE,
                    "The URL wasn't found or couldn't be opened.", ex);
        }
        return newEvents;
    }

    /**
     * Parses an iCalendar file to a List of Events
     *
     * @param iCalendar Reader containing a list of ThaliaEvents in
     *                  iCalendarformat
     * @return List of all ThaliaEvents read from the Reader
     * @throws java.io.IOException
     */
    private List<ThaliaEvent> Parsing(Reader iCalendar) throws IOException {
        List<ThaliaEvent> parsedEvents = new ArrayList<>();
        Scanner scan = new Scanner(iCalendar);
        scan.useDelimiter(":");
        scan.findWithinHorizon("X-PUBLISHED-TTL:P1W", 200);
        while(scan.findWithinHorizon("END:VCALENDAR", 200) == null){
            ThaliaEvent t = ParseThaliaEvent(scan);
            parsedEvents.add(t);
        }
        scan.close();
        return parsedEvents;
    }

    /**
     * Scans one ThaliaEvent
     * @param scan a scanner opened on a text in iCalendar format,
     * @return the scanned ThaliaEvent
     */
    private ThaliaEvent ParseThaliaEvent(Scanner scan){
        String startDate;
        String endDate;
        String location;
        String description;
        String summary;

        scan.findWithinHorizon("BEGIN:VEVENT", 500);

        scan.findWithinHorizon("DTSTART:", 50);
        startDate = scan.nextLine();
        scan.findWithinHorizon("DTEND:", 50);
        endDate = scan.nextLine();

        scan.findWithinHorizon("LOCATION:", 50);
        location = scan.nextLine();
        scan.findWithinHorizon("SUMMARY:", 50);
        summary = scan.nextLine();
        scan.findWithinHorizon("DESCRIPTION:", 200);

        description = scan.nextLine();
        String dump = "";
        // Injectionsensitive
        while (!dump.contains("DTSTAMP")) {
            description = description.concat(dump);
            dump = scan.nextLine();
        }

        scan.findWithinHorizon("END:VEVENT", 50);
        Long startDateMS = getGregCalFormat(startDate).getTimeInMillis();
        Long endDateMS = getGregCalFormat(endDate).getTimeInMillis();
        return (new ThaliaEvent(startDateMS, endDateMS, location, description,
                summary));
    }

    /**
     * Parses an iCalendar date string(DATE-TIME) to tiny bits which are never
     * returned.
     *
     * @param date Time in a DATE-TIME format
     * @return Time in GregorianCalendar format
     */
    public GregorianCalendar getGregCalFormat(String date) {
        String temp = date.substring(0, 4);
        int year = Integer.parseInt(temp);
        temp = date.substring(4, 6);
        // GregorianCalendar has January = 0, iCal has January = 1
        // This is what makes programming fun!
        int month = Integer.parseInt(temp) - 1;
        temp = date.substring(6, 8);
        int day = Integer.parseInt(temp);
        temp = date.substring(9, 11);
        int hour = Integer.parseInt(temp);
        temp = date.substring(11, 13);
        int minute = Integer.parseInt(temp);
        int second = 0;

        // Converting from GMT to CET
        GregorianCalendar GMTime = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        GMTime.set(year, month, day, hour, minute, second);
        GregorianCalendar CETime = new GregorianCalendar(TimeZone.getTimeZone("CET"));
        CETime.setTimeInMillis(GMTime.getTimeInMillis());
        return CETime;
    }

    //TODO: bepalen of dit weg kan, Frank?
    /*
    // http://stackoverflow.com/questions/3821423/background-task-progress-dialog-orientation-change-is-there-any-100-working/3821998#3821998
    private void lockScreenOrientation() {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }*/

    /**
     * getter for new events
     * @return a list of new thalia events
     */
    public List<ThaliaEvent> getNewEvents(){
        return newEvents;
    }
}