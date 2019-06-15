package it.polimi.group02.controller.background_task;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.polimi.group02.controller.activity.PlayActivity;

import static it.polimi.group02.controller.background_task.CreateConnection.APIKey;

/**
 * This class gets the opponents moves.
 * It extends the AsyncTask because it could not be run on the UI Thread
 */
public class GetMoveOnline extends AsyncTask<Object, Void, String> {

    private static int counterOfWaits;
    private static HttpURLConnection gameURLConnection;
    private static InputStream inStream;

    @Override
    protected String doInBackground(Object... objects) {
        counterOfWaits = 2;
        gameURLConnection = CreateConnection.playingHTTP;
        PlayActivity play = (PlayActivity) objects[0];
        JSONObject jsonObject = null;
        URL tempURL = null;
        do {
            try {
                counterOfWaits--;
                tempURL = CreateConnection.playingURL;
                gameURLConnection = (HttpURLConnection) tempURL.openConnection();
                gameURLConnection.setConnectTimeout(45000);//+5 extra seconds to avoid conflicts with the executor
                gameURLConnection.setRequestProperty("authorization", "APIKey " + APIKey);
                gameURLConnection.setRequestMethod("GET");
                gameURLConnection.setDoInput(true);
                gameURLConnection.connect();
                final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                service.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        gameURLConnection.disconnect();
                        try {
                            inStream.close();
                            service.shutdown();
                        } catch (IOException e) {
                            service.shutdown();
                            e.printStackTrace();
                        }
                    }
                }, 45, 45, TimeUnit.SECONDS);
                try {
                    int responseCode = gameURLConnection.getResponseCode();
                    while (responseCode != 200 && responseCode != 410) {
                        responseCode = gameURLConnection.getResponseCode();
                    }
                    if (responseCode == 200 || responseCode == 410) {
                        if (responseCode == 200) {
                            inStream = gameURLConnection.getInputStream();
                            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                            String temp, response = "";
                            while ((temp = bReader.readLine()) != null) {
                                response += temp;
                            }
                            bReader.close();
                            inStream.close();
                            jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                            play.onlineOpponentMove = jsonObject.get("move").toString();
                            play.onlineOpponentPlayed = true;
                            play.myMoveSent = false;
                            play.opponentTurn = false;
                            play.myTurn = true;
                            return null;
                        }
                        if (responseCode == 410) {
                            play.onlineOpponentPlayed = false;
                            play.opponentTurn = true;
                            play.myTurn = false;
                            play.gameEnded = true;
                            return null;
                        }
                        gameURLConnection.disconnect();
                        return null;
                    }
                }catch (SocketException e){
                    if(counterOfWaits==0){
                        play.gameEnded = true;
                        play.iWonForOpponentResign = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }while (counterOfWaits>0);
        if(counterOfWaits==0){
            play.gameEnded = true;
            play.iWonForOpponentResign = true;
        }
        return null;
    }



}

