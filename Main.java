public class Main {
    public static void main(String[] args) {
        View gameUI = new View();
        Players allPlayers = new Players();
        allPlayers.addPlayer(gameUI.getPlayer());
        allPlayers.savePlayersToCsv();
    }
}