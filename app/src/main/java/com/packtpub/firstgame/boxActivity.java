package com.packtpub.firstgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class boxActivity extends Activity {

    private CheckBox plus, min, keer, deel;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        addListenerOnButton();
        //addListenerOnButtonSave();
        //preferencesshit
        loadSavedPreferences();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean checkBoxValuePlus = sharedPreferences.getBoolean("CheckBox_Value_Plus", false);
        boolean checkBoxValueMin = sharedPreferences.getBoolean("CheckBox_Value_Min", false);
        boolean checkBoxValueKeer = sharedPreferences.getBoolean("CheckBox_Value_Keer", false);
        boolean checkBoxValueDeel = sharedPreferences.getBoolean("CheckBox_Value_Deel", false);
        if (checkBoxValuePlus) {
            plus.setChecked(true);
        } else {
            plus.setChecked(false);
        }
        if (checkBoxValueMin) {
            min.setChecked(true);
        } else {
            min.setChecked(false);
        }
        if (checkBoxValueKeer) {
            keer.setChecked(true);
        } else {
            keer.setChecked(false);
        }
        if (checkBoxValueDeel) {
            deel.setChecked(true);
        } else {
            deel.setChecked(false);
        }
    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void addListenerOnButton() {

        plus = (CheckBox) findViewById(R.id.plus_option);
        min = (CheckBox) findViewById(R.id.min_option);
        keer = (CheckBox) findViewById(R.id.keer_option);
        deel = (CheckBox) findViewById(R.id.deel_option);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                /* Misschien ooit nog handig, een toast met de waarde van een checkbox
                StringBuffer result = new StringBuffer();
                result.append("Plus: ").append(plus.isChecked());
                Toast.makeText(boxActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                */

                savePreferences("CheckBox_Value_Plus", plus.isChecked());
                savePreferences("CheckBox_Value_Min", min.isChecked());
                savePreferences("CheckBox_Value_Keer", keer.isChecked());
                savePreferences("CheckBox_Value_Deel", deel.isChecked());
                finish();
            }
        });
    }
/*
    public void addListenerOnButtonSave() {

        BackButton = (Button) findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(boxActivity.this, AndroidSharedPreferences.class);
                startActivity(i);
                }
        });

    }*/
}