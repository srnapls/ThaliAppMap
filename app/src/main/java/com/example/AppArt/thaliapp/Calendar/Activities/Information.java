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

import com.example.AppArt.thaliapp.ThaliappActivity;


import com.example.AppArt.thaliapp.Calendar.Backend.ThaliaEvent;
import com.example.AppArt.thaliapp.R;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Information extends ThaliappActivity {

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
     * returns to calendar
     */
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this,Calendar.class));
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EventFragment extends ListFragment {
        private int index;
        private ThaliaEvent event;
        private String[] information;

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
            event = b.getParcelable("event");
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
            Html htmlConverter;
            information = new String[4];
            information[0] = Html.fromHtml(event.getSummary()).toString();
            information[1] = event.duration();
            System.out.println("EoF fillString" + event.duration());
            information[2] = Html.fromHtml(event.getLocation()).toString();
            information[3] = Html.fromHtml(event.getDescription()).toString();
        }
    }
}
