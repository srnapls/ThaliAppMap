package com.example.AppArt.thaliapp.Settings.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.AppArt.thaliapp.ThaliappActivity;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * This class shows the overview of every order that has been made
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Overview extends ThaliappActivity {


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
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu, the menu that needs to be created
     * @return whether it succeeded
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
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
        switch (item.getItemId()) {
            case R.id.action_clear:
                Database.getDatabase().emptyReceipts();
                startActivity(new Intent(this, Overview.class));
                break;
            case R.id.action_logout:
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("access", false);
                editor.apply();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
