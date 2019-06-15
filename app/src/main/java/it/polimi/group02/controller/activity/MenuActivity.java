package it.polimi.group02.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import it.polimi.group02.R;

import it.polimi.group02.controller.activity.help.TutorialActivity;
import it.polimi.group02.controller.utility.BackgroundMusic;
import it.polimi.group02.controller.utility.PreferencesUtility;


import static it.polimi.group02.controller.utility.PreferencesUtility.appFirstLaunch;
import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.forthButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.initSounds;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;
import static it.polimi.group02.controller.utility.PreferencesUtility.releaseSoundPool;



/**
 * This class is the launcher activity, that is, the activity which is launched at application startup. Its purpose is that of a
 * "gateway", it allows one to move to other activities:
 *  - Play activity: where one can select the type of game to play
 *  - Settings activity: where one can toggle the settings
 *  - Games history activity: where one can see the last games played
 *  - Best scores activity: where one can see the players ranked for highest points
 *  - Help activity: where one can read the tutorial explaining how to play a game in this app and the rules of the game
 */
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_menu);

        // set the background image
        PreferencesUtility.setBackground(this, findViewById(R.id.activity_menu));

        // set the correct buttons based on the theme
        setButtonsImages();

        // execute this code only when app is launched to avoid restart music every time you go back to menu
        if (appFirstLaunch) {
            // initialize sounds
            initSounds(this);
            // start the background music
            PreferencesUtility.setBackgroundMusic(this);
            appFirstLaunch = false;
        }
    }

    public void playButtonClick(View view) {
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, OnlineLocalActivity.class);
        startActivity(intent);
        forthButtonPressed = true;
    }

    public void bestScoresButtonClick(View view) {
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, BestScoresActivity.class);
        startActivity(intent);
        forthButtonPressed = true;
    }

    public void gameHistoryButtonClick(View view) {
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, GameHistoryActivity.class);
        startActivity(intent);
        forthButtonPressed = true;
    }

    public void helpButtonClick(View view) {
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
        forthButtonPressed = true;
    }

    public void settingsButtonClick(View view) {
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        forthButtonPressed = true;
    }

    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button play = (Button) findViewById(R.id.buttonPlay);
        Button help = (Button) findViewById(R.id.help);
        Button history = (Button) findViewById(R.id.history);
        Button bestScores = (Button) findViewById(R.id.bestScores);

        if(isFantasyTheme(this)){
            play.setBackgroundResource(R.drawable.inca_play);
            help.setBackgroundResource(R.drawable.inca_help);
            history.setBackgroundResource(R.drawable.inca_history);
            bestScores.setBackgroundResource(R.drawable.inca_best_scores);
        }else{
            play.setBackgroundResource(R.drawable.classic_play);
            help.setBackgroundResource(R.drawable.classic_help);
            history.setBackgroundResource(R.drawable.classic_history);
            bestScores.setBackgroundResource(R.drawable.classic_score);
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

        if (!backButtonPressed && !forthButtonPressed) {
            BackgroundMusic.pauseMusic();
        }
        backButtonPressed = false;
        forthButtonPressed = false;
    }

    // this callback is invoked whenever the smartphone BACK key is pressed to exit the app
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backButtonPressed = true;
        // stop the music when exiting the app
        BackgroundMusic.stopMusic();
        //playGameSound(this, R.raw.large_door_slam);   NEVER PLAYED BECAUSE THE SOUNDPOOL IS RELEASED TOO EARLY
        Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
        releaseSoundPool();
        appFirstLaunch = true;
        finish();
    }

}
