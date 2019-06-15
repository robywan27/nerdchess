package it.polimi.group02.controller.utility;


import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;


import it.polimi.group02.R;
import it.polimi.group02.model.utility.Utility;



/**
 * This class contains only static methods of interest to implement some of the settings behaviours. Moreover, there are defined some
 * static attributes of use for the app.
 */
public class PreferencesUtility {
    private static SoundPool soundPool;

    // These are not part of preferences, but are other static variables of use in the app
    public static boolean appFirstLaunch = true;
    public static boolean backButtonPressed = false;
    public static boolean forthButtonPressed = false;
    public static String savedGameConfiguration = Utility.STANDARD_GAME_CONFIGURATION;
    public static boolean repriseGameFromLatestTurn = false;
    public static String whitePlayerName = "";
    public static String blackPlayerName = "";


    // preferences utilities
    public static String VIBRATION_KEY = "vibration_key";
    public static String SOUND_KEY = "sound_key";
    public static String TOGGLE_MUSIC_KEY = "toggle_music_key";
    public static String BACKGROUND_PICTURE_KEY = "background_picture_key";
    public static String THEME_KEY = "theme_key";
    public static String BACKGROUND_MUSIC_KEY = "background_music_key";


    // vibrate or show toast
    public static void toggleVibration(Context context) {
        boolean isVibrationOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(VIBRATION_KEY, true);
        if (isVibrationOn) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
        else {
            Toast.makeText(context, "Vibration effects are disabled", Toast.LENGTH_SHORT).show();
        }
    }

    // play a sound or show toast
    public static void toggleSound(Context context) {
        boolean soundOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SOUND_KEY, true);
        if (soundOn)
            playGameSound(context, R.raw.button_pressed);
        else {
            Toast.makeText(context, "Sounds are disabled", Toast.LENGTH_SHORT).show();
        }
    }

    // play background music or stop it if playing
    public static void toggleMusic(Context context) {
        boolean musicOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TOGGLE_MUSIC_KEY, true);
        if (musicOn) {
            BackgroundMusic.switchMusicSetting(context);
            setBackgroundMusic(context);
        }
        else {
            Toast.makeText(context, "Music is disabled", Toast.LENGTH_SHORT).show();
            BackgroundMusic.stopMusic();
            BackgroundMusic.switchMusicSetting(context);
        }
    }

    // pick the chosen background image
    public static void setBackground(Context context, View view) {
        int backgroundId = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(BACKGROUND_PICTURE_KEY, "1"));
        switch(backgroundId) {
            case 1:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background1));
                break;
            case 2:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background10));
                break;
            case 3:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background11));
                break;
            case 4:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background12));
                break;
            case 5:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background2));
                break;
            case 6:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background3));
                break;
            case 7:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background4));
                break;
            default:
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.background6));
        }
    }

    /*
     * This method returns a boolean because currently there are only two choices of theme: Fantasy and Classic. If in the future other
     * themes will be added, the return type will change.
     */
    public static boolean isFantasyTheme(Context context) {
        int themeId = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(THEME_KEY, "1"));
        return (themeId == 1) ? true : false;
    }

    // vibrate
    public static void vibrate(Context context) {
        boolean isVibrationOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(VIBRATION_KEY, true);
        if (isVibrationOn) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }
    }

    // play background music
    public static void setBackgroundMusic(Context context) {
        int musicId = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(BACKGROUND_MUSIC_KEY, "1"));
        BackgroundMusic.startSong(context, musicId);
    }

    /*
     * The following methods manage the sounds in the PlayActivity by means of SoundPool class.
     */
    private static int arrow_damage_key;
    private static int arrow_shot_key;
    private static int blastwave_key;
    private static int bow_stretching_key;
    private static int button_click_key;
    private static int button_click_2_key;
    private static int button_pressed_key;
    private static int calm_horse_key;
    private static int dragon_roar_key;
    private static int dragon_wings_flapping_key;
    private static int dragonflame_key;
    private static int dying_dragon_key;
    private static int dying_giant_key;
    private static int end_game_fanfare_key;
    private static int footstep_key;
    private static int freezing_spell_key;
    private static int galloping_horse_key;
    private static int giant_footstep_key;
    private static int giant_growling_key;
    private static int giant_roar_key;
    private static int heal_revive_spell_key;
    private static int horse_whinny_key;
    private static int human_grunt_key;
    private static int large_door_slam_key;
    private static int mage_aura_key;
    private static int open_play_key;
    private static int special_cell_occupied_key;
    private static int start_game_fanfare_key;
    private static int swift_footstep_key;
    private static int sword_hit_key;
    private static int sword_sheathing_key;
    private static int teleport_spell_key;


    // initialize the soundpool
    public static void initSounds(Context context) {
        // if API level <= 21, use deprecated initialization
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        else {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(10);
            builder.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build());
            soundPool = builder.build();
        }

        loadPoolOfSounds(context);
    }

    // load sounds in the soundpool
    public static void loadPoolOfSounds(Context context) {
        button_pressed_key = soundPool.load(context, R.raw.button_pressed, 1);
        start_game_fanfare_key = soundPool.load(context, R.raw.start_game_fanfare, 1);
        arrow_damage_key = soundPool.load(context, R.raw.arrow_damage, 1);
        arrow_shot_key = soundPool.load(context, R.raw.arrow_shot, 1);
        blastwave_key = soundPool.load(context, R.raw.blastwave, 1);
        bow_stretching_key = soundPool.load(context, R.raw.bow_stretching, 1);
        button_click_key = soundPool.load(context, R.raw.button_click, 1);
        button_click_2_key = soundPool.load(context, R.raw.button_click_2, 1);
        calm_horse_key = soundPool.load(context, R.raw.calm_horse, 1);
        dragon_roar_key = soundPool.load(context, R.raw.dragon_roar, 1);
        dragon_wings_flapping_key = soundPool.load(context, R.raw.dragon_wings_flapping, 1);
        dragonflame_key = soundPool.load(context, R.raw.dragonflame, 1);
        dying_dragon_key = soundPool.load(context, R.raw.dying_dragon, 1);
        dying_giant_key = soundPool.load(context, R.raw.dying_giant, 1);
        end_game_fanfare_key = soundPool.load(context, R.raw.end_game_fanfare, 1);
        footstep_key = soundPool.load(context, R.raw.footstep, 1);
        freezing_spell_key = soundPool.load(context, R.raw.freezing_spell, 1);
        galloping_horse_key = soundPool.load(context, R.raw.galloping_horse, 1);
        giant_footstep_key = soundPool.load(context, R.raw.giant_footstep, 1);
        giant_growling_key = soundPool.load(context, R.raw.giant_growling, 1);
        giant_roar_key = soundPool.load(context, R.raw.giant_roar, 1);
        heal_revive_spell_key = soundPool.load(context, R.raw.heal_revive_spell, 1);
        horse_whinny_key = soundPool.load(context, R.raw.horse_whinny, 1);
        human_grunt_key = soundPool.load(context, R.raw.human_grunt, 1);
        large_door_slam_key = soundPool.load(context, R.raw.large_door_slam, 1);
        mage_aura_key = soundPool.load(context, R.raw.mage_aura, 1);
        open_play_key = soundPool.load(context, R.raw.open_play, 1);
        special_cell_occupied_key = soundPool.load(context, R.raw.special_cell_occupied, 1);
        swift_footstep_key = soundPool.load(context, R.raw.swift_footstep, 1);
        sword_hit_key = soundPool.load(context, R.raw.sword_hit, 1);
        sword_sheathing_key = soundPool.load(context, R.raw.sword_sheathing, 1);
        teleport_spell_key = soundPool.load(context, R.raw.teleport_spell, 1);
    }

    // play the sound corresponding to the given resource id
    public static void playGameSound(Context context, int resourceId) {
        boolean soundOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SOUND_KEY, true);
        if (soundOn) {
            if (resourceId == R.raw.arrow_damage)
                soundPool.play(arrow_damage_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.arrow_shot)
                soundPool.play(arrow_shot_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.blastwave)
                soundPool.play(blastwave_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.bow_stretching)
                soundPool.play(bow_stretching_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.button_click)
                soundPool.play(button_click_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.button_click_2)
                soundPool.play(button_click_2_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.button_pressed)
                soundPool.play(button_pressed_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.calm_horse)
                soundPool.play(calm_horse_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.dragon_roar)
                soundPool.play(dragon_roar_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.dragon_wings_flapping)
                soundPool.play(dragon_wings_flapping_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.dragonflame)
                soundPool.play(dragonflame_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.dying_dragon)
                soundPool.play(dying_dragon_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.dying_giant)
                soundPool.play(dying_giant_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.end_game_fanfare)
                soundPool.play(end_game_fanfare_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.footstep)
                soundPool.play(footstep_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.freezing_spell)
                soundPool.play(freezing_spell_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.galloping_horse)
                soundPool.play(galloping_horse_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.giant_footstep)
                soundPool.play(giant_footstep_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.giant_growling)
                soundPool.play(giant_growling_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.giant_roar)
                soundPool.play(giant_roar_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.heal_revive_spell)
                soundPool.play(heal_revive_spell_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.horse_whinny)
                soundPool.play(horse_whinny_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.large_door_slam)
                soundPool.play(large_door_slam_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.mage_aura)
                soundPool.play(mage_aura_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.open_play)
                soundPool.play(open_play_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.special_cell_occupied)
                soundPool.play(special_cell_occupied_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.start_game_fanfare)
                soundPool.play(start_game_fanfare_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.swift_footstep)
                soundPool.play(swift_footstep_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.sword_hit)
                soundPool.play(sword_hit_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.sword_sheathing)
                soundPool.play(sword_sheathing_key, 1, 1, 1, 0, 1);
            else if (resourceId == R.raw.teleport_spell)
                soundPool.play(teleport_spell_key, 1, 1, 1, 0, 1);
        }
    }

    // remember to release the soundpool when it's no more of use
    public static void releaseSoundPool() {
        soundPool.release();
        soundPool = null;
    }
}
