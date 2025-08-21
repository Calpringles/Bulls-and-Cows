import java.util.Random;
import java.util.ArrayList;
import java.util.List;

class NumbersCode extends SecretCode {
    private String secret;

    public NumbersCode(Player p) {
        this.secret = generateRandomNumberCode();
        p.incrementCodesAttempted();
    }

    String getSecret(){
        return secret;
    }

    boolean validateGuess(String guess){
        try{
            // Check if the guess contains only digits
            if (!guess.matches("\\d+")) {
                throw new IllegalArgumentException("ERROR: input must only contain numbers");
            }
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }

        try{
            // Check if the guess has the correct number of digits
            if (guess.length() != secret.length()) {
                throw new IllegalArgumentException("ERROR: input must be of length " + secret.length());
            }
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }

        try{
            //Check if the guess contains duplicate digits
            String digcheck = String.valueOf(guess);
            for(int i = 0; i<digcheck.length(); i++){
                for(int j = i+1; j<digcheck.length(); j++){
                    if(digcheck.charAt(i) == digcheck.charAt(j)){
                        throw new IllegalArgumentException("ERROR: input cannot contain duplicate characters");
                    }
                }
            }
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    static boolean checkForDups(String secret){
        //Check if the code contains duplicate digits
        String digcheck = secret;
        for(int i = 0; i<digcheck.length(); i++){
            for(int j = i+1; j<digcheck.length(); j++){
                if(digcheck.charAt(i) == digcheck.charAt(j)){
                    return false;
                }
            }
        }
        return true;
    }

    int[] calcCowsAndBulls(String guess) {
        int bulls = 0;
        int cows = 0;

        // Check for bulls and cows
        for (int i = 0; i < secret.length(); i++) {
            char digit = guess.charAt(i);
            if (secret.charAt(i) == digit) {
                bulls++;
            } else {
                for (int j = 0; j < secret.length(); j++) {
                    if (digit == secret.charAt(j)) {
                        cows++;
                    }
                }
            }
        }
        return new int[]{bulls, cows};
    }

    int getLength() {
        return secret.length();
    }

    private String generateRandomNumberCode() {
        Random random = new Random();
        List<Integer> digits = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(digits.size());
            code.append(digits.remove(randomIndex));
        }

        return code.toString();
    }

    void setSecret(String s) {
        if(s.matches("[0-9]+")){
            this.secret = s;
        }
    }
}