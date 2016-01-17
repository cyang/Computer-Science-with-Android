package com.pig;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity {

    private int userTotalScore = 0;
    private int userTurnScore = 0;
    private int computerTotalScore = 0;
    private int computerTurnScore = 0;
    private Random rand = new Random();
    private String userScoreLabel = "Your Score: %s Turn Score: %s";
    private String computerScoreLabel = "Computer Score: %s Turn Score: %s";

    private int diceValue = 0;

    private Button rollButton;
    private Button holdButton;

    private TextView turnTextView;

    private Handler timerHandler = new Handler();
    private Runnable timerRunner = new Runnable() {
        @Override
        public void run() {
            diceValue = getDiceValue();

            if(diceValue == 1 || computerTurnScore >= 20) {
                if(diceValue == 1) {
                    computerTurnScore = 0;
                    displayToastMessage("The computer rolled a 1");
                } else
                    displayToastMessage("The computer has decided to hold");

                computerEnd();

                return;
            }

            computerRoll(diceValue);

            timerHandler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollButton = (Button) findViewById(R.id.rollButton);
        holdButton = (Button) findViewById(R.id.holdButton);

        turnTextView = (TextView) findViewById(R.id.turnText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void roll(View view) {
        diceValue = getDiceValue();
        updateDiceImage(diceValue);
        updateUserTurnScore(diceValue);
    }

    public void hold(View view) {
        userTotalScore += userTurnScore;
        userTurnScore = 0;

        TextView userTextView = (TextView) findViewById(R.id.userScore);
        userTextView.setText(String.format(userScoreLabel, userTotalScore, userTurnScore));

        ImageView imageView = (ImageView) findViewById(R.id.diceView);
        imageView.setImageResource(R.drawable.dice1);

        if(userTotalScore >= 100){
            displayToastMessage("You Win");

            turnTextView.setText("");

            rollButton.setEnabled(false);
            holdButton.setEnabled(false);

            return;
        }

        computerTurn();
    }

    public void reset(View view){
        userTotalScore = 0;
        userTurnScore = 0;
        computerTotalScore = 0;
        computerTurnScore = 0;

        TextView userTextView = (TextView) findViewById(R.id.userScore);
        userTextView.setText(String.format(userScoreLabel, userTotalScore, userTurnScore));

        TextView computerTextView = (TextView) findViewById(R.id.computerScore);
        computerTextView.setText(String.format(computerScoreLabel, computerTotalScore, computerTurnScore));

        ImageView imageView = (ImageView) findViewById(R.id.diceView);
        imageView.setImageResource(R.drawable.dice1);

        rollButton.setEnabled(true);
        holdButton.setEnabled(true);

        turnTextView.setText("Your turn");
    }

    public int getDiceValue(){
        return rand.nextInt(5)+1;
    }

    public void updateDiceImage(int diceValue){
        ImageView imageView = (ImageView) findViewById(R.id.diceView);

        switch (diceValue) {
            case 1:
                imageView.setImageResource(R.drawable.dice1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.dice2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.dice3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.dice4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.dice5);
                break;
            case 6:
                imageView.setImageResource(R.drawable.dice6);
                break;
        }
    }

    public void updateUserTurnScore(int diceValue){
        switch (diceValue) {
            case 1:
                userTurnScore = 0;
                displayToastMessage("You rolled a 1");
                computerTurn();

                break;
            case 2:
                userTurnScore += 2;
                break;
            case 3:
                userTurnScore += 3;
                break;
            case 4:
                userTurnScore += 4;
                break;
            case 5:
                userTurnScore += 5;
                break;
            case 6:
                userTurnScore += 6;
                break;
        }

        TextView textView = (TextView) findViewById(R.id.userScore);
        textView.setText(String.format(userScoreLabel, userTotalScore, userTurnScore));
    }

    public void computerTurn() {
        turnTextView.setText("Computer's turn");

        rollButton.setEnabled(false);
        holdButton.setEnabled(false);

        timerHandler.postDelayed(timerRunner, 0);
    }

    public void updateComputerTurnScore(int diceValue){
        switch (diceValue) {
            case 1:
                computerTurnScore = 0;
                break;
            case 2:
                computerTurnScore += 2;
                break;
            case 3:
                computerTurnScore += 3;
                break;
            case 4:
                computerTurnScore += 4;
                break;
            case 5:
                computerTurnScore += 5;
                break;
            case 6:
                computerTurnScore += 6;
                break;
        }

        TextView computerTextView = (TextView) findViewById(R.id.computerScore);
        computerTextView.setText(String.format(computerScoreLabel, computerTotalScore, computerTurnScore));
    }

    public void computerRoll(int diceValue){
        updateDiceImage(diceValue);
        updateComputerTurnScore(diceValue);
    }

    public void computerEnd(){
        computerTotalScore += computerTurnScore;
        computerTurnScore = 0;
        updateDiceImage(1);

        TextView computerTextView = (TextView) findViewById(R.id.computerScore);
        computerTextView.setText(String.format(computerScoreLabel, computerTotalScore, computerTurnScore));

        rollButton.setEnabled(true);
        holdButton.setEnabled(true);

        if(computerTotalScore >= 100){
            displayToastMessage("You Lose");
            turnTextView.setText("");

            rollButton.setEnabled(false);
            holdButton.setEnabled(false);
        } else
            turnTextView.setText("Your turn");

    }

    public void displayToastMessage(CharSequence text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
