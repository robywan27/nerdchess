package it.polimi.group02.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import it.polimi.group02.R;
import it.polimi.group02.controller.utility.BackgroundMusic;
import it.polimi.group02.controller.utility.PreferencesUtility;


import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.forthButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;


/**
 * In this activity a player can select his name and choose the level of difficulty of the ai (easy, difficult) and whether he should
 * start the first turn or the next one
 */
public class SelectPvAIActivity extends AppCompatActivity{
    private EditText humanEditText;
    private String humanPlayerName;
    private boolean whitePlayerConfirmed;
    private boolean easy;
    private boolean hard;
    private boolean aiFirst;
    private boolean aiSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_player_vs_ai);

        // set the background image
        PreferencesUtility.setBackground(this, findViewById(R.id.activity_select_player));

        // set the correct buttons based on the theme
        setButtonsImages();

        humanEditText = (EditText) findViewById(R.id.human_edit_text);
        humanPlayerName = "";
        whitePlayerConfirmed = false;
    }

    public void confirmWhitePlayerName(View view) {
        // if the white player's name hasn't been assigned yet and its length is greater than 0
        if (!whitePlayerConfirmed && humanEditText.getText().toString().length() > 0) {
            playGameSound(this, R.raw.button_pressed);
            humanPlayerName = humanEditText.getText().toString();
            whitePlayerConfirmed = true;
            humanEditText.setEnabled(false);
            humanEditText.setClickable(false);
        }
    }

    public void setAISecond(View view){
        playGameSound(this, R.raw.button_pressed);
        aiSecond = true;
        aiFirst = false;
        Button button = (Button) findViewById(R.id.setAIfirst);
        button.setVisibility(View.INVISIBLE);
    }

    public void setAIFirst(View view){
        playGameSound(this, R.raw.button_pressed);
        aiFirst = true;
        aiSecond = false;
        Button button = (Button) findViewById(R.id.setAIsecond);
        button.setVisibility(View.INVISIBLE);
    }

    public void setAIHard(View view){
        playGameSound(this, R.raw.button_pressed);
        easy = false;
        hard = true;
        Button button = (Button) findViewById(R.id.setAIEasy);
        button.setVisibility(View.INVISIBLE);
    }

    public void setAIEasy(View view){
        playGameSound(this, R.raw.button_pressed);
        easy = true;
        hard = false;
        Button button = (Button) findViewById(R.id.setAIHard);
        button.setVisibility(View.INVISIBLE);
    }

    public void playButtonClick(View view) {
        forthButtonPressed = true;
        // if both players haven't inserted their names, they will play anonymously; otherwise, they both must have confirmed their names
        if ((humanEditText.getText().toString().length() == 0 && ((easy == true || hard == true ) &&(aiFirst==true || aiSecond == true))
                || (whitePlayerConfirmed && easy == true || hard == true ) &&(aiFirst==true || aiSecond == true))) {
            playGameSound(this, R.raw.open_play);
            Intent intent = new Intent(this, PlayActivity.class);
            if(aiSecond) {
                PlayActivity.firstAI = false;
                intent.putExtra("WHITE_PLAYER", humanPlayerName);
                if(easy) {
                    intent.putExtra("BLACK_PLAYER","EASY_AI");
                }
                if(hard){
                    intent.putExtra("BLACK_PLAYER","HARD_AI");
                }
            }
            if(aiFirst) {
                PlayActivity.firstAI = true;
                intent.putExtra("BLACK_PLAYER", humanPlayerName);
                if (easy) {
                    intent.putExtra("WHITE_PLAYER", "EASY_AI");
                }
                if (hard) {
                    intent.putExtra("WHITE_PLAYER", "HARD_AI");
                }
            }
            PlayActivity.playVersuAI = true;
            PlayActivity.playOnline = false;
            startActivity(intent);
            finish();
        }
    }

    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button back = (Button) findViewById(R.id.back_button_click);
        Button play = (Button) findViewById(R.id.play_button_click);
        Button confirm = (Button) findViewById(R.id.white_player_confirm_button);
        Button easy = (Button) findViewById(R.id.setAIEasy);
        Button hard = (Button) findViewById(R.id.setAIHard);
        Button first = (Button) findViewById(R.id.setAIfirst);
        Button second = (Button) findViewById(R.id.setAIsecond);

        if(isFantasyTheme(this)){
            back.setBackgroundResource(R.drawable.fantasy_back);
            play.setBackgroundResource(R.drawable.fantasy_play);
            confirm.setBackgroundResource(R.drawable.fantasy_confirm);
            easy.setBackgroundResource(R.drawable.fantasy_easy);
            hard.setBackgroundResource(R.drawable.fantasy_hard);
            first.setBackgroundResource(R.drawable.fantasy_start_ai);
            second.setBackgroundResource(R.drawable.fantasy_second_ai);

        }else{
            back.setBackgroundResource(R.drawable.classic_back);
            play.setBackgroundResource(R.drawable.classic_play2);
            confirm.setBackgroundResource(R.drawable.classic_confirm);
            easy.setBackgroundResource(R.drawable.classic_easy_ai);
            hard.setBackgroundResource(R.drawable.classic_hard_ai);
            first.setBackgroundResource(R.drawable.classic_start_ai);
            second.setBackgroundResource(R.drawable.classic_second_ai);
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
