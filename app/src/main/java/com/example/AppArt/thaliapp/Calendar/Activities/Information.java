package com.example.AppArt.thaliapp.Calendar.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Information extends ActionBarActivity {

    /**
     * Creates the actionbar and fragments
     *
     * @param savedInstanceState saves set events
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E61B9B")));
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_information, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns to calendar
     *
     * @param keyCode
     * @param event
     * @return whether it succeeded
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(this, Calendar.class);
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EventFragment extends ListFragment {
        private int index;
        private ThaliaEvent event;
        private String[] information;
        private Html htmlConverter;

        /**
         * empty constructor
         */
        public EventFragment() {
        }

        //TODO: wat is container? Wat is een ViewGroup?

        /**
         *
         * @param inflater to inflate the menu
         * @param container
         * @param savedInstanceState
         * @return the view it created
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle b = getActivity().getIntent().getExtras();
            index = b.getInt("index");
            event = Database.getDatabase().getEvents().get(index);
            fillString();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    inflater.getContext(), android.R.layout.simple_list_item_1,
                    information);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        /**
         * Fills the screen/ListActivity with the information given from Calendar
         */
        private void fillString() {
            information = new String[4];
            information[0] = htmlConverter.fromHtml(event.getSummary()).toString();
            information[1] = event.duration();
            information[2] = htmlConverter.fromHtml(event.getLocation()).toString();
            information[3] = htmlConverter.fromHtml(event.getDescription()).toString();
        }
    }
}
