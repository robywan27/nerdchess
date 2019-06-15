package it.polimi.group02.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.polimi.group02.R;
import it.polimi.group02.controller.utility.BackgroundMusic;
import it.polimi.group02.controller.utility.PreferencesUtility;


import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.forthButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;
import static it.polimi.group02.controller.utility.PreferencesUtility.vibrate;



/**
 * The purpose of this activity is that of getting the names of the players about to play a match. There are two possibilities allowed:
 *  - no player inserts his name and directly plays the game
 *  - both players submit their names and then start a match
 */
public class SelectPvPActivity extends AppCompatActivity {
    private EditText whiteEditText;
    private EditText blackEditText;
    private String whitePlayerName;
    private String blackPlayerName;
    private boolean whitePlayerConfirmed;
    private boolean blackPlayerConfirmed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player_vs_player);

        // set the background image
        PreferencesUtility.setBackground(this, findViewById(R.id.activity_select_player));

        // set the correct buttons based on the theme
        setButtonsImages();

        whiteEditText = (EditText) findViewById(R.id.white_player_edit_text);
        blackEditText = (EditText) findViewById(R.id.black_player_edit_text);
        whitePlayerName = "";
        blackPlayerName = "";
        whitePlayerConfirmed = false;
        blackPlayerConfirmed = false;
    }

    public void confirmWhitePlayerName(View view) {
        // if the selected name is equals to the black player
        if (whiteEditText.getText().toString().equals(blackPlayerName)) {
            vibrate(this);
            Toast toast = Toast.makeText(this, "You must choose different names", Toast.LENGTH_SHORT);
            toast.show();
        }
        // if the white player's name hasn't been assigned yet and its length is greater than 0
        else if (!whitePlayerConfirmed && whiteEditText.getText().toString().length() > 0) {
            playGameSound(this, R.raw.button_pressed);
            whitePlayerName = whiteEditText.getText().toString();
            whitePlayerConfirmed = true;
            whiteEditText.setEnabled(false);
            whiteEditText.setClickable(false);
        }
    }

    public void confirmBlackPlayerName(View view) {
        // if the selected name is equals to the white player
        if (blackEditText.getText().toString().equals(whitePlayerName)) {
            vibrate(this);
            Toast toast = Toast.makeText(this, "You must choose different names", Toast.LENGTH_SHORT);
            toast.show();
        }
        // if the black player's name hasn't been assigned yet and its length is greater than 0
        else if (!blackPlayerConfirmed && blackEditText.getText().toString().length() > 0) {
            playGameSound(this, R.raw.button_pressed);
            blackPlayerName = blackEditText.getText().toString();
            blackPlayerConfirmed = true;
            blackEditText.setEnabled(false);
            blackEditText.setClickable(false);
        }
    }

    public void playButtonClick(View view) {
        forthButtonPressed = true;
        // if both players haven't inserted their names, they will play anonymously; otherwise, they both must have confirmed their names
        if ((whiteEditText.getText().toString().length() == 0 && blackEditText.getText().toString().length() == 0) || (whitePlayerConfirmed && blackPlayerConfirmed)) {
            playGameSound(this, R.raw.open_play);
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("WHITE_PLAYER", whitePlayerName);
            intent.putExtra("BLACK_PLAYER", blackPlayerName);
            startActivity(intent);
            finish();
            PlayActivity.playVersuAI = false;
            PlayActivity.playOnline = false;
        }
    }

    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button back = (Button) findViewById(R.id.back_button_click);
        Button play = (Button) findViewById(R.id.play_button_click);
        Button confirm1 = (Button) findViewById(R.id.white_player_confirm_button);
        Button confirm2 = (Button) findViewById(R.id.black_player_confirm_button);

        if(isFantasyTheme(this)){
            back.setBackgroundResource(R.drawable.fantasy_back);
            play.setBackgroundResource(R.drawable.fantasy_play);
            confirm1.setBackgroundResource(R.drawable.fantasy_confirm);
            confirm2.setBackgroundResource(R.drawable.fantasy_confirm);

        }else{
            back.setBackgroundResource(R.drawable.classic_back);
            play.setBackgroundResource(R.drawable.classic_play2);
            confirm1.setBackgroundResource(R.drawable.classic_confirm);
            confirm2.setBackgroundResource(R.drawable.classic_confirm);
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
        playGameSound(this, R.raw.button_pressed);
    }

    public void backButtonClick(View view) {
        backButtonPressed = true;
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, OnlineLocalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
