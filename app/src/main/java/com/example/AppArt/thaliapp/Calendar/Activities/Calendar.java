package com.example.AppArt.thaliapp.Calendar.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.AppArt.thaliapp.Calendar.Backend.EventCategory;
import com.example.AppArt.thaliapp.Calendar.Backend.Group;
import com.example.AppArt.thaliapp.Calendar.Backend.MyExpandableListAdapter;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.FoodList.Activities.Restaurant;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Activities.Settings;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import com.example.AppArt.thaliapp.ThaliappActivity;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.AppArt.thaliapp.R.id.ListView;

/**
 * Calendar activity, shows a list of all currently known ThaliaEvents that
 * have yet to end.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Calendar extends ThaliappActivity implements SwipeRefreshLayout.OnRefreshListener, Observer {
    private MyExpandableListAdapter adapter;
    private SwipeRefreshLayout mSwipeLayout;

    /**
     * calls this method on start
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * @param savedInstanceState, saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ExpandableListView listView = (ExpandableListView) findViewById(ListView);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.mainactivity);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(
                R.color.thaliapink,
                R.color.lightpink,
                R.color.darkpink
        );

        Database.getDatabase().addObserver(this);

        ArrayList<ThaliaEvent> events = (ArrayList<ThaliaEvent>) Database.getDatabase().getEvents();
        if (events == null) {
            Toast.makeText(this, "Er zijn geen evenementen. \n" +
                    "Swipe om te updaten.", Toast.LENGTH_SHORT).show();
            adapter = new MyExpandableListAdapter(this, new SparseArray<Group>(), null);
        } else {
            adapter = new MyExpandableListAdapter(this, createData(events), makeCategories(events));
        }
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setGroupIndicator(null);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Kalender");
    }

    /**
     * Method to refresh the ArrayList when swiped up, in case of refreshing and the events are still empty
     * there has most likely been an error on the server side
     */
    public void onRefresh() {
        Database.getDatabase().updateEvents();
    }

    public void update(Observable db, Object r) {
        if (Database.getDatabase().getEvents() == null) {
            Toast.makeText(this, "Er is een fout opgetreden, probeer later opniew", LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Kalender geüpdatet", LENGTH_SHORT).show();
        }
        mSwipeLayout.setRefreshing(false);
        ArrayList<ThaliaEvent> events = (ArrayList<ThaliaEvent>) Database.getDatabase().getEvents();
        adapter.setData(createData(events), makeCategories(events));
    }

    /**
     * Method to fill the ArrayList, such that it is sorted on day
     */
    private SparseArray<Group> createData(ArrayList<ThaliaEvent> events) {
        SparseArray<Group> groups = new SparseArray<>();
        int j = 0, i = 0;
        while (i < events.size()) {
            ThaliaEvent t = events.get(i);
            Group group = new Group(t.getDateString());
            group.children.add(events.get(i).makeSynopsis());
            i++;
            while (i < events.size() && events.get(i).getDateString().equals(t.getDateString())) {
                group.children.add(events.get(i).makeSynopsis());
                i++;
            }
            groups.append(j, group);
            j++;
        }
        return groups;
    }

    /**
     * Makes a stringarray and fills it with the Categories
     */
    private EventCategory[] makeCategories(ArrayList<ThaliaEvent> events) {
        EventCategory[] kindOfEvent = new EventCategory[events.size()];
        for (int i = 0; i < kindOfEvent.length; i++) {
            kindOfEvent[i] = events.get(i).getCategory();
        }
        return kindOfEvent;
    }
}
