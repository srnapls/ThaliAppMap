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
import android.widget.Toast;

import com.example.AppArt.thaliapp.Calendar.Backend.EventCategory;
import com.example.AppArt.thaliapp.Calendar.Backend.Group;
import com.example.AppArt.thaliapp.Calendar.Backend.MyExpandableListAdapter;
import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.Eetlijst.Activities.Restaurant;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Activities.Settings;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import java.util.ArrayList;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen
 *         (s4182804)
 */

public class Calendar extends ActionBarActivity {
    private MyExpandableListAdapter adapter;
    SparseArray<Group> groups = new SparseArray<>();
    public ArrayList<ThaliaEvent> events;
    private EventCategory[] kindOfEvent;

    @Override
    protected void onStart() {
        Database database = Database.getDatabase();
        database.updateEvents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ListView);
        System.out.println("check1");
        events = (ArrayList<ThaliaEvent>) Database.getDatabase().getEvents();
        System.out.println("check2");
        if (events == null) {
            Toast.makeText(this, "There seem to be no events", Toast.LENGTH_SHORT).show();
            return;
        } else {
            createData();
            makeCategories();
            adapter = new MyExpandableListAdapter(this, groups, kindOfEvent);
            adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
            listView.setAdapter(adapter);
            listView.setClickable(true);
            listView.setGroupIndicator(null);
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    /**
     * Function to fill the ArrayList, such that it is sorted on day
     */
    private void createData() {
        int j = 0, i = 0;
        while (i < events.size()) {
            ThaliaEvent t = events.get(i);
            Group group = new Group(t.getDatumString());
            group.children.add(events.get(i).makeSummary());
            i++;
            while (i < events.size() && events.get(i).getDatumString().equals(t.getDatumString())) {
                group.children.add(events.get(i).makeSummary());
                i++;
            }
            groups.append(j, group);
            j++;
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
                Intent intent2 = new Intent(this, Restaurant.class);
                startActivity(intent2);
                break;
            case R.id.menu4:
                Intent intent4 = new Intent(this, Settings.class);
                startActivity(intent4);
                break;
        }
        return true;
    }

    /**
     * Makes a stringarray and fills it with the Categories
     */
    private void makeCategories() {
        kindOfEvent = new EventCategory[events.size()];
        for (int i = 0; i < kindOfEvent.length; i++) {
            kindOfEvent[i] = events.get(i).getCategory();
        }
    }
}



