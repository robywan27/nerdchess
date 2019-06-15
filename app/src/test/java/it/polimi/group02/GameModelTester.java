package it.polimi.group02;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import it.polimi.group02.model.GameModel;
import it.polimi.group02.model.piece.Archer;
import it.polimi.group02.model.piece.Dragon;
import it.polimi.group02.model.piece.Giant;
import it.polimi.group02.model.piece.Knight;
import it.polimi.group02.model.piece.Mage;
import it.polimi.group02.model.piece.Squire;
import it.polimi.group02.model.utility.Status;
import it.polimi.group02.model.validator.DiagonalAttackValidator;
import it.polimi.group02.model.validator.HorizontalVerticalAttackValidator;

import static it.polimi.group02.model.utility.Color.BLACK;
import static it.polimi.group02.model.utility.Color.WHITE;
import static it.polimi.group02.model.utility.Utility.NUMBER_OF_PLAYERS;
import static it.polimi.group02.model.utility.Utility.UNUSED_SPELLS;
import static it.polimi.group02.model.utility.Utility.squire_initial_vitality;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;



/**
 * This class has been designed to perform unit tests in order to assess the correct execution of the methods provided by the
 * GameModel class.
 */
public class GameModelTester {
    /**
     * Model instance.
     */
    private GameModel gameModel;
    /**
     * This attribute stores the current turn.
     */
    private int turn;


    /**
     * This method is set up each type a test is performed. Its purpose is to initialize the needed variables and setting the environment.
     */
    @Before
    public void initialization() {
        gameModel = new GameModel();
        gameModel.initializePlayer("", 0, WHITE);
        gameModel.initializePlayer("", 1, BLACK);
        gameModel.createStandardConfigurationMatrix();
        gameModel.createPieces();
        turn = gameModel.getTurn();
    }



    /**
     * This method tests the correct creation of a game with a configuration different from the initial standard one.
     */
    @Test
    public void testCreateCustomConfigurationBoard() {
        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("D00000GK00sa00S0kmMK00sdAS00k000000g");
        gameModel.createPieces();

        assertEquals("Cell 0x0 has inside the player one dragon",'D',this.gameModel.getCurrentConfiguration()[0][0]);
        assertEquals("Cell 0x1 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][1]);
        assertEquals("Cell 0x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][2]);
        assertEquals("Cell 0x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][3]);
        assertEquals("Cell 0x4 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][4]);
        assertEquals("Cell 0x5 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][5]);

        assertEquals("Cell 1x0 has inside the player one giant",'G',this.gameModel.getCurrentConfiguration()[1][0]);
        assertEquals("Cell 1x1 has inside the player one first knight",'K',this.gameModel.getCurrentConfiguration()[1][1]);
        assertEquals("Cell 1x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[1][2]);
        assertEquals("Cell 1x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[1][3]);
        assertEquals("Cell 1x4 has inside the player two first squire",'s',this.gameModel.getCurrentConfiguration()[1][4]);
        assertEquals("Cell 1x5 has inside the player two archer",'a',this.gameModel.getCurrentConfiguration()[1][5]);

        assertEquals("Cell 2x0 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[2][0]);
        assertEquals("Cell 2x1 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[2][1]);
        assertEquals("Cell 2x2 has inside the player one first squire",'S',this.gameModel.getCurrentConfiguration()[2][2]);
        assertEquals("Cell 2x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[2][3]);
        assertEquals("Cell 2x4 has inside the player two first knight",'k',this.gameModel.getCurrentConfiguration()[2][4]);
        assertEquals("Cell 2x5 has inside the player two mage",'m',this.gameModel.getCurrentConfiguration()[2][5]);

        assertEquals("Cell 3x0 has inside the player one mage",'M',this.gameModel.getCurrentConfiguration()[3][0]);
        assertEquals("Cell 3x1 has inside the player one second knight",'K',this.gameModel.getCurrentConfiguration()[3][1]);
        assertEquals("Cell 3x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[3][2]);
        assertEquals("Cell 3x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[3][3]);
        assertEquals("Cell 3x4 has inside the player two second squire",'s',this.gameModel.getCurrentConfiguration()[3][4]);
        assertEquals("Cell 3x5 has inside the player two dragon",'d',this.gameModel.getCurrentConfiguration()[3][5]);

        assertEquals("Cell 4x0 has inside the player one archer",'A',this.gameModel.getCurrentConfiguration()[4][0]);
        assertEquals("Cell 4x1 has inside the player one second squire",'S',this.gameModel.getCurrentConfiguration()[4][1]);
        assertEquals("Cell 4x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[4][2]);
        assertEquals("Cell 4x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[4][3]);
        assertEquals("Cell 4x4 has inside the player two first knight",'k',this.gameModel.getCurrentConfiguration()[4][4]);
        assertEquals("Cell 4x5 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[4][5]);

        assertEquals("Cell 5x0 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][0]);
        assertEquals("Cell 5x1 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][1]);
        assertEquals("Cell 5x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][2]);
        assertEquals("Cell 5x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][3]);
        assertEquals("Cell 5x4 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][4]);
        assertEquals("Cell 5x5 has inside the player two giant",'g',this.gameModel.getCurrentConfiguration()[5][5]);
    }


    /**
     * This method tests the correct creation of a game in its initial standard configuration.
     */
    @Test
    public void testCreateStandardConfigurationBoard() {
        assertEquals("Cell 0x0 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][0]);
        assertEquals("Cell 0x1 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][1]);
        assertEquals("Cell 0x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][2]);
        assertEquals("Cell 0x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][3]);
        assertEquals("Cell 0x4 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][4]);
        assertEquals("Cell 0x5 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[0][5]);

        assertEquals("Cell 1x0 has inside the player one giant",'G',this.gameModel.getCurrentConfiguration()[1][0]);
        assertEquals("Cell 1x1 has inside the player one first knight",'K',this.gameModel.getCurrentConfiguration()[1][1]);
        assertEquals("Cell 1x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[1][2]);
        assertEquals("Cell 1x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[1][3]);
        assertEquals("Cell 1x4 has inside the player two first squire",'s',this.gameModel.getCurrentConfiguration()[1][4]);
        assertEquals("Cell 1x5 has inside the player two archer",'a',this.gameModel.getCurrentConfiguration()[1][5]);

        assertEquals("Cell 2x0 has inside the player one dragon",'D',this.gameModel.getCurrentConfiguration()[2][0]);
        assertEquals("Cell 2x1 has inside the player one first squire",'S',this.gameModel.getCurrentConfiguration()[2][1]);
        assertEquals("Cell 2x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[2][2]);
        assertEquals("Cell 2x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[2][3]);
        assertEquals("Cell 2x4 has inside the player two first knight",'k',this.gameModel.getCurrentConfiguration()[2][4]);
        assertEquals("Cell 2x5 has inside the player two mage",'m',this.gameModel.getCurrentConfiguration()[2][5]);

        assertEquals("Cell 3x0 has inside the player one mage",'M',this.gameModel.getCurrentConfiguration()[3][0]);
        assertEquals("Cell 3x1 has inside the player one second knight",'K',this.gameModel.getCurrentConfiguration()[3][1]);
        assertEquals("Cell 3x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[3][2]);
        assertEquals("Cell 3x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[3][3]);
        assertEquals("Cell 3x4 has inside the player two second squire",'s',this.gameModel.getCurrentConfiguration()[3][4]);
        assertEquals("Cell 3x5 has inside the player two dragon",'d',this.gameModel.getCurrentConfiguration()[3][5]);

        assertEquals("Cell 4x0 has inside the player one archer",'A',this.gameModel.getCurrentConfiguration()[4][0]);
        assertEquals("Cell 4x1 has inside the player one second squire",'S',this.gameModel.getCurrentConfiguration()[4][1]);
        assertEquals("Cell 4x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[4][2]);
        assertEquals("Cell 4x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[4][3]);
        assertEquals("Cell 4x4 has inside the player two first knight",'k',this.gameModel.getCurrentConfiguration()[4][4]);
        assertEquals("Cell 4x5 has inside the player two giant",'g',this.gameModel.getCurrentConfiguration()[4][5]);

        assertEquals("Cell 5x0 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][0]);
        assertEquals("Cell 5x1 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][1]);
        assertEquals("Cell 5x2 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][2]);
        assertEquals("Cell 5x3 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][3]);
        assertEquals("Cell 5x4 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][4]);
        assertEquals("Cell 5x5 has no pieces inside",'0',this.gameModel.getCurrentConfiguration()[5][5]);
    }


    /**
     * This method tests the correct creation of the pieces according to the given configuration.
     */
    @Test
    public void createPiecesTest() {
        assertEquals("Cell 0x0 has no pieces inside", null, this.gameModel.getPieceAtTurn(turn, 0, 0));
        assertEquals("Cell 0x0 has no pieces inside", null, this.gameModel.getPieceAtTurn(turn, 1, 0));
        assertEquals("Cell 0x0 has no pieces inside", null, this.gameModel.getPieceAtTurn(turn, 5, 0));
        assertFalse(new Knight('K', 3, 2).equals(this.gameModel.getPieceAtTurn(turn, 2, 3))); // -> returns null

        // in order for these test to run you need to override equals and hashCode methods in class Piece
        assertTrue("Player one giant", new Giant('G', 1, 0).equals(this.gameModel.getPieceAtTurn(turn, 0, 1)));
        assertTrue("Player one first knight", new Knight('K', 1, 1).equals(this.gameModel.getPieceAtTurn(turn, 1, 1)));
        assertTrue("Player two first squire", new Squire('s', 1, 4).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 4, 1)));
        assertTrue("Player two archer", new Archer('a', 1, 5).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 5, 1)));

        assertTrue("Player one dragon", new Dragon('D', 2, 0).equals(this.gameModel.getPieceAtTurn(turn, 0, 2)));
        assertTrue("Player one first squire", new Squire('S', 2, 1).equals(this.gameModel.getPieceAtTurn(turn, 1, 2)));
        assertTrue("Player two first knight", new Knight('k', 2, 4).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 4, 2)));
        assertTrue("Player two mage", new Mage('m', 2, 5).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 5, 2)));

        assertTrue("Player one mage", new Mage('M', 3, 0).equals(this.gameModel.getPieceAtTurn(turn, 0, 3)));
        assertTrue("Player one second knight", new Knight('K', 3, 1).equals(this.gameModel.getPieceAtTurn(turn, 1, 3)));
        assertTrue("Player two second squire", new Squire('s', 3, 4).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 4, 3)));
        assertTrue("Player two dragon", new Dragon('d', 3, 5).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 5, 3)));

        assertTrue("Player one archer", new Archer('A', 4, 0).equals(this.gameModel.getPieceAtTurn(turn, 0, 4)));
        assertTrue("Player one second squire", new Squire('S', 4, 1).equals(this.gameModel.getPieceAtTurn(turn, 1, 4)));
        assertTrue("Player two second knight", new Knight('k', 4, 4).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 4, 4)));
        assertTrue("Player two giant", new Giant('g', 4, 5).equals(this.gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 5, 4)));
    }


    /**
     * This method tests the correct parsing of an action.
     */
    @Test
    public void testParseAction() {
        // Assess: invalid format of string, positions out of fantasy_board, not playing player moves
        assertEquals("Both positions out of fantasy_board", "ERROR: the final position is out of the fantasy_board", gameModel.parseAction("A8109"));
        assertEquals("Source position out of fantasy_board", "ERROR: the final position is out of the fantasy_board", gameModel.parseAction("M3801"));
        assertEquals("Destination position out of fantasy_board", "ERROR: the final position is out of the fantasy_board", gameModel.parseAction("A2372"));

        // move player 2 first squire
        assertEquals("Source position contains a piece of the non-playing player", "ERROR: the starting player isn't correct", gameModel.parseAction("M5242"));
        // move player 2 first knight
        assertEquals("Source position contains a piece of the non-playing player", "ERROR: the starting player isn't correct", gameModel.parseAction("M5344"));

        /*
         Assessing the correctness of the validators
          */
        /*
         Assess movements
          */
        /*
         Assess: out of range moves, not playing player moves in two turns, empty cell move
          */
        assertEquals("Invalid move: first knight out of range", "ERROR: it's not possible to make a move", gameModel.parseAction("M2242"));
        // check if the knight has moved from its initial position (now in position 4,2)
        assertNotEquals("Invalid move: no piece selected", "ERROR: the starting player isn't correct", gameModel.parseAction("M2242"));
        // move the knight classic_back to its initial position
        assertNotEquals("Invalid move: no piece selected", "ERROR: it's not possible to make a move", gameModel.parseAction("M4222"));
        assertEquals("Invalid move: dragon out of range", "ERROR: it's not possible to make a move", gameModel.parseAction("M1352"));

        // now set the turn to player 2
        gameModel.repriseTurn();
        assertEquals("Turn must be of second player", 1, gameModel.getTurn());
        assertNotEquals("Invalid move: archer out of range", "ERROR: it's not possible to make a move", gameModel.parseAction("M6261"));
        assertEquals("Invalid move: second knight out of range", "ERROR: it's not possible to make a move", gameModel.parseAction("M5543"));
        // move first squire of player 1
        assertEquals("Invalid move: player 1 can't classic_play in this turn", "ERROR: the starting player isn't correct", gameModel.parseAction("M2536"));

        // classic_back to player 1 turn
        gameModel.repriseTurn();
        /*
         Assess: pieces moved in cells occupied by allies, dragon flying over its allies
          */
        assertEquals("Invalid move: can't move first squire over allied first knight", "ERROR: it's not possible to make a move", gameModel.parseAction("M2322"));
        assertEquals("Invalid move: can't move giant over allied first squire", "ERROR: it's not possible to make a move", gameModel.parseAction("M1223"));
        assertEquals("Invalid move: can't move dragon piece over allied second knight", "ERROR: it's not possible to make a move", gameModel.parseAction("M1324"));
        assertEquals("Invalid move: can't move dragon piece over allied second squire", "ERROR: it's not possible to make a move", gameModel.parseAction("M1325"));


        /*
         Assess attacks
         */
        /*
         Assess: mage/squire attack, attacks targeted to nothing, out of range attacks, horizontal/vertical attacks, attack ally, no piece (ally or enemy) in between the target
         */
        // mage/squire attacks
        assertEquals("Invalid attack: mage attacking", "ERROR: neither mage nor squire can attack", gameModel.parseAction("A1424"));
        assertEquals("Invalid attack: first squire attacking", "ERROR: neither mage nor squire can attack", gameModel.parseAction("A2322"));
        assertEquals("Invalid attack: second squire attacking", "ERROR: neither mage nor squire can attack", gameModel.parseAction("A2515"));

        // attacks targeted to nothing
        assertEquals("Invalid attack: giant attacking empty cell", "ERROR: it's not possible to make an attack", gameModel.parseAction("A1211"));
        assertEquals("Invalid attack: archer attacking empty cell", "ERROR: it's not possible to make an attack", gameModel.parseAction("A1516"));

        // out of range attacks, horizontal/vertical attacks, attack ally, no piece (ally or enemy) in between the target
        assertEquals("Move dragon three cells to the right", "", gameModel.parseAction("M1343"));
        // now the dragon is at position (4,3)
        assertEquals("Invalid attack: dragon out of range", "ERROR: it's not possible to make an attack", gameModel.parseAction("A4354"));
        assertEquals("Invalid attack: archer attacking his comrade mage", "ERROR: you can't attack an ally", gameModel.parseAction("A1514"));
        assertEquals("Invalid attack: dragon attacks enemy mage, but enemy knight is in between", "ERROR: it's not possible to make an attack", gameModel.parseAction("A4363"));
        assertEquals("Move dragon classic_back to its initial position", "", gameModel.parseAction("M4313"));
        // now the dragon is at position (1,3)
        assertEquals("Invalid attack: archer attacks allied dragon, but allied mage is in between", "ERROR: you can't attack an ally", gameModel.parseAction("A1513"));

        assertEquals("Move archer one cell down and one right", "", gameModel.parseAction("M1526"));
        assertEquals("Move archer one cell right and one up", "", gameModel.parseAction("M2635"));
        assertEquals("Invalid attack: archer attacks enemy giant, but enemy knight is in between", "ERROR: it's not possible to make an attack", gameModel.parseAction("A3565"));
    }


    /**
     * This method tests the correct performance of a move.
     */
    @Test
    public void testPerformMove() {
        assertEquals("", "ERROR: it's not possible to make a move", gameModel.parseAction("M1212"));
        assertEquals("", "ERROR: it's not possible to make a move", gameModel.parseAction("M1515"));
        gameModel.parseAction("M2333");
        assertEquals("First squire is in position (3,3)", 'S', gameModel.getCurrentConfiguration()[2][2]);
        assertEquals("First squire is in position (3,3)", 2, gameModel.getPieceAtTurn(turn, 2, 2).getxCoordinate());
        assertEquals("First squire is in position (3,3)", 2, gameModel.getPieceAtTurn(turn, 2, 2).getyCoordinate());

        gameModel.parseAction("M1334");
        assertEquals("Dragon is in position (3,4)", 'D', gameModel.getCurrentConfiguration()[3][2]);
        assertEquals("Dragon is in position (3,4)", 3, gameModel.getPieceAtTurn(turn, 2, 3).getxCoordinate());
        assertEquals("Dragon is in position (3,4)", 2, gameModel.getPieceAtTurn(turn, 2, 3).getyCoordinate());
    }


    /**
     * This method tests the correct performance of horizontal/vertical attacks.
     */
    @Test
    public void testPerformHVAttack() {
        gameModel.parseAction("M1333");
        gameModel.parseAction("A3353");
        // knight vitality = 4; dragon strength = 3 -> knight remaining vitality = 1
        assertEquals("Dragon attacks knight at position (5,3)", 'k', gameModel.getCurrentConfiguration()[2][4]);
        assertEquals("Dragon attacks knight at position (5,3)", 1, gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 4, 2).getVitality());

        gameModel.parseAction("M3334");
        gameModel.parseAction("A3454");
        // squire vitality = 1; dragon strength = 3 -> squire remaining vitality = -2 -> squire removed
        assertEquals("Dragon attacks squire at position (5,4)", '0', gameModel.getCurrentConfiguration()[3][4]);
        assertEquals("Dragon attacks squire at position (5,4)", null, gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 4, 3));

        gameModel.repriseTurn();
        gameModel.parseAction("M6444");
        gameModel.parseAction("A4434");
        assertEquals("", 3, gameModel.getPieceAtTurn(turn, 2, 3).getVitality());

        gameModel.parseAction("M4433");
        gameModel.parseAction("M3313");
        gameModel.parseAction("A1312");
        assertEquals("", 2, gameModel.getPieceAtTurn(turn, 0, 1).getVitality());
        gameModel.parseAction("A1314");
        assertEquals("", 4, gameModel.getPieceAtTurn(turn, 0, 3).getVitality());
    }


    /**
     * This method tests the correct performance of diagonal attacks.
     */
    @Test
    public void testPerformDiagonalAttack() {
        // change turn
        gameModel.repriseTurn();
        gameModel.parseAction("M5544");
        gameModel.parseAction("M4433");
        gameModel.parseAction("A3322");
        assertEquals("Second knight attacks first enemy knight at position (2,2)", 'K', gameModel.getCurrentConfiguration()[1][1]);
        assertEquals("Second knight attacks first enemy knight at position (2,2)", 2, gameModel.getPieceAtTurn(turn, 1, 1).getVitality());
        gameModel.repriseTurn();

        gameModel.parseAction("A2233");
        assertEquals("", 'k', gameModel.getCurrentConfiguration()[2][2]);
        assertEquals("", 2, gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 2, 2).getVitality());

        gameModel.parseAction("M1332");
        gameModel.playTurn("M3242");
        gameModel.parseAction("A3342");
        assertEquals("", 4, gameModel.getPieceAtTurn(turn, 3, 1).getVitality());
        gameModel.parseAction("A3324");
        assertEquals("", 2, gameModel.getPieceAtTurn(turn, 1, 3).getVitality());
    }


    /**
     * This method tests the correctness of a combat.
     */
    @Test
    public void testEngageInBattle() {
        /*
         1) moving dragon vs knight -> dragon victory
          */
        gameModel.parseAction("M1343");
        gameModel.parseAction("M4353");
        // outcome: dragon v: 2; knight v: -2
        assertEquals("Dragon has moved over the cell occupied by the enemy first knight: they engaged in a battle", 2, gameModel.getPieceAtTurn(turn, 4, 2).getVitality());
        assertEquals("Dragon has moved over the cell occupied by the enemy first knight: they engaged in a battle", 'D', gameModel.getCurrentConfiguration()[2][4]);

        /*
         2) knight vs knight -> both die
          */
        gameModel.parseAction("M2434");
        gameModel.parseAction("M3444");
        gameModel.parseAction("M4455");
        // outcome: knight v = 0; enemy knight v: 0
        assertEquals("Second knight has moved over the cell occupied by the enemy second knight: they engaged in a battle", '0', gameModel.getCurrentConfiguration()[3][3]);
        assertEquals("Second knight has moved over the cell occupied by the enemy second knight: they engaged in a battle", '0', gameModel.getCurrentConfiguration()[4][4]);

        /*
        3) moving hurt dragon vs mage -> mage victory
         */
        gameModel.parseAction("M5363");
        // outcome: dragon v = 0; mage v = 4
        assertEquals("Dragon has moved over the cell occupied by the enemy mage: they engaged in a battle", '0', gameModel.getCurrentConfiguration()[2][4]);
        assertEquals("Dragon has moved over the cell occupied by the enemy mage: they engaged in a battle", 4, gameModel.getPieceAtTurn((turn + 1) % NUMBER_OF_PLAYERS, 5, 2).getVitality());
    }


    /**
     * This method checks all heal constraints.
     */
    @Test
    public void testHealSpell(){
        assertEquals("", "ERROR: you can't heal a mage", gameModel.parseAction("H1400"));
        assertEquals("", "ERROR: the starting player isn't correct", gameModel.parseAction("H6300"));
        gameModel.parseAction("M1211");
        assertEquals("", "ERROR: you can't heal a piece standing on a special cell", gameModel.parseAction("H1100"));
        // can heal a piece with full life
        assertEquals("", "", gameModel.parseAction(("H2200")));
        assertEquals("","ERROR: the selected player can heal no more", gameModel.parseAction("H1500"));
    }


    /**
     * This method checks all teleport constraints.
     */
    @Test
    public void testTeleportSpell(){
        assertEquals("","ERROR: you can't teleport a mage", gameModel.parseAction("T1433"));
        assertEquals("","ERROR: the starting player isn't correct", gameModel.parseAction("T5233"));
        assertEquals("","ERROR: you can't teleport a piece to a special cell", gameModel.parseAction("T2511"));
        assertEquals("","ERROR: you can't teleport a piece on a cell occupied by an allied piece", gameModel.parseAction("T2513"));
        gameModel.parseAction("T2444");
        assertEquals("","ERROR: this player can teleport no more", gameModel.parseAction("T2233"));
    }


    /**
     * This method checks all freeze constraints.
     */
    @Test
    public void testFreezeSpell(){
        assertEquals("", "ERROR: You can't freeze a mage", gameModel.parseAction("F6300"));
        gameModel.playTurn("M1211");
        assertEquals("", "ERROR: Can't cast spells on special cells", gameModel.parseAction("F1100"));
        assertEquals("", "ERROR: the starting player isn't correct", gameModel.parseAction("F6200"));
        gameModel.parseAction("F1300");
        assertEquals("", "ERROR: This player cannot cast spells", gameModel.parseAction("F1400"));
    }


    /**
     * This method checks all revive constraints.
     */
    @Test
    public void testReviveSpell(){
        assertEquals("", "ERROR: on the position you are trying to revive there is already another of your pieces", gameModel.parseAction("R1200"));
        gameModel.parseAction("M1332");
        gameModel.playTurn("A3252");    // s: 0
        gameModel.parseAction("M5343");
        assertEquals("", "ERROR: revive can't be cast because in this position originally there was no piece", gameModel.parseAction("R4600"));
        assertEquals("", "ERROR: all the pieces of the kind you're trying to resurrect are still alive", gameModel.parseAction("R5300"));
        assertEquals("", "ERROR: can't revive enemy's piece", gameModel.parseAction("R1200"));
        gameModel.parseAction("M6261");
        gameModel.parseAction("M6362");
        assertEquals("", "ERROR: you can't revive a mage", gameModel.parseAction("R6300"));
        gameModel.parseAction("M5444");
        gameModel.parseAction("R5400");
        assertEquals("", "ERROR: revive can't be cast because already used", gameModel.parseAction("R5200"));
        assertEquals("", '0', gameModel.getCurrentConfiguration()[1][4]);
        assertEquals("", 's', gameModel.getCurrentConfiguration()[3][4]);
    }


    /**
     * This method tests the correct of casting spells.
     */
    @Test
    public void testCastSpell(){
        //teleport the Giant of player one to Dragon player two, both have to die
        gameModel.playTurn("T1264");
        assertEquals("Both pieces died after the giant has been teleported over the dragon", '0', gameModel.getCurrentConfiguration()[1][0]);
        assertEquals("Both pieces died after the giant has been teleported over the dragon", '0', gameModel.getCurrentConfiguration()[3][5]);
        assertEquals("Player one has no more the spell of teleport in his available spells", "FHR0", gameModel.getPlayers()[0].getUnusedSpells());

        //go ahead with the turns and cast a freeze from player two to player 1 dragon
        gameModel.playTurn("F1300");
        assertEquals("The dragon of player one is frozen but still present on the fantasy_board", 'D', gameModel.getCurrentConfiguration()[2][0]);
        assertEquals("Player tow has no more the spell of frozen in his available spells", "0HRT", gameModel.getPlayers()[1].getUnusedSpells());
        assertEquals("Player one has a frozen piece for 3 turns", "133", gameModel.getPlayers()[0].getFrozenPieceInformation());
        assertEquals("Player one has a frozen piece", true, gameModel.getPlayers()[0].hasFrozenPiece());
        assertEquals("The player one dragon has status frozen", Status.FROZEN, gameModel.getPieceAtTurn(0,0,2).getStatus());

        //go ahead with the turns and cast a revive
        gameModel.playTurn("R1200");
        assertEquals("The giant has been revived", 'G', gameModel.getCurrentConfiguration()[1][0]);
        assertEquals("Player two has no more the spell of revive in his available spells", "FH00", gameModel.getPlayers()[0].getUnusedSpells());
        assertEquals("Player one has a frozen piece for 2 turns", "132", gameModel.getPlayers()[0].getFrozenPieceInformation());
        assertEquals("Player one has a frozen piece", true, gameModel.getPlayers()[0].hasFrozenPiece());
        assertEquals("The player one dragon has status frozen", Status.FROZEN, gameModel.getPieceAtTurn(0,0,2).getStatus());

        //go ahead with the turn to see how the remaining turns for the frozen piece decrease
        gameModel.playTurn("M5242");
        assertEquals("Player one has a frozen piece for 2 turns", "132", gameModel.getPlayers()[0].getFrozenPieceInformation());
        gameModel.playTurn("M2232");
        assertEquals("Player one has a frozen piece for 1 turn", "131", gameModel.getPlayers()[0].getFrozenPieceInformation());
        gameModel.playTurn("M5344");
        assertEquals("Player one has a frozen piece for 1 turns", "131", gameModel.getPlayers()[0].getFrozenPieceInformation());
        gameModel.playTurn("M3242");
        assertEquals("The life of the knight after the combat has to be 2", 2, gameModel.getPieceAtTurn(0,3,1).getVitality());
        // dragon has unfrozen
        assertEquals("Player one has a frozen piece for 0 turns", "000", gameModel.getPlayers()[0].getFrozenPieceInformation());
        assertEquals("The frozen piece become unfrozen", "000", gameModel.getPlayers()[0].getFrozenPieceInformation());
        assertEquals("The player one dragon has no more status frozen", Status.ACTIVE, gameModel.getPieceAtTurn(0,0,2).getStatus());

        //revive the dead squire
        gameModel.playTurn("R5200");
        assertEquals("Revived the squire of the second player", 's', gameModel.getCurrentConfiguration()[1][4]);
        assertEquals("player 2 has no more revival spells", "0H0T", gameModel.getPlayers()[1].getUnusedSpells());

        //now use the heal on the knight of the first player
        gameModel.playTurn("H4200");
        assertEquals("Restored initial vitality of the knight", 4, gameModel.getPieceAtTurn(0,3,1).getVitality());
        assertEquals("player 1 has no more heal spells", "F000", gameModel.getPlayers()[0].getUnusedSpells());

        // suppose black has done something irrelevant
        gameModel.repriseTurn();

        // white player turn again
        //now use the frozen over the giant of player 2
        gameModel.playTurn("F6500");
        assertEquals("Player 2 has Giant frozen", true, gameModel.getPlayers()[1].hasFrozenPiece());
        assertEquals("The giant is frozen", Status.FROZEN, gameModel.getPieceAtTurn(1,5,4).getStatus());

        //now try to move the giant to see if it can't move and after that use the teleport over an enemy piece and see if it automatically dies
        gameModel.parseAction("M6566");
        assertEquals("The giant could not move", 'g', gameModel.getCurrentConfiguration()[4][5]);
        gameModel.playTurn("T6525");
        assertEquals("The giant is no more present on the fantasy_board", '0', gameModel.getCurrentConfiguration()[4][5]);
        assertEquals("The squire on which the giant was teleported have maintained the initial vitality", squire_initial_vitality, gameModel.getPieceAtTurn(0,1,4).getVitality());
        assertEquals("Since the giant died the player 2 has no more frozen pieces", "000", gameModel.getPlayers()[1].getFrozenPieceInformation());

        //now we restore the spells to try some cases in which they shouldn't work
        gameModel.getPlayers()[0].setUnusedSpells(UNUSED_SPELLS);
        gameModel.getPlayers()[1].setUnusedSpells(UNUSED_SPELLS);


        //try with player one all the spells over the special coordinates, they should not work
        gameModel.parseAction("T1211");
        assertEquals("The giant has not been teleported", '0', this.gameModel.getCurrentConfiguration()[0][0]);
        assertEquals("The giant has not been teleported", 'G', this.gameModel.getCurrentConfiguration()[1][0]);
        gameModel.parseAction("T1263");
        assertEquals("The giant has not been teleported", 'm', this.gameModel.getCurrentConfiguration()[2][5]);
        assertEquals("The giant has not been teleported", 'G', this.gameModel.getCurrentConfiguration()[1][0]);
        gameModel.parseAction("T1242");
        assertEquals("The giant has not been teleported", 'K', this.gameModel.getCurrentConfiguration()[1][3]);
        assertEquals("The giant has not been teleported", 'G', this.gameModel.getCurrentConfiguration()[1][0]);
        gameModel.parseAction("T1434");
        assertEquals("The mage has not been teleported", 'M', this.gameModel.getCurrentConfiguration()[3][0]);
        assertEquals("The mage has not been teleported", '0', this.gameModel.getCurrentConfiguration()[3][2]);
        gameModel.parseAction("T1536");
        assertEquals("The archer has not been teleported over the special cell", 'A', this.gameModel.getCurrentConfiguration()[4][0]);
        assertEquals("The archer has not been teleported", '0', this.gameModel.getCurrentConfiguration()[5][2]);
        //move the archer
        gameModel.parseAction("T1545");
        assertEquals("The archer has been teleported", 'A', this.gameModel.getCurrentConfiguration()[4][3]);
        assertEquals("The archer has been teleported", '0', this.gameModel.getCurrentConfiguration()[4][0]);

        //we go ahead with the turn
        gameModel.repriseTurn();
        //now try to teleport the enemy piece
        gameModel.parseAction("T4535");
        assertEquals("The archer could not been teleported because enemy", 'A', this.gameModel.getCurrentConfiguration()[4][3]);
        assertEquals("The archer has not been teleported", '0', this.gameModel.getCurrentConfiguration()[4][0]);
        //now attack to decrease archer's life
        assertEquals("The archer's life is full", 5, this.gameModel.getPieceAtTurn(0,3,4).getVitality());
        gameModel.repriseTurn();
        gameModel.parseAction("M4546");
        gameModel.repriseTurn();
        gameModel.parseAction("A5546");
        assertEquals("The archer's life is reduced by the archer attack", 3, this.gameModel.getPieceAtTurn(0,3,5).getVitality());
        //now we move the archer in a special cell and try to heal it
        gameModel.repriseTurn();
        gameModel.parseAction("M4636");
        gameModel.parseAction("H3600");
        assertEquals("The archer's life is reduced by the archer attack and could not be healed in the special cell", 3, this.gameModel.getPieceAtTurn(0,2,5).getVitality());
        gameModel.parseAction("M3646");
        gameModel.parseAction("H4600");
        assertEquals("The archer's life is revived with the heal", 5, this.gameModel.getPieceAtTurn(0,3,5).getVitality());
        //we move the giant in a special cell and try to teleport to it's classic_back position
        gameModel.parseAction("M1211");
        gameModel.getPlayers()[0].setUnusedSpells(UNUSED_SPELLS);
        gameModel.parseAction("T1112");
        assertEquals("The giant has not been teleported", 'G', this.gameModel.getCurrentConfiguration()[0][0]);
        assertEquals("The giant has not been teleported", '0', this.gameModel.getCurrentConfiguration()[1][0]);
        //now we move the archer to attack the mage so we can verify that mage cannot be healed or revived
        gameModel.parseAction("M4644");
        gameModel.parseAction("M4443");
        gameModel.parseAction("A4363");
        assertEquals("The mage's life is reduced by the archer attack", 5, this.gameModel.getPieceAtTurn(1,5,2).getVitality());
        gameModel.repriseTurn();
        gameModel.parseAction("H6300");
        assertEquals("The mage's life is reduced by the archer attack", 5, this.gameModel.getPieceAtTurn(1,5,2).getVitality());
        gameModel.parseAction("H4300");
        assertEquals("The archer's life was decreased by combatting with the knight and was not healed", 1, this.gameModel.getPieceAtTurn(0,3,2).getVitality());
        gameModel.repriseTurn();
        gameModel.parseAction("A4363");
        gameModel.parseAction("A4363");
        gameModel.parseAction("A4363");
        //now we try to revive the mage
        gameModel.repriseTurn();
        gameModel.parseAction("M5242");
        gameModel.getPlayers()[1].setUnusedSpells(UNUSED_SPELLS);
        gameModel.parseAction("R6300");
        assertEquals("Themage can't be revived", '0', this.gameModel.getCurrentConfiguration()[5][2]);
        //the spell can't be cast if the mage is died
        gameModel.parseAction("R6400");
        assertEquals("The dragon can't be revived if the mage is died", '0', this.gameModel.getCurrentConfiguration()[5][3]);
    }


    /**
     * This method tests the correct checks establishing the end of a game.
     */
    @Test
    public void testEndOfGame() {
        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("0000000000s0000000m00000000000000000");
        gameModel.createPieces();
        assertEquals("Player 1 has no pieces; enemy has at least one piece", "BLACK", gameModel.checkEndOfGame());

        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("000000000000000000000000000000000000");
        gameModel.createPieces();
        assertEquals("Player 1 has no pieces; enemy has no pieces", "DRAW", gameModel.checkEndOfGame());

        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("G00M00000000000k0000000d00000000000K");
        gameModel.createPieces();
        assertEquals("Player 1 has at least one piece and has occupied at least three special cells", "WHITE", gameModel.checkEndOfGame());

        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("G00000000000000000000000000000000000");
        gameModel.createPieces();
        assertEquals("Player 1 has at least one piece; enemy has no piece", "WHITE", gameModel.checkEndOfGame());

        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("g00s0000000000000000000G00000000000k");
        gameModel.createPieces();
        assertEquals("Player 1 has at least one piece; enemy has occupied at least three special cells", "BLACK", gameModel.checkEndOfGame());
    }


    /**
     * This method tests the correct string representation of a game.
     */
    @Test
    public void testToString() {
        assertEquals("Testing string representation of the game: standard initial configuration", "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT", gameModel.toString());

        gameModel.playTurn("M1343"); // turn: white moves dragon to (4,3)
        assertEquals("Testing string representation of the game: action 1", "B000000GK00sa0S0DkmMK00sdAS00kg0000005754343634345765000000FHRTFHRT", gameModel.toString());
        gameModel.playTurn("M5242"); // turn: black moves a squire to (4,2)
        assertEquals("Testing string representation of the game: action 2", "W000000GK0s0a0S0DkmMK00sdAS00kg0000005754343364345765000000FHRTFHRT", gameModel.toString());
        gameModel.playTurn("A4353"); // turn: white dragon attacks knight (5,3)
        assertEquals("Testing string representation of the game: action 3", "B000000GK0s0a0S0DkmMK00sdAS00kg0000005754343361345765000000FHRTFHRT", gameModel.toString());
        gameModel.playTurn("M4243"); // turn: black squire moves to (4,3) thus engaging in a combat with dragon
        assertEquals("Testing string representation of the game: action 4", "W000000GK000a0S0DkmMK00sdAS00kg0000005754343513457650000000FHRTFHRT", gameModel.toString());

        // check wins and draw
        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("G00K00000000a000Dk00000s00S000g0000S");
        gameModel.createPieces();
        gameModel.setNumberOfDeadPieces(7);
        gameModel.checkEndOfGame();
        assertEquals("Testing string representation of the game: white player has won because has occupied three cells", "WG00K00000000a000Dk00000s00S000g0000S5553464330000000000000FHRTFHRTWHITE", gameModel.toString());

        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("g00k000000000000D000000000S00000000s");
        gameModel.createPieces();
        gameModel.setNumberOfDeadPieces(11);
        gameModel.checkEndOfGame();
        assertEquals("Testing string representation of the game: black player has won because has occupied three cells", "Wg00k000000000000D000000000S00000000s5346300000000000000000FHRTFHRTBLACK", gameModel.toString());

        gameModel.resetPlayers();
        gameModel.createCustomConfigurationMatrix("000000000000000000000000000000000000");
        gameModel.createPieces();
        gameModel.setNumberOfDeadPieces(16);
        gameModel.checkEndOfGame();
        assertEquals("Testing string representation of the game: draw", "W0000000000000000000000000000000000000000000000000000000000FHRTFHRTDRAW", gameModel.toString());

        // check errors
        gameModel.resetPlayers();
        gameModel.createStandardConfigurationMatrix();
        gameModel.createPieces();
        gameModel.playTurn("M2242");
        assertEquals("Testing string representation of the game: first white knight out of range", "ERROR: it's not possible to make a move", gameModel.toString());

        gameModel.resetPlayers();
        gameModel.createStandardConfigurationMatrix();
        gameModel.createPieces();
        gameModel.playTurn("A1213");
        assertEquals("Testing string representation of the game: white giant attacks white dragon", "ERROR: you can't attack an ally", gameModel.toString());
    }


    /**
     * This method tests the correct setting of vitalities according to the given game configuration.
     */
    @Test
    public void testSetVitalities() {
        assertEquals("Assessing the player and fantasy_board", "W000000GK00saDS00kmMK00sdAS00kg000000", gameModel.toString().substring(0,37));
        gameModel.setVitalities("5675434334345765");
        assertEquals("Assessing setting of vitalities", "5675434334345765", gameModel.toString().substring(37, 53));

        gameModel.setVitalities("0300110010201202");
        assertEquals("Assessing setting of vitalities with no pieces", "0300110010201202", gameModel.toString().substring(37, 53));

        gameModel.setVitalities("0000000000000000");
        assertEquals("Assessing setting of vitalities with no pieces", "0000000000000000", gameModel.toString().substring(37, 53));
    }
}
