package it.polimi.group02.controller.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import it.polimi.group02.R;
import it.polimi.group02.controller.database.StatisticsDatabaseImplementation;
import it.polimi.group02.controller.background_task.ComputeAIMove;
import it.polimi.group02.controller.background_task.DeleteConnection;
import it.polimi.group02.controller.background_task.GetMoveOnline;
import it.polimi.group02.controller.background_task.PostMoveOnline;
import it.polimi.group02.controller.utility.BackgroundMusic;
import it.polimi.group02.controller.utility.PreferencesUtility;
import it.polimi.group02.model.ArtificialIntelligence;
import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.Piece;
import it.polimi.group02.model.Player;
import it.polimi.group02.model.StringToGameConverter;
import it.polimi.group02.model.utility.ActionType;
import it.polimi.group02.model.validator.CalculateReachableCells;
import it.polimi.group02.model.validator.DiagonalAttackValidator;
import it.polimi.group02.model.validator.HorizontalVerticalAttackValidator;

import static it.polimi.group02.R.drawable.a;
import static it.polimi.group02.R.drawable.a_highlighted;
import static it.polimi.group02.R.drawable.classic_archer_black_frozen;
import static it.polimi.group02.R.drawable.classic_archer_white_frozen;
import static it.polimi.group02.R.drawable.classic_dragon_black_frozen;
import static it.polimi.group02.R.drawable.classic_dragon_white_frozen;
import static it.polimi.group02.R.drawable.classic_giant_black_frozen;
import static it.polimi.group02.R.drawable.classic_giant_white_frozen;
import static it.polimi.group02.R.drawable.classic_knight_black_frozen;
import static it.polimi.group02.R.drawable.classic_knight_white_frozen;
import static it.polimi.group02.R.drawable.classic_squire_black_frozen;
import static it.polimi.group02.R.drawable.classic_squire_white_frozen;
import static it.polimi.group02.R.drawable.fantasy_archer_black;
import static it.polimi.group02.R.drawable.fantasy_archer_black_frozen;
import static it.polimi.group02.R.drawable.fantasy_archer_white;
import static it.polimi.group02.R.drawable.classic_archer_blk;
import static it.polimi.group02.R.drawable.classic_archer_wht;
import static it.polimi.group02.R.drawable.classic_dragon_blk;
import static it.polimi.group02.R.drawable.classic_dragon_wht;
import static it.polimi.group02.R.drawable.classic_giant_blk;
import static it.polimi.group02.R.drawable.classic_giant_wht;
import static it.polimi.group02.R.drawable.classic_knight_blk;
import static it.polimi.group02.R.drawable.classic_knight_wht;
import static it.polimi.group02.R.drawable.classic_mage_blk;
import static it.polimi.group02.R.drawable.classic_mage_wht;
import static it.polimi.group02.R.drawable.classic_squire_blk;
import static it.polimi.group02.R.drawable.classic_squire_wht;
import static it.polimi.group02.R.drawable.fantasy_archer_white_frozen;
import static it.polimi.group02.R.drawable.fantasy_dragon_black;
import static it.polimi.group02.R.drawable.fantasy_dragon_black_frozen;
import static it.polimi.group02.R.drawable.fantasy_dragon_white;
import static it.polimi.group02.R.drawable.f;
import static it.polimi.group02.R.drawable.f_highlighted;
import static it.polimi.group02.R.drawable.fantasy_dragon_white_frozen;
import static it.polimi.group02.R.drawable.fantasy_giant_black;
import static it.polimi.group02.R.drawable.fantasy_giant_black_frozen;
import static it.polimi.group02.R.drawable.fantasy_giant_white;
import static it.polimi.group02.R.drawable.fantasy_giant_white_frozen;
import static it.polimi.group02.R.drawable.fantasy_knight_black_frozen;
import static it.polimi.group02.R.drawable.fantasy_knight_white_frozen;
import static it.polimi.group02.R.drawable.fantasy_squire_black_frozen;
import static it.polimi.group02.R.drawable.fantasy_squire_white_frozen;
import static it.polimi.group02.R.drawable.h;
import static it.polimi.group02.R.drawable.h_highlighted;
import static it.polimi.group02.R.drawable.fantasy_knight_black;
import static it.polimi.group02.R.drawable.fantasy_knight_white;
import static it.polimi.group02.R.drawable.life_bar_1;
import static it.polimi.group02.R.drawable.life_bar_10;
import static it.polimi.group02.R.drawable.life_bar_2;
import static it.polimi.group02.R.drawable.life_bar_3;
import static it.polimi.group02.R.drawable.life_bar_4;
import static it.polimi.group02.R.drawable.life_bar_5;
import static it.polimi.group02.R.drawable.life_bar_6;
import static it.polimi.group02.R.drawable.life_bar_7;
import static it.polimi.group02.R.drawable.life_bar_8;
import static it.polimi.group02.R.drawable.life_bar_9;
import static it.polimi.group02.R.drawable.m;
import static it.polimi.group02.R.drawable.m_highlighted;
import static it.polimi.group02.R.drawable.fantasy_mage_black;
import static it.polimi.group02.R.drawable.fantasy_mage_white;
import static it.polimi.group02.R.drawable.r;
import static it.polimi.group02.R.drawable.r_highlighted;
import static it.polimi.group02.R.drawable.fantasy_squire_black;
import static it.polimi.group02.R.drawable.fantasy_squire_white;
import static it.polimi.group02.R.drawable.t;
import static it.polimi.group02.R.drawable.t_highlighted;
import static it.polimi.group02.controller.utility.PreferencesUtility.TOGGLE_MUSIC_KEY;
import static it.polimi.group02.controller.utility.PreferencesUtility.backButtonPressed;
import static it.polimi.group02.controller.utility.PreferencesUtility.isFantasyTheme;
import static it.polimi.group02.controller.utility.PreferencesUtility.playGameSound;
import static it.polimi.group02.controller.utility.PreferencesUtility.repriseGameFromLatestTurn;
import static it.polimi.group02.controller.utility.PreferencesUtility.vibrate;
import static it.polimi.group02.model.utility.ActionType.ATTACK;
import static it.polimi.group02.model.utility.ActionType.CAST_REVIVE;
import static it.polimi.group02.model.utility.ActionType.CAST_HEAL;
import static it.polimi.group02.model.utility.ActionType.CAST_TELEPORT;
import static it.polimi.group02.model.utility.ActionType.CAST_FREEZE;
import static it.polimi.group02.model.utility.ActionType.MOVE;
import static it.polimi.group02.model.utility.Color.BLACK;
import static it.polimi.group02.model.utility.Color.WHITE;
import static it.polimi.group02.model.utility.Utility.NUMBER_OF_PLAYERS;
import static it.polimi.group02.model.utility.Utility.archer_initial_vitality;
import static it.polimi.group02.model.utility.Utility.dragon_initial_vitality;
import static it.polimi.group02.model.utility.Utility.giant_initial_vitality;
import static it.polimi.group02.model.utility.Utility.knight_initial_vitality;
import static it.polimi.group02.model.utility.Utility.mage_initial_vitality;
import static it.polimi.group02.model.utility.Utility.squire_initial_vitality;



/**
 * This activity is the most important one for this project, since it displays the game on the screen and implements all the rules of the
 * game. In order to guarantee the correct execution of the game logic, this activity must hold an instance of the game model. This
 * activity represents all three configurations of game: online, local, against ai.
 */
public class PlayActivity extends AppCompatActivity{
    private GameModel game;
    private ArrayList<FrameLayout> cellsFrames = new ArrayList<>();
    private ArrayList<FrameLayout> actionFrames = new ArrayList<>();
    private ArrayList<FrameLayout> lifeFrames = new ArrayList<>();
    private ActionType action;
    private Piece startingPiece;
    private Piece destinationPiece;
    private ArrayList<String> reachableCells;
    private ArrayList<String> attackableCells;
    private TextView statusBoard;
    private boolean teleportTargetSelected;
    private boolean gameOver;

    public boolean aiTurn = false;
    protected static boolean firstAI;
    protected static boolean playVersuAI;
    public boolean aiPlayed = false;
    public static boolean myTurn;
    public static boolean opponentTurn ;
    public String onlineOpponentMove = "";
    public boolean onlineOpponentPlayed = false;
    protected static boolean playOnline;
    public static String gameID;
    protected static String stringUrl;
    protected boolean iPlayed;
    public String myMoveOnline;
    public boolean gameEnded;
    public static boolean iAmFirst;
    public static boolean iAmSecond;
    private static boolean lastPlayedVersusAI = false;
    private static boolean lastPlayedVersusAIfirst = false;
    private static boolean lastPlayedVersusAIsecond = false;
    private static boolean lastPlayedLocal = false;
    protected String deleteMessage;
    public boolean iWonForOpponentResign;
    private boolean timeElapsed;
    public boolean myMoveSent;
    protected static boolean opponentMoveReceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);

        // set the background image
        PreferencesUtility.setBackground(this, findViewById(R.id.activity_play));

        // set the correct buttons based on the theme
        setButtonsImages();

        // initialization of attributes
        statusBoard = (TextView) findViewById(R.id.actionToBeDone);
        action = null;
        startingPiece = null;
        destinationPiece = null;
        reachableCells = null;
        attackableCells = null;
        teleportTargetSelected = false;
        gameOver = false;
        gameEnded = false;

        deleteMessage = "";
        iWonForOpponentResign = false;
        timeElapsed = false;
        iPlayed = false;
        onlineOpponentPlayed = false;
        myMoveSent = false;
        opponentMoveReceived = false;

        game = new GameModel();
        // if game was paused, the names of the players who left the game are the same and the typology of the game is the same (note that online games are not saved)
        // check if the previous game was ended and the names inserted now are the same as the ones from the previous game
        if (PreferencesUtility.repriseGameFromLatestTurn && PreferencesUtility.whitePlayerName.equals(getIntent().getStringExtra("WHITE_PLAYER"))
                && PreferencesUtility.blackPlayerName.equals(getIntent().getStringExtra("BLACK_PLAYER"))) {
            if(playVersuAI && lastPlayedVersusAI && (firstAI && lastPlayedVersusAIfirst || !firstAI && lastPlayedVersusAIsecond)) {
                StringToGameConverter converter = new StringToGameConverter(getIntent().getStringExtra("WHITE_PLAYER"), getIntent().getStringExtra("BLACK_PLAYER"));
                converter.turnTest(PreferencesUtility.savedGameConfiguration);
                game = converter.getGameModel();
                PreferencesUtility.repriseGameFromLatestTurn = false;
            }
            // if last game was local
                else if (lastPlayedLocal && !playOnline && !playVersuAI) {
                StringToGameConverter converter = new StringToGameConverter(getIntent().getStringExtra("WHITE_PLAYER"), getIntent().getStringExtra("BLACK_PLAYER"));
                converter.turnTest(PreferencesUtility.savedGameConfiguration);
                game = converter.getGameModel();
                PreferencesUtility.repriseGameFromLatestTurn = false;
            }
        }
        // otherwise, start a new game
        else {
            game.createStandardConfigurationMatrix();

            if(!playOnline && !playVersuAI) {
                game.initializePlayer(getIntent().getStringExtra("WHITE_PLAYER"), 0, WHITE);
                game.initializePlayer(getIntent().getStringExtra("BLACK_PLAYER"), 1, BLACK);
            }

            if(playVersuAI) {
                if (getIntent().getStringExtra("WHITE_PLAYER").equals("EASY_AI") || getIntent().getStringExtra("BLACK_PLAYER").equals("EASY_AI")) {
                    if (getIntent().getStringExtra("WHITE_PLAYER").equals("EASY_AI")) {
                        game.initializePlayer("AI EASY", 0, WHITE);
                        game.initializePlayer(getIntent().getStringExtra("BLACK_PLAYER"), 1, BLACK);
                    }else{
                        game.initializePlayer(getIntent().getStringExtra("WHITE_PLAYER"), 0, WHITE);
                        game.initializePlayer("AI EASY", 1, BLACK);
                    }
                    ArtificialIntelligence.setRecursionLevel(0);
                }

                if (getIntent().getStringExtra("WHITE_PLAYER").equals("HARD_AI") || getIntent().getStringExtra("BLACK_PLAYER").equals("HARD_AI")) {
                    if (getIntent().getStringExtra("WHITE_PLAYER").equals("HARD_AI")) {
                        game.initializePlayer("AI HARD", 0, WHITE);
                        game.initializePlayer(getIntent().getStringExtra("BLACK_PLAYER"), 1, BLACK);
                    }else{
                        game.initializePlayer(getIntent().getStringExtra("WHITE_PLAYER"), 0, WHITE);
                        game.initializePlayer("AI HARD", 1, BLACK);
                    }
                    ArtificialIntelligence.setRecursionLevel(1);
                }
            }

            if(playOnline) {
                if (getIntent().getStringExtra("WHITE_PLAYER") != null) {
                    game.initializePlayer(getIntent().getStringExtra("WHITE_PLAYER"), 0, WHITE);
                    game.initializePlayer("Online opponent", 1, BLACK);
                }

                if (getIntent().getStringExtra("BLACK_PLAYER") != null) {
                    game.initializePlayer(getIntent().getStringExtra("BLACK_PLAYER"), 1, BLACK);
                    game.initializePlayer("Online opponent", 0, WHITE);
                }
            }

            game.setPlayerUnusedSpells(0, "FHRT");
            game.setPlayerUnusedSpells(1, "FHRT");
            game.createPieces();
        }

        addLifesToFrames();
        addCellsToFrames();
        addActionToFrames();

        if(playVersuAI) {
            ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(2);
            /*This schedules a runnable task every 2 seconds*/
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (aiPlayed) {
                                refreshUI();
                                checkGameStatus();
                                aiPlayed = false;
                            }
                        }

                    });
                }
            }, 0, 2, TimeUnit.SECONDS);
        }

        drawPiecesOnTheBoard();

        if(playOnline) {
            Button restart = (Button) findViewById(R.id.restart_button);
            restart.setVisibility(View.INVISIBLE);
            final ScheduledExecutorService scheduleMoveExecutor = Executors.newScheduledThreadPool(1);
            /*This schedules a runnable task every 2 seconds*/
            scheduleMoveExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(gameEnded==true){
                                if(!onlineOpponentPlayed && opponentTurn && deleteMessage.equals("") || iWonForOpponentResign){
                                    Toast.makeText(getApplicationContext(), "Opponent Abandoned the game, you WON", Toast.LENGTH_SHORT).show();
                                    scheduleMoveExecutor.shutdown();
                                    gameOver = true;
                                    return;
                                }
                            }
                            if (onlineOpponentPlayed) {
                                game.playTurn(onlineOpponentMove);
                                refreshUI();
                                checkGameStatus();
                                if(game.isGameOver()){
                                    gameOver();
                                }
                                onlineOpponentPlayed = false;
                                iPlayed = false;
                                opponentTurn = false;
                                myTurn = true;
                                myMoveOnline = "";
                            }
                        }

                    });
                }
            }, 0, 3, TimeUnit.SECONDS);

            while (!iAmSecond || !iAmFirst) {
                if (iAmSecond) {
                    Toast.makeText(this, "Your opponent starts first, she/he has 60 seconds", Toast.LENGTH_SHORT).show();
                    Object[] objects = {this};
                    new GetMoveOnline().execute(objects);
                    break;
                }
                if (iAmFirst) {
                    setListenersOnFrames();
                    drawActionButtons();
                    setNextAction();
                    timeElapsed = false;
                    final int[] counterOfCycles = {60};
                    final ScheduledExecutorService scheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
                    scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(myMoveSent || gameEnded || !playOnline || gameOver || opponentTurn){
                                        scheduleTaskExecutor.shutdownNow();
                                    }
                                    if(counterOfCycles[0] == 0){
                                        timeElapsed = true;
                                    }
                                    if (timeElapsed) {
                                        Toast.makeText(getApplicationContext(), "You LOST because you didn't do any move", Toast.LENGTH_SHORT).show();
                                        scheduleTaskExecutor.shutdownNow();
                                    }else {
                                        if (counterOfCycles[0] % 10 == 0) {
                                            if(counterOfCycles[0]==60) {
                                                Toast.makeText(getApplicationContext(), "You and your opponent have " + String.valueOf(counterOfCycles[0]) + " seconds to make your move per each turn", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(), "You still have " + String.valueOf(counterOfCycles[0]) + " seconds to make your move", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    counterOfCycles[0] = counterOfCycles[0] - 1;

                                }

                            });
                        }
                    }, 0, 1, TimeUnit.SECONDS);
                    break;
                }
            }
        }

        if (playVersuAI) {
            if(firstAI && game.toString().charAt(0)=='W' || !firstAI && game.toString().charAt(0)=='B') {//if not always restarts the AI
                setNextAction();
                nextMoveFromAI();
            }else{
                setListenersOnFrames();
                drawActionButtons();
                setNextAction();
            }
        }
        if(!playOnline && !playVersuAI){
            setListenersOnFrames();
            drawActionButtons();
            setNextAction();
        }

        // play fanfare only if background music is not playing
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(TOGGLE_MUSIC_KEY, true))
            playGameSound(this, R.raw.start_game_fanfare);
    }

    // based on the theme selected, select the appropriate buttons
    private void setButtonsImages() {
        Button back = (Button) findViewById(R.id.back_button);
        Button play = (Button) findViewById(R.id.restart_button);
        if(isFantasyTheme(this)){
            back.setBackgroundResource(R.drawable.fantasy_back);
            play.setBackgroundResource(R.drawable.fantasy_restart);
        }else{
            back.setBackgroundResource(R.drawable.classic_back);
            play.setBackgroundResource(R.drawable.classsic_restart);
        }
    }

    /**
     * this method calls the async task to compute in background the AI move
     */
    private void nextMoveFromAI(){
        Toast.makeText(this, "Waiting ai move", Toast.LENGTH_SHORT).show();
        Object[] objects = {game,this};
        AsyncTask aiMove = new ComputeAIMove();
        aiMove.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,objects);

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

        if (!backButtonPressed) {
            BackgroundMusic.pauseMusic();
        }
        backButtonPressed = false;
    }

    // this callback is invoked whenever the smartphone BACK key is pressed to exit the app
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backButtonPressed = true;
        playGameSound(this, R.raw.button_pressed);
        // save the names of the players so that if they both start a game and their name is the same as the ones saved, the game is reprise from the status it was left
        PreferencesUtility.savedGameConfiguration = game.toString();
        if(game.isGameOver()){
            repriseGameFromLatestTurn = false;
        }else{
            repriseGameFromLatestTurn = true;
        }
        PreferencesUtility.whitePlayerName = getIntent().getStringExtra("WHITE_PLAYER");
        PreferencesUtility.blackPlayerName = getIntent().getStringExtra("BLACK_PLAYER");

        //closing the game connection
        deleteMessage = "aborted";
        myTurn = true;
        opponentTurn = false;
        if(playOnline){
            lastPlayedVersusAI = false;
            lastPlayedLocal = false;
        }
        if(playVersuAI){
            lastPlayedVersusAI = true;
            if(firstAI){
                lastPlayedVersusAIfirst = true;
                lastPlayedVersusAIsecond = false;
            }else{
                lastPlayedVersusAIsecond = true;
                lastPlayedVersusAIfirst = false;
            }
            lastPlayedLocal = false;
        }
        if(!playOnline && !playVersuAI){
            lastPlayedLocal = true;
            lastPlayedVersusAI = false;
        }

        Object[] objects = {this.deleteMessage};
        AsyncTask deleteConnection = new DeleteConnection();
        deleteConnection.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,objects);
    }

    public void backButtonClick(View view) {
        backButtonPressed = true;
        playGameSound(this, R.raw.button_pressed);
        // save the names of the players so that if they both start a game and their name is the same as the ones saved, the game is reprise from the status it was left
        PreferencesUtility.savedGameConfiguration = game.toString();
        if(game.isGameOver()){
            repriseGameFromLatestTurn = false;
        }else{
            repriseGameFromLatestTurn = true;
        }
        PreferencesUtility.whitePlayerName = getIntent().getStringExtra("WHITE_PLAYER");
        PreferencesUtility.blackPlayerName = getIntent().getStringExtra("BLACK_PLAYER");
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //closing the game connection
        deleteMessage = "aborted";
        myTurn = true;
        opponentTurn = false;
        if(playOnline){
            lastPlayedVersusAI = false;
            lastPlayedLocal = false;
        }
        if(playVersuAI){
            lastPlayedVersusAI = true;
            if(firstAI){
                lastPlayedVersusAIfirst = true;
                lastPlayedVersusAIsecond = false;
            }else{
                lastPlayedVersusAIsecond = true;
                lastPlayedVersusAIfirst = false;
            }
            lastPlayedLocal = false;
        }
        if(!playOnline && !playVersuAI){
            lastPlayedLocal = true;
            lastPlayedVersusAI = false;
        }

        Object[] objects = {this.deleteMessage};
        AsyncTask deleteConnection = new DeleteConnection();
        deleteConnection.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,objects);

        startActivity(intent);
        finish();
    }

    /**
     * If the restart button is clicked, a Dialog is shown.
     * @param view
     */
    public void restartButtonClick(View view) {
        playGameSound(this, R.raw.button_pressed);
        DialogFragment fragment = new QuitGameDialogFragment();
        fragment.show(getSupportFragmentManager(), "QUIT_GAME");
    }

    /**
     * This method is invoked once the player has selected the 'Yes' option in the Dialog. The consequence of this action is the game
     * is deleted and only a new one can be started.
     */
    public void quitGame() {
        backButtonPressed = true;
        repriseGameFromLatestTurn = false;
        playGameSound(this, R.raw.button_pressed);
        Intent intent = new Intent(this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * In this method all the chessboard cells are scanned in the game model to get the ones occupied by a piece. For each of these,
     * an ImageView is created to display that piece.
     */
    public void drawPiecesOnTheBoard() {
        char[][] board = game.getCurrentConfiguration();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] != '0') {
                    FrameLayout frame = this.cellsFrames.get(i * 6 + j);
                    ImageView iv = new ImageView(this);
                    Player firstPlayer = game.getPlayers()[game.getTurn()];
                    Player secondPlayer = game.getPlayers()[(game.getTurn() + 1) % NUMBER_OF_PLAYERS];
                    // if this piece is frozen, select a frozen piece image
                    if (firstPlayer.getColor() == WHITE
                            && Character.getNumericValue(firstPlayer.getFrozenPieceInformation().charAt(0)) == getYFromFrameId(frame)
                            && Character.getNumericValue(firstPlayer.getFrozenPieceInformation().charAt(1)) == getXFromFrameId(frame)
                        || firstPlayer.getColor() == BLACK
                            && Character.getNumericValue(firstPlayer.getFrozenPieceInformation().charAt(0)) == getYFromFrameId(frame)
                            && Character.getNumericValue(firstPlayer.getFrozenPieceInformation().charAt(1)) == getXFromFrameId(frame)
                        || secondPlayer.getColor() == BLACK
                            && Character.getNumericValue(secondPlayer.getFrozenPieceInformation().charAt(0)) == getYFromFrameId(frame)
                            && Character.getNumericValue(secondPlayer.getFrozenPieceInformation().charAt(1)) == getXFromFrameId(frame)
                        || secondPlayer.getColor() == WHITE
                            && Character.getNumericValue(secondPlayer.getFrozenPieceInformation().charAt(0)) == getYFromFrameId(frame)
                            && Character.getNumericValue(secondPlayer.getFrozenPieceInformation().charAt(1)) == getXFromFrameId(frame))

                                setFrozenPieceImage(iv, board[i][j], frame);
                    else
                        setPieceImage(iv, board[i][j], frame);

                    FrameLayout lifeFrame = this.lifeFrames.get(i * 6 + j);
                    ImageView iv2 = new ImageView(this);
                    double pieceLife = 0;
                    char symbol = '0';
                    if(Character.isUpperCase(board[i][j])) {
                        pieceLife = game.getPieceAtTurn(0, j, i).getVitality();
                        symbol = game.getPieceAtTurn(0, j, i).getPieceSymbol();
                    }else{
                        pieceLife = game.getPieceAtTurn(1, j, i).getVitality();
                        symbol = game.getPieceAtTurn(1, j, i).getPieceSymbol();
                    }
                    setLifeImage(iv2,lifeFrame, pieceLife, symbol);
                } else if (board[i][j] == '0') {
                    FrameLayout frame = this.cellsFrames.get(i * 6 + j);
                    setListenerOnEmptyFrame(frame);
                }
            }
        }
    }

    private void setLifeImage(ImageView imgView, FrameLayout frame, double life, char symbol) {
        TextView text = new TextView(this);
        double vitality = 0;

        if(symbol=='m' || symbol=='M'){
            vitality = mage_initial_vitality;
        }
        if(symbol=='a' || symbol=='A'){
            vitality = archer_initial_vitality;
        }
        if(symbol=='k' || symbol=='K'){
            vitality = knight_initial_vitality;
        }
        if(symbol=='g' || symbol=='G'){
            vitality = giant_initial_vitality;
        }
        if(symbol=='d' || symbol=='D'){
            vitality = dragon_initial_vitality;
        }
        if(symbol=='s' || symbol=='S'){
            vitality = squire_initial_vitality;
        }

        String tempText =  String.valueOf((int)life) + "/" + String.valueOf((int)vitality);
        text.setText(tempText);
        text.setTextColor(Color.WHITE);

        double percentage = (life/vitality);
        percentage = percentage*10;

        if(percentage<=10 && percentage>9) {
            text.setBackgroundResource(life_bar_10);
        }
        if(percentage<=9 && percentage>8){
            text.setBackgroundResource(life_bar_9);
        }
        if(percentage<=8 && percentage>7){
            text.setBackgroundResource(life_bar_8);
        }
        if(percentage<=7 && percentage>6){
            text.setBackgroundResource(life_bar_7);
        }
        if(percentage<=6 && percentage>5){
            text.setBackgroundResource(life_bar_6);
        }
        if(percentage<=5 && percentage>4){
            text.setBackgroundResource(life_bar_5);
        }
        if(percentage<=4 && percentage>3){
            text.setBackgroundResource(life_bar_4);
        }
        if(percentage<=3 && percentage>2){
            text.setBackgroundResource(life_bar_3);
        }
        if(percentage<=2 && percentage>1){
            text.setBackgroundResource(life_bar_2);
        }
        if(percentage<=1 && percentage>0){
            text.setBackgroundResource(life_bar_1);
        }
        frame.addView(text);
    }

    /**
     * This method is invoked whenever an empty piece is touched by a player. It is of interest to capture this action only after the
     * player has already selected a piece and an action to perform. Furthermore, the only actions for which is sensible to capture an
     * empty frame information are: move, teleport, revive.
     * @param frame the FrameLayout containing the ImageView
     */
    private void setListenerOnEmptyFrame(final FrameLayout frame) {
        frame.removeAllViews();
        frame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (action == MOVE) {
                    if (startingPiece != null) {
                        String tempMove = "M" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                String.valueOf(getYFromFrameId(frame)) + String.valueOf(getXFromFrameId(frame));
                        // if any event (an error, the end of the game) has occurred after this action
                        if (game.playTurn("M" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                String.valueOf(getYFromFrameId(frame)) + String.valueOf(getXFromFrameId(frame))).length() > 0) {
                            checkGameStatus();
                            if(gameOver){
                                myMoveOnline = tempMove;
                                iPlayed = true;
                                opponentTurn = true;
                                onlineOpponentMove = "";
                                onlineOpponentPlayed = false;
                                refreshUI();
                            }
                            // vibrate whenever an invalid action is performed
                            if (!gameOver) {
                                vibrate(getApplicationContext());
                                refreshUI();
                            }
                        } else {
                            emitMoveSound();
                            if (game.isSpecialCell(getXFromFrameId(frame) - 1, getYFromFrameId(frame) - 1))
                                playGameSound(getApplicationContext(), R.raw.special_cell_occupied);
                            aiTurn = true;
                            opponentTurn = true;
                            onlineOpponentMove = "";
                            onlineOpponentPlayed = false;
                            myMoveOnline = tempMove;
                            iPlayed = true;
                            refreshUI();
                        }
                        action = null;
                        startingPiece = null;
                    }
                } else if (action == CAST_TELEPORT) {
                    if (!teleportTargetSelected) {
                        startingPiece = game.getPieceAtTurn(game.getTurn(), getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                        if (startingPiece != null) {
                            teleportTargetSelected = true;
                            Toast.makeText(getApplicationContext(), "Select a cell on which to teleport the piece", Toast.LENGTH_SHORT).show();
                            refreshUI();
                        } else {
                            Toast.makeText(getApplicationContext(), "You must select one of your pieces", Toast.LENGTH_SHORT).show();
                            action = null;
                            startingPiece = null;
                            // vibrate whenever an invalid action is performed
                            vibrate(getApplicationContext());
                            refreshUI();
                        }
                    } else {
                        String tempMove = "T" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                String.valueOf(getYFromFrameId(frame)) + String.valueOf(getXFromFrameId(frame));
                        // if any event (an error, the end of the game) has occurred after this action
                        if (game.playTurn("T" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                String.valueOf(getYFromFrameId(frame)) + String.valueOf(getXFromFrameId(frame))).length() > 0) {
                            checkGameStatus();
                            if(gameOver){
                                myMoveOnline = tempMove;
                                iPlayed = true;
                                opponentTurn = true;
                                onlineOpponentMove = "";
                                onlineOpponentPlayed = false;
                                refreshUI();
                            }
                            // vibrate whenever an invalid action is performed
                            if (!gameOver) {
                                vibrate(getApplicationContext());
                                refreshUI();
                            }
                        } else {
                            playGameSound(getApplicationContext(), R.raw.teleport_spell);
                            opponentTurn = true;
                            onlineOpponentMove = "";
                            onlineOpponentPlayed = false;
                            myMoveOnline = tempMove;
                            iPlayed = true;
                            aiTurn = true;
                            refreshUI();
                        }
                        teleportTargetSelected = false;
                        action = null;
                        startingPiece = null;
                        destinationPiece = null;
                    }
                } else if (action == CAST_REVIVE) {
                    String tempMove = "R" + String.valueOf(getYFromFrameId(frame)) + String.valueOf(getXFromFrameId(frame)) + "00";
                    // if any event (an error, the end of the game) has occurred after this action
                    if (game.playTurn("R" + String.valueOf(getYFromFrameId(frame)) + String.valueOf(getXFromFrameId(frame)) + "00").length() > 0) {
                        checkGameStatus();
                        if(gameOver){
                            myMoveOnline = tempMove;
                            iPlayed = true;
                            opponentTurn = true;
                            onlineOpponentMove = "";
                            onlineOpponentPlayed = false;
                            refreshUI();
                        }
                        // vibrate whenever an invalid action is performed
                        if (!gameOver) {
                            vibrate(getApplicationContext());
                            refreshUI();
                        }
                    } else {
                        playGameSound(getApplicationContext(), R.raw.heal_revive_spell);
                        Toast.makeText(getApplicationContext(), "Piece is revived in cell (" + getXFromFrameId(frame) + "," + getYFromFrameId(frame) + ")", Toast.LENGTH_SHORT).show();
                        destinationPiece = null;
                        opponentTurn = true;
                        onlineOpponentMove = "";
                        onlineOpponentPlayed = false;
                        myMoveOnline = tempMove;
                        iPlayed = true;
                        aiTurn = true;
                        refreshUI();
                    }
                    action = null;
                    startingPiece = null;
                }
            }
        });
    }

    /**
     * This method sets a listener on each FrameLayout corresponding to each cell of the chessboard.
     */
    public void setListenersOnFrames() {
        char[][] board = game.getCurrentConfiguration();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (board[i][j] != '0') {
                    FrameLayout frame = this.cellsFrames.get(i * 6 + j);
                    ImageView iv = new ImageView(this);
                    setListenerOnImage(frame);
                } else if (board[i][j] == '0') {
                    FrameLayout frame = this.cellsFrames.get(i * 6 + j);
                    setListenerOnEmptyFrame(frame);
                }
            }
        }
    }

    /**
     * In this method listeners are attached on each ImageView objects corresponding to the pieces. There are two cases which should be
     * reckoned while a piece is selected: the piece is selected before touching any action button, so it is the starting piece; the piece
     * is selected after one action button has been selected, thus it is the target piece.
     * @param frame the FrameLayout containing the ImageView
     */
    public void setListenerOnImage(final FrameLayout frame) {
        frame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if you've selected a frozen piece and it's the starting piece reject the action (remember a frozen piece can receive an action)
                if (Character.getNumericValue(game.getPlayers()[game.getTurn()].getFrozenPieceInformation().charAt(0)) == getYFromFrameId(frame)
                        && Character.getNumericValue(game.getPlayers()[game.getTurn()].getFrozenPieceInformation().charAt(1)) == getXFromFrameId(frame)
                        && startingPiece == null) {
                    vibrate(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "This piece is frozen; choose another piece", Toast.LENGTH_SHORT).show();
                }
                // no piece has been selected in this turn yet, so it is the piece which will perform an action
                else if (startingPiece == null) {
                    startingPiece = game.getPieceAtTurn(game.getTurn(), getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                    if (startingPiece != null) {
                        emitSoundOnSelectedPiece();
                        // get reachable cells by move
                        reachableCells = CalculateReachableCells.calculateAllReachableCellsWithouthTarget(startingPiece, game.getCurrentConfiguration());
                        // if a piece (not a mage, or a mage with no more spells available) has no way out and can't do any action (eg is surrounded by its friends)
                        if (reachableCells.size() == 1 && (!game.playerHasMage(game.getTurn()) || (game.playerHasMage(game.getTurn()) && game.getPlayers()[game.getTurn()].unusedSpells.equals("0000")))) {
                            vibrate(getApplicationContext());
                            Toast.makeText(getApplicationContext(), "This piece can't do anything; pick another one", Toast.LENGTH_SHORT).show();
                            startingPiece = null;
                        } else {
                            // get reachable enemies to attack
                            attackableCells = (startingPiece.getPieceSymbol() == 'k' || startingPiece.getPieceSymbol() == 'K') ? DiagonalAttackValidator.checkAttackWithoutTarget(startingPiece, game.getCurrentConfiguration()) : HorizontalVerticalAttackValidator.checkAttackWithoutTarget(startingPiece, game.getCurrentConfiguration());
                            removeAllImagesAndListeners();
                            drawPiecesOnTheBoard();
                            drawActionButtons();
                            setListenerOnActions();
                            setNextAction();
                        }
                    }
                }
                // if one piece has already been selected, the piece now selected will be the one on which the selectd action will be performed
                else {
                    if (action != null) {
                        if (action == MOVE) {
                            destinationPiece = game.getPieceAtTurn((game.getTurn() + 1) % NUMBER_OF_PLAYERS, getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                            if (destinationPiece != null) {
                                String tempMove = "M" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                        String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1);
                                // if any event (an error, the end of the game) has occurred after this action
                                if (game.playTurn("M" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                        String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1)).length() > 0) {
                                    checkGameStatus();
                                    if(gameOver){
                                        myMoveOnline = tempMove;
                                        iPlayed = true;
                                        opponentTurn = true;
                                        onlineOpponentMove = "";
                                        onlineOpponentPlayed = false;
                                        refreshUI();
                                    }
                                    // vibrate whenever an invalid action is performed
                                    if (!gameOver) {
                                        vibrate(getApplicationContext());
                                        refreshUI();
                                    }
                                } else {
                                    opponentTurn = true;
                                    onlineOpponentMove = "";
                                    onlineOpponentPlayed = false;
                                    myMoveOnline = tempMove;
                                    iPlayed = true;
                                    aiTurn = true;
                                    emitMoveSound();
                                    if (game.isSpecialCell(getXFromFrameId(frame), getYFromFrameId(frame)))
                                        playGameSound(getApplicationContext(), R.raw.special_cell_occupied);
                                    emitSoundOnCombat();
                                    destinationPiece = null;
                                    refreshUI();
                                }
                                action = null;
                                startingPiece = null;
                                aiTurn = true;
                            }
                        } else if (action == ATTACK) {
                            destinationPiece = game.getPieceAtTurn((game.getTurn() + 1) % NUMBER_OF_PLAYERS, getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                            if (destinationPiece != null) {
                                String tempMove = "A" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                        String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1);
                                // if any event (an error, the end of the game) has occurred after this action
                                if (game.playTurn("A" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                        String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1)).length() > 0) {
                                    checkGameStatus();
                                    if(gameOver){
                                        myMoveOnline = tempMove;
                                        iPlayed = true;
                                        opponentTurn = true;
                                        onlineOpponentMove = "";
                                        onlineOpponentPlayed = false;
                                        refreshUI();
                                    }
                                    // vibrate whenever an invalid action is performed
                                    if (!gameOver) {
                                        vibrate(getApplicationContext());
                                        refreshUI();
                                    }
                                } else {
                                    emitSoundOnAttack(startingPiece);
                                    // if the attacked piece is still alive
                                    if (destinationPiece.getVitality() > 0) {
                                        Toast.makeText(getApplicationContext(), "The attacked piece has now vitality: " + destinationPiece.getVitality(), Toast.LENGTH_SHORT).show();
                                    }
                                    // if it's dead
                                    else {
                                        emitSoundOnDyingPiece(destinationPiece);
                                    }
                                    opponentTurn = true;
                                    onlineOpponentMove = "";
                                    onlineOpponentPlayed = false;
                                    myMoveOnline = tempMove;
                                    iPlayed = true;
                                    aiTurn = true;
                                    refreshUI();
                                }
                                action = null;
                                startingPiece = null;
                            }
                        } else if (action == CAST_TELEPORT) {
                            if (!teleportTargetSelected) {
                                startingPiece = game.getPieceAtTurn(game.getTurn(), getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                                if (startingPiece != null) {
                                    teleportTargetSelected = true;
                                    playGameSound(getApplicationContext(), R.raw.button_click_2);
                                    Toast.makeText(getApplicationContext(), "Select a cell on which to teleport the piece", Toast.LENGTH_SHORT).show();
                                    refreshUI();
                                } else {
                                    Toast.makeText(getApplicationContext(), "You can't teleport an enemy piece", Toast.LENGTH_SHORT).show();
                                    action = null;
                                    startingPiece = null;
                                    // vibrate whenever an invalid action is performed
                                    vibrate(getApplicationContext());
                                    refreshUI();
                                }
                            } else {
                                destinationPiece = game.getPieceAtTurn((game.getTurn() + 1) % NUMBER_OF_PLAYERS, getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                                if (destinationPiece != null) {
                                    String tempMove = "T" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                            String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1);
                                    // if any event (an error, the end of the game) has occurred after this action
                                    if (game.playTurn("T" + String.valueOf(startingPiece.getyCoordinate() + 1) + String.valueOf(startingPiece.getxCoordinate() + 1) +
                                            String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1)).length() > 0) {
                                        checkGameStatus();
                                        if(gameOver){
                                            myMoveOnline = tempMove;
                                            iPlayed = true;
                                            opponentTurn = true;
                                            onlineOpponentMove = "";
                                            onlineOpponentPlayed = false;
                                            refreshUI();
                                        }
                                        // vibrate whenever an invalid action is performed
                                        if (!gameOver) {
                                            vibrate(getApplicationContext());
                                            refreshUI();
                                        }
                                    } else {
                                        playGameSound(getApplicationContext(), R.raw.teleport_spell);
                                        if (game.isSpecialCell(getXFromFrameId(frame), getYFromFrameId(frame)))
                                            playGameSound(getApplicationContext(), R.raw.special_cell_occupied);
                                        emitSoundOnCombat();
                                        opponentTurn = true;
                                        onlineOpponentMove = "";
                                        onlineOpponentPlayed = false;
                                        myMoveOnline = tempMove;
                                        iPlayed = true;
                                        aiTurn = true;
                                        refreshUI();
                                    }
                                    teleportTargetSelected = false;
                                    action = null;
                                    startingPiece = null;
                                    destinationPiece = null;
                                }
                            }
                        } else if (action == CAST_FREEZE) {
                            destinationPiece = game.getPieceAtTurn((game.getTurn() + 1) % NUMBER_OF_PLAYERS, getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                            if (destinationPiece != null) {
                                String tempMove = "F" + String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1) + "00";
                                // if any event (an error, the end of the game) has occurred after this action
                                if (game.playTurn("F" + String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1) + "00").length() > 0) {
                                    checkGameStatus();
                                    if(gameOver){
                                        myMoveOnline = tempMove; myMoveOnline = tempMove;
                                        iPlayed = true;
                                        opponentTurn = true;
                                        onlineOpponentMove = "";
                                        onlineOpponentPlayed = false;
                                        refreshUI();
                                    }
                                    // vibrate whenever an invalid action is performed
                                    if (!gameOver) {
                                        vibrate(getApplicationContext());
                                        refreshUI();
                                    }
                                } else {
                                    playGameSound(getApplicationContext(), R.raw.freezing_spell);
                                    Toast.makeText(getApplicationContext(), "The piece has been frozen", Toast.LENGTH_SHORT).show();
                                    destinationPiece = null;
                                    aiTurn = true;
                                    opponentTurn = true;
                                    onlineOpponentMove = "";
                                    onlineOpponentPlayed = false;
                                    myMoveOnline = tempMove;
                                    iPlayed = true;
                                    refreshUI();
                                }
                                action = null;
                                startingPiece = null;
                            }
                        } else if (action == CAST_HEAL) {
                            destinationPiece = game.getPieceAtTurn(game.getTurn(), getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                            if (destinationPiece != null) {
                                String tempMove = "H" + String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1) + "00";
                                // if any event (an error, the end of the game) has occurred after this action
                                if (game.playTurn("H" + String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1) + "00").length() > 0) {
                                    checkGameStatus();
                                    if(gameOver){
                                        myMoveOnline = tempMove;
                                        iPlayed = true;
                                        opponentTurn = true;
                                        onlineOpponentMove = "";
                                        onlineOpponentPlayed = false;
                                        refreshUI();
                                    }
                                    // vibrate whenever an invalid action is performed
                                    if (!gameOver) {
                                        vibrate(getApplicationContext());
                                        refreshUI();
                                    }
                                } else {
                                    playGameSound(getApplicationContext(), R.raw.heal_revive_spell);
                                    Toast.makeText(getApplicationContext(), "The piece's vitality has been restored", Toast.LENGTH_SHORT).show();
                                    destinationPiece = null;
                                    aiTurn = true;
                                    opponentTurn = true;
                                    onlineOpponentMove = "";
                                    onlineOpponentPlayed = false;
                                    myMoveOnline = tempMove;
                                    iPlayed = true;
                                    refreshUI();
                                }
                                action = null;
                                startingPiece = null;
                            }
                        } else if (action == CAST_REVIVE) {
                            destinationPiece = game.getPieceAtTurn((game.getTurn() + 1) % NUMBER_OF_PLAYERS, getYFromFrameId(frame) - 1, getXFromFrameId(frame) - 1);
                            if (destinationPiece != null) {
                                String tempMove = "R" + String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1) + "00";
                                // if any event (an error, the end of the game) has occurred after this action
                                if (game.playTurn("R" + String.valueOf(destinationPiece.getyCoordinate() + 1) + String.valueOf(destinationPiece.getxCoordinate() + 1) + "00").length() > 0) {
                                    checkGameStatus();
                                    if(gameOver){
                                        myMoveOnline = tempMove;
                                        iPlayed = true;
                                        opponentTurn = true;
                                        onlineOpponentMove = "";
                                        onlineOpponentPlayed = false;
                                        refreshUI();
                                    }
                                    // vibrate whenever an invalid action is performed
                                    if (!gameOver) {
                                        vibrate(getApplicationContext());
                                        refreshUI();
                                    }
                                } else {
                                    playGameSound(getApplicationContext(), R.raw.heal_revive_spell);
                                    if (game.isSpecialCell(getXFromFrameId(frame), getYFromFrameId(frame)))
                                        playGameSound(getApplicationContext(), R.raw.special_cell_occupied);
                                    emitSoundOnCombat();
                                    Toast.makeText(getApplicationContext(), "Piece is revived in cell (" + getXFromFrameId(frame) + "," + getYFromFrameId(frame) + ")", Toast.LENGTH_SHORT).show();
                                    destinationPiece = null;
                                    aiTurn = true;
                                    opponentTurn = true;
                                    onlineOpponentMove = "";
                                    onlineOpponentPlayed = false;
                                    myMoveOnline = tempMove;
                                    iPlayed = true;
                                    refreshUI();
                                }
                                action = null;
                                startingPiece = null;
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * In this method a listener is attached to each FrameLayout corresponding to each action button; whenever one of these buttons
     * is touched, an action is performed.
     */
    private void setListenerOnActions() {
        removeAllImagesAndListeners();
        drawPiecesOnTheBoard();
        drawActionButtons();
        // if there is at least one cell on which the selected piece can move
        if (reachableCells != null && action == null && reachableCells.size() > 1) {
            ImageView moveImg = new ImageView(this);
            moveImg.setImageResource(m_highlighted);
            actionFrames.get(0).removeAllViews();
            actionFrames.get(0).addView(moveImg);
            actionFrames.get(0).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    action = MOVE;
                    playGameSound(getApplicationContext(), R.raw.button_click);
                    refreshUI();
                    drawReachableCellsByMove();
                }
            });
        }
        // if there is at least one enemy piece which can be attacked by the selected piece
        if (attackableCells.size() > 0 && action == null) {
            ImageView attackImg = new ImageView(this);
            attackImg.setImageResource(a_highlighted);
            actionFrames.get(1).removeAllViews();
            actionFrames.get(1).addView(attackImg);
            actionFrames.get(1).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    action = ATTACK;
                    playGameSound(getApplicationContext(), R.raw.button_click);
                    refreshUI();
                    drawReachableCellsByAttack();
                }
            });
        }
        // if and only if the selected piece is a mage (the only one who can cast spells)
        if (game.playerHasMage(game.getTurn()) && (startingPiece.getPieceSymbol() == 'M' || startingPiece.getPieceSymbol() == 'm')) {
            // if the mage has freezing spell still available
            if (game.getPlayers()[game.getTurn()].unusedSpells.contains("F") && action == null) {
                ImageView freezeImg = new ImageView(this);
                freezeImg.setImageResource(f_highlighted);
                actionFrames.get(2).removeAllViews();
                actionFrames.get(2).addView(freezeImg);
                actionFrames.get(2).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        action = CAST_FREEZE;
                        playGameSound(getApplicationContext(), R.raw.button_click);
                        refreshUI();
                    }
                });
            }
            // if the mage has healing spell still available
            if (game.getPlayers()[game.getTurn()].unusedSpells.contains("H") && action == null) {
                ImageView healImg = new ImageView(this);
                healImg.setImageResource(h_highlighted);
                actionFrames.get(3).removeAllViews();
                actionFrames.get(3).addView(healImg);
                actionFrames.get(3).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        action = CAST_HEAL;
                        playGameSound(getApplicationContext(), R.raw.button_click);
                        refreshUI();
                    }
                });
            }
            // if the mage has reviving spell still available
            if (game.getPlayers()[game.getTurn()].unusedSpells.contains("R") && action == null) {
                ImageView reviveImg = new ImageView(this);
                reviveImg.setImageResource(r_highlighted);
                actionFrames.get(4).removeAllViews();
                actionFrames.get(4).addView(reviveImg);
                actionFrames.get(4).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        action = CAST_REVIVE;
                        playGameSound(getApplicationContext(), R.raw.button_click);
                        refreshUI();
                    }
                });
            }
            // if the mage has teleporting spell still available
            if (game.getPlayers()[game.getTurn()].unusedSpells.contains("T") && action == null) {
                ImageView teleportImg = new ImageView(this);
                teleportImg.setImageResource(t_highlighted);
                actionFrames.get(5).removeAllViews();
                actionFrames.get(5).addView(teleportImg);
                actionFrames.get(5).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        action = CAST_TELEPORT;
                        playGameSound(getApplicationContext(), R.raw.button_click);
                        refreshUI();
                    }
                });
            }
        }
    }

    /**
     * In this method the text corresponding to the next action to be performed is displayed in the TextView at the top of the screen.
     */
    public void setNextAction() {
        String color;
        if(game.getPlayers()[game.getTurn()].getName().length()!=0) {
            color = game.getPlayers()[game.getTurn()].getName() + " ";
        }else {
            color = game.getPlayers()[game.getTurn()].getColor().toString();
            if(isFantasyTheme(this)){
                if (color.equals("WHITE")){
                    color = "BLUE";
                }else{
                    color = "RED";
                }
            }
            color = "Player " + color;
        }

        if(playVersuAI && aiTurn || playOnline && opponentTurn){
            statusBoard.setText("Waiting the opponent's next move");
        }else {
            String tempString = "";
            if (game.getPlayers()[game.getTurn()].hasFrozenPiece()) {
                if (game.getPlayers()[game.getTurn()].getFrozenPieceInformation().charAt(2) == '1')
                    tempString += "; " + game.getPlayers()[game.getTurn()].getFrozenPieceInformation().charAt(2) + " turn to unfreeze piece";
                tempString += "; " + game.getPlayers()[game.getTurn()].getFrozenPieceInformation().charAt(2) + " turns to unfreeze piece";
            }
            if (startingPiece == null) {
                statusBoard.setText(color + " select a piece" + tempString);
            } else {
                if (action == null) {
                    statusBoard.setText(color + " select an action to do from the highlighted");
                } else {
                    statusBoard.setText(color + " select a target");
                }
            }
            statusBoard.setTextColor(Color.WHITE);
        }
    }

    /**
     * This method is invoked to refresh the layout. The steps performed are the followings ones:
     *  - all listeners and ImageView objects are removed from each ViewGroup
     *  - the pieces are redrawn (all ImageView objects are reassigned to the correct FrameLayout)
     *  - listeners are attached to each FrameLayout
     *  - the action buttons are redrawn
     *  - the next action to perform is displayed in the TextView at the top of the screen
     */
    public void refreshUI() {
        removeAllImagesAndListeners();
        drawPiecesOnTheBoard();

        if (playVersuAI && !gameOver) {
            if (aiTurn) {
                setNextAction();
                nextMoveFromAI();
            } else {
                setListenersOnFrames();
                drawActionButtons();
                setNextAction();
            }
        } else {
            if (playOnline) {
                if (!myMoveSent) {
                    if (startingPiece == null && action == null) {
                        timeElapsed = false;
                        final int[] counterOfCycles = {60};
                        final ScheduledExecutorService scheduleTaskExecutor = Executors.newSingleThreadScheduledExecutor();
                        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (myMoveSent || gameEnded || !playOnline || gameOver || opponentTurn) {
                                            scheduleTaskExecutor.shutdownNow();
                                        }
                                        if (counterOfCycles[0] == 0) {
                                            timeElapsed = true;
                                        }
                                        if (timeElapsed) {
                                            Toast.makeText(getApplicationContext(), "You LOST because you didn't do any move", Toast.LENGTH_SHORT).show();
                                            Object[] objects = {"abbandoned"};
                                            new DeleteConnection().execute(objects);
                                            scheduleTaskExecutor.shutdown();
                                        } else {
                                            if (counterOfCycles[0] % 10 == 0) {
                                                if (counterOfCycles[0] == 60) {
                                                    Toast.makeText(getApplicationContext(), "You and your opponent have " + String.valueOf(counterOfCycles[0]) + " seconds to make your move per each turn", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "You still have " + String.valueOf(counterOfCycles[0]) + " seconds to make your move", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                        counterOfCycles[0] = counterOfCycles[0] - 1;
                                    }

                                });
                            }
                        }, 0, 1, TimeUnit.SECONDS);
                    }
                }

                if (myTurn && iPlayed) {
                    setNextAction();
                    iPlayed = false;
                    myTurn = false;
                    Object[] objects = {this};
                    new PostMoveOnline().execute(objects);
                } else {
                    if (myTurn) {
                        setListenersOnFrames();
                        drawActionButtons();
                        setNextAction();
                    } else {
                        setNextAction();
                        Object[] objects = {this};
                        new GetMoveOnline().execute(objects);
                    }
                }
                if (game.isGameOver() || gameEnded || gameOver) {
                    this.deleteMessage = "terminated";
                    Object[] objects = {this.deleteMessage};
                    new DeleteConnection().execute(objects);
                }

            } else {
                if(!gameOver) {
                    setListenersOnFrames();
                    drawActionButtons();
                    setNextAction();
                }
            }
        }
    }

    /**
     * This method is invoked to verify of either the game has incurred in an invalid action or it has ended.
     */
    public void checkGameStatus() {
        // if an error has occurred, print it in the TextView
        if (!game.isGameOver()) {
            Toast.makeText(this, game.getFinalMessage(), Toast.LENGTH_SHORT).show();
        }
        // if the game has ended invoke gameOver()
        else {
            gameOver();
        }
    }

    /**
     * This method is invoked when the game has ended. The steps performed are the following:
     *  - the layout is refreshed
     *  - a message is displayed in order to inform of the outcome of the game
     *  - some statistics regarding the game played and the players
     */
    public void gameOver() {
        gameOver = true;
        // classic_play fanfare only if background music is not playing
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(TOGGLE_MUSIC_KEY, true))
            playGameSound(this, R.raw.end_game_fanfare);

        // refresh the layout
        removeAllImagesAndListeners();
        drawPiecesOnTheBoard();

        // display the outcome message
        String endMessage = game.getFinalMessage();
        boolean hasName = false;
        if (endMessage.startsWith("WHITE") || endMessage.startsWith("BLACK")) {
            if(endMessage.startsWith("WHITE")){
                if(isFantasyTheme(this)){
                    endMessage = "BLUE";
                }
                if(game.getPlayers()[0].getName().length()>0) {
                    endMessage = game.getPlayers()[0].getName();
                    hasName = true;
                }
            }else{
                if(isFantasyTheme(this)){
                    endMessage = "RED";
                }
                if(game.getPlayers()[1].getName().length()>0) {
                    endMessage = game.getPlayers()[1].getName();
                    hasName = true;
                }
            }
            if (!hasName)
                Toast.makeText(this, endMessage.toUpperCase() + " PLAYER HAS WON!!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, endMessage + " HAS WON!!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "THE GAME ENDED IN A " + endMessage, Toast.LENGTH_LONG).show();
        }

        // save game statistics in db
        StatisticsDatabaseImplementation dbImplementation = new StatisticsDatabaseImplementation(this);

        dbImplementation.saveGame(game);

        // we are interested in saving the scores only of human players with a username
        // if both players have no name (this scenario presents only in local games)
        if (!game.getPlayers()[0].getName().equals("") && !game.getPlayers()[1].getName().equals("")) {
            int firstPlayerVictory = (game.getWinner().equals(game.getPlayers()[0].getColor().toString())) ? 1 : 0;
            int secondPlayerVictory = (game.getWinner().equals(game.getPlayers()[1].getColor().toString())) ? 1 : 0;
            // if first player was an ai, don't save him
            if (!game.getPlayers()[0].getName().equals("AI EASY") && !game.getPlayers()[0].getName().equals("AI HARD"))
                dbImplementation.savePlayer(game.getPlayers()[0].getName(), game.getScoreForPlayerAtIndex(0), firstPlayerVictory);
            // if second player was an ai, don't save him
            if (!game.getPlayers()[1].getName().equals("AI EASY") && !game.getPlayers()[1].getName().equals("AI HARD"))
                dbImplementation.savePlayer(game.getPlayers()[1].getName(), game.getScoreForPlayerAtIndex(1), secondPlayerVictory);
        }
    }


    /**
     * This method displays all the valid cells containing a cell on which the currently selected piece can move in
     * compliance with the game rules for the attack.
     */
    public void drawReachableCellsByMove() {
        if (reachableCells != null && reachableCells.size() > 1) {
            for (int k = 1; k < reachableCells.size(); k++) {
                int tmpx = Character.getNumericValue(reachableCells.get(k).charAt(0));
                int tmpy = Character.getNumericValue(reachableCells.get(k).charAt(1));
                FrameLayout frame = this.cellsFrames.get(tmpx * 6 + tmpy);
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.blue_square);
                frame.addView(iv);
            }
        }
        reachableCells = null;
    }

    /**
     * This method displays all the valid cells containing an opponent which can be attacked by the currently selected piece in
     * compliance with the game rules for the attack.
     */
    public void drawReachableCellsByAttack() {
        if (attackableCells != null && attackableCells.size() > 0) {
            for (int k = 0; k < attackableCells.size(); k++) {
                int tmpx = Character.getNumericValue(attackableCells.get(k).charAt(0));
                int tmpy = Character.getNumericValue(attackableCells.get(k).charAt(1));
                FrameLayout frame = this.cellsFrames.get(tmpx * 6 + tmpy);
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.blue_square);
                frame.addView(iv);
            }
        }
        attackableCells = null;
    }

    /**
     * This method handles the display of the action buttons; for each button an ImageView is created, the corresponding drawable is
     * assigned to it and finally it is added to the corresponding FrameLayout.
     */
    public void drawActionButtons() {
        FrameLayout moveFrame = this.actionFrames.get(0);
        ImageView moveImage = new ImageView(this);
        moveImage.setImageResource(m);
        moveFrame.addView(moveImage);

        FrameLayout attackFrame = this.actionFrames.get(1);
        ImageView attackImage = new ImageView(this);
        attackImage.setImageResource(a);
        attackFrame.addView(attackImage);

        FrameLayout freezeFrame = this.actionFrames.get(2);
        ImageView freezeImage = new ImageView(this);
        freezeImage.setImageResource(f);
        freezeFrame.addView(freezeImage);

        FrameLayout healFrame = this.actionFrames.get(3);
        ImageView healImage = new ImageView(this);
        healImage.setImageResource(h);
        healFrame.addView(healImage);

        FrameLayout reviveFrame = this.actionFrames.get(4);
        ImageView reviveImage = new ImageView(this);
        reviveImage.setImageResource(r);
        reviveFrame.addView(reviveImage);

        FrameLayout teleportFrame = this.actionFrames.get(5);
        ImageView teleportImage = new ImageView(this);
        teleportImage.setImageResource(t);
        teleportFrame.addView(teleportImage);
    }

    /**
     * This method performs a complete refresh of all the listeners and ImageView objects in each ViewGroup in this layout.
     */
    void removeAllImagesAndListeners() {
        // remove all listeners and ImageView objects corresponding to each cell
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                FrameLayout frame = this.cellsFrames.get(i * 6 + j);
                frame.removeAllViews();
                frame.setOnClickListener(null);
                FrameLayout lifeFrame = this.lifeFrames.get(i * 6 + j);
                lifeFrame.removeAllViews();
                lifeFrame.setOnClickListener(null);
            }
        }
        // remove all listeners and ImageView objects corresponding to the action buttons
        this.actionFrames.get(0).removeAllViews();
        this.actionFrames.get(0).setOnClickListener(null);
        this.actionFrames.get(1).removeAllViews();
        this.actionFrames.get(1).setOnClickListener(null);
        this.actionFrames.get(2).removeAllViews();
        this.actionFrames.get(2).setOnClickListener(null);
        this.actionFrames.get(3).removeAllViews();
        this.actionFrames.get(3).setOnClickListener(null);
        this.actionFrames.get(4).removeAllViews();
        this.actionFrames.get(4).setOnClickListener(null);
        this.actionFrames.get(5).removeAllViews();
        this.actionFrames.get(5).setOnClickListener(null);
    }

    /**
     * Emit a sound specific to the piece type whenever it is selected
     */
    public void emitSoundOnSelectedPiece() {
        if (startingPiece.getPieceSymbol() == 'K' || startingPiece.getPieceSymbol() == 'k')
            playGameSound(this, R.raw.horse_whinny);
        else if (startingPiece.getPieceSymbol() == 'D' || startingPiece.getPieceSymbol() == 'd')
            playGameSound(this, R.raw.dragon_roar);
        else if (startingPiece.getPieceSymbol() == 'A' || startingPiece.getPieceSymbol() == 'a')
            playGameSound(this, R.raw.bow_stretching);
        else if (startingPiece.getPieceSymbol() == 'G' || startingPiece.getPieceSymbol() == 'g')
            playGameSound(this, R.raw.giant_growling);
        if (startingPiece.getPieceSymbol() == 'S' || startingPiece.getPieceSymbol() == 's')
            playGameSound(this, R.raw.sword_sheathing);
        else if (startingPiece.getPieceSymbol() == 'M' || startingPiece.getPieceSymbol() == 'm')
            playGameSound(this, R.raw.mage_aura);
    }

    /**
     * Emit a sound specific to the piece type whenever it moves
     */
    public void emitMoveSound() {
        if (startingPiece.getPieceSymbol() == 'S' || startingPiece.getPieceSymbol() == 's'
                || startingPiece.getPieceSymbol() == 'M' || startingPiece.getPieceSymbol() == 'm')
            playGameSound(this, R.raw.footstep);
        else if (startingPiece.getPieceSymbol() == 'A' || startingPiece.getPieceSymbol() == 'a')
            playGameSound(this, R.raw.swift_footstep);
        else if (startingPiece.getPieceSymbol() == 'K' || startingPiece.getPieceSymbol() == 'k')
            playGameSound(this, R.raw.calm_horse);
        else if (startingPiece.getPieceSymbol() == 'G' || startingPiece.getPieceSymbol() == 'g')
            playGameSound(this, R.raw.giant_footstep);
        else if (startingPiece.getPieceSymbol() == 'D' || startingPiece.getPieceSymbol() == 'd')
            playGameSound(this, R.raw.dragon_wings_flapping);
    }

    /**
     * Emit a sound specific to the piece type whenever it attacks
     */
    public void emitSoundOnAttack(Piece startingPiece) {
        if (startingPiece.getPieceSymbol() == 'A' || startingPiece.getPieceSymbol() == 'a')
            playGameSound(this, R.raw.arrow_shot);
        else if (startingPiece.getPieceSymbol() == 'K' || startingPiece.getPieceSymbol() == 'k')
            playGameSound(this, R.raw.sword_hit);
        else if (startingPiece.getPieceSymbol() == 'G' || startingPiece.getPieceSymbol() == 'g')
            playGameSound(this, R.raw.giant_roar);
        else if (startingPiece.getPieceSymbol() == 'D' || startingPiece.getPieceSymbol() == 'd')
            playGameSound(this, R.raw.dragonflame);
    }

    /**
     * Emit the sounds specific to the pieces type whenever they engage in a combat
     */
    public void emitSoundOnCombat() {
        emitSoundOnAttack(startingPiece);
        emitSoundOnAttack(destinationPiece);
        if (startingPiece.getPieceSymbol() == 'M' || destinationPiece.getPieceSymbol() == 'M'
                || startingPiece.getPieceSymbol() == 'm' || destinationPiece.getPieceSymbol() == 'm')
            playGameSound(this, R.raw.blastwave);
        else if (startingPiece.getPieceSymbol() == 'S' || startingPiece.getPieceSymbol() == 's'
                || destinationPiece.getPieceSymbol() == 'S' || destinationPiece.getPieceSymbol() == 's')
            playGameSound(this, R.raw.sword_hit);
        if (startingPiece.getVitality() <= 0)
            emitSoundOnDyingPiece(startingPiece);
        if (destinationPiece.getVitality() <= 0)
            emitSoundOnDyingPiece(destinationPiece);
    }

    /**
     * Emit a sound specific to the piece type whenever it dies
     */
    public void emitSoundOnDyingPiece(Piece destinationPiece) {
        if (destinationPiece.getPieceSymbol() == 'S' || destinationPiece.getPieceSymbol() == 's'
                || destinationPiece.getPieceSymbol() == 'M' || destinationPiece.getPieceSymbol() == 'm'
                || destinationPiece.getPieceSymbol() == 'A' || destinationPiece.getPieceSymbol() == 'a'
                || destinationPiece.getPieceSymbol() == 'K' || destinationPiece.getPieceSymbol() == 'k')
            playGameSound(this, R.raw.human_grunt);
        else if (destinationPiece.getPieceSymbol() == 'G' || destinationPiece.getPieceSymbol() == 'g')
            playGameSound(this, R.raw.dying_giant);
        else if (destinationPiece.getPieceSymbol() == 'D' || destinationPiece.getPieceSymbol() == 'd')
            playGameSound(this, R.raw.dying_dragon);
        Toast.makeText(this, "The attacked piece has been killed", Toast.LENGTH_SHORT).show();
    }

    /**
     * This method handles the creation of the ImageView objects for each piece; based on its type, a certain drawable is assigned to
     * this Imageview. Finally, the ImageView is added to the given FrameLayout.
     * @param iv an ImageView for the piece to represent
     * @param c the character indicating the type of the piece
     * @param frame the layout on which to add the ImageView
     */
    private void setPieceImage(ImageView iv, char c, FrameLayout frame) {
        if (isFantasyTheme(this)) {
            if (c == 's') {
                iv.setImageResource(fantasy_squire_black);
            } else if (c == 'S') {
                iv.setImageResource(fantasy_squire_white);
            } else if (c == 'm') {
                iv.setImageResource(fantasy_mage_black);
            } else if (c == 'M') {
                iv.setImageResource(fantasy_mage_white);
            } else if (c == 'k') {
                iv.setImageResource(fantasy_knight_black);
            } else if (c == 'K') {
                iv.setImageResource(fantasy_knight_white);
            } else if (c == 'd') {
                iv.setImageResource(fantasy_dragon_black);
            } else if (c == 'D') {
                iv.setImageResource(fantasy_dragon_white);
            } else if (c == 'g') {
                iv.setImageResource(fantasy_giant_black);
            } else if (c == 'G') {
                iv.setImageResource(fantasy_giant_white);
            } else if (c == 'a') {
                iv.setImageResource(fantasy_archer_black);
            } else if (c == 'A') {
                iv.setImageResource(fantasy_archer_white);
            }
        }
        else {
            if (c == 's') {
                iv.setImageResource(classic_squire_blk);
            } else if (c == 'S') {
                iv.setImageResource(classic_squire_wht);
            } else if (c == 'm') {
                iv.setImageResource(classic_mage_blk);
            } else if (c == 'M') {
                iv.setImageResource(classic_mage_wht);
            } else if (c == 'k') {
                iv.setImageResource(classic_knight_blk);
            } else if (c == 'K') {
                iv.setImageResource(classic_knight_wht);
            } else if (c == 'd') {
                iv.setImageResource(classic_dragon_blk);
            } else if (c == 'D') {
                iv.setImageResource(classic_dragon_wht);
            } else if (c == 'g') {
                iv.setImageResource(classic_giant_blk);
            } else if (c == 'G') {
                iv.setImageResource(classic_giant_wht);
            } else if (c == 'a') {
                iv.setImageResource(classic_archer_blk);
            } else if (c == 'A') {
                iv.setImageResource(classic_archer_wht);
            }
        }
        frame.addView(iv);
    }

    /**
     * This method handles the creation of the frozen ImageView objects for each piece; based on its type, a certain drawable is assigned
     * to this Imageview. Finally, the ImageView is added to the given FrameLayout.
     * @param iv an ImageView for the piece to represent
     * @param c the character indicating the type of the piece
     * @param frame the layout on which to add the ImageView
     */
    private void setFrozenPieceImage(ImageView iv, char c, FrameLayout frame) {
        if (isFantasyTheme(this)) {
            if (c == 's') {
                iv.setImageResource(fantasy_squire_black_frozen);
            } else if (c == 'S') {
                iv.setImageResource(fantasy_squire_white_frozen);
            } else if (c == 'k') {
                iv.setImageResource(fantasy_knight_black_frozen);
            } else if (c == 'K') {
                iv.setImageResource(fantasy_knight_white_frozen);
            } else if (c == 'd') {
                iv.setImageResource(fantasy_dragon_black_frozen);
            } else if (c == 'D') {
                iv.setImageResource(fantasy_dragon_white_frozen);
            } else if (c == 'g') {
                iv.setImageResource(fantasy_giant_black_frozen);
            } else if (c == 'G') {
                iv.setImageResource(fantasy_giant_white_frozen);
            } else if (c == 'a') {
                iv.setImageResource(fantasy_archer_black_frozen);
            } else if (c == 'A') {
                iv.setImageResource(fantasy_archer_white_frozen);
            }
        }
        else {
            if (c == 's') {
                iv.setImageResource(classic_squire_black_frozen);
            } else if (c == 'S') {
                iv.setImageResource(classic_squire_white_frozen);
            } else if (c == 'k') {
                iv.setImageResource(classic_knight_black_frozen);
            } else if (c == 'K') {
                iv.setImageResource(classic_knight_white_frozen);
            } else if (c == 'd') {
                iv.setImageResource(classic_dragon_black_frozen);
            } else if (c == 'D') {
                iv.setImageResource(classic_dragon_white_frozen);
            } else if (c == 'g') {
                iv.setImageResource(classic_giant_black_frozen);
            } else if (c == 'G') {
                iv.setImageResource(classic_giant_white_frozen);
            } else if (c == 'a') {
                iv.setImageResource(classic_archer_black_frozen);
            } else if (c == 'A') {
                iv.setImageResource(classic_archer_white_frozen);
            }
        }
        frame.addView(iv);
    }

    /**
     * In this method all the FrameLayout objects corresponding to the actions to be performed in the game are assigned to a list.
     */
    public void addActionToFrames() {
        actionFrames.add((FrameLayout) findViewById(R.id.move_frame));
        actionFrames.add((FrameLayout) findViewById(R.id.attack_frame));
        actionFrames.add((FrameLayout) findViewById(R.id.freeze_frame));
        actionFrames.add((FrameLayout) findViewById(R.id.heal_frame));
        actionFrames.add((FrameLayout) findViewById(R.id.revive_frame));
        actionFrames.add((FrameLayout) findViewById(R.id.teleport_frame));
    }

    /**
     * In this method all the FrameLayout objects corresponding to the vitalities of a piece are assigned to a list. Note that in the
     * layout each cell has been assigned a slot to display a piece's vitality.
     */
    public void addLifesToFrames() {
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_1_1));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_1_2));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_1_3));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_1_4));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_1_5));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_1_6));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_2_1));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_2_2));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_2_3));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_2_4));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_2_5));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_2_6));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_3_1));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_3_2));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_3_3));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_3_4));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_3_5));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_3_6));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_4_1));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_4_2));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_4_3));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_4_4));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_4_5));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_4_6));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_5_1));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_5_2));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_5_3));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_5_4));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_5_5));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_5_6));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_6_1));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_6_2));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_6_3));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_6_4));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_6_5));
        lifeFrames.add((FrameLayout) findViewById(R.id.life_cell_6_6));
    }

    /**
     * In this method all the FrameLayout objects, each representing a cell, defined in the layout are assigned to a list, representing
     * the chessboard as a list of cells.
     */
    public void addCellsToFrames() {
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_1_1));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_1_2));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_1_3));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_1_4));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_1_5));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_1_6));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_2_1));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_2_2));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_2_3));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_2_4));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_2_5));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_2_6));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_3_1));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_3_2));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_3_3));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_3_4));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_3_5));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_3_6));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_4_1));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_4_2));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_4_3));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_4_4));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_4_5));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_4_6));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_5_1));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_5_2));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_5_3));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_5_4));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_5_5));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_5_6));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_6_1));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_6_2));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_6_3));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_6_4));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_6_5));
        cellsFrames.add((FrameLayout) findViewById(R.id.cell_6_6));
    }

    /**
     * This method returns the int value corresponding to the row position the given frame would have in a 6x6 matrix corresponding
     * to the chessboard.
     * @param frame the FrameView object of which to retrieve its row value
     * @return int value corresponding to the given frame's row position in a 6x6 matrix
     */
    public int getXFromFrameId(FrameLayout frame) {
        if (frame.equals(findViewById(R.id.cell_1_1)) || frame.equals(findViewById(R.id.cell_1_2)) || frame.equals(findViewById(R.id.cell_1_3))
                || frame.equals(findViewById(R.id.cell_1_4)) || frame.equals(findViewById(R.id.cell_1_5)) || frame.equals(findViewById(R.id.cell_1_6))) {
            return 1;
        }
        if (frame.equals(findViewById(R.id.cell_2_1)) || frame.equals(findViewById(R.id.cell_2_2)) || frame.equals(findViewById(R.id.cell_2_3))
                || frame.equals(findViewById(R.id.cell_2_4)) || frame.equals(findViewById(R.id.cell_2_5)) || frame.equals(findViewById(R.id.cell_2_6))) {
            return 2;
        }
        if (frame.equals(findViewById(R.id.cell_3_1)) || frame.equals(findViewById(R.id.cell_3_2)) || frame.equals(findViewById(R.id.cell_3_3))
                || frame.equals(findViewById(R.id.cell_3_4)) || frame.equals(findViewById(R.id.cell_3_5)) || frame.equals(findViewById(R.id.cell_3_6))) {
            return 3;
        }
        if (frame.equals(findViewById(R.id.cell_4_1)) || frame.equals(findViewById(R.id.cell_4_2)) || frame.equals(findViewById(R.id.cell_4_3))
                || frame.equals(findViewById(R.id.cell_4_4)) || frame.equals(findViewById(R.id.cell_4_5)) || frame.equals(findViewById(R.id.cell_4_6))) {
            return 4;
        }
        if (frame.equals(findViewById(R.id.cell_5_1)) || frame.equals(findViewById(R.id.cell_5_2)) || frame.equals(findViewById(R.id.cell_5_3))
                || frame.equals(findViewById(R.id.cell_5_4)) || frame.equals(findViewById(R.id.cell_5_5)) || frame.equals(findViewById(R.id.cell_5_6))) {
            return 5;
        }
        if (frame.equals(findViewById(R.id.cell_6_1)) || frame.equals(findViewById(R.id.cell_6_2)) || frame.equals(findViewById(R.id.cell_6_3))
                || frame.equals(findViewById(R.id.cell_6_4)) || frame.equals(findViewById(R.id.cell_6_5)) || frame.equals(findViewById(R.id.cell_6_6))) {
            return 6;
        }
        return -1;
    }

    /**
     * This method returns the int value corresponding to the column position the given frame would have in a 6x6 matrix corresponding
     * to the chessboard.
     * @param frame the FrameView object of which to retrieve its column value
     * @return int value corresponding to the given frame's column position in a 6x6 matrix
     */
    public int getYFromFrameId(FrameLayout frame) {
        if (frame.equals(findViewById(R.id.cell_1_1)) || frame.equals(findViewById(R.id.cell_2_1)) || frame.equals(findViewById(R.id.cell_3_1))
                || frame.equals(findViewById(R.id.cell_4_1)) || frame.equals(findViewById(R.id.cell_5_1)) || frame.equals(findViewById(R.id.cell_6_1))) {
            return 1;
        }
        if (frame.equals(findViewById(R.id.cell_1_2)) || frame.equals(findViewById(R.id.cell_2_2)) || frame.equals(findViewById(R.id.cell_3_2))
                || frame.equals(findViewById(R.id.cell_4_2)) || frame.equals(findViewById(R.id.cell_5_2)) || frame.equals(findViewById(R.id.cell_6_2))) {
            return 2;
        }
        if (frame.equals(findViewById(R.id.cell_1_3)) || frame.equals(findViewById(R.id.cell_2_3)) || frame.equals(findViewById(R.id.cell_3_3))
                || frame.equals(findViewById(R.id.cell_4_3)) || frame.equals(findViewById(R.id.cell_5_3)) || frame.equals(findViewById(R.id.cell_6_3))) {
            return 3;
        }
        if (frame.equals(findViewById(R.id.cell_1_4)) || frame.equals(findViewById(R.id.cell_2_4)) || frame.equals(findViewById(R.id.cell_3_4))
                || frame.equals(findViewById(R.id.cell_4_4)) || frame.equals(findViewById(R.id.cell_5_4)) || frame.equals(findViewById(R.id.cell_6_4))) {
            return 4;
        }
        if (frame.equals(findViewById(R.id.cell_1_5)) || frame.equals(findViewById(R.id.cell_2_5)) || frame.equals(findViewById(R.id.cell_3_5))
                || frame.equals(findViewById(R.id.cell_4_5)) || frame.equals(findViewById(R.id.cell_5_5)) || frame.equals(findViewById(R.id.cell_6_5))) {
            return 5;
        }
        if (frame.equals(findViewById(R.id.cell_1_6)) || frame.equals(findViewById(R.id.cell_2_6)) || frame.equals(findViewById(R.id.cell_3_6))
                || frame.equals(findViewById(R.id.cell_4_6)) || frame.equals(findViewById(R.id.cell_5_6)) || frame.equals(findViewById(R.id.cell_6_6))) {
            return 6;
        }
        return -1;
    }


    /**
     * In order to display a dialog in an activity, the DialogFragment should be used. It acts as a container for the dialog, providing
     * all controls to create it and manage its appearance.It also ensures to correctly handle lifecycle events such as when the user
     * presses the back button.
     */
    public static class QuitGameDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.exit_dialog)
                    .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // invoke the quitGame() method defined in PlayActivity
                            ((PlayActivity) getActivity()).quitGame();
                        }
                    })
                    .setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            playGameSound(getContext(), R.raw.button_pressed);
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
