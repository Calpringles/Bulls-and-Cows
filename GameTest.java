
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

class GameTest{
    private Game testClass;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private  String filename = "players.csv";

    @AfterEach
    public void restoreStreams(){
        File file = new File("players.csv");
        if (file.exists()) {
            file.delete();
        }
        System.setOut(originalOut);
    }

    @BeforeEach
    void setUp(){

        System.setOut(new PrintStream(out));
        Player p = new Player("exampleName");
        SecretCode c = new NumbersCode(p);
        testClass = new Game(p, c);
        testClass.getPlayer().setSavedCode("1234");
    }

    //Story 2
    @Test
    void bullsUpdated(){
        testClass.getSecretCode().setSecret("1234");

        int[] result1 = testClass.enterGuess("1256");
        int[] result2 = testClass.enterGuess("1237");

        Assertions.assertEquals(2, result1[0]); // 2 bulls
        Assertions.assertEquals(3, result2[0]); // 3 bulls
    }

    @Test
    void cowsUpdated(){
        testClass.getSecretCode().setSecret("1234");

        int[] result1 = testClass.enterGuess("4321");
        int[] result2 = testClass.enterGuess("5621");

        Assertions.assertEquals(4, result1[1]); // 4 cows
        Assertions.assertEquals(2, result2[1]); // 2 cows
    }

    @Test
    void playerStatsUpdated(){
        testClass.getSecretCode().setSecret("1234");

        testClass.enterGuess("1256");

        Assertions.assertEquals(2, testClass.getPlayer().getBulls()); // Bulls updated
        Assertions.assertEquals(0, testClass.getPlayer().getCows()); // Cows updated

    }

    @Test
    void playerStatsUpdatedOnSolve(){
        testClass.getSecretCode().setSecret("1234");

        int[] res = testClass.enterGuess("1234");
        testClass.checkCodeCracked(res[0]);

        Assertions.assertEquals(4, testClass.getPlayer().getBulls()); // Bulls updated
        Assertions.assertEquals(0, testClass.getPlayer().getCows());
        Assertions.assertEquals(1, testClass.getPlayer().getCodesDeciphered());

        testClass.requestCode("N");
        testClass.getSecretCode().setSecret("5678");

        int[] res1 = testClass.enterGuess("5678");
        testClass.checkCodeCracked(res1[0]);
        Assertions.assertEquals(8, testClass.getPlayer().getBulls());
        Assertions.assertEquals(0, testClass.getPlayer().getCows());
        Assertions.assertEquals(2, testClass.getPlayer().getCodesDeciphered());

    }

    @Test
    void gameEndOnSolve(){
        testClass.getSecretCode().setSecret("1234");
        int res[] = testClass.enterGuess("1234");
        testClass.checkCodeCracked(res[0]);
        Assertions.assertTrue(testClass.getstatus());

    }

    // Story 3
    @Test
    void saveGame(){
        testClass.getSecretCode().setSecret("1357");
        testClass.saveGame(filename);
        Assertions.assertEquals("Game saved successfully." + System.lineSeparator(), out.toString());
    }

    @Test
    void confirmSavedGameOverwrite(){
        testClass.getSecretCode().setSecret("1357");
        testClass.getPlayer().setSavedCode("1234");
        Players ps = new Players();
        ps.addPlayer(testClass.getPlayer());
        testClass.saveCurrentCode();
        Assertions.assertEquals("No player data found. Will create file on save." + System.lineSeparator() +
                "Overwriting Previous Save." + System.lineSeparator() + "Code saved." + System.lineSeparator(), out.toString());
    }

    @Test
    void overwriteSavedGame(){

    }

    // Story 4
    @Test
    void loadSavedGame(){

    }

    @Test
    void loadEmptySave(){
        File file = new File("players.csv");
        if (file.exists()) {
            file.delete();
        }
        testClass.loadGame(filename);
        Assertions.assertEquals("Error loading game: players.csv (The system cannot find the file specified)" + System.lineSeparator(), out.toString());
    }

    // Story 5
    @Test
    void displaySolution(){
        testClass.getSecretCode().setSecret("12345678");
        testClass.showSolution();
        Assertions.assertEquals("The solution is: 12345678" + System.lineSeparator(), out.toString());
    }

    // Story 6
    @Test
    void savePlayerDataOnExit(){

    }

    // Story 7
    @Test
    void incrementCodesDecipheredOnWin(){
        int winsBefore = testClass.getPlayer().getCodesDeciphered();
        testClass.getSecretCode().setSecret("1234");
        testClass.checkCodeCracked(4);
        Assertions.assertEquals(winsBefore+1,testClass.getPlayer().getCodesDeciphered());
    }

    @Test
    void displayBullsAndCowsOnIncorrectGuess(){
        testClass.getSecretCode().setSecret("1234");
        testClass.enterGuess("1245"); // intentionally incorrect guess
        Assertions.assertEquals("Bulls: 2, Cows: 1" + System.lineSeparator(), out.toString());
    }

    @Test
    void doesNotDisplayBullsAndCowsOnCorrectGuess(){
        testClass.getSecretCode().setSecret("1234");
        // does not display cows and bulls for a correct guess
        testClass.enterGuess("1234");
        // only change regex if Bulls and Cows output string has altered this is working
        Assertions.assertFalse(out.toString().matches("(?s).*Bulls: \\d, Cows: \\d.*"));
    }

    // Story 8
    @Test
    void incrementAttemptsOnStart(){
        int codesBefore = testClass.getPlayer().getCodesAttempted();
        testClass.requestCode("N");
        Assertions.assertEquals(codesBefore+1, testClass.getPlayer().getCodesAttempted());
    }

    @Test
    void noIncrementAttemptsOnLoad(){
        String filename = "testFile.sav";
        testClass.saveGame(filename);
        int codesBefore = testClass.getPlayer().getCodesAttempted();
        testClass.loadGame(filename);

        Assertions.assertEquals(codesBefore, testClass.getPlayer().getCodesAttempted());
    }

    // Story 9
    @Test
    void incrementBullsAndCows(){

    }

    // Story 10
    @Test
    void errorOnDisplayStatsForNewPlayer(){

    }

    @Test
    void displayStats(){

    }

    //Story 11
    @Test
    void detailsLoadedSuccessfully(){
        
    }

    @Test
    void errorLoadingDetailsDueToProblem(){

    }

    @Test
    void errorLoadingDetails_PlayerDoesNotExist(){

    }

    //Story 12
    @Test
    void seeTop10Players(){
        testClass.getSecretCode().setSecret("12345678");
        Players ps = new Players();

        Player dean = new Player("Dean");
        dean.incrementCodesDeciphered(); dean.incrementCodesDeciphered(); dean.incrementCodesDeciphered();dean.incrementCodesDeciphered();
        dean.incrementCodesDeciphered(); dean.incrementCodesDeciphered(); dean.incrementCodesDeciphered(); dean.incrementCodesDeciphered();
        dean.incrementCodesAttempted(); dean.incrementCodesAttempted(); dean.incrementCodesAttempted(); dean.incrementCodesAttempted();
        dean.incrementCodesAttempted(); dean.incrementCodesAttempted(); dean.incrementCodesAttempted(); dean.incrementCodesAttempted();
        dean.incrementCodesAttempted(); dean.incrementCodesAttempted();

        Player frank = new Player("Frank");
        frank.incrementCodesDeciphered(); frank.incrementCodesDeciphered(); frank.incrementCodesDeciphered(); frank.incrementCodesDeciphered();
        frank.incrementCodesDeciphered(); frank.incrementCodesAttempted(); frank.incrementCodesAttempted(); frank.incrementCodesAttempted();
        frank.incrementCodesAttempted(); frank.incrementCodesAttempted();

        ps.addPlayer(dean);
        ps.addPlayer(frank);

        testClass.displayTop10Scores();

        String expectedOutput =
                "No player data found. Will create file on save." + System.lineSeparator() +
                        "Top 10 Scores:" + System.lineSeparator() +
                        "1. Frank - Solved: 5 - Proportion: 1.00" + System.lineSeparator() +
                        "2. Dean - Solved: 8 - Proportion: 0.80" + System.lineSeparator() +
                        "3. " + System.lineSeparator() +
                        "4. " + System.lineSeparator() +
                        "5. " + System.lineSeparator() +
                        "6. " + System.lineSeparator() +
                        "7. " + System.lineSeparator() +
                        "8. " + System.lineSeparator() +
                        "9. " + System.lineSeparator() +
                        "10. " + System.lineSeparator();

        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void noStatsStored(){
        testClass.getSecretCode().setSecret("12345678");
        Players ps = new Players();
        testClass.displayTop10Scores();
        assertEquals("No player data found. Will create file on save." + System.lineSeparator() +
                "No player data found. Will create file on save." + System.lineSeparator() +
                "Error: No player stats have been stored." + System.lineSeparator(), out.toString());

    }

    //Story 13
    @Test
    void noGuessHint(){
        testClass.getSecretCode().setSecret("12345678");
        testClass.giveHint();
        Assertions.assertEquals("Must Have A Previous Guess To Get A Hint." + System.lineSeparator(), out.toString());
    }

    @Test
    void firstHint(){
        testClass.getSecretCode().setSecret("12345678");
        testClass.lastGuess = "21340987";
        testClass.giveHint();
        Assertions.assertEquals("The Character '2' Should Be At Position 2" + System.lineSeparator(), out.toString());
    }

    @Test
    void secondHint(){
        testClass.getSecretCode().setSecret("12345678");
        testClass.lastGuess = "21340987";
        testClass.giveHint();
        testClass.giveHint();
        Assertions.assertEquals("The Character '2' Should Be At Position 2" + System.lineSeparator() +
                "The Character '1' Should Be At Position 1" + System.lineSeparator(), out.toString());
    }

}
