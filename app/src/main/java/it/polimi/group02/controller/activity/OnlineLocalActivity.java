package it.polimi.group02.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import it.polimi.group02.R;
import it.polimi.group02.controller.utility.BackgroundMusic;
import it.polimi.group02.controller.utility.PreferencesUtility;


import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.forthButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;


/**
 * This activity displys three buttons, each leading one to a game configuration, which can be one of the following:
 *  - Local game: two human players play on the same device
 *  - Online game: two human players play on each device connected to the local network
 *  - Ai game: one human player plays against the device ai
 */
public class OnlineLocalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_online_local);

        // set the background image
        PreferencesUtility.setBackground(this, findViewById(R.id.activity_online_local));

        // set the correct buttons based on the theme
        setButtonsImages();

        setStaticAttributes();
    }

    public void backButtonClick(View view) {
        backButtonPressed = true;
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void localButtonOnClick(View view) {
        forthButtonPressed = true;
        PlayActivity.playOnline = false;
        PlayActivity.playVersuAI = false;
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, SelectPvPActivity.class);
        startActivity(intent);
        finish();
    }

    public void aiButtonOnClick(View view) {
        forthButtonPressed = true;
        PlayActivity.playVersuAI = true;
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, SelectPvAIActivity.class);
        startActivity(intent);
        finish();
    }

    public void onlineButtonOnClick(View view) {
        forthButtonPressed = true;
        PlayActivity.playOnline = true;
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, SelectOnlineActivity.class);
        startActivity(intent);
        finish();
    }

    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button back = (Button) findViewById(R.id.back_button_click);
        Button playOnline = (Button) findViewById(R.id.playOnlyne);
        Button playLocal = (Button) findViewById(R.id.playLocal);
        Button playAi = (Button) findViewById(R.id.playVsAI);
        if(isFantasyTheme(this)){
            back.setBackgroundResource(R.drawable.fantasy_back);
            playLocal.setBackgroundResource(R.drawable.fantasy_play_local);
            playOnline.setBackgroundResource(R.drawable.fantasy_play_online);
            playAi.setBackgroundResource(R.drawable.fantasy_play_ai);
        }else{
            back.setBackgroundResource(R.drawable.classic_back);
            playLocal.setBackgroundResource(R.drawable.classic_play_local);
            playOnline.setBackgroundResource(R.drawable.classic_play_online);
            playAi.setBackgroundResource(R.drawable.classic_play_ai);
        }
    }

    // This method sets to false/null all the static values needed in the next activities
    private void setStaticAttributes() {
        SelectOnlineActivity.oppoentCnnected = false;
        SelectOnlineActivity.backPressedInSelectOnline = false;
        PlayActivity.playVersuAI = false;
        PlayActivity.playOnline = false;
        PlayActivity.gameID = "";
        PlayActivity.stringUrl = "";
        PlayActivity.iAmFirst = false;
        PlayActivity.iAmSecond = false;
        PlayActivity.myTurn = false;
        PlayActivity.opponentTurn = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setStaticAttributes();
    }

    /* if the music was stopped, which occurs only when the HOME button is pressed and the user has exited the app, restart it as soon as
    he enters the app again
    */
    @Override
    protected void onResume() {
        super.onResume();

        setStaticAttributes();
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
        backButtonPressed = true;
        super.onBackPressed();
        playGameSound(this, R.raw.button_pressed);
    }

}
