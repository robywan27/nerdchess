package it.polimi.group02;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import it.polimi.group02.model.StringToGameConverter;

import static junit.framework.Assert.assertEquals;


/**
 * This is a really mock class used only for "fun"
 * It creates some random acts by the player to see how can the game develop
 */
public class RandomGameTestDriver {
    /**
     * Instance of StringToGameConverter class in order to call the turnTest(String) method.
     */
    private StringToGameConverter stringToGameConverter;

    
    /**
     * Set the environment for each test: initialize StringToGameConverter object.
     */
    @Before
    public void initialization(){
        this.stringToGameConverter = new StringToGameConverter();
    }


    /**
     * This method creates a random configuration of possible actions of the player and it's result
     * We have put a System.out.println(); to see which is the output so we can verify it
     */
    public String[] randomCrator(){
        double randomNumber;
        int randomInt;
        char action[] = {'M','A','F','H','R','T'};
        String config = "W" // moving player
                + "000000" // fantasy_board (row 1)
                + "GK00sa" // fantasy_board (row 2)
                + "DS00km" // fantasy_board (row 3)
                + "MK00sd" // fantasy_board (row 4)
                + "AS00kg" // fantasy_board (row 5)
                + "000000" // fantasy_board (row 6)
                + "5675434334345765" // vitality
                + "000000" // frozen pieces
                + "FHRTFHRT" // unused spells
                ;
        ArrayList<String> randomNumbers = new ArrayList<>();
        String valuesForTest[] = {config,config};
        String result = "";

        int i;
        for(i = 0; i < 1000000; i++){
            randomNumber = (Math.random() * 6) % 6 + 1;
            randomInt = (int) randomNumber;
            randomNumbers.add(String.valueOf(randomInt));
        }

        String newConfig = config;
        String workingString = config;
        String lastWorkingResult = stringToGameConverter.turnTest(config);

        for( i = 0; i<1000000; i=i+5){
            for(int k = 0; k<5; k++){
                if((String.valueOf(action[Integer.valueOf(randomNumbers.get(i)) - 1]).equals("F") || String.valueOf(action[Integer.valueOf(randomNumbers.get(i)) - 1]).equals("R") || String.valueOf(action[Integer.valueOf(randomNumbers.get(i)) - 1]).equals("H"))&&k==0){
                    newConfig = newConfig + String.valueOf(action[Integer.valueOf(randomNumbers.get(i)) - 1])+String.valueOf((randomNumbers.get(i+1)))+String.valueOf((randomNumbers.get(i+2)))+"0"+"0";
                }else if(!(String.valueOf(action[Integer.valueOf(randomNumbers.get(i)) - 1]).equals("F") || String.valueOf(action[Integer.valueOf(randomNumbers.get(i)) - 1]).equals("R") || String.valueOf(action[Integer.valueOf(randomNumbers.get(i)) - 1]).equals("H"))){
                    if (k == 0) {
                        newConfig = workingString + String.valueOf(action[Integer.valueOf(randomNumbers.get(i + k)) - 1]);
                    } else {
                        newConfig = newConfig + String.valueOf((randomNumbers.get(i + k)));
                    }
                }
                if(k==4){
                    //System.out.println(newConfig);
                    stringToGameConverter = new StringToGameConverter();
                    result = stringToGameConverter.turnTest(newConfig);

                }
            }

            if(result.charAt(0) != 'E' && (!result.equals(lastWorkingResult))) {

                workingString = newConfig;
                lastWorkingResult = result;

                if((result.contains("WHITE") || result.contains("BLACK") || result.contains("DRAW"))){
                    System.out.println(newConfig);
                    System.out.println(result);
                    valuesForTest[0] = newConfig;
                    valuesForTest[1] = result;
                    return valuesForTest;
                }
            }
            valuesForTest[0] = newConfig;
            valuesForTest[1] = result;
        }
        return valuesForTest;
    }

    @Test
    public void testRandomStrings(){
        String[] valuesToBeTested = this.randomCrator();
        this.stringToGameConverter = new StringToGameConverter();
        assertEquals("we try some of them by hands",valuesToBeTested[1], stringToGameConverter.turnTest(valuesToBeTested[0]));
    }

}
