package it.polimi.group02.controller.utility;


import android.content.Context;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import it.polimi.group02.R;

import static it.polimi.group02.controller.utility.PreferencesUtility.TOGGLE_MUSIC_KEY;


/**
 * This class exposes the methods to start, restart, pause, stop and select a music to play.
 */
public class BackgroundMusic {
    private static MediaPlayer mediaPlayer;
    private static boolean musicOn;

    public static void startSong(Context context, int musicId) {
        // if the radio music setting is set to on, play the music
        musicOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TOGGLE_MUSIC_KEY, true);
        if (musicOn) {
            mediaPlayer = MediaPlayer.create(context, selectTuneFromId(musicId));
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(100, 100);
            mediaPlayer.start();
            mediaPlayer.setScreenOnWhilePlaying(true);
        }
    }

    public static int selectTuneFromId(int musicId) {
        switch(musicId) {
            case 2:
                return R.raw.age_of_empires_2_tazer;
            case 3:
                return R.raw.medieval_music_cobblestone_village;
            case 4:
                return R.raw.medieval_tavern_music_black_wolf_inn;
            case 5:
                return R.raw.medieval_music_wild_boar_inn;
            case 6:
                return R.raw.medieval_2_total_war_song_for_toomba;
            default:
                return R.raw.age_of_empires_2_menu_music;
        }
    }

    public static void pauseMusic() {
        if (musicOn)
         mediaPlayer.pause();
    }

    public static void restartMusic() {
        if (musicOn)
            mediaPlayer.start();
    }

    public static void stopMusic() {
        if (musicOn) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void switchMusicSetting(Context context) {
        musicOn = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(TOGGLE_MUSIC_KEY, true);
    }
}
