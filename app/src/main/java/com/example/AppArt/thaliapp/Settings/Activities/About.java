package com.example.AppArt.thaliapp.Settings.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.media.MediaPlayer;

import com.example.AppArt.thaliapp.ThaliappActivity;

import com.example.AppArt.thaliapp.R;

/**
 * Info screen for the app
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

public class About extends ThaliappActivity implements View.OnClickListener {

    int no_clicks;
    MediaPlayer mediaPlayer = null;

    /**
     *
     * @param savedInstanceState, the saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        no_clicks = 0;
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setElevation(0);
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.imageView4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (++no_clicks == 3) {
            new AlertDialog.Builder(this)
                .setIcon(R.drawable.feesticoon)
                .setTitle("Party mode engaged!")
                .setPositiveButton("OK", null)
                .show();
            mediaPlayer = MediaPlayer.create(this, R.raw.tequila);
            mediaPlayer.start();
        } else if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
