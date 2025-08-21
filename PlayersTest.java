import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayersTest {
    private Players players;
    private final String filename = "testPlayerFile.csv";

    @BeforeEach
    void setUp(){
        players = new Players();
    }

    @AfterEach
    void cleanUpFiles(){
        File f = new File(filename);
        f.delete();
        players.dropPlayersData();
    }

    @Test
    void saveLoadPlayers(){
        // create new example player
        Player p1 = new Player("sample1");
        p1.updateBulls(8);
        p1.updateCows(4);

        // add player to list of players without automatically saving player to non-test file
        players.getAllPlayers().add(p1);
        players.savePlayersToCsv(filename); // save to test file
        players.loadPlayersFromCsv(filename); // load from test file

        // check player details match
        Player p2 = players.getPlayer("sample1");
        Assertions.assertEquals(p1.getBulls(), p2.getBulls());
        Assertions.assertEquals(p1.getCows(), p2.getCows());
    }
}