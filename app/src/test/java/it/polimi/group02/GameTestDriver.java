package it.polimi.group02;

import org.junit.Before;
import org.junit.Test;

import it.polimi.group02.model.StringToGameConverter;

import static junit.framework.Assert.assertEquals;


/**
 * This class performs a test for each given input string representing a game configuration by comparing an expected string representing
 * the aftermath of the input actions with the resulting string returned by StringToGameConverter's turnTest(String) method.
 */
public class GameTestDriver {
    /**
     * Instance of StringToGameConverter class in order to call the turnTest(String) method.
     */
    private StringToGameConverter stringToGameConverter;

    private String configuration1 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M1343" // turn: white moves dragon to (4,3)
            + "M5242" // turn: black moves a squire to (4,2)
            + "A4353" // turn: white dragon attacks knight (5,3)
            + "M4243" // turn: black squire moves to (4,3) // thus engaging in a combat with dragon
            + "F6400"; // turn: white mage freezes black dragon
    private String result1 = "B" + // moving player\n
            "000000" + // fantasy_board (row 1)
            "GK000a" + // fantasy_board (row 2)
            "0S0Dkm" + // fantasy_board (row 3)
            "MK00sd" + // fantasy_board (row 4)
            "AS00kg" + // fantasy_board (row 5)
            "000000" + // fantasy_board (row 6)
            "5754343513457650" + // vitality
            "000643" + // frozen pieces
            "0HRTFHRT"; // unused spells

    private String configuration2 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M2232"
            + "M6443"
            + "M1211"
            + "M6566"
            + "M2435"
            + "M5242"
            + "M3536"
            + "M5546"
            + "M3241";
    private String result2 = "B" +
            "G00K00" +
            "000s0a" +
            "DS0dkm" +
            "M000s0" +
            "AS0000" +
            "00Kk0g" +
            "5675334436443575" +
            "000000" +
            "FHRTFHRT" +
            "WHITE";

    private String configuration3 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M2232"
            + "F3200"
            + "M1222"
            + "M5242"
            + "T2242"
            + "R5200"
            + "F6400"
            + "M5242"
            + "M4252"
            + "A6252"
            + "H5200"
            + "A6252"
            + "A5262"
            + "H6200";
    private String result3 = "W" +
            "000000" +
            "00K0Ga" +
            "DS00km" +
            "MK00sd" +
            "AS00kg" +
            "000000" +
            "6753434343457650" +
            "000000" +
            "00R0000T";

    private String configuration4 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M2535"
            + "M5546"
            + "M3536"	// w on special cell
            + "M4636"	// b on special cell, combat -> k = 2, S = 0
            + "M2231"
            + "M6261"
            + "M3141"	// w on special cell
            + "A6141"	// K = 2
            + "M4131"	// w moved from special cell
            + "A6131"	// K = 0
            + "M1211"	// w on special cell
            + "M5251"
            + "M1516"
            + "M5141"	// b on special cell
            + "A1636"	// k = 0
            + "M6566";	// b on special cell
    private String result4 = "W" +
            "G00s0a" +
            "000000" +
            "DS00km" +
            "MK00sd" +
            "000000" +
            "A0000g" +
            "5675343435765000" +
            "000000" +
            "FHRTFHRT";

    private String configuration5 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M1343"
            + "F4300"
            + "M1211"
            + "M6566"
            + "M2231"
            + "M5546"
            + "M3141"
            + "M6444"
            + "F4400"
            + "M6261"
            + "M4346"
            + "A6141"
            + "M4636";
    private String result5 = "B" +
            "G00K0a" +
            "0000s0" +
            "0S00km" +
            "MK0ds0" +
            "AS0000" +
            "00D00g" +
            "5753432263435750" +
            "000441" +
            "0HRT0HRT" +
            "WHITE";

    private String configuration6 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "F5521"
            + "M6566";
    private String result6 = "ERROR: invalid action";

    private String configuration7 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M2221"
            + "M6566"
            + "M2300"
            + "M1516"
            + "M5251";
    private String result7 = "ERROR: invalid action";

    private String configuration8 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M2221"
            + "M6566"
            + "M2350";
    private String result8 = "ERROR: invalid action";

    private String configuration9 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M22210"
            + "M6566";
    private String result9 = "ERROR: wrong game configuration, missing or exceeding some final characters";

    private String configuration10 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M0062";
    private String result10 = "ERROR: the final position is out of the fantasy_board";

    private String configuration11 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M6756";
    private String result11 = "ERROR: the final position is out of the fantasy_board";

    private String configuration12 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M6444";
    private String result12 = "ERROR: the starting player isn't correct";

    private String configuration13 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "R1500";
    private String result13 = "ERROR: on the position you are trying to revive there is already another of your pieces";

    private String configuration14 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "F6400"
            + "M6444";
    private String result14 = "ERROR: This piece cannot be moved because frozen";

    private String configuration15 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M1343"
            + "F4300"
            + "A4353";
    private String result15 = "ERROR: The selected piece cannot attack because it's frozen";

    private String configuration16 = "W"
            + "000000"
            + "000000"
            + "000k0m"
            + "0000M0"
            + "S00s00"
            + "000000"
            + "3437700000000000"
            + "000433"
            + "F0000000"
            + "F4300"
            + "M4535"
            + "M1525"
            + "M3525"
            + "M5463";
    private String result16 = "B"
            + "000000"
            + "000000"
            + "000k00"
            + "000000"
            + "000000"
            + "000000"
            + "4000000000000000"
            + "000431"
            + "00000000"
            + "DRAW";

    private String configuration17 = "B"
            + "000000"
            + "000000"
            + "000k0m"
            + "0000M0"
            + "000000"
            + "000000"
            + "4770000000000000"
            + "000431"
            + "00000000"
            + "M6354";
    private String result17 = "W"
            + "000000"
            + "000000"
            + "000k00"
            + "000000"
            + "000000"
            + "000000"
            + "4000000000000000"
            + "000000"
            + "00000000"
            + "BLACK";

 private String configuration18 = "W"
             + "000000"
             + "000000"
             + "000k0m"
             + "0000M0"
             + "000000"
             + "000000"
             + "4770000000000000"
             + "000431"
             + "00000000"
             + "M5463";
    private String result18 = "B"
            + "000000"
            + "000000"
            + "000k00"
            + "000000"
            + "000000"
            + "000000"
            + "4000000000000000"
            + "000431"
            + "00000000"
            + "DRAW";

    private String configuration19 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M1211"
            + "M6566"
            + "M2231"
            + "M5546"
            + "M3141"
            + "M6261"
            + "T2536";
    private String result19 = "ERROR: you can't teleport a piece to a special cell";

    private String configuration20 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M1211"
            + "M6566"
            + "M2231"
            + "M5546"
            + "M3141"
            + "M4636"
            + "M1334"
            + "F3400"   // D frozen
            + "T1533"   // 343
            + "M6261"
            + "A3353"   // 342, k: 2
            + "M5344"
            + "A3363"   // 341, m: 5
            + "A4433"   // A: 3
            + "M3444"   // D: 4, k: 0
            + "A6141"   // K: 2
            + "F6100"  // a frozen
            + "M6462"   // 613
            + "M4436"   // D: 0, k: 0
            + "M6243"   //612
            + "H3300"   // A: 5
            + "M4324"   // d: 2, K: 0, 611
            + "R1300"
            + "A6141"   // K: 0
            + "M1324"   // D: 3, d: 0
            + "M6362"
            + "M2436"
            + "M6646"
            + "A3646"   // g: 2
            + "M4636"   // D: 0, g: 0
            + "M3332"
            + "R6400"   // d: 6
            + "A3252"   // s: 1
            + "M6434"
            + "A3234"   // d: 4
            + "A3432"   // A: 2
            + "M3231"
            + "A6131"   // A: 0
            + "M1122"
            + "M3442"
            + "M2232"
            + "M4244"
            + "M3242"
            + "A4442"   // G: 2
            + "M4252"   // G: 1, s: 0
            + "M6152"   // G: 0, a: 1
            + "M2524"
            + "H5200"   // a: 5
            + "M2313"
            + "T5233"
            + "M1323"
            + "A3323"   // S: 1
            + "M2333"   // S: 0, a: 4
            + "A4424"   // S: 0
            + "M1424"
            + "M3332"
            + "M2434"
            + "M4436"
            + "M3425"
            + "M3645"
            + "M2536"
            + "M4524"
            + "M3635"
            + "M2412"
            + "M3544"
            + "M1224"
            + "M4454"   // s: 0, M: 5
            + "M2434"
            + "M5443"
            + "M3241"
            + "M4334";   // M: 0, d: 0
    private String result20 = "B" +
            "000a00" +
            "00000m" +
            "000000" +
            "000000" +
            "000000" +
            "000000" +
            "4500000000000000" +
            "000000" +
            "00000000" +
            "BLACK";

    private String configuration21 = "B"
            + "M00000"
            + "000000"
            + "D0000m"
            + "0k0000"
            + "000000"
            + "000000"
            + "7647000000000000"
            + "000000"
            + "FHRTFHRT"
            + "F1300"
            + "M1122"
            + "M2413";
    private String result21 = "W"
            + "000000"
            + "0M0000"
            + "k0000m"
            + "000000"
            + "000000"
            + "000000"
            + "4770000000000000"
            + "000000"
            + "FHRT0HRT";

    private String configuration22 = "B"
            + "M00000"
            + "000000"
            + "D0000m"
            + "000000"
            + "000000"
            + "0000k0"
            + "7647000000000000"
            + "000000"
            + "FHRTFHRT"
            + "F1300"
            + "F5600"
            + "T5613";
    private String result22 = "W"
            + "M00000"
            + "000000"
            + "00000m"
            + "000000"
            + "000000"
            + "000000"
            + "7700000000000000"
            + "000000"
            + "0HRT0HR0";

    private String configuration23 = "B"
            + "000000"
            + "000000"
            + "D00000"
            + "00Ss00"
            + "000000"
            + "0000k0"
            + "3334000000000000"
            + "131562"
            + "000T0000"
            + "M4434";
    private String result23 = "W"
            + "000000"
            + "000000"
            + "D00000"
            + "000000"
            + "000000"
            + "0000k0"
            + "3400000000000000"
            + "131561"
            + "000T0000"
            + "DRAW";

    private String configuration24 = "B"
            + "000000"
            + "000000"
            + "D00000"
            + "00Ss00"
            + "000000"
            + "0000k0"
            + "3334000000000000"
            + "131000"
            + "000T0000"
            + "M4434";
    private String result24 = "W"
            + "000000"
            + "000000"
            + "D00000"
            + "000000"
            + "000000"
            + "0000k0"
            + "3400000000000000"
            + "131000"
            + "000T0000"
            + "BLACK";

    private String configuration25 = "B"
            + "000000"
            + "000000"
            + "D00000"
            + "00Ss00"
            + "000000"
            + "0000k0"
            + "3334000000000000"
            + "000563"
            + "000T0000"
            + "M4434";
    private String result25 = "W"
            + "000000"
            + "000000"
            + "D00000"
            + "000000"
            + "000000"
            + "0000k0"
            + "3400000000000000"
            + "000562"
            + "000T0000"
            + "WHITE";

    private String configuration26 = "B"
            + "000000"
            + "000000"
            + "000K0m"
            + "0000M0"
            + "000000"
            + "000000"
            + "4770000000000000"
            + "431000"
            + "00000000"
            + "M6354";
    private String result26 = "W"
            + "000000"
            + "000000"
            + "000K00"
            + "000000"
            + "000000"
            + "000000"
            + "4000000000000000"
            + "431000"
            + "00000000"
            + "DRAW";

    private String configuration27 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M2232"
            + "M5242"
            + "T1262"
            + "M6362"
            + "F5300"
            ;
    private String result27 = "B" // moving player
            + "000000" // fantasy_board (row 1)
            + "00Ks0m" // fantasy_board (row 2)
            + "DS00k0" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "6753434343436500" // vitality
            + "000533" // frozen pieces
            + "0HR0FHRT" // unused spells
            ;

    private String configuration28 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M2232"
            + "M5242"
            + "T1262"
            + "M6362"
            + "F5300"
            + "H6200"
            ;
    private String result28 = "ERROR: you can't heal a mage";

    private String configuration29 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M2232"
            + "M5242"
            + "T1262"
            + "M6362"
            + "F5300"
            + "M5444"
            + "M3242"
            + "A5342"
            ;
    private String result29 = "ERROR: The selected piece cannot attack because it's frozen";

    private String configuration30 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M2232"
            + "M5242"
            + "T1262"
            + "M6362"
            + "F5300"
            + "M5444"
            + "M3242"
            + "M5566"
            + "M4253"
            + "M6453"
            + "M2434"
            + "F1300"
            + "M3444"
            + "M5323"
            + "M1523"
            + "T6513"
            + "R1300"
            + "M6655"
            + "H4400"
            + "A5544"
            + "M4453"
            + "R6400"
            + "A5362"
            + "M6253"
            + "M2524"
            + "M5544"
            + "M2434"
            + "M4434"
            + "M1424"
            + "M3424"
            + "M2434"
            + "M6434"
            ;

    private String result30 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "000000" // fantasy_board (row 2)
            + "000000" // fantasy_board (row 3)
            + "00d000" // fantasy_board (row 4)
            + "000000" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "2000000000000000" // vitality
            + "000000" // frozen pieces
            + "00000H00" // unused spells
            + "BLACK" //black has won
            ;

    private String configuration31 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675434334345765"
            + "000000"
            + "FHRTFHRT"
            + "M1343"
            + "F4300"
            + "F6400"   // 433
            + "M5343"   // 643, D: 0
            + "T2364"   // d: 0
            + "A6564"   // S: 0
            + "M1535"
            + "M6244"
            + "A3555"   // k: 2
            + "A4424"   // K: 2
            + "A3555"   // k: 0
            + "T6515"
            + "R1300"   // D: 6
            + "A1514"   // M: 3
            + "M1315"   // D: 0, g: 0
            + "R6400"   // d: 6
            + "M3534"
            + "A4434"   // A: 3
            + "A3444"   // a: 3
            + "A4434"   // A: 1
            + "H3400"   // A: 5
            + "A4434"   // A: 3
            + "A3444"   // a: 1
            + "H4400"   // a: 5
            + "A3444"   // a: 3
            + "A4434"   // A: 1
            + "A3444"   // a: 1
            + "A4434"   // A: 0
            + "M2233"
            + "M4436"
            + "M3323"
            + "M3616"
            + "M2515"
            + "M1636"
            + "M1232"
            + "M6444"
            + "M3234"
            + "A4434"   // G: 2
            + "A3444"   // d: 2
            + "A3634"   // G: 0
            + "M2333"
            + "M4446"
            + "M2425"
            + "M3655"
            + "M2535"
            + "M4634"
            + "M1425"
            + "M3453"
            + "M3544"
            + "M5544"   // K: 0, a: 0
            + "M3342"
            + "M5323"
            + "M4232"
            + "M2353"
            + "A3243"   // k: 2
            + "A4332"   // K: 2
            + "A3243"   // k: 0
            + "M5345"
            + "M2534"
            + "M4536"
            + "M3443"
            + "M3633"
            + "M4333"   // d: 0, M: 3
            + "M6353"
            + "M3242"
            + "M5242"   // K: 0, s: 0
            + "M1525"
            + "M5344"
            + "M2524"
            + "M4434"
            + "M2434";   // S: 0, m: 5
    private String result31 = "B"
            + "000000"
            + "000000"
            + "000000"
            + "00m0s0"
            + "000000"
            + "000000"
            + "5300000000000000"
            + "000000"
            + "00000000"
            + "BLACK";

    private String configuration32 = "W" // moving player
            + "000000" // fantasy_board (row 1)
            + "GK00sa" // fantasy_board (row 2)
            + "DS00km" // fantasy_board (row 3)
            + "MK00sd" // fantasy_board (row 4)
            + "AS00kg" // fantasy_board (row 5)
            + "000000" // fantasy_board (row 6)
            + "5675434334345765" // vitality
            + "000000" // frozen pieces
            + "FHRTFHRT" // unused spells
            + "M2526"
            + "M5545"
            + "M1211"
            + "M6443"
            + "A1545"  // k vitality 2
            + "A4323"  // S killed
            + "A1545"  // k killed
            + "M4346"
            + "M1516"
            + "A4626"  // S killed
            + "A1646"  // d vitality 4
            + "M4666"
            + "M1636"
            + "M6261"
            + "M3646"
            + "F4600"  // A freezed
            + "M1343"
            + "M6646"  // A killed
            + "F4600"
            + "M6566"
            + "T2246"  //d killed
            + "M6364"
            + "M4636"
            + "M6465"
            + "M4341";
    private String result32 = "B" // moving player
            + "G00D0a" // fantasy_board (row 1)
            + "0000s0" // fantasy_board (row 2)
            + "0000k0" // fantasy_board (row 3)
            + "MK00s0" // fantasy_board (row 4)
            + "00000m" // fantasy_board (row 5)
            + "00K00g" // fantasy_board (row 6)
            + "5744634357500000" // vitality
            + "000000" // frozen pieces
            + "0HR00HRT" // unused spells
            + "WHITE";    // 3 red cells captured

    private String configuration33 = "W"
            + "000000"
            + "GK00sa"
            + "DS00km"
            + "MK00sd"
            + "AS00kg"
            + "000000"
            + "5675444334345765"
            + "000000"
            + "FHRTFHRT"
            + "M1211";
    private String result33 = "ERROR: you tried to initialize a piece with a vitality exceeding its maximum allowed.";

    //from here are configurations generated with the randomCreator
    //all these written below have been tested to work
    //other can be added but have to bee tested by hand
    private String randomConfigration1 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHR" +
            "TT1345T5513M4543M6434H1200A1324M4333H3400M3342M3415M2231A1514M2322M5364M2435M1324M3526M6243M1415" +
            "M2433M4254M4323M5465A3322M1514M6354M1415M2334M2221M3342M1223M6453M2616M4243M3132M5362A3243M6251" +
            "M2111M3456M2524M5655M2314M5253M1626M5545M3233M5464M3332M6463M3231M4533M2616M4332M1412M3345F3200" +
            "M4555M3122M5142M2223M5566M2334M3243M1121M6354M1211M5455M3445M5554M1525M5455M4554M6646M2122M4624" +
            "M1626M2422A5443M4232M2536M5566M3646M2244M2616M6665M5444M5363M4434M6554M1112M3242M4656M5453M1214" +
            "M4241M3443M4151M4333M6364M1424M5161M1625M5354M2534M5463M3332M6352M3425M6162M3243M5241M5646M6252" +
            "M4334M5253M2514M5363M3444R5400M2434M6465M3435M6364M4645M6566M3533M6665R1300M5453M4554M6454M4454" +
            "M4152M1424M6555M1325M5251M2514M5141M2434M4152M3425M5263M2535M6352M1411M5565M3313M5242M1312M5352" +
            "M3524M4233M1141M3334M2423M6564M4122M5262M2242M3425A4262M6465M4234M6564M3443M2535M2333M3536M3344" +
            "M3625M4354M2516M5453M6454M1232M1626M5364M2616M3243M1626M4342M2636M4251M5453M4454M3645M6444M4544" +
            "M5465M5354M5131M5453M3151M5352M5131M5253M6554M5363M3142M6353M4244M5352M4433M5262M3344M6263M4446" +
            "M6362M5453M6252M4626M5253";
    private String randomResult1 = "W0000000000000000000000000000000G000050000000000000000000000000F000WHITE";

    private String randomConfigration2 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT" +
            "T2316T6446M1526M4656A2656M5545M1315M4546M1535M5343M3554M6564M5464M5664M2645M6445M2515M4354M2423M6264" +
            "M2332M6462M3241M5464M4131M6261M3121M5253M1525M6152M2131M5251M1423M5354M3142M5455M1626M6354M2211M4635M4253A6453";
    private String randomResult2 = "WK000a0G000000M00K00000mk0Sk0s00S00004573345273400000000000FHR0FHR0";

    private String randomConfigration3 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT" +
            "T2434T5251M1333M5152M2524M5242M2313M6445A1545M5344M1211M4526M3342M6252M3435M2646M3545M5464M1526" +
            "A5242M4536M5566M2223M6362A4252M5254M2644M6463M4452M6656M2334M5433M2423M5645M1424M3334M4261M4625" +
            "M3425M6564M5234M6454A6162M2516M1314M4535M6131M6353M3132M3536M1113M5456M2435M5354M1311M6261M3442" +
            "M5453M1413M5645M1121M6152M2322M5263M4263M4556M3525M5352M2536M1613M2131M1325M2221M5251M3252M2533" +
            "M5261M3342A6163M4212M3625M1231M6151M5654M5163M5443M6352M4341M2514M4132M1423M3241M5253A4131M2322" +
            "M4151M5344M5162M4436M6264M3615M6444M2231M4434M1523M3443M2332M4342M3233M4252M2111M5253M3352M5354" +
            "M5243M5434M4313M3445M1334M4543M3413M4354M1314M5463M1434M6364M3453M6466M5365M6656M1121M5646M6556" +
            "M4626M5666M2616M6655M1614M5563M1434M3132M3445M3242M4556M6333M5665M3325M6566M4251M6655M2535M5544" +
            "M3545M4442M2122M4222M4564M2213M6453M1323M5354M2313M5152M1315M5262M1524M6261M2426M6151M2624M5465" +
            "M2423M6564M2313M5141M1323M4131M2343M3132M4344M3222M4443M2211M4353M6444M5351M1112M5153M4456M5351" +
            "M5653M5153M1221M5354M2111M5455M1112M5565M1213M6563M1322M6361M2232M6151M3233M5153M3343M5352M4332M5232";

    private String randomResult3 = "W00000000M0000000000000000000000000003000000000000000000000FHR0FHR0WHITE";

    private String randomConfigration4 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT" +
            "T2434T5544M1536M6566M1333M4455M2313M5251A3353M6656M3624M6434M2416M3413M3313M5152M1333M5242M2231" +
            "M5364M3324M5636M1423M6353M1636M6243M1232M4232M2312M5344M2436M5545M3234M4362M3424M6455M2444M5546" +
            "M1222M4635M2535M4556M3534M6242M3132M5453M2211M4222M3444M2233M3221M3324M2132M5343M4434M2416M3231" +
            "M5646M1112M1625M3435M4353M3141M4645M1211M4546M1122M2516M3536M5352M4142M4635M3626M3534M2636M1615" +
            "M3626M3443M2213M1513M4232M4354M3242M5444M4232M5253M3221M4433M1324M3334M2425M3435M2534M5354M3424" +
            "A3524M2616M3524M1615M5455M2132M2433M1514M5554M3221M3323M1413M2324M2111M2435M1122M3545M2211M5444" +
            "M1312M4534M1121M3423M1213M2313M2112M1312";

    private String randomResult4 = "W000000000000000000000s000000000000003000000000000000000000FHR0FHR0BLACK";

    private String randomConfigration5 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT" +
            "M2333T6261T2423M6466M2211M6636M1324M6564M2436M6466M2313M5444M3332M6362M1516M5363M1121M6141M1615" +
            "M4122M1324M6354M2423M2224M1526M2415M1424A1525M2111M5445M2313M4554M2645M1524M4554M6251M5452M5554" +
            "M1223M5262M2433M5443M2314M5141M3323M4434M2333M3433M1122M4132M2223M3223M1323M6656M1412M3323M2313" +
            "M5666M1211M4352M1322M5263M1112M6655M2524M5544M1211M4434M2221M3454M2112M5443M1131M4345M2434M4555" +
            "M3121M5535M3444M6252M2131M3544M3151M5242M5131M4443M1221M6364M3141M4323M2132M2314M3221M1412M2111" +
            "M6463A4142M6362M1122M6261M4131M6152M3121M5241M2212M1222M2122M4151M2221M5162M2122M6261M2242M6162" +
            "M4222M6251M2242M5162M4262";

    private String randomResult5 = "B0000000000000000000000000000000000000000000000000000000000FHR0FHR0DRAW";

    private String randomConfigration6 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT" +
            "T2556M6466M1343T5426M4345M5564M4546F1500M2233M2616H2300M6241M2413M5354M4645M6545M1425M4161M1535" +
            "M6142M3554M4244M2322M6465M3342M6554M1221M4464M4253M6362M5646M5455M2141M5544A5364M5242M1314M6663" +
            "M2534M4453M5344M6465M4152M6351M3445M6546A5262M5153M4453M4645M4544M5355M4445M5544M4535R5300M3526" +
            "M4433M1425M3336M2535M3645M2615M4566A5262M6665M5241M6556M3536M5635M1524M3543M4161M4352M2423M5241" +
            "M6152M4132M2334M3241M3445M5362M5263M4243M3646M4153R1300M5341M1315M4161M1524M6253M4534M6142M2433" +
            "M5352M4636M4234M3321M5242M6361M1615M6151M1514M5161M4253M2142M5362M3635M6263M3536M6354M2223M5453" +
            "M3626M5362M4252M6261M5253M4353M2324M1424";
    private String randomResult6 = "W00000G0000000000D00000000000000K00004530000000000000000000F0000H00WHITE";

    private String randomConfigration7 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT" +
            "F5400T5225M1321M6452T1254M5264M1525M6556M2133M6352M2435M5342M2232M5253M3243M4252M2515M5263M4334" +
            "M6445M3526M6261M1424M5362M3435M4546M3312M4666M5464M6636M2434M6141M1525M3633M2513M6261M2616M3345" +
            "M1314M5666M3424F6400M1626M6664M3544M4546M4455M4142M1416M6453M2333M6152M2635M4625M3334M5343M1233" +
            "M2536M3352M4334M1614M4244M3534M4455M1413M5546M1333M3624M3342M3435M4261M6364M6151M4626M5161M2624" +
            "M6151M6454M5163M2425M6351M3546M5152M2523M5264M2335M6446M3514M4665M1413M6544M5464M4433M6463M3313";
    private String randomResult7 = "B000000000000a0000k00000000000000000034000000000000000000000HR00HR0BLACK";

    private String randomConfigration8 = "W000000GK00saDS00kmMK00sdAS00kg0000005675434334345765000000FHRTFHRT" +
            "T2365T5551M1516F2500R2300M6434M2433H5200M3344M6564M1211A3444M1415M3435M4433M3516M1314M1626M2213" +
            "M2645M1426M6465M2635M5242M2526M5343M1525M6261M2324M4524M2515M2421M1324M5444M3545M6354F4300M2112" +
            "M4566M5463M1131M1233M2413M6354M1525M4232M3121M6152M1323M5264M2535A6566M2312M5152M3536M5263M6646" +
            "M6555M4666M6353M6665M3233M6553M3323M1211M5556M2625M5352M2526M5262M2132M5655M3635M4333M3242M5556" +
            "M4243M6446M3544M5445M4454M4544M4341M4434M2636";
    private String randomResult8 = "BK00G0000000k0sk00000m0M000000000Sag043473555410000000000000H0000R0WHITE";



    /**
     * Set the environment for each test: initialize StringToGameConverter object.
     */
    @Before
    public void initialization(){
        this.stringToGameConverter = new StringToGameConverter();
    }


    /**
     * Tests that assure the correctness of the model, by receiving in input a string representing a game configuration plus some turns
     * to be played and testing the string representation of the execution of those turns in the game against an expected string.
     */
    @Test
    public void testConfiguration1(){
        assertEquals("Testing moves from initial configuration", result1, stringToGameConverter.turnTest(configuration1));
    }

    @Test
    public void testConfiguration2(){
        assertEquals("W occupies 3 special cells -> W wins", result2, stringToGameConverter.turnTest(configuration2));
    }

    @Test
    public void testConfiguration3(){
        assertEquals("Only one casualty, many spells used", result3, stringToGameConverter.turnTest(configuration3));
    }

    @Test
    public void testConfiguration4(){
        assertEquals("B occupies 2 special cells -> B doesn't win", result4, stringToGameConverter.turnTest(configuration4));
    }

    @Test
    public void testConfiguration5(){
        assertEquals("W occupies 3 special cells -> W wins", result5, stringToGameConverter.turnTest(configuration5));
    }

    @Test
    public void testConfiguration6(){
        assertEquals("ERROR: invalid action", result6, stringToGameConverter.turnTest(configuration6));
    }

    @Test
    public void testConfiguration7(){
        assertEquals("ERROR: invalid action", result7, stringToGameConverter.turnTest(configuration7));
    }

    @Test
    public void testConfiguration8(){
        assertEquals("ERROR: invalid action", result8, stringToGameConverter.turnTest(configuration8));
    }

    @Test
    public void testConfiguration9(){
        assertEquals("ERROR: wrong game configuration, missing or exceeding some final characters", result9, stringToGameConverter.turnTest(configuration9));
    }

    @Test
    public void testConfiguration10(){
        assertEquals("ERROR: the final position is out of the fantasy_board", result10, stringToGameConverter.turnTest(configuration10));
    }

    @Test
    public void testConfiguration11(){
        assertEquals("ERROR: the final position is out of the fantasy_board", result11, stringToGameConverter.turnTest(configuration11));
    }

    @Test
    public void testConfiguration12(){
        assertEquals("ERROR: the starting player isn't correct", result12, stringToGameConverter.turnTest(configuration12));
    }

    @Test
    public void testConfiguration13(){
        assertEquals("ERROR: on the position you are trying to revive there is already another of your pieces", result13, stringToGameConverter.turnTest(configuration13));
    }

    @Test
    public void testConfiguration14(){
        assertEquals("ERROR: This piece cannot be moved because frozen", result14, stringToGameConverter.turnTest(configuration14));
    }

    @Test
    public void testConfiguration15(){
        assertEquals("ERROR: The selected piece cannot attack because it's frozen", result15, stringToGameConverter.turnTest(configuration15));
    }

    @Test
    public void testConfiguration16(){
        assertEquals("Limit case: one frozen piece -> draw", result16, stringToGameConverter.turnTest(configuration16));
    }

    @Test
    public void testConfiguration17(){
        assertEquals("Limit case: one frozen piece -> B win", result17, stringToGameConverter.turnTest(configuration17));
    }

    @Test
    public void testConfiguration18(){
        assertEquals("Limit case: one frozen piece -> draw", result18, stringToGameConverter.turnTest(configuration18));
    }

    @Test
    public void testConfiguration19(){
        assertEquals("Teleport on special cell gone wrong", result19, stringToGameConverter.turnTest(configuration19));
    }

    @Test
    public void testConfiguration20(){
        assertEquals("Game from start to end -> black wins", result20, stringToGameConverter.turnTest(configuration20));
    }

    @Test
    public void testConfiguration21(){
        assertEquals("Attack frozen piece", result21, stringToGameConverter.turnTest(configuration21));
    }

    @Test
    public void testConfiguration22(){
        assertEquals("Teleport frozen piece over frozen piece", result22, stringToGameConverter.turnTest(configuration22));
    }

    @Test
    public void testConfiguration23(){
        assertEquals("White has one frozen piece, black too -> draw", result23, stringToGameConverter.turnTest(configuration23));
    }

    @Test
    public void testConfiguration24(){
        assertEquals("White has one frozen piece, black one non frozen -> black wins", result24, stringToGameConverter.turnTest(configuration24));
    }

    @Test
    public void testConfiguration25(){
        assertEquals("White has one piece, black one frozen piece -> white wins", result25, stringToGameConverter.turnTest(configuration25));
    }

    @Test
    public void testConfiguration26(){
        assertEquals("White has one piece, black no piece -> draw", result26, stringToGameConverter.turnTest(configuration26));
    }

    @Test
    public void testConfiguration27(){
        assertEquals("Initial part of the game", result27, stringToGameConverter.turnTest(configuration27));
    }

    @Test
    public void testConfiguration28(){
        assertEquals("Errore the heal can't be cast", result28, stringToGameConverter.turnTest(configuration28));
    }

    @Test
    public void testConfiguration29(){
        assertEquals("The frozen piece can't attack", result29, stringToGameConverter.turnTest(configuration29));
    }

    @Test
    public void testConfiguration30(){
        assertEquals("Black wins", result30, stringToGameConverter.turnTest(configuration30));
    }

    @Test
    public void testConfiguration31(){
        assertEquals("Black wins", result31, stringToGameConverter.turnTest(configuration31));
    }

    @Test
    public void testConfiguration32(){
        assertEquals("Black wins", result32, stringToGameConverter.turnTest(configuration32));
    }

    @Test
    public void testConfiguration33(){
        assertEquals("Initialize a piece with a greater vitality than allowed", result33, stringToGameConverter.turnTest(configuration33));
    }

    @Test
    public void testRandom1(){
        assertEquals("have been tested by trying the moves",randomResult1, stringToGameConverter.turnTest(randomConfigration1));
    }

    @Test
    public void testRandom2(){
        assertEquals("have been tested by trying the moves",randomResult2, stringToGameConverter.turnTest(randomConfigration2));
    }

    @Test
    public void testRandom3(){
        assertEquals("have been tested by trying the moves",randomResult3, stringToGameConverter.turnTest(randomConfigration3));
    }

    @Test
    public void testRandom4(){
        assertEquals("have been tested by trying the moves",randomResult4, stringToGameConverter.turnTest(randomConfigration4));
    }

    @Test
    public void testRandom5(){
        assertEquals("have been tested by trying the moves",randomResult5, stringToGameConverter.turnTest(randomConfigration5));
    }

    @Test
    public void testRandom6(){
        assertEquals("have been tested by trying the moves",randomResult6, stringToGameConverter.turnTest(randomConfigration6));
    }

    @Test
    public void testRandom7(){
        assertEquals("have been tested by trying the moves",randomResult7, stringToGameConverter.turnTest(randomConfigration7));
    }

    @Test
    public void testRandom8(){
        assertEquals("have been tested by trying the moves",randomResult8, stringToGameConverter.turnTest(randomConfigration8));
    }

}
