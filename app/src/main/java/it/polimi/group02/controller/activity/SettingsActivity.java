package it.polimi.group02.controller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import it.polimi.group02.R;
import it.polimi.group02.controller.utility.BackgroundMusic;


import static it.polimi.group02.controller.utility.PreferencesUtility.BACKGROUND_MUSIC_KEY;
import static it.polimi.group02.controller.utility.PreferencesUtility.BACKGROUND_PICTURE_KEY;
import static it.polimi.group02.controller.utility.PreferencesUtility.SOUND_KEY;
import static it.polimi.group02.controller.utility.PreferencesUtility.THEME_KEY;
import static it.polimi.group02.controller.utility.PreferencesUtility.TOGGLE_MUSIC_KEY;
import static it.polimi.group02.controller.utility.PreferencesUtility.VIBRATION_KEY;
import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;
import static it.polimi.group02.controller.utility.PreferencesUtility.setBackground;
import static it.polimi.group02.controller.utility.PreferencesUtility.setBackgroundMusic;
import static it.polimi.group02.controller.utility.PreferencesUtility.toggleMusic;
import static it.polimi.group02.controller.utility.PreferencesUtility.toggleSound;
import static it.polimi.group02.controller.utility.PreferencesUtility.toggleVibration;



/**
 * This activity allows to display some settings which the user can toggle and fiddle to better customize the game on his taste.
 * The available settings are the following:
 *  - Vibration on/off
 *  - Sound on/off
 *  - Background music on/off
 *  - List of background pictures to select
 *  - List of themes to select (now available only two: fantasy and classic)
 *  - List of background music to play
 */
public class SettingsActivity extends AppCompatActivity {
    private SettingsFragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        settingsFragment = new SettingsFragment();
        settingsFragment.setSharedPreferenceListener(getOnSharedPreferenceChangedListener());

        getFragmentManager().beginTransaction().replace(R.id.activity_settings, settingsFragment).commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.activity_settings);

        // set the background image
        setBackground(this, findViewById(R.id.activity_settings));

        // set the correct buttons based on the theme
        setButtonsImages();
    }

    /* if the music was stopped, which occurs only when the HOME button is pressed and the user has exited the app, restart it as soon as
    he enters the app again
    */
    @Override
    protected void onResume() {
        super.onResume();
        BackgroundMusic.restartMusic();

        //register listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(settingsFragment.getSharedPreferenceListener());
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

        //unregister listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(settingsFragment.getSharedPreferenceListener());
    }

    // this method implements a listener which registers to a change of any setting
    public SharedPreferences.OnSharedPreferenceChangeListener getOnSharedPreferenceChangedListener() {
        final SettingsActivity settingsActivity = this;
        final SettingsFragment fragment = this.settingsFragment;

        return new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                // as soon as one of any setting has been modified, perform one of the following actions
                try {
                    // vibrate or show a toast
                    if (key.equals(VIBRATION_KEY)) {
                        toggleVibration(getApplicationContext());
                    }
                    // play a sound or show a toast
                    if (key.equals(SOUND_KEY)) {
                        toggleSound(getApplicationContext());
                    }
                    // play the background music or show a toast
                    if (key.equals(TOGGLE_MUSIC_KEY)) {
                        toggleMusic(getApplicationContext());
                    }
                    // change background image
                    if (key.equals(BACKGROUND_PICTURE_KEY)) {
                        setBackground(settingsActivity, findViewById(R.id.activity_settings));
                        fragment.findPreference(key).setSummary(((ListPreference) prefs).getEntry());
                    }
                    // set the theme of the back button
                    if (key.equals(THEME_KEY)) {
                        setButtonsImages();
                        fragment.findPreference(key).setSummary(((ListPreference) prefs).getEntry());
                    }
                    // stop the currently playing music and start the selected one
                    if (key.equals(BACKGROUND_MUSIC_KEY)) {
                        BackgroundMusic.stopMusic();
                        setBackgroundMusic(settingsActivity);
                        fragment.findPreference(key).setSummary(((ListPreference) prefs).getEntry());
                    }
                }
                catch (Exception e) {
                    Log.e("exception on settings", e.getMessage());
                }
            }
        };
    }


    /*
    This fragment shows a hierarchy of Preference objects as lists. The activity hosting this fragment doesn't extend a PreferenceActivity
    class, while instead defines a fragment extending a PreferenceFragment since it is the recommended solution starting from Android 3.0.
     */
    public static class SettingsFragment extends PreferenceFragment {
        private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceListener;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // get the selected entry in a ListPreference type of class
            findPreference(BACKGROUND_PICTURE_KEY).setSummary(((ListPreference) findPreference(BACKGROUND_PICTURE_KEY)).getEntry());
            findPreference(THEME_KEY).setSummary(((ListPreference) findPreference(THEME_KEY)).getEntry());
            findPreference(BACKGROUND_MUSIC_KEY).setSummary(((ListPreference) findPreference(BACKGROUND_MUSIC_KEY)).getEntry());
        }

        public void setSharedPreferenceListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
            this.sharedPreferenceListener = listener;
        }

        public SharedPreferences.OnSharedPreferenceChangeListener getSharedPreferenceListener() {
            return sharedPreferenceListener;
        }
    }


    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button back = (Button) findViewById(R.id.back_button_click);
        Log.i("settings", Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString(THEME_KEY, "1")) + "");
        if(isFantasyTheme(this)){
            back.setBackgroundResource(R.drawable.fantasy_back);
        }else{
            back.setBackgroundResource(R.drawable.classic_back);
        }
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
