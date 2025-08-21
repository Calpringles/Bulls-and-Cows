import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

class NumbersCodeTest {
    private SecretCode code;
    private Player p = new Player("Example");
    private int codesattempted = p.getCodesAttempted();

    @BeforeEach
    void setUp() {
        code = new NumbersCode(p);
    }

    @Test
    void testCodeIsGenerated(){
        assertNotNull("Code Was Not Generated", code);
    }

    @Test
    void allDigitsAreUnique(){
        assertTrue("Duplicate Digits In Code", NumbersCode.checkForDups(code.getSecret()));
    }

    @Test
    void checkAttemptIncrementation(){
        assertTrue("codesAttempted Was Not Incremented", codesattempted < p.getCodesAttempted());
    }

    @Test
    void lettersInNumberGuess(){
        assertFalse("Chars In NumbersCode", code.validateGuess("123a"));
    }

    @Test
    void errorOnInvalidGuessLength(){
        assertFalse("Invalid Guess Length", code.validateGuess("12345"));
    }
}