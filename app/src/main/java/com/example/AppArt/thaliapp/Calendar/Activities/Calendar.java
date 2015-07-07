package com.example.AppArt.thaliapp.Calendar.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.AppArt.thaliapp.Calendar.Backhand.GetiCal;
import com.example.AppArt.thaliapp.Calendar.Backhand.Group;
import com.example.AppArt.thaliapp.Calendar.Backhand.MyExpandableListAdapter;
import com.example.AppArt.thaliapp.Calendar.Backhand.ThaliaEvent;
import com.example.AppArt.thaliapp.Eetlijst.Activities.Eetlijst;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Calendar extends ActionBarActivity {
    private MyExpandableListAdapter adapter;
    SparseArray<Group> groups = new SparseArray<>();
    public ArrayList<ThaliaEvent> events = new ArrayList<>();
    private String[] kindOfEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ListView);
        getData();
        createData();
        makeCategories();
        adapter = new MyExpandableListAdapter(this, groups, kindOfEvent);
        adapter.addCategories(kindOfEvent);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setGroupIndicator(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    /**
     * A function to receive all the data from the iCalendar
     */
    public void getData() {
        GetiCal getiCal = new GetiCal();
        getiCal.execute();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        events = (ArrayList<ThaliaEvent>) getiCal.getNewEvents();
        Collections.sort(events);
        if (events.size() != 0) removeLast();
    }

    /**
     * Function to fill the ArrayList
     */
    private void createData() {
        int j = 0, i = 0;
        while (i < events.size()) {
            ThaliaEvent t = events.get(i);
            Group groep = new Group(t.getDatumString());
            groep.children.add(events.get(i).makeSummary());
            i++;
            while (i < events.size() && events.get(i).getDatumString().equals(t.getDatumString())) {
                groep.children.add(events.get(i).makeSummary());
                i++;
            }
            groups.append(j, groep);
            j++;
        }
    }

    /**
     * Removes events that already happened
     */
    private void removeLast() {
        Date nu = new Date();
        int i = 0;
        while (i < events.size()) {
            while (events.get(i).getGregCalFormat(events.get(i).getStartDate()).getTime().compareTo(nu) < 0) {
                events.remove(i);
            }
            i++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent1 = new Intent(this, Calendar.class);
                startActivity(intent1);
                break;
            case R.id.menu2:
                Intent intent2 = new Intent(this, Eetlijst.class);
                startActivity(intent2);
                break;
            case R.id.menu4:
                Intent intent4 = new Intent(this, Settings.class);
                startActivity(intent4);
                break;
        }
        return true;
    }

    private void makeCategories() {
        kindOfEvent = new String[events.size()];
        for (int i = 0; i < kindOfEvent.length; i++) {
            kindOfEvent[i] = events.get(i).getCategory().toString();
        }
    }
}



