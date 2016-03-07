package com.packtpub.firstgame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HighScoreActivity extends Activity implements View.OnClickListener {

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        Button buttonMenu1 = (Button) findViewById(R.id.buttonMenu1);
        buttonMenu1.setOnClickListener(this);

        TextView textHighScore1 = (TextView) findViewById(R.id.textHighScore1);
        TextView textHighScore2 = (TextView) findViewById(R.id.textHighScore2);
        TextView textHighScore3 = (TextView) findViewById(R.id.textHighScore3);
        TextView textHighScore4 = (TextView) findViewById(R.id.textHighScore4);
        TextView textHighScore5 = (TextView) findViewById(R.id.textHighScore5);
        TextView textHsNaam1 = (TextView) findViewById(R.id.textHsNaam1);
        TextView textHsNaam2 = (TextView) findViewById(R.id.textHsNaam2);
        TextView textHsNaam3 = (TextView) findViewById(R.id.textHsNaam3);
        TextView textHsNaam4 = (TextView) findViewById(R.id.textHsNaam4);
        TextView textHsNaam5 = (TextView) findViewById(R.id.textHsNaam5);
        TextView textTijd1 = (TextView) findViewById(R.id.textTijd1);
        TextView textTijd2 = (TextView) findViewById(R.id.textTijd2);
        TextView textTijd3 = (TextView) findViewById(R.id.textTijd3);
        TextView textTijd4 = (TextView) findViewById(R.id.textTijd4);
        TextView textTijd5 = (TextView) findViewById(R.id.textTijd5);

        database = openOrCreateDatabase("gamedb.db", MODE_PRIVATE, null);

        database.execSQL("create table if not exists highScore(naam string, tijd string, level int)");
        database.execSQL("insert into highScore values ('Test', '0000', 1)");
        database.execSQL("insert into highScore values ('Test', '0000', 1)");
        database.execSQL("insert into highScore values ('Test', '0000', 1)");
        database.execSQL("insert into highScore values ('Test', '0000', 1)");
        database.execSQL("insert into highScore values ('Test', '0000', 1)");
        Cursor cursor = database.rawQuery("Select level from highScore order by level desc", null);
        cursor.moveToFirst();

        String[] array = new String[5];
        int i = 0;
        while(i < 5){
            String uname = cursor.getString(cursor.getColumnIndex("level"));
            array[i] = uname;
            i++;
            cursor.moveToNext();
        }

        textHighScore1.setText(array[0]);
        textHighScore2.setText(array[1]);
        textHighScore3.setText(array[2]);
        textHighScore4.setText(array[3]);
        textHighScore5.setText(array[4]);

        cursor = database.rawQuery("Select naam from highScore order by level desc", null);
        cursor.moveToFirst();

        String[] array2 = new String[5];
        i = 0;
        while(i < 5){
            String uname = cursor.getString(cursor.getColumnIndex("naam"));
            array2[i] = uname;
            i++;
            cursor.moveToNext();
        }

        textHsNaam1.setText(array2[0]);
        textHsNaam2.setText(array2[1]);
        textHsNaam3.setText(array2[2]);
        textHsNaam4.setText(array2[3]);
        textHsNaam5.setText(array2[4]);

        cursor = database.rawQuery("Select tijd from highScore order by level desc", null);
        cursor.moveToFirst();

        String[] array3 = new String[5];
        i = 0;
        while(i < 5){
            String uname = cursor.getString(cursor.getColumnIndex("tijd"));
            array3[i] = uname;
            i++;
            cursor.moveToNext();
        }

        textTijd1.setText(array3[0]);
        textTijd2.setText(array3[1]);
        textTijd3.setText(array3[2]);
        textTijd4.setText(array3[3]);
        textTijd5.setText(array3[4]);

        database.close();

    }
    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.buttonMenu1:
                i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
        }
    }
}
