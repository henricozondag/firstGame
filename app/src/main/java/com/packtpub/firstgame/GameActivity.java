package com.packtpub.firstgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends Activity implements View.OnClickListener {

    public int partA, partB, it, correctAnswer, generatedIndex, currentLevel = 1;
    public ArrayList<String> mathList = new ArrayList<>();
    Button buttonObjectChoice1, buttonObjectChoice2, buttonObjectChoice3;
    TextView textObjectPartA, textObjectPartB, textObjectLevel, textOperator;
    boolean checkBoxValuePlus, checkBoxValueMin, checkBoxValueKeer, checkBoxValueDeel;

    //timer shit
    String realTime, playerName;
    TextView timerValue;
    Handler customHandler = new Handler();
    long startTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        loadSavedPreferences();

        textObjectPartA = (TextView) findViewById(R.id.textPartA);
        textObjectPartB = (TextView) findViewById(R.id.textPartB);
        textObjectLevel = (TextView) findViewById(R.id.textLevel);
        textOperator = (TextView) findViewById(R.id.textOperator);
        timerValue = (TextView) findViewById(R.id.timerValue);
        buttonObjectChoice1 = (Button) findViewById(R.id.buttonChoice1);
        buttonObjectChoice2 = (Button) findViewById(R.id.buttonChoice2);
        buttonObjectChoice3 = (Button) findViewById(R.id.buttonChoice3);

        buttonObjectChoice1.setOnClickListener(this);
        buttonObjectChoice2.setOnClickListener(this);
        buttonObjectChoice3.setOnClickListener(this);

        setQuestion2();
        askName();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        checkBoxValuePlus = sharedPreferences.getBoolean("CheckBox_Value_Plus", false);
        checkBoxValueMin = sharedPreferences.getBoolean("CheckBox_Value_Min", false);
        checkBoxValueKeer = sharedPreferences.getBoolean("CheckBox_Value_Keer", false);
        checkBoxValueDeel = sharedPreferences.getBoolean("CheckBox_Value_Deel", false);
    }

    @Override
    public void onClick(View view) {
        //variabele waarin we bijhouden wat het gegeven antwoord is
        int answerGiven = 0;
        switch (view.getId()) {
            case R.id.buttonChoice1:
                //als op button 1 gedrukt wordt dan de waarde van button 1 in answerGiven zetten
                answerGiven = Integer.parseInt("" + buttonObjectChoice1.getText());
                break;
            case R.id.buttonChoice2:
                answerGiven = Integer.parseInt("" + buttonObjectChoice2.getText());
                break;
            case R.id.buttonChoice3:
                answerGiven = Integer.parseInt("" + buttonObjectChoice3.getText());
                break;
        }

        updateScoreAndLevel(answerGiven);
        setQuestion2();
    }

    void setQuestion() {
        //De som opbouwen
        int numberRange = currentLevel * 3;
        Random randInt = new Random();

        int partA = randInt.nextInt(numberRange);
        partA++;//Mag geen 0 zijn

        int partB = randInt.nextInt(numberRange);
        partB++;//Mag geen 0 zijn

        correctAnswer = partA * partB;

        int wrongAnswer1 = correctAnswer - randInt.nextInt(10);
        if (wrongAnswer1 == correctAnswer) {
            wrongAnswer1++;
        }
        ;
        int wrongAnswer2 = correctAnswer + randInt.nextInt(10);
        if (wrongAnswer2 == correctAnswer) {
            wrongAnswer2++;
        }
        ;

        textObjectPartA.setText("" + partA);
        textObjectPartB.setText("" + partB);

        //Opbouwen van de drie mogelijke antwoorden
        //Random nummer 0,1 of 2 zodat het goede antwoord niet altijd op dezelfde plek komt te staan
        int buttonLayout = randInt.nextInt(3);
        switch (buttonLayout) {
            case 0:
                buttonObjectChoice1.setText("" + correctAnswer);
                buttonObjectChoice2.setText("" + wrongAnswer1);
                buttonObjectChoice3.setText("" + wrongAnswer2);
                break;

            case 1:
                buttonObjectChoice2.setText("" + correctAnswer);
                buttonObjectChoice3.setText("" + wrongAnswer1);
                buttonObjectChoice1.setText("" + wrongAnswer2);
                break;

            case 2:
                buttonObjectChoice3.setText("" + correctAnswer);
                buttonObjectChoice1.setText("" + wrongAnswer1);
                buttonObjectChoice2.setText("" + wrongAnswer2);
                break;
        }
    }

    void updateScoreAndLevel(int answerGiven) {

        if (isCorrect(answerGiven)) {
            currentLevel++;
        } else {
            //timerzooi
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
            //timerzooi
            updateHighScoreList(currentLevel);
            currentLevel = 1;
        }

        //Update de waarde op het scherm voor de level
        textObjectLevel.setText("Level: " + currentLevel);

    }

    boolean isCorrect(int answerGiven) {
        boolean correctTrueOrFalse;
        if (answerGiven == correctAnswer) {
            Toast.makeText(getApplicationContext(), "Goed!", Toast.LENGTH_LONG).show();
            correctTrueOrFalse = true;
        } else {
            Toast.makeText(getApplicationContext(), "Game Over!" + "\n" + "Uw level was: " + currentLevel + "\n" + "Uw tijd was: " + realTime, Toast.LENGTH_LONG).show();
            correctTrueOrFalse = false;
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

        return correctTrueOrFalse;
    }

    void updateHighScoreList(int currentLevel) {

        SQLiteDatabase database = openOrCreateDatabase("gamedb.db", MODE_PRIVATE, null);
        database.execSQL("create table if not exists highScore(naam string, tijd string, level int)");
        database.execSQL("insert into highScore values (" + "'" + playerName + "'" + ", " + "'" + realTime + "'" + ", " + currentLevel + ")");
        database.close();
    }

    void askName () {

        final EditText inputText = new EditText(this);

        // Hier kan je een placeholder gebruiken
        inputText.setHint("");

        new AlertDialog.Builder(this)
                // Titel van de popup
                //.setTitle("Informatie voor de Highscore lijst.")

                // Vraag die bovenaan het inputveld staat
                .setMessage("Wat is je naam?")
                .setView(inputText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        playerName = inputText.getText().toString();
                        //timerzooi
                        startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);
                        //timerzooi
                    }
                })
                // Als je wil dat de gebruiker op annuleren kan klikken dan kan je deze box activeren
                /*
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                */
                .show();
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
            realTime = timerValue.getText().toString();
        }

    };

    void setMath () {

        boolean valuePlus = checkBoxValuePlus;
        boolean valueMin = checkBoxValueMin;
        boolean valueKeer = checkBoxValueKeer;
        boolean valueDeel = checkBoxValueDeel;

        it = 0;
        while (it < 5) {
            if (checkBoxValuePlus && valuePlus) {
                mathList.add("+");
                it++;
                valuePlus = false;
            }  else if (checkBoxValueMin && valueMin) {
                mathList.add("-");
                it++;
                valueMin = false;
            } else if (checkBoxValueKeer && valueKeer) {
                mathList.add("x");
                it++;
                valueKeer = false;
            } else if (checkBoxValueDeel && valueDeel) {
                mathList.add("/");
                it++;
                valueDeel = false;
            }   else {
                break;
            }
        }
    }

    void setQuestion2() {
        //De som opbouwen
        int numberRange = currentLevel * 3;
        Random randInt = new Random();

        partA = randInt.nextInt(numberRange);
        partA++;//Mag geen 0 zijn

        partB = randInt.nextInt(numberRange);
        partB++;//Mag geen 0 zijn

        setMath();
        updateTextOperator();
        setCorrectAnswer();

        int wrongAnswer1 = correctAnswer - randInt.nextInt(10);
        if (wrongAnswer1 == correctAnswer) {
            wrongAnswer1++;
        }
        ;
        int wrongAnswer2 = correctAnswer + randInt.nextInt(10);
        if (wrongAnswer2 == correctAnswer) {
            wrongAnswer2++;
        }
        ;

        textObjectPartA.setText("" + partA);
        textObjectPartB.setText("" + partB);

        //Opbouwen van de drie mogelijke antwoorden
        //Random nummer 0,1 of 2 zodat het goede antwoord niet altijd op dezelfde plek komt te staan
        int buttonLayout = randInt.nextInt(3);
        switch (buttonLayout) {
            case 0:
                buttonObjectChoice1.setText("" + correctAnswer);
                buttonObjectChoice2.setText("" + wrongAnswer1);
                buttonObjectChoice3.setText("" + wrongAnswer2);
                break;

            case 1:
                buttonObjectChoice2.setText("" + correctAnswer);
                buttonObjectChoice3.setText("" + wrongAnswer1);
                buttonObjectChoice1.setText("" + wrongAnswer2);
                break;

            case 2:
                buttonObjectChoice3.setText("" + correctAnswer);
                buttonObjectChoice1.setText("" + wrongAnswer1);
                buttonObjectChoice2.setText("" + wrongAnswer2);
                break;
        }
    }

    private void updateTextOperator() {
        Random random = new Random();

        int maxIndex = mathList.size();
        generatedIndex = random.nextInt(maxIndex);

        textOperator.setText(mathList.get(generatedIndex));
    }

    private void setCorrectAnswer() {

        if (mathList.get(generatedIndex) == "+") {
            correctAnswer = partA + partB;
        }
        else if (mathList.get(generatedIndex) == "-") {
            correctAnswer = partA - partB;
        }
        else if (mathList.get(generatedIndex) == "x") {
            correctAnswer = partA * partB;
        }
        else if (mathList.get(generatedIndex) == "/") {
            correctAnswer = partA / partB;
        }
    }
}