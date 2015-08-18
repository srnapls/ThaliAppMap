package com.example.AppArt.thaliapp.FoodList.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.ThaliappActivity;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

/**
 * Makes a receipt from all the ordered food
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Receipt extends ThaliappActivity {

    private String[] chosen;
    private Double amount;
    private String name;
    private String[] all;
    private int size;

    /**
     * Makes the receipt
     *
     * @param savedInstanceState, the saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bon);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ReceiptFragment())
                    .commit();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chosen = extras.getStringArray("chosen");
            amount = extras.getDouble("amount", 0.0);
            name = extras.getString("name", " ");
        }
        if (chosen == null) {
            size = 2;
        } else {
            size = chosen.length + 2;
        }
        all = new String[size];
        all[0] = name;
        for (int i = 1; i < size - 1; i++) {
            all[i] = chosen != null ? chosen[i - 1] : null;
        }
        String temp = Double.toString(amount);
        if (temp.length() >= 6) {
            StringBuilder sb = new StringBuilder();
            sb.append(temp.charAt(0));
            sb.append(temp.charAt(1));
            sb.append(temp.charAt(2));
            sb.append(temp.charAt(3));
            if (temp.charAt(2) == '.') {
                sb.append(temp.charAt(4));
            } else if (temp.charAt(3) == '.') {
                sb.append(temp.charAt(5));
            }
            all[size - 1] = sb.toString();
        } else {
            all[size - 1] = Double.toString(amount);
        }
    }

    /**
     * Getter for all
     *
     * @return stringarray all
     */
    public String[] getAll() {
        return all;
    }

    /**
     * Sends the receipt to the database and wipes the current receipt
     *
     * @param v, the view of the activity
     */
    public void send(View v) {
        Database.getDatabase().addReceipt(chosen);
        Toast.makeText(this, "Uw gegevens zijn verzonden.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Restaurant.class);
        intent.putExtra("chosen", (String[]) null);
        intent.putExtra("amount", 0.0);
        intent.putExtra("name", (String) null);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ReceiptFragment extends ListFragment {
        private String[] all;

        public ReceiptFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Receipt b = (Receipt) getActivity();
            all = b.getAll();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    inflater.getContext(), android.R.layout.simple_list_item_1,
                    all);
            setListAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}
