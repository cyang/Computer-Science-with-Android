package com.google.engedu.ghost;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String COMPUTER_VICTORY = "Computer victory";
    private static final String USER_VICTORY = "User victory";


    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    private TextView ghostTextView;
    private TextView gameStatusTextView;

    private Button challengeButton;

    private String wordFragment = "";

    private Handler timerHandler = new Handler();
    private Runnable timerRunner = new Runnable() {
        @Override
        public void run() {
            if(wordFragment.length() >= 4) {
                if (dictionary.isWord(wordFragment)) {

                    // User player has completed a word
                    gameStatusTextView.setText(COMPUTER_VICTORY + "; you completed a word");
                    challengeButton.setEnabled(false);

                    return;
                }
            }

            // Get a new possible longer word
            String possibleWord = dictionary.getAnyWordStartingWith(wordFragment);

            if(possibleWord == null){
                // Word doesn't exist
                gameStatusTextView.setText(COMPUTER_VICTORY + "; not a valid prefix");
                challengeButton.setEnabled(false);

                return;
            } else {
                // Word does exist
                wordFragment = possibleWord.substring(0, wordFragment.length()+1);
                ghostTextView.setText(wordFragment);
            }

            userTurn = true;
            gameStatusTextView.setText(USER_TURN);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        ghostTextView = (TextView) findViewById(R.id.ghostText);
        gameStatusTextView = (TextView) findViewById(R.id.gameStatus);

        challengeButton = (Button) findViewById(R.id.challengeButton);

        try {
            dictionary = new SimpleDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        onStart(null);
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
        timerHandler.postDelayed(timerRunner, 500);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        ghostTextView.setText("");
        wordFragment = "";

        if (userTurn) {
            gameStatusTextView.setText(USER_TURN);
        } else {
            gameStatusTextView.setText(COMPUTER_TURN);
            computerTurn();
        }

        challengeButton.setEnabled(true);

        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if(keyCode >= 29 && keyCode <= 54) {
            // Key is a valid letter
            wordFragment = String.format("%s%s", ghostTextView.getText(), String.valueOf(event.getDisplayLabel()).toLowerCase());
            ghostTextView.setText(wordFragment);

            // End user turn and begin computer's turn
            gameStatusTextView.setText(COMPUTER_TURN);
            userTurn = false;
            computerTurn();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    public void challenge(View view) {
        if(wordFragment.length() >= 4){
            if(dictionary.isWord(wordFragment)){
                // Computer has finished a word
                gameStatusTextView.setText(USER_VICTORY);
            } else {
                String possibleWord = dictionary.getAnyWordStartingWith(wordFragment);

                if(possibleWord != null) {
                    // Computer creates a fragment that is a prefix of a word from the dictionary
                    gameStatusTextView.setText(String.format(COMPUTER_VICTORY + "; Possible word: %s", possibleWord));
                } else {
                    // Computer creates a bad fragment
                    gameStatusTextView.setText(USER_VICTORY);

                }
            }

            challengeButton.setEnabled(false);

        } else {
            gameStatusTextView.setText("You may only challenge a fragment that is at least 4 letters long!");
        }
    }

}
