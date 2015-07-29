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
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a list of new events using the online Thalia iCalendar
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */
public class EventParser extends AsyncTask<String, Integer, List<ThaliaEvent>> {

    private List<ThaliaEvent> newEvents;

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
        return (new ThaliaEvent(startDate, endDate, location, description,
                summary));
    }

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

    public List<ThaliaEvent> getNewEvents(){
        return newEvents;
    }
}