package it.polimi.group02.controller.background_task;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import it.polimi.group02.controller.activity.PlayActivity;

import static it.polimi.group02.controller.background_task.CreateConnection.APIKey;

/**
 * This class posts on the server the player that is playing moves.
 * After the post is done it's called the get to wait the opponent's move.
 * It extends the AsyncTask because it could not be run on the UI Thread
 */
public class PostMoveOnline extends AsyncTask<Object, Void, PlayActivity> {

    @Override
    protected PlayActivity doInBackground(Object... objects) {
        HttpURLConnection gameURLConnection = CreateConnection.playingHTTP;
        PlayActivity play = (PlayActivity) objects[0];
        String move = play.myMoveOnline;
        URL tempURL = null;
        try {
            tempURL = CreateConnection.playingURL;
            gameURLConnection = (HttpURLConnection) tempURL.openConnection();
            gameURLConnection.setRequestProperty("authorization", "APIKey " + APIKey);
            gameURLConnection.setConnectTimeout(30000);
            gameURLConnection.setRequestMethod("POST");
            gameURLConnection.setRequestProperty("Content-Type", "text/plain"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            gameURLConnection.setDoOutput(true);
            gameURLConnection.setDoInput(true);
            do {
                try {
                    OutputStreamWriter wr = new OutputStreamWriter(
                            gameURLConnection.getOutputStream());
                    wr.write(move);
                    wr.flush();
                    wr.close();
                    play.myMoveSent = true;
                }catch (ProtocolException pE){
                    pE.printStackTrace();
                }
            }while(gameURLConnection.getResponseCode()!=200);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return play;
    }

    @Override
    protected void onPostExecute(PlayActivity playActivity) {
        Object[] objects = {playActivity};
        new GetMoveOnline().execute(objects);
    }

}

