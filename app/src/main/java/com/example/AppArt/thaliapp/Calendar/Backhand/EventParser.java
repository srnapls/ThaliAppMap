package com.example.AppArt.thaliapp.Calendar.Backhand;


import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Post-Lisa interventie series:
Gitcomment: test 1
 */

/**
 * Generates a list of Events using an iCalendar inputstream
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */
public class EventParser {

    // Absolutely maximum amount of events that could be within the file
    private final int bigConstant = 100;
    private List<ThaliaEvent> parsedEvents;

    /**
     * Parses an iCalendar file to a List of Events
     *
     * @param iCalendar
     * @return
     * @throws java.io.IOException
     */
    public List<ThaliaEvent> Parsing(Reader iCalendar) throws IOException {

        // Variables that are about to be filled
        parsedEvents = new ArrayList<>();
        String organizer;
        String organizerMail;
        String startDate;
        String endDate;
        String location;
        String description;
        String summary;

        // Scanning the file and parsing it
        Scanner scan = new Scanner(iCalendar);
        scan.useDelimiter(":");
        scan.findWithinHorizon("METHOD:PUBLISH", bigConstant);
        for (int i = 0; scan.findWithinHorizon("END:VCALENDAR", 200) == null
                && i < bigConstant; i++) {
            scan.findWithinHorizon("BEGIN:VEVENT", 500);
            scan.findWithinHorizon("ORGANIZER;CN=", 50);
            organizer = scan.next("[a-zA-Z]+");
            scan.findWithinHorizon("MAILTO:", 20);
            organizerMail = scan.nextLine();

            scan.findWithinHorizon("DTSTART:", 50);
            startDate = scan.nextLine();
            scan.findWithinHorizon("DTEND:", 50);
            endDate = scan.nextLine();

            scan.findWithinHorizon("LOCATION;LANGUAGE=nl:", 50);
            location = scan.nextLine();
            scan.findWithinHorizon("DESCRIPTION;LANGUAGE=nl:", 200);

            description =scan.nextLine();
            summary = scan.nextLine();
            // Injectionsensitive
            while (!summary.contains("SUMMARY;LANGUAGE=nl:")) {
                description = description.concat(summary);
                summary = scan.nextLine();
            }
            summary = summary.replaceAll("SUMMARY;LANGUAGE=nl:", "");

            // Put all parsed information together in an ThaliaEvent
            ThaliaEvent newEvent = new ThaliaEvent(organizer, organizerMail, startDate,
                    endDate, location, description, summary);
            parsedEvents.add(newEvent);
        }
        scan.close();
        return parsedEvents;
    }
}
