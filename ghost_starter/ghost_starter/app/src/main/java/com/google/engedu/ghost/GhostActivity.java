package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    TextView ghostText, gameStatus;
    Button challengeBtn, restartBtn;
    InputStream dictionaryStream;
    SimpleDictionary simpleDictionary;
    String word = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        ghostText = (TextView) findViewById(R.id.ghostText);
        gameStatus = (TextView) findViewById(R.id.gameStatus);
        challengeBtn = (Button) findViewById(R.id.buttonChallenge);
        restartBtn = (Button) findViewById(R.id.buttonRestart);
        challengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleDictionary.isWord(word) && word.length() > 4) {
                    gameStatus.setText("User Victory");
                }
            }
        });
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = "";
                onStart(null);
            }
        });


        try {
            dictionaryStream = getAssets().open("words.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            simpleDictionary = new SimpleDictionary(dictionaryStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


        onStart(null);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(userTurn) {
            keyCode = event.getUnicodeChar();
            userTurn = false;
            if ((96 < keyCode && keyCode < 123) || (64 < keyCode || keyCode < 91)) {
                word = word + (char) keyCode;
                ghostText.setText(word);
                if (simpleDictionary.isWord(word)) {
                    gameStatus.setText("is word");
                }
            }
            Log.d("computer", "turn going to");
            computerTurn();
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        // Do computer turn stuff then make it the user's turn again
        Log.d("computer", "turn");
        Log.d("user turn", "" + userTurn);
        if (word.length() >= 4&&simpleDictionary.isWord(word)) {
            gameStatus.setText("User Victory");
            return;
        }
        String newWord = simpleDictionary.getAnyWordStartingWith(word);
        Log.d("new word is", "" + newWord);
        Log.d("word is", "" + word);
        if (newWord == null) {
            gameStatus.setText("Computer's Victory");
            return;
        } else {
            word = newWord.substring(0, word.length() + 1);
            ghostText.setText(word);
        }
        userTurn = true;
        gameStatus.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
