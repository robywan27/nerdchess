package it.polimi.group02.controller.background_task;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import static it.polimi.group02.controller.background_task.CreateConnection.APIKey;

/**
 * This class stops the connection when the game ends or when some of the players does not connect.
 * It extends the AsyncTask because it could not be run on the UI Thread
 */
public class DeleteConnection extends AsyncTask<Object, Void, Void> {

    @Override
    protected Void doInBackground(Object... objects) {
        String deleteMessage = (String)objects[0];
        HttpURLConnection gameURLConnection = it.polimi.group02.controller.background_task.CreateConnection.playingHTTP;
        URL tempURL = null;
        try {
            if (CreateConnection.playingURL != null) {
                CreateConnection.myURLConnection.disconnect();
                CreateConnection.playingHTTP.disconnect();
                CreateConnection.inStream.close();
                tempURL = CreateConnection.playingURL;
                gameURLConnection = (HttpURLConnection) tempURL.openConnection();
                gameURLConnection.setRequestProperty("authorization", "APIKey " + APIKey);
                gameURLConnection.setConnectTimeout(30000);
                gameURLConnection.setRequestMethod("DELETE");
                gameURLConnection.setRequestProperty("Content-Type", "text/plain"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                gameURLConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(
                        gameURLConnection.getOutputStream());
                wr.write(deleteMessage);
                wr.flush();
                wr.close();
                gameURLConnection.disconnect();

            }
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;
    }

}

