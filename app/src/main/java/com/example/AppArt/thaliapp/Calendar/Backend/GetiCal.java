package com.example.AppArt.thaliapp.Calendar.Backend;

/**
 * Created by Srna on 28-5-2015.
 */

import android.os.AsyncTask;

import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
public class GetiCal extends AsyncTask<Void, Void, List<ThaliaEvent>> {
    private final String icalAddress
            = "https://www.thalia.nu/events/ical/feed.ics";
    private List<ThaliaEvent> newEvents;

    /**
     * Opens an URL stream with the iCalendaradress and extracts a list of
     * Events out of it
     *
     * @return
     */
    @Override
    protected List<ThaliaEvent> doInBackground(Void... params) {
        try {
            String resource_location = icalAddress;
            URL iCalURL = new URL(resource_location);
            Reader iCalSource = new BufferedReader(
                    new InputStreamReader(iCalURL.openStream()));
            this.newEvents = this.Parsing(iCalSource);
        } catch (IOException ex) {
            Logger.getLogger(GetiCal.class.getName()).log(Level.SEVERE,
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
        List parsedEvents = new ArrayList<>();
        Scanner scan = new Scanner(iCalendar);
        scan.useDelimiter(":");
        scan.findWithinHorizon("X-PUBLISHED-TTL:P1W", 200);
        while(!(scan.findWithinHorizon("END:VCALENDAR", 200) == null)){
            parsedEvents.add(ParseThaliaEvent(scan));
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
        System.out.println(startDate);
        scan.findWithinHorizon("DTEND:", 50);
        endDate = scan.nextLine();

        scan.findWithinHorizon("LOCATION:", 50);
        location = scan.nextLine();
        summary = scan.nextLine();
        scan.findWithinHorizon("DESCRIPTION:", 200);

        description = scan.nextLine();
        // Injectionsensitive
        while (!summary.contains("DTSTAMP:")) {
            description = description.concat(summary);
            summary = scan.nextLine();
        }
        scan.findWithinHorizon("END:VEVENT", 50);

        return (new ThaliaEvent(startDate, endDate, location, description,
                summary));
    }

    //TODO Frank: Add pruning of passed events?
    public List<ThaliaEvent> getNewEvents() {
        return newEvents;
    }
}