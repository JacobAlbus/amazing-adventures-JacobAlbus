package student.adventure;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerTests {
    GameBoard board;
    Player player;

    /**
     * Initializes GameBoard and Player objects for testing
     */
    @Before
    public void setUp() {
        player = new Player("bob");

        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/student/adventure/Rooms.json"));

            board = gson.fromJson(reader, GameBoard.class);
            reader.close();
        } catch (NullPointerException e) {
            System.out.println("Null value passed");
        } catch (IOException e) {
            System.out.println("ERORR: File not found!");
        }
    }

    @Test
    public void testPlayerUpdatePosition(){
        player.updatePosition(board.getRoom(0),"east");

        int [] position = player.getPosition();
        int [] roomCoordinates = board.getRoom(1).getRoomCoordinates();

        assertArrayEquals(roomCoordinates, position);
    }

    @Test
    public void testPlayerUpdatePositionInvalidDirection(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);
        player.updatePosition(board.getRoom(0), "weast");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You cannot go in that direction\r\n", printedString);
    }

    @Test
    public void testPlayerUpdatePositionNoInput(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);
        player.updatePosition(board.getRoom(0), "weast");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You cannot go in that direction\r\n", printedString);
    }

    @Test
    public void testPlayerTakeItem(){
        player.takeItem(board.getRoom(1), "torch");
        ArrayList<String> items = player.getItems();
        Assert.assertTrue(items.contains("torch"));
    }

    @Test
    //code from here: https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
    public void testPlayerTakeItemNoneFound(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        player.takeItem(board.getRoom(1), "borch");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("It seems like the room doesn't have that item\r\n", printedString);
    }

    @Test
    public void testPlayerDropsItem(){
        player.updatePosition(board.getRoom(0), "east");
        player.takeItem(board.getRoom(1), "torch");
        Assert.assertTrue(player.getItems().contains("torch"));

        player.dropItem(board.getRoom(1), "torch");
        Assert.assertTrue(board.getRoom(1).getAvailableItems().contains("torch"));
    }

    @Test
    public void testPlayerDropsItemNoneFoundInInventory(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        player.dropItem(board.getRoom(0), "torch");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("It seems like you don't have that item\r\n", printedString);
    }

    @Test
    public void testPlayerDropsItemAlreadyInRoom(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);
        player.takeItem(board.getRoom(2), "torch");
        player.dropItem(board.getRoom(1), "torch");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[1];
        assertEquals(" Can't drop the item, it's already in the room\r\n", printedString);
    }

}