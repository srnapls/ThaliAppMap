package com.example.AppArt.thaliapp.Calendar.Backend;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates a list of new events using the online Thalia iCalendar
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */
public class GetiCal extends AsyncTask<Void, Void, List<ThaliaEvent>> {
    private final String icalAddress = "http://www.thalia.nu/nieuws/agenda/vcal.php";
    private List<ThaliaEvent> newEvents = new ArrayList<>();

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
            EventParser parse = new EventParser();
            this.newEvents = parse.Parsing(iCalSource);
        } catch (IOException ex) {
            Logger.getLogger(EventParser.class.getName()).log(Level.SEVERE,
                    "The URL wasn't found or couldn't be opened.", ex);
        }
        return newEvents;
    }

    public List<ThaliaEvent> getNewEvents() {
        Date nu = new Date();
        int i = 0;
        while (i < newEvents.size()) {
            while (newEvents.get(i).getGregCalFormat(newEvents.get(i).getStartDate()).getTime().compareTo(nu) < 0) {
                newEvents.remove(i);
            }
            i++;
        }
        Collections.sort(newEvents);
        return newEvents;
    }
}