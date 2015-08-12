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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.FoodList.Activities.Restaurant;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Activities.Settings;
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
     * @param menu, menu that needs to be created
     * @return whether it succeeded
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_information, menu);
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
            case R.id.Calendar:
                startActivity(new Intent(this, Calendar.class));
                break;
            case R.id.Restaurant:
                startActivity(new Intent(this, Restaurant.class));
                break;
            case R.id.Settings:
                startActivity(new Intent(this, Settings.class));
                break;
        }
        return true;
    }

    /**
     * Returns to calendar
     *
     * @param keyCode, the key code
     * @param event,   the event of the key
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

        /**
         * Creates the view of the fragment
         *
         * @param inflater            inflater of the fragment
         * @param container,          the container where the fragment needs to go
         * @param savedInstanceState, the bundle that could possible been added
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
            information[0] = Html.fromHtml(event.getSummary()).toString();
            information[1] = event.duration();
            information[2] = Html.fromHtml(event.getLocation()).toString();
            information[3] = Html.fromHtml(event.getDescription()).toString();
        }
    }
}
