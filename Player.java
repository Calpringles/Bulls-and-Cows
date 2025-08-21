public class Player {

    public String username;
    private int numberOfBulls;
    private int numberOfCows;
    public int codesAttempted;
    public int codesDeciphered = 0;
    private String savedCode;

    public Player(String username) {
        this.username = username;
        this.savedCode = null;
    }

    public void updateBulls(int b) {
        this.numberOfBulls = b;
    }

    public void updateCows(int c) {
        this.numberOfCows = c;
    }

    public void incBulls(int b) {this.numberOfBulls += b;}
    public void incCows(int c) {this.numberOfCows += c;}

    public void incrementCodesAttempted() {
        this.codesAttempted++;
    }

    public void incrementCodesDeciphered() {
        this.codesDeciphered++;
    }

    public int getBulls() {
        return this.numberOfBulls;
    }

    public int getCows() {
        return this.numberOfCows;
    }

    public int getCodesAttempted() {
        return this.codesAttempted;
    }

    public int getCodesDeciphered() {
        return this.codesDeciphered;
    }

    public String getSavedCode() {
        return this.savedCode;
    }

    public void setSavedCode(String savedCode) {
        this.savedCode = savedCode;
    }

    public String toCsvString() {
        return username + "," + numberOfBulls + "," + numberOfCows + "," + codesAttempted + "," + codesDeciphered + "," + savedCode;
    }

    public static Player fromCsvString(String csv) {
        String[] parts = csv.split(",", -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        Player player = new Player(parts[0]);
        player.numberOfBulls = Integer.parseInt(parts[1]);
        player.numberOfCows = Integer.parseInt(parts[2]);
        player.codesAttempted = Integer.parseInt(parts[3]);
        player.codesDeciphered = Integer.parseInt(parts[4]);
        if (parts.length > 5) {
            player.savedCode = parts[5];
        } else {
            player.savedCode = null;
        }
        return player;
    }
}