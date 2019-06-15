package it.polimi.group02.controller.background_task;

import android.os.AsyncTask;

import it.polimi.group02.controller.activity.PlayActivity;
import it.polimi.group02.model.ArtificialIntelligence;
import it.polimi.group02.model.GameModel;

/**
 * This class implements the Asyntask to compute the next AI move in background.
 * This avoids the computation to block the UI thread
 */
public class ComputeAIMove extends AsyncTask<Object, Void, Void> {

    @Override
    protected Void doInBackground(Object... objects) {
        GameModel gameModel = (GameModel)objects[0];
        PlayActivity play = (PlayActivity)objects[1];
        String nextMove = ArtificialIntelligence.nextMove(gameModel.toString());
        gameModel.playTurn(nextMove);
        play.aiTurn = false;
        play.aiPlayed = true;
        return null;
    }


}
