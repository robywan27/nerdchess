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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import it.polimi.group02.R;
import it.polimi.group02.controller.database.StatisticsDatabaseImplementation;
import it.polimi.group02.controller.utility.BackgroundMusic;
import it.polimi.group02.controller.utility.PreferencesUtility;
import it.polimi.group02.model.statistics.GameStatistics;


import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;



/**
 * This activity shows all the latest top-k finished games played. The information displayed for each player is:
 *  - Date of start game
 *  - White player name
 *  - Black player name
 *  - Winner color
 *  - Number of pieces at the end of the game
 *  - Number of turns played
 *  - Yes/No value if three special cells have been occupied for the victory or not
 */
public class GameHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game_history);

        // set the background image
        PreferencesUtility.setBackground(this, findViewById(R.id.activity_game_history));

        // set the correct buttons based on the theme
        setButtonsImages();


        // create a table displaying all games' data
        GridLayout historyTable = (GridLayout) findViewById(R.id.history_table);

        // create the headers for this table
        createHeaderRow(historyTable);

        StatisticsDatabaseImplementation dbImplementation = new StatisticsDatabaseImplementation(this);
        ArrayList<GameStatistics> gameStatistics = (ArrayList<GameStatistics>) dbImplementation.retrieveLatestGames();

        // create iteratively an entry for each distinct game
        for (GameStatistics gs : gameStatistics) {
            TextView dateEntry = new TextView(this);
            dateEntry.setText(gs.getDateFormat());
            dateEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            dateEntry.setBackgroundResource(R.drawable.cell_border);
            historyTable.addView(dateEntry);

            TextView whitePlayerEntry = new TextView(this);
            whitePlayerEntry.setText(gs.getWhitePlayer());
            whitePlayerEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            whitePlayerEntry.setBackgroundResource(R.drawable.cell_border);
            historyTable.addView(whitePlayerEntry);

            TextView blackPlayerEntry = new TextView(this);
            blackPlayerEntry.setText(gs.getBlackPlayer());
            blackPlayerEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            blackPlayerEntry.setBackgroundResource(R.drawable.cell_border);
            historyTable.addView(blackPlayerEntry);

            TextView winnerEntry = new TextView(this);
            winnerEntry.setText(gs.getWinner());
            winnerEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            winnerEntry.setBackgroundResource(R.drawable.cell_border);
            historyTable.addView(winnerEntry);

            TextView numberOfPiecesEntry = new TextView(this);
            numberOfPiecesEntry.setText(String.valueOf(gs.getNumberOfPiecesLeft()));
            numberOfPiecesEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            numberOfPiecesEntry.setBackgroundResource(R.drawable.cell_border);
            historyTable.addView(numberOfPiecesEntry);

            TextView numberOfTurnsEntry = new TextView(this);
            numberOfTurnsEntry.setText(String.valueOf(gs.getNumberOfTurns()));
            numberOfTurnsEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            numberOfTurnsEntry.setBackgroundResource(R.drawable.cell_border);
            historyTable.addView(numberOfTurnsEntry);

            TextView threeSpecialCellsEntry = new TextView(this);
            threeSpecialCellsEntry.setText(gs.getOccupiedThreeSpecialCells());
            threeSpecialCellsEntry.setTextColor(ContextCompat.getColor(this, R.color.colorLightGreen));
            threeSpecialCellsEntry.setBackgroundResource(R.drawable.cell_border);
            historyTable.addView(threeSpecialCellsEntry);
        }
    }

    // create a header for each element of a game's profile
    private void createHeaderRow(GridLayout historyTable) {
        TextView dateHeader = new TextView(this);
        dateHeader.setText("DATE");
        dateHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        dateHeader.setTypeface(Typeface.DEFAULT_BOLD);
        dateHeader.setBackgroundResource(R.drawable.cell_border);
        historyTable.addView(dateHeader);

        TextView whitePlayerHeader = new TextView(this);
        whitePlayerHeader.setText("WHITE");
        whitePlayerHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        whitePlayerHeader.setTypeface(Typeface.DEFAULT_BOLD);
        whitePlayerHeader.setBackgroundResource(R.drawable.cell_border);
        historyTable.addView(whitePlayerHeader);

        TextView blackPlayerHeader = new TextView(this);
        blackPlayerHeader.setText("BLACK");
        blackPlayerHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        blackPlayerHeader.setTypeface(Typeface.DEFAULT_BOLD);
        blackPlayerHeader.setBackgroundResource(R.drawable.cell_border);
        historyTable.addView(blackPlayerHeader);

        TextView winnerHeader = new TextView(this);
        winnerHeader.setText("WINNER");
        winnerHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        winnerHeader.setTypeface(Typeface.DEFAULT_BOLD);
        winnerHeader.setBackgroundResource(R.drawable.cell_border);
        historyTable.addView(winnerHeader);

        TextView numberOfPiecesHeader = new TextView(this);
        numberOfPiecesHeader.setText("PIECES");
        numberOfPiecesHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        numberOfPiecesHeader.setTypeface(Typeface.DEFAULT_BOLD);
        numberOfPiecesHeader.setBackgroundResource(R.drawable.cell_border);
        historyTable.addView(numberOfPiecesHeader);

        TextView numberOfTurnsHeader = new TextView(this);
        numberOfTurnsHeader.setText("TURNS");
        numberOfTurnsHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        numberOfTurnsHeader.setTypeface(Typeface.DEFAULT_BOLD);
        numberOfTurnsHeader.setBackgroundResource(R.drawable.cell_border);
        historyTable.addView(numberOfTurnsHeader);

        TextView threeSpecialCellsHeader = new TextView(this);
        threeSpecialCellsHeader.setText("SPECIAL CELLS");
        threeSpecialCellsHeader.setTextColor(ContextCompat.getColor(this, R.color.colorLimeYellow));
        threeSpecialCellsHeader.setTypeface(Typeface.DEFAULT_BOLD);
        threeSpecialCellsHeader.setBackgroundResource(R.drawable.cell_border);
        historyTable.addView(threeSpecialCellsHeader);
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
