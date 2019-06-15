package it.polimi.group02.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.polimi.group02.R;
import it.polimi.group02.controller.background_task.CreateConnection;
import it.polimi.group02.controller.background_task.DeleteConnection;
import it.polimi.group02.controller.utility.BackgroundMusic;


import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.forthButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;


/**
 * In this activity a player can select his name and then wait until a remote player connects himself and then start a game.
 */
public class SelectOnlineActivity extends AppCompatActivity {
    private EditText playerEditText;
    private String onlinePlayer;
    private boolean playerConfirmed;
    public static boolean oppoentCnnected;
    protected static boolean backPressedInSelectOnline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_select_online);

        // set the correct buttons based on the theme
        setButtonsImages();

        Button play = (Button) findViewById(R.id.play_button_click);
        play.setVisibility(View.INVISIBLE);
        playerEditText = (EditText) findViewById(R.id.white_player_edit_text);
        onlinePlayer = "";
        playerConfirmed = false;
        backPressedInSelectOnline = false;

        SelectOnlineActivity[] selectOnlineActivity = {this};
        new CreateConnection().execute(selectOnlineActivity);
        Toast.makeText(this, "Waiting Opponent", Toast.LENGTH_SHORT).show();
        final ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        /*This schedules a runnable task every 2 seconds*/
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!oppoentCnnected && !backPressedInSelectOnline) {
                            Toast.makeText(getApplicationContext(), "Waiting Opponent", Toast.LENGTH_SHORT).show();
                            if(!PlayActivity.playOnline){
                                scheduleTaskExecutor.shutdown();
                                //connectTask.cancel(true);
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Opponent connected, you can classic_play now", Toast.LENGTH_SHORT).show();
                            Button play = (Button) findViewById(R.id.play_button_click);
                            play.setVisibility(View.VISIBLE);
                            scheduleTaskExecutor.shutdown();
                        }
                    }

                });
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    public void confirmWhitePlayerName(View view) {
        // if the white player's name hasn't been assigned yet and its length is greater than 0
        if (!playerConfirmed && playerEditText.getText().toString().length() > 0) {
            playGameSound(this, R.raw.button_pressed);
            onlinePlayer = playerEditText.getText().toString();
            playerConfirmed = true;
            playerEditText.setEnabled(false);
            playerEditText.setClickable(false);
        }
    }

    public void playButtonClick(View view) {
        forthButtonPressed = true;
        // if both players haven't inserted their names, they will play anonymously; otherwise, they both must have confirmed their names
        playGameSound(this, R.raw.open_play);
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("WHITE_PLAYER", "ONLINE"+onlinePlayer);
        intent.putExtra("BLACK_PLAYER", "");//if not the playActivity crashes
        startActivity(intent);
        PlayActivity.playVersuAI = false;
        PlayActivity.playOnline = true;
        finish();
    }

    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button back = (Button) findViewById(R.id.back_button_click);
        Button play = (Button) findViewById(R.id.play_button_click);
        Button confirm = (Button) findViewById(R.id.white_player_confirm_button);

        if(isFantasyTheme(this)){
            back.setBackgroundResource(R.drawable.fantasy_back);
            play.setBackgroundResource(R.drawable.fantasy_play);
            confirm.setBackgroundResource(R.drawable.fantasy_confirm);

        }else{
            back.setBackgroundResource(R.drawable.classic_back);
            play.setBackgroundResource(R.drawable.classic_play2);
            confirm.setBackgroundResource(R.drawable.classic_confirm);
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
        new DeleteConnection().execute("aborted");
        startActivity(intent);
        finish();
        backPressedInSelectOnline = true;
    }

}