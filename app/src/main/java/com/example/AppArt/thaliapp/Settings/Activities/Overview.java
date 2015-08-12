package com.example.AppArt.thaliapp.Settings.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * This class shows the overview of every order that has been made
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Overview extends ActionBarActivity {


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Settings";

    /**
     * @param savedInstanceState, the saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzicht);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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
     * @param menu, the menu that needs to be created
     * @return whether it succeeded
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overzicht, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item, the item that is clicked
     * @return if the action has succeeded
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent i1 = new Intent(this, Settings.class);
                startActivity(i1);
                break;
            case R.id.action_clear:
                Database.getDatabase().emptyReceipts();
                Intent i2 = new Intent(this, Overview.class);
                startActivity(i2);
                break;
            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("access", false);
                editor.commit();
                Intent i3 = new Intent(this, Settings.class);
                startActivity(i3);
                break;
        }



            return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends ListFragment {
        String[] info;
        List<String[]> receipts = new ArrayList<>();

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            receipts = Database.getDatabase().getReceipts();
            fillInfo();
            if (info.length == 0) {
                Toast.makeText(getActivity(), "De bon is leeg", Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    inflater.getContext(), android.R.layout.simple_list_item_1,
                    info);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        /**
         * A function to fill the string array info;
         */
        private void fillInfo() {
            int length = 0;
            for (int i = 0; i < receipts.size(); i++) {
                length += receipts.get(i).length;
            }
            int place = 0;
            info = new String[length];
            for (int i = 0; i < receipts.size(); i++) {
                for (int j = 0; j < receipts.get(i).length; j++) {
                    info[place] = receipts.get(i)[j];
                    place++;
                }
            }
        }
    }
}
