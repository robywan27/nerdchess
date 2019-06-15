package it.polimi.group02.controller.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;

import it.polimi.group02.R;
import it.polimi.group02.controller.database.StatisticsDatabaseImplementation;
import it.polimi.group02.controller.utility.BackgroundMusic;
import it.polimi.group02.controller.utility.PreferencesUtility;
import it.polimi.group02.model.statistics.PlayerStatistics;


import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;



/**
 * This activity shows all the players with distinct name ranked from top to bottom by highest score racked during each game.
 * The information displayed for each player is:
 *  - Player name
 *  - Score
 *  - Number of games played
 *  - Number of victories
 *  - Number of losses
 */
public class BestScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_best_scores);

        // set the background image
        PreferencesUtility.setBackground(this, findViewById(R.id.activity_best_scores));

        // set the correct buttons based on the theme
        setButtonsImages();


        // create a table displaying all players' data
        GridLayout bestScoresTable = (GridLayout) findViewById(R.id.history_table);

        // create the headers for this table
        createHeaderRow(bestScoresTable);

        StatisticsDatabaseImplementation dbImplementation = new StatisticsDatabaseImplementation(this);
        ArrayList<PlayerStatistics> playerStatistics = (ArrayList<PlayerStatistics>) dbImplementation.retrieveBestPlayersStatistics();

        // create iteratively an entry for each distinct player
        for (PlayerStatistics ps : playerStatistics) {
            TextView nameEntry = new TextView(this);
            nameEntry.setText(ps.getName());
            nameEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            nameEntry.setBackgroundResource(R.drawable.cell_border);
            bestScoresTable.addView(nameEntry);

            TextView scoreEntry = new TextView(this);
            scoreEntry.setText(String.valueOf(ps.getScore()));
            scoreEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            scoreEntry.setBackgroundResource(R.drawable.cell_border);
            bestScoresTable.addView(scoreEntry);

            TextView numberOfGamesEntry = new TextView(this);
            numberOfGamesEntry.setText(String.valueOf(ps.getNumberOfGamesPlayed()));
            numberOfGamesEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            numberOfGamesEntry.setBackgroundResource(R.drawable.cell_border);
            bestScoresTable.addView(numberOfGamesEntry);

            TextView numberOfVictoriesEntry = new TextView(this);
            numberOfVictoriesEntry.setText(String.valueOf(ps.getNumberOfVictories()));
            numberOfVictoriesEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            numberOfVictoriesEntry.setBackgroundResource(R.drawable.cell_border);
            bestScoresTable.addView(numberOfVictoriesEntry);

            TextView numberOfLossesEntry = new TextView(this);
            numberOfLossesEntry.setText(String.valueOf(ps.getNumberOfLosses()));
            numberOfLossesEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            numberOfLossesEntry.setBackgroundResource(R.drawable.cell_border);
            bestScoresTable.addView(numberOfLossesEntry);
        }
    }

    // create a header for each element of a player's profile
    private void createHeaderRow(GridLayout bestScoresTable) {
        TextView nameHeader = new TextView(this);
        nameHeader.setText("NAME");
        nameHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        nameHeader.setTypeface(Typeface.DEFAULT_BOLD);
        nameHeader.setBackgroundResource(R.drawable.cell_border);
        bestScoresTable.addView(nameHeader);

        TextView scoreHeader = new TextView(this);
        scoreHeader.setText("SCORE");
        scoreHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        scoreHeader.setTypeface(Typeface.DEFAULT_BOLD);
        scoreHeader.setBackgroundResource(R.drawable.cell_border);
        bestScoresTable.addView(scoreHeader);

        TextView numberOfGamesHeader = new TextView(this);
        numberOfGamesHeader.setText("GAMES");
        numberOfGamesHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        numberOfGamesHeader.setTypeface(Typeface.DEFAULT_BOLD);
        numberOfGamesHeader.setBackgroundResource(R.drawable.cell_border);
        bestScoresTable.addView(numberOfGamesHeader);

        TextView numberOfVictories = new TextView(this);
        numberOfVictories.setText("VICTORIES");
        numberOfVictories.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        numberOfVictories.setTypeface(Typeface.DEFAULT_BOLD);
        numberOfVictories.setBackgroundResource(R.drawable.cell_border);
        bestScoresTable.addView(numberOfVictories);

        TextView numberOfLosses = new TextView(this);
        numberOfLosses.setText("LOSSES");
        numberOfLosses.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        numberOfLosses.setTypeface(Typeface.DEFAULT_BOLD);
        numberOfLosses.setBackgroundResource(R.drawable.cell_border);
        bestScoresTable.addView(numberOfLosses);
    }

    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button back = (Button) findViewById(R.id.back_button_click);
        if(isFantasyTheme(this)){
            back.setBackgroundResource(R.drawable.fantasy_back);
        }else{
            back.setBackgroundResource(R.drawable.classic_back);
        }
    }

    /* if the music was stopped, which occurs only when the HOME button is pressed and the user has exited the app, restart it as soon as
    he enters the app again
    */
    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusic.restartMusic();
    }

    /* pause the music only when the user presses the built-in HOME button in his smartphone keyboard; however, the music shouldn't
       be stopped whenever this callback it's called to not hinder the fluidity of the music reproduction, such as when starting another activity
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (!backButtonPressed) {
            BackgroundMusic.pauseMusic();
        }
        backButtonPressed = false;
    }

    // this callback is invoked whenever the smartphone BACK key is pressed to exit the app
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backButtonPressed = true;
        playGameSound(this, R.raw.button_pressed);
    }

    public void backButtonClick(View view) {
        backButtonPressed = true;
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
