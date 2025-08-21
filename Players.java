import java.io.*;
import java.util.ArrayList;

public class Players {
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private final String playersFile = "players.csv";

    public Players() {
        loadPlayersFromCsv();
    }

    public void addPlayer(Player p) {
        Player existingPlayer = getPlayer(p.username);
        if (existingPlayer != null) {
            // Update existing player's stats
            existingPlayer.updateBulls(p.getBulls());
            existingPlayer.updateCows(p.getCows());
            existingPlayer.codesAttempted = p.codesAttempted;
            existingPlayer.codesDeciphered = p.codesDeciphered;
            existingPlayer.setSavedCode(p.getSavedCode());
        } else {
            // Add new player
            allPlayers.add(p);
        }
        savePlayersToCsv();
    }

    public void savePlayersToCsv() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playersFile))) {
            for (Player player : allPlayers) {
                writer.write(player.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving players: " + e.getMessage());
        }
    }

    public void savePlayersToCsv(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Player player : allPlayers) {
                writer.write(player.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving players: " + e.getMessage());
        }
    }

    public void loadPlayersFromCsv() {
        allPlayers.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(playersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = Player.fromCsvString(line);
                allPlayers.add(player);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No player data found. Will create file on save.");
        } catch (IOException e) {
            System.out.println("Error loading players: " + e.getMessage());
        }
    }

    public void loadPlayersFromCsv(String filename) {
        allPlayers.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = Player.fromCsvString(line);
                allPlayers.add(player);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No player data found. Will create file on save.");
        } catch (IOException e) {
            System.out.println("Error loading players: " + e.getMessage());
        }
    }

    public Player getPlayer(String username) {
        for (Player p : allPlayers) {
            if (p.username.equals(username)) {
                return p;
            }
        }
        return null;
    }

    public void dropPlayersData(){
        allPlayers = new ArrayList<>();
    }

    public ArrayList<Player> getAllPlayers(){
        return allPlayers;
    }
}
