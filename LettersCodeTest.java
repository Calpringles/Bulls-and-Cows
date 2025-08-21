import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

class LettersCodeTest {
    private LettersCode code;
    private Player p = new Player("example");
    private int codesattempted = p.getCodesAttempted();

    @BeforeEach
    void setUp(){
        code = new LettersCode(p);
    }

    @Test
    void testCodeIsGenerated(){
        assertNotNull("Code Was Not Generated", code);
    }

    @Test
    void checkAttemptIncrementation(){
        assertTrue("codesAttempted Was Not Incremented", codesattempted < p.getCodesAttempted());
    }

    @Test
    void checkForNoFile(){
        String badFilename = "fake_file.txt";
        ArrayList<String> result = code.readWordsFile(badFilename);
        assertTrue("No Error For Bad Filepath", result.isEmpty());
    }

    @Test
    void numbersInLetterGuess(){
        assertFalse("Chars In NumbersCode", code.validateGuess("abc1"));
    }

    @Test
    void errorOnInvalidGuessLength(){
        assertFalse("Invalid Guess Length", code.validateGuess("abcde"));
    }
}