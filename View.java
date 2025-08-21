import java.util.Scanner;

public class View {
    public Player player;
    public Game game;
    private Players players;

    public View() {
        players = new Players();
        String uName = getUsername();
        player = players.getPlayer(uName);
        if (player == null){
            player = new Player(uName);
        }

        game = new Game(player, this);

        if(game.getSecretCode() != null) {
            saveCodeAttempted(game.getSecretCode().getSecret());

            int status = game.playGame();
            while (status == 1) {
                System.out.println("------------------------");
                game = new Game(player, this);
                status = game.playGame();
            }
        }
        updatePlayerAndSave(game.getPlayer());
    }

    // constructor for testing
    public View(Game g, Player p){
        game = g;
        player = p;
    }

    public String getUsername() {
        Scanner sc = new Scanner(System.in);
        String username = null;
        System.out.print("Enter your username: ");
        if (sc.hasNextLine()) {
            username = sc.nextLine();
        }
        return username;
    }

    public String getGuess() {
        Scanner sc = new Scanner(System.in);
        String guess = "";
        System.out.print("Enter your guess or type 'save' to save the code and return later, 'solution' to show the solution and end the game or 'hint' to get a hint: ");
        if (sc.hasNextLine()) {
            guess = sc.nextLine();
        }
        return guess;
    }

    public void outCodeGenerated() {
        System.out.println("A secret code has been generated.");
    }

    public void outCodeDeciphered() {
        System.out.println("Congratulations! You've cracked the code.");
    }

    public void outNumCowsAndBulls(int[] result) {
        System.out.println("Bulls: " + result[0] + ", Cows: " + result[1]);
    }

    public void outMessage(String message) {
        System.out.println(message);
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public void updatePlayerAndSave(Player updatedPlayer) {
        Players allPlayers = new Players();
        allPlayers.addPlayer(updatedPlayer);
        allPlayers.savePlayersToCsv();
    }

    private void saveCodeAttempted(String code) {
        player.setSavedCode(code); // Set the code being attempted
        Players allPlayers = new Players();
        allPlayers.addPlayer(player); // Update the player
        allPlayers.savePlayersToCsv(); // Save to CSV
    }
}