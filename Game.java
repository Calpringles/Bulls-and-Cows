import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

class Game {

    private Player currentPlayer;
    private SecretCode secretCode;
    private View gameUI;
    private boolean gameOver;
    private int totalBullsGuessed;
    private int totalGuesses;

    private char[] hints;

    private String guess;

    public String lastGuess;
    private int[] numCowsAndBulls;

    public Game(Player p, View v) {
        this.currentPlayer = p;
        this.gameUI = v;
        hints = new char[8];
        for(int i = 0; i<hints.length; i++){
            hints[i] = '#';
        }
        chooseGameTypeOrDetails();

    }

    // constructor for testing
    public Game(Player p, SecretCode c) {
        this.currentPlayer = p;
        secretCode = c;
        gameUI = new View(this, p);
        hints = new char[8];
        for(int i = 0; i<hints.length; i++){
            hints[i] = '#';
        }
    }

    private void chooseGameTypeOrDetails() {
        Scanner scanner = new Scanner(System.in);
        boolean rungame = true;
        while (rungame) {
            gameUI.outMessage("Choose an option:");
            gameUI.outMessage("(N)umbers game, (L)etters game, (D)isplay details, (C)load saved code, (T)op 10 scores, (Q)uit game");
            String choice = scanner.nextLine().toUpperCase();

            if (choice.equals("N") || choice.equals("L")) {
                this.secretCode = requestCode(choice);
                this.gameOver = false;
                break;
            } else if (choice.equals("D")) {
                displayPlayerStats();
            } else if (choice.equals("Q")) {
                rungame = false;
                gameUI.outMessage("Game is exiting...");
                this.secretCode = null;
                quitProgram();
            } else if (choice.equals("C")) {
                loadSavedCode();
                if (this.secretCode != null) {
                    this.gameOver = false;
                    break;
                }
            } else if (choice.equals("T")) {
                displayTop10Scores();
            } else {
                gameUI.outMessage("Invalid choice. Please enter N, L, D, C, T, or Q.");
            }
        }
    }


    public void displayTop10Scores() {
        Players players = new Players();
        ArrayList<Player> allPlayers = players.getAllPlayers();

        if (allPlayers.isEmpty()) {
            gameUI.outMessage("Error: No player stats have been stored.");
            return;
        }


        ArrayList<Player> sortedPlayers = new ArrayList<>();
        for (Player p : allPlayers) {
            insertSorted(sortedPlayers, p);
        }

        gameUI.outMessage("Top 10 Scores:");
        for (int i = 0; i < Math.min(10, sortedPlayers.size()); i++) {
            Player p = sortedPlayers.get(i);
            double proportion = p.getCodesAttempted() == 0 ? 0 : (double) p.getCodesDeciphered() / p.getCodesAttempted();
            gameUI.outMessage((i + 1) + ". " + p.username + " - Solved: " + p.getCodesDeciphered() + " - Proportion: " + String.format("%.2f", proportion));
        }
        for (int i = sortedPlayers.size(); i < 10; i++){
            gameUI.outMessage((i+1) + ". ");
        }
    }
    private void insertSorted(ArrayList<Player> sortedPlayers, Player player) {
        double playerProportion = player.getCodesAttempted() == 0 ? 0 : (double) player.getCodesDeciphered() / player.getCodesAttempted();
        int i;
        for (i = 0; i < sortedPlayers.size(); i++) {
            double currentProportion = sortedPlayers.get(i).getCodesAttempted() == 0 ? 0 : (double) sortedPlayers.get(i).getCodesDeciphered() / sortedPlayers.get(i).getCodesAttempted();
            if (playerProportion > currentProportion) {
                break;
            }
        }
        sortedPlayers.add(i, player);
    }


    public int playGame() {
        gameOver = false;
        while (!gameOver) {
            String guess = gameUI.getGuess();
            if (guess.equalsIgnoreCase("save")) {
                saveGame(currentPlayer.username + ".sav");
                saveCurrentCode();
                return -1;
            } else if (guess.equalsIgnoreCase("load")) {
                loadGame(currentPlayer.username + ".sav");
                continue;
            } else if (guess.equalsIgnoreCase("solution")) {
                showSolution();
                deleteSavedCode(currentPlayer);
                gameUI.updatePlayerAndSave(currentPlayer);
                break;
            } else if (guess.equalsIgnoreCase("hint")) {
                giveHint();
            }else{
            lastGuess = guess;
            int[] numCowsAndBulls = enterGuess(guess);
            checkCodeCracked(numCowsAndBulls[0]);}

        }
        return 1;
    }

    public SecretCode requestCode(String codeType) {
        SecretCode code;
        switch (codeType) {
            case "N":
                code = new NumbersCode(currentPlayer);
                break;
            case "L":
                code = new LettersCode(currentPlayer);
                break;
            default:
                throw new IllegalArgumentException("codeType invalid!");
        }
        gameUI.outCodeGenerated();
        return code;
    }

    public int[] enterGuess(String guess) {
        int[] result = {0, 0};
        if (secretCode.validateGuess(guess)) {
            result = secretCode.calcCowsAndBulls(guess);
            currentPlayer.incBulls(result[0]);
            currentPlayer.incCows(result[1]);

            // will not output number of cows and bulls if guess was correct
            // required by user stories
            if (result[0] != 4) {
                gameUI.outNumCowsAndBulls(result);
            }

            if (result[0] > 0) {
                totalBullsGuessed += result[0];
                totalGuesses++;

            }

        }
        return result;
    }

    public boolean checkCodeCracked(int numBulls) {
        if (numBulls == secretCode.getLength()) {
            gameUI.outCodeDeciphered();
            currentPlayer.incrementCodesDeciphered();
            this.gameOver = true;
            deleteSavedCode(currentPlayer);
            gameUI.updatePlayerAndSave(currentPlayer);
            return true;
        }
        return false;
    }

    public boolean saveGame(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this.secretCode.getSecret());
            oos.writeObject(this.currentPlayer.getBulls());
            oos.writeObject(this.currentPlayer.getCows());
            oos.writeObject(this.currentPlayer.codesDeciphered);
            oos.writeObject(this.currentPlayer.codesAttempted);
            oos.writeObject(this.totalBullsGuessed);
            oos.writeObject(this.totalGuesses);
            gameUI.outMessage("Game saved successfully.");
            return true;
        } catch (IOException e) {
            gameUI.outMessage("Error saving game: " + e.getMessage());
            return false;
        }
    }

    public void loadGame(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            String secret = (String) ois.readObject();
            int bulls = (int) ois.readObject();
            int cows = (int) ois.readObject();
            int codesDeciphered = (int) ois.readObject();
            int codesAttempted = (int) ois.readObject();
            int totalBulls = (int) ois.readObject();
            int totalGuess = (int) ois.readObject();

            this.secretCode.setSecret(secret);
            this.currentPlayer.updateBulls(bulls);
            this.currentPlayer.updateCows(cows);
            this.currentPlayer.codesDeciphered = codesDeciphered;
            this.currentPlayer.codesAttempted = codesAttempted;
            this.totalBullsGuessed = totalBulls;
            this.totalGuesses = totalGuess;
            gameUI.outMessage("Game loaded successfully.");

        } catch (IOException | ClassNotFoundException e) {
            gameUI.outMessage("Error loading game: " + e.getMessage());
        }
    }

    public void showSolution() {
        gameUI.outMessage("The solution is: " + secretCode.getSecret());
        this.gameOver = true;
    }

    public SecretCode getSecretCode() {
        return secretCode;
    }

    public Player getPlayer() {
        return currentPlayer;
    }

    private void displayPlayerStats() {
        Players players = new Players();
        Player player = players.getPlayer(currentPlayer.username);

        if (player != null) {
            gameUI.outMessage("Player Stats for " + player.username + ":");
            gameUI.outMessage("  Bulls: " + player.getBulls());
            gameUI.outMessage("  Cows: " + player.getCows());
            gameUI.outMessage("  Codes Attempted: " + player.getCodesAttempted());
            gameUI.outMessage("  Codes Deciphered: " + player.getCodesDeciphered());
        } else {
            gameUI.outMessage("Player stats not found.");
        }
    }

    public void saveCurrentCode() {
        Players players = new Players();
        Player player = players.getPlayer(currentPlayer.username);
        if (player.getSavedCode() != null) {
            gameUI.outMessage("Overwriting Previous Save.");
        }
        if (player != null && secretCode != null) {
            player.setSavedCode(secretCode.getSecret());
            players.addPlayer(player);
            players.savePlayersToCsv();
            gameUI.outMessage("Code saved.");
        }
    }

    private void loadSavedCode() {
        Players players = new Players();
        Player player = players.getPlayer(currentPlayer.username);
        if (player != null && player.getSavedCode() != null && !player.getSavedCode().equals("null")) {
            if (player.getSavedCode().matches("[0-9]+")) {
                this.secretCode = new NumbersCode(currentPlayer);
                this.secretCode.setSecret(player.getSavedCode());
            } else if (player.getSavedCode().matches("[a-zA-Z]+")) {
                this.secretCode = new LettersCode(currentPlayer);
                this.secretCode.setSecret(player.getSavedCode());
            }
            gameUI.outMessage("Code loaded.");
        } else {
            gameUI.outMessage("No saved code found.");
        }
    }

    private void deleteSavedCode(Player deleteP) {
        if (deleteP != null && deleteP.getSavedCode() != null) {
            deleteP.setSavedCode(null);
        }
    }

    private void quitProgram() {
        gameUI.updatePlayerAndSave(this.currentPlayer);
        System.exit(0);
    }

    public int giveHint() {
        char dig;
        int pos;
        if(lastGuess != null) {
            for (int i = 0; i < (secretCode.getSecret()).length(); i++) {
                char digit = lastGuess.charAt(i);
                if ((secretCode.getSecret()).charAt(i) == digit) {

                } else {
                    for (int j = 0; j < (secretCode.getSecret()).length(); j++) {
                        if (digit == (secretCode.getSecret()).charAt(j) && hints[j] == '#') {
                            dig = digit;
                            pos = j;
                            hints[j] = dig;
                            System.out.println("The Character '" + dig + "' Should Be At Position " + (pos + 1));
                            return 1;
                        }
                    }
                }
            }
        }
        else {
            System.out.println("Must Have A Previous Guess To Get A Hint.");
            return 0;
        }
        return 0;
}

    public boolean getstatus() {
        return gameOver;
    }
    public int getTotalBullsGuessed(){
        return totalBullsGuessed;
    }
    public int getTotalGuesses(){
        return totalGuesses;
    }
}