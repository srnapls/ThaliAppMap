package com.example.AppArt.thaliapp.Calendar.Backend;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.AppArt.thaliapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Withholds all knowledge of a ThaliaEvent
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class ThaliaEvent implements Comparable<ThaliaEvent>, Parcelable {

    private final GregorianCalendar startDate = new GregorianCalendar();
    private final GregorianCalendar endDate = new GregorianCalendar();

    private final String location;
    private final String description;
    private final String summary;
    private final EventCategory category;
    private final int catIcon;

    /**
     * Initialises the Event object given string input.
     *
     * @param startDate     The starting time of the event in millis
     * @param endDate       The ending time of the event in millis
     * @param location      The location of the event
     * @param description   A large description of the event
     * @param summary       The event in 3 words or fewer
     */
    public ThaliaEvent(Long startDate, Long endDate,
                       String location, String description, String summary) {
        this.startDate.setTimeInMillis(startDate);
        this.endDate.setTimeInMillis(endDate);

        this.location = location;
        this.description = description;
        this.summary = summary;
        this.category = categoryFinder();
        this.catIcon = catIconFinder(category);
    }

    /*****************************************************************
     Methods to help initialise
     *****************************************************************/

    /**
     * Uses the summary and the description of an ThaliaEvent to figure out what
     * category it is.
     *
     * When multiple keywords are found it will use the following order:
     *  LECTURE > PARTY > ALV > WORKSHOP > BORREL > DEFAULT
     * (e.g. kinderFEESTjesBORREL -> PARTY)
     *
     * @return an EventCategory
     */
    private EventCategory categoryFinder() {
        String eventText = this.summary.concat(this.description);
        if (eventText.matches("(?i:.*lezing.*)")) {
            return EventCategory.LECTURE;
        } else if (eventText.matches("(?i:.*feest.*)") ||
                eventText.matches("(?i:.*party.*)")) {
            return EventCategory.PARTY;
        } else if (eventText.matches("(?i:.*alv.*)")){
            return EventCategory.ALV;
        } else if (eventText.matches("(?i:.*workshop.*)")) {
            return EventCategory.WORKSHOP;
        } else if (eventText.matches("(?i:.*borrel.*)")) {
            return EventCategory.BORREL;
        } else return EventCategory.DEFAULT;
    }

    /**
     * Returns the right drawable according to the category
     *
     * @param cat the category of this event
     * @return A .png file that represents the category of this event
     */
    private int catIconFinder(EventCategory cat) {
        int catIcon;
        switch (cat) {
            case ALV:
                catIcon = R.drawable.alvicoon;
                break;
            case BORREL:
                catIcon = R.drawable.borrelicoon;
                break;
            case LECTURE:
                catIcon = R.drawable.lezingicoon;
                break;
            case PARTY:
                catIcon = R.drawable.feesticoon;
                break;
            case WORKSHOP:
                catIcon = R.drawable.workshopicoon;
                break;
            default:
                catIcon = R.drawable.overigicoon;
        }
        return catIcon;
    }

    /*****************************************************************
    Getters for all attributes
     *****************************************************************/
    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    /**
     * A possibly broad description of the ThaliaEvent
     * Can contain HTML
     * @return possibly very large string
     */
    public String getDescription() {
        return description;
    }

    /**
     * The ThaliaEvent in less than 5 words
     * Can contain HTML
     * @return small String
     */
    public String getSummary() {
        return summary;
    }

    public EventCategory getCategory() {
        return category;
    }

    public int getCatIcon() {
        return catIcon;
    }

    /**
     * Small composition of ThaliaEvent information
     * @return summary + "\n" + duration() + "\n" + location
     */
    public String makeSynopsis() {
        return summary + "\n" + duration() + "\n" + location;
    }

    /**
     * A readable abbreviation of the day
     * e.g. di 18 aug
     *
     * @return dd - dd - mmm
     */
    public String getDateString() {
        StringBuilder date;
        date = new StringBuilder("");
        date.append(this.startDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        date.append(" ");
        date.append(this.startDate.get(Calendar.DAY_OF_MONTH));
        date.append(" ");
        date.append(this.startDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
        return date.toString();
    }

    /*****************************************************************
     Default Methods
     *****************************************************************/

    /**
     * A neat stringformat of the beginning and ending times
     *
     * @return hh:mm-hh:mm
     */
    public String duration() {
        StringBuilder sb = new StringBuilder();
        sb.append(startDate.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        if (startDate.get(Calendar.MINUTE) == 0) {
            sb.append("00");
        } else {
            sb.append(startDate.get(Calendar.MINUTE));
        }
        sb.append(" - ");
        sb.append(endDate.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        if (endDate.get(Calendar.MINUTE) == 0) {
            sb.append("00");
        } else {
            sb.append(endDate.get(Calendar.MINUTE));
        }
        System.out.println("EoF durationfunction: "+sb.toString());
        return sb.toString();
    }

    /**
     * Printmethod, useful when you're debugging
     *
     * @return a string of the event
     */
    @Override
    public String toString() {
        return ("\nstart = " + startDate + ", end = " + endDate
                + "\nlocation = " + location + "\ndescription = " + description
                + "\nsummary = " + summary);
    }

    /**
     * @param another the ThaliaEvent with which you want to compare it
     * @return The difference in time between the two
     */
    @Override
    public int compareTo(@NonNull ThaliaEvent another) {
        return startDate.compareTo(another.startDate);
    }

    /*****************************************************************
     Making it a Parcelable, so it can be passed through with an intent
     *****************************************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Pretty much all information about this ThaliaEvent object is being
     * compressed into a Parcel
     *
     * @param dest Destination
     * @param flags Flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startDate.getTimeInMillis());
        dest.writeLong(endDate.getTimeInMillis());

        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(summary);
    }

    /**
     * Reconstructs the ThaliaEvent through a parcel.
     */
    public static final Parcelable.Creator<ThaliaEvent> CREATOR
            = new Parcelable.Creator<ThaliaEvent>() {
        // Parcels work FIFO
        public ThaliaEvent createFromParcel(Parcel parcel) {
            Long startDate = parcel.readLong();
            Long endDate = parcel.readLong();

            String location = parcel.readString();
            String description = parcel.readString();
            String summary = parcel.readString();
            return new ThaliaEvent(startDate, endDate, location, description, summary);
        }

        public ThaliaEvent[] newArray(int size) {
            return new ThaliaEvent[size];
        }
    };
}
