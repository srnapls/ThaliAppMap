package com.example.AppArt.thaliapp.Settings.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.AppArt.thaliapp.ThaliappActivity;

import com.example.AppArt.thaliapp.R;

/**
 * Loginscreen for the admin
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class Login extends ThaliappActivity {

    private final String inlog = "admin";
    private final String wachtwoord = "admin";
    private EditText name;
    private EditText password;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "Settings";

    /**
     *
     * @param savedInstanceState, the saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inlog);
        name = (EditText) findViewById(R.id.Inlognaam);
        password = (EditText) findViewById(R.id.Wachtwoord);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setTitle("Log in");
    }



    /**
     * Function that is called when you click on the button Send,
     * It checks whether you are allowed to enter the class with all of the receipts
     *
     * @param v: the view of the Inlog activity
     */
    public void send(View v) {
        String s1, s2;

        s1 = name.getText().toString();
        s2 = password.getText().toString();
        if (s1.equals(inlog) && s2.equals(wachtwoord)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("access", true);
            editor.apply();
            Intent i = new Intent(this, Overview.class);
            startActivity(i);
            finish();
        }
        else {
            Toast.makeText(this, "U heeft een foutieve combinatie van gebruikersnaam en wachtwoord. " +
                    "Probeer opnieuw.", Toast.LENGTH_SHORT).show();
        }
    }
}
