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
import it.polimi.group02.controller.activity.SelectOnlineActivity;

/**
 * This class creates the first connection of the game.
 * It extends the AsyncTask because it could not be run on the UI Thread
 */
public class CreateConnection extends AsyncTask<Object, Void, Void> {

    private final String GAME_URL = "http://mobileapp16.bernaschina.com/api/room/group02";
    protected static final String APIKey = "48421a89-2dc2-46a0-b624-5305d332c926";
    protected static String playingStringURL;
    protected static URL playingURL;
    protected static HttpURLConnection playingHTTP;
    protected static HttpURLConnection myURLConnection;
    protected static InputStream inStream;

    @Override
    protected Void doInBackground(Object... objects) {
        while (SelectOnlineActivity.oppoentCnnected==false) {
            URL connectionURL = null;
            int timeOut = 35000;//long timeout to make possible more games+extra 5 second for the service
            try {
                connectionURL = new URL(GAME_URL);
                if (myURLConnection != null) {
                    myURLConnection.disconnect();
                    myURLConnection = null;
                }
                myURLConnection = (HttpURLConnection) connectionURL.openConnection();
                myURLConnection.setRequestProperty("authorization", "APIKey " + APIKey);
                myURLConnection.setRequestMethod("GET");
                myURLConnection.setDoInput(true);
                myURLConnection.setConnectTimeout(timeOut);
                myURLConnection.connect();
                final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                service.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        myURLConnection.disconnect();
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 30, 30, TimeUnit.SECONDS);
                try {
                    inStream = myURLConnection.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                    String temp, response = "";
                    System.out.println("Before while create");
                    while ((temp = bReader.readLine()) != null) {//408 = timeout
                        System.out.println(myURLConnection.getResponseCode());
                        response += temp;
                    }
                    bReader.close();
                    inStream.close();
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                    SelectOnlineActivity play = (SelectOnlineActivity) objects[0];
                    String color = (String) jsonObject.get("color");
                    if (color.equals("white")) {
                        PlayActivity.myTurn = true;
                        PlayActivity.iAmFirst = true;
                        PlayActivity.opponentTurn = false;
                    }
                    if (color.equals("black")) {
                        PlayActivity.myTurn = false;
                        PlayActivity.opponentTurn = true;
                        PlayActivity.iAmSecond = true;
                    }
                    PlayActivity.gameID = (String) jsonObject.get("game");
                    playingStringURL = (String) jsonObject.get("url");
                    playingURL = new URL(playingStringURL);
                    playingHTTP = (HttpURLConnection) playingURL.openConnection();
                    SelectOnlineActivity.oppoentCnnected = true;
                    myURLConnection.disconnect();
                }catch (SocketException socketException){
                    System.out.println("No one connected");
                }
                service.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}