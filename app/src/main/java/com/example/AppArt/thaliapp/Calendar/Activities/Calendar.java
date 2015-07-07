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
    SparseArray<Group> groups = new SparseArray<Group>();
    public ArrayList<ThaliaEvent> evenementen = new ArrayList<>();
    public ArrayList<ThaliaEvent> parties = new ArrayList<>();
    public ArrayList<ThaliaEvent> workshops = new ArrayList<>();
    public ArrayList<ThaliaEvent> ALVs = new ArrayList<>();
    public ArrayList<ThaliaEvent> borrel = new ArrayList<>();
    public ArrayList<ThaliaEvent> lezing = new ArrayList<>();
    public ArrayList<ThaliaEvent> overig = new ArrayList<>();
    private String[] soort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ListView);
        getData();
        createData();
        makeCategories();
        adapter = new MyExpandableListAdapter(this, groups,soort);
        adapter.addCategories(soort);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setGroupIndicator(null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    private void sort() {
        for (int i = 0; i < evenementen.size(); i++) {
            switch (evenementen.get(i).getCategory()) {
                case BORREL:
                    borrel.add(evenementen.get(i));
                    break;
                case LECTURE:
                    lezing.add(evenementen.get(i));
                    break;
                case ALV:
                    ALVs.add(evenementen.get(i));
                    break;
                case PARTY:
                    parties.add(evenementen.get(i));
                    break;
                case WORKSHOP:
                    workshops.add(evenementen.get(i));
                    break;
                case DEFAULT:
                    overig.add(evenementen.get(i));
                    break;
                default:
                    break;
            }
        }
    }

    private String Inhoud(ThaliaEvent t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getSummary());
        sb.append("\n");
        sb.append(t.duration());
        sb.append("\n");

        sb.append(t.getLocation());
        return sb.toString();
    }

    public void getData() {
        GetiCal getiCal = new GetiCal();
        getiCal.execute();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        evenementen = (ArrayList<ThaliaEvent>) getiCal.getNewEvents();
        Collections.sort(evenementen);
        sort();
        if (evenementen.size() != 0) removeLast();
    }

    private void createData() {
        int j = 0, i = 0;
        while (i < evenementen.size()) {
            ThaliaEvent t = evenementen.get(i);
            Group groep = new Group(t.getDatumString());
            groep.children.add(Inhoud(evenementen.get(i)));
            i++;
            while (i < evenementen.size() && evenementen.get(i).getDatumString().equals(t.getDatumString())) {
                groep.children.add(Inhoud(evenementen.get(i)));
                i++;
            }
            groups.append(j, groep);
            j++;
        }
    }

    private void removeLast() {
        Date nu = new Date();
        int i = 0;
        while (i < evenementen.size()) {
            while (evenementen.get(i).getGregCalFormat(evenementen.get(i).getStartDate()).getTime().compareTo(nu) < 0) {
                evenementen.remove(i);
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
        soort = new String[evenementen.size()];
        for (int i = 0; i < soort.length; i++) {
            soort[i] = evenementen.get(i).getCategory().toString();
        }
    }
}



