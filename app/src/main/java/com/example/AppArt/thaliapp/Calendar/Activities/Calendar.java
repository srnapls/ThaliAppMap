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

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.AppArt.thaliapp.R.id.ListView;

/**
 * Calendar activity, shows a list of all currently known ThaliaEvents that
 * have yet to end.
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Calendar extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    private MyExpandableListAdapter adapter;
    private SparseArray<Group> groups = new SparseArray<>();
    private ArrayList<ThaliaEvent> events;
    private EventCategory[] kindOfEvent;
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

        events = (ArrayList<ThaliaEvent>) Database.getDatabase().getEvents();
        if (events == null) {
            Toast.makeText(this, "Er zijn geen evenementen. \n" +
                    "Swipe om te updaten.", Toast.LENGTH_SHORT).show();
            adapter = new MyExpandableListAdapter(this, groups, kindOfEvent);
            adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
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
        actionBar.setTitle("Kalender");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu, menu that needs to be made
     * @return whether it succeeded
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar item clicks here. The action bar will automatically
     * handle clicks on the Home/Up button, as long as you specify a parent
     * activity in AndroidManifest.xml.
     *
     * @param item on which is clicked
     * @return The action has prevailed!
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Food:
                startActivity(new Intent(this, Restaurant.class));
                break;
            case R.id.Settings:
                startActivity(new Intent(this, Settings.class));
                break;
        }
        return true;
    }

    /**
     * Method to refresh the ArrayList when swiped up
     */
    public void onRefresh() {
        Database.getDatabase().updateEvents();
        Toast.makeText(this, "Kalender geupdate", LENGTH_SHORT).show();
        mSwipeLayout.setRefreshing(false);
        Intent intent1 = new Intent(this, Calendar.class);
        startActivity(intent1);
        this.finish();
    }

    /**
     * Method to fill the ArrayList, such that it is sorted on day
     */
    private void createData() {
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
