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
    private static final String USER_TURN = "Your turn";
    private static final String USER_VICTORY = "User victory";
    private static final String USER_SCORE_LABEL = "User Score: %d";

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String COMPUTER_VICTORY = "Computer victory";
    private static final String COMPUTER_SCORE_LABEL = "Computer Score: %d";

    private boolean gameOver = false;

    private int userScore = 0;
    private int computerScore = 0;

    static final String STATE_STATUS = "gameStatus";
    static final String STATE_FRAGMENT = "wordFragment";
    static final String STATE_TURN = "turn";
    static final String STATE_USER_SCORE = "userScore";
    static final String STATE_COMPUTER_SCORE = "computerScore";
    static final String STATE_GAME_OVER = "gameOver";


    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    private TextView ghostTextView;
    private TextView gameStatusTextView;
    private TextView userScoreTextView;
    private TextView computerScoreTextView;

    private Button challengeButton;

    private String wordFragment = "";

    private Handler timerHandler = new Handler();
    private Runnable timerRunner = new Runnable() {
        @Override
        public void run() {
            if(gameOver) {
                return;
            }

            if(wordFragment.length() >= 4) {
                if (dictionary.isWord(wordFragment)) {

                    // User player has completed a word
                    gameStatusTextView.setText(COMPUTER_VICTORY + "; you completed a word");
                    updateScore("computer");

                    challengeButton.setEnabled(false);

                    return;
                }
            }

            // Get a new possible longer word
            String possibleWord = dictionary.getAnyWordStartingWith(wordFragment);

            if(possibleWord == null){
                // Word doesn't exist
                gameStatusTextView.setText(COMPUTER_VICTORY + "; not a valid prefix");
                updateScore("computer");

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
        userScoreTextView = (TextView) findViewById(R.id.userScore);
        computerScoreTextView = (TextView) findViewById(R.id.computerScore);

        challengeButton = (Button) findViewById(R.id.challengeButton);

        try {
            dictionary = new SimpleDictionary(getAssets().open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            gameStatusTextView.setText(savedInstanceState.getString(STATE_STATUS));

            wordFragment = savedInstanceState.getString(STATE_FRAGMENT);
            ghostTextView.setText(wordFragment);

            userScore = savedInstanceState.getInt(STATE_USER_SCORE);
            computerScore = savedInstanceState.getInt(STATE_COMPUTER_SCORE);
            userScoreTextView.setText(String.format(USER_SCORE_LABEL, userScore));
            computerScoreTextView.setText(String.format(COMPUTER_SCORE_LABEL, computerScore));

            gameOver = savedInstanceState.getBoolean(STATE_GAME_OVER);
            userTurn = savedInstanceState.getBoolean(STATE_TURN);

            if(gameOver){
                challengeButton.setEnabled(false);
            } else {
                challengeButton.setEnabled(true);
                checkTurn();
            }

        } else {
            onStart(null);
        }
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

        gameOver = false;

        checkTurn();

        challengeButton.setEnabled(true);

        return true;
    }

    public void checkTurn(){
        if (userTurn) {
            gameStatusTextView.setText(USER_TURN);
        } else {
            gameStatusTextView.setText(COMPUTER_TURN);
            computerTurn();
        }
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
                gameStatusTextView.setText(USER_VICTORY + "; computer completed a word");
                updateScore("user");
            } else {
                String possibleWord = dictionary.getAnyWordStartingWith(wordFragment);

                if(possibleWord != null) {
                    // Computer creates a fragment that is a prefix of a word from the dictionary
                    gameStatusTextView.setText(String.format(COMPUTER_VICTORY + "; Possible word: %s", possibleWord));
                    updateScore("computer");
                } else {
                    // Computer creates a bad fragment
                    gameStatusTextView.setText(USER_VICTORY);
                    updateScore("user");
                }
            }

            challengeButton.setEnabled(false);

        } else {
            gameStatusTextView.setText("You may only challenge a fragment that is at least 4 letters long!");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(STATE_STATUS, (String) gameStatusTextView.getText());
        savedInstanceState.putString(STATE_FRAGMENT, wordFragment);
        savedInstanceState.putInt(STATE_USER_SCORE, userScore);
        savedInstanceState.putInt(STATE_COMPUTER_SCORE, computerScore);
        savedInstanceState.putBoolean(STATE_TURN, userTurn);
        savedInstanceState.putBoolean(STATE_GAME_OVER, gameOver);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void updateScore(String victor){
        if(victor.equals("user")){
            userScore++;
            userScoreTextView.setText(String.format(USER_SCORE_LABEL, userScore));
        } else {
            computerScore++;
            computerScoreTextView.setText(String.format(COMPUTER_SCORE_LABEL, computerScore));
        }
        gameOver = true;
    }
}
