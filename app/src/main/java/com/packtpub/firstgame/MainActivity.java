package com.packtpub.firstgame;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
        Button buttonHighScore = (Button) findViewById(R.id.buttonHighScore);
        buttonHighScore.setOnClickListener(this);
        Button buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.buttonPlay:
                i = new Intent(this, GameActivity.class);
                startActivity(i);
                break;
            case R.id.buttonHighScore:
                i = new Intent(this, HighScoreActivity.class);
                startActivity(i);
                break;
            case R.id.buttonSettings:
                i = new Intent(this, boxActivity.class);
                startActivity(i);
                break;
        }
    }
}