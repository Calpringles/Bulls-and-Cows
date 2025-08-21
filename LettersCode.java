import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;

public class LettersCode extends SecretCode implements Serializable {

    private ArrayList<String> codes = new ArrayList<String>();
    public String file = "src/words.txt";
    private String secret;

    public LettersCode(Player p) {
        codes = readWordsFile(file);
        this.secret = codes.get((int) (Math.random() * codes.size()));
        p.incrementCodesAttempted();
    }

    String getSecret() {
        return secret;
    }

    public ArrayList<String> readWordsFile(String filename) {
        ArrayList<String> words = new ArrayList<>();
        try {
            File file1 = new File(filename);
            if (!file1.exists()) {
                System.out.println("ERROR: The file " + filename + " does not exist.");
                return words;
            }
            Scanner myReader = new Scanner(file1);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                words.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return words;
        }

        return words;
    }

    public boolean validateGuess(String guess) {
        try {
            // Check if the guess contains only letters
            if (!guess.matches("^[a-zA-Z]+$")) {
                throw new IllegalArgumentException("ERROR: input must only contain letters");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        try {
            // Check if the guess has the correct number of digits
            if (guess.length() != secret.length()) {
                throw new IllegalArgumentException("ERROR: input must be of length " + secret.length());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        try {
            //Check for duplicate chars
            for (int i = 0; i < guess.length(); i++) {
                for (int j = i + 1; j < guess.length(); j++) {
                    if (guess.charAt(i) == guess.charAt(j)) {
                        throw new IllegalArgumentException("ERROR: input cannot contain duplicate characters");
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public int[] calcCowsAndBulls(String guess) {
        int bulls = 0, cows = 0;

        // Check for bulls and cows
        for (int i = 0; i < secret.length(); i++) {
            char letter = guess.charAt(i);
            if (secret.charAt(i) == letter) {
                bulls++;
            } else {
                for (int j = 0; j < secret.length(); j++) {
                    if (letter == secret.charAt(j)) {
                        cows++;
                    }
                }
            }
        }
        return new int[]{bulls, cows};
    }

    void setSecret(String s) {
        if (s.matches("^[a-zA-Z]+$")) {
            this.secret = s;
        }
    }

    int getLength() {
        int length = secret.length();
        return length;
    }
}