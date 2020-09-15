package student.adventure;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerTests {
    GameEngine engine;

    @Before
    public void setUp() throws IOException {
        engine = new GameEngine("src/main/java/resources/Rooms.json", "bob");

    }

    @Test
    public void testPlayerUpdatePositionEast(){
        engine.player.updatePosition(engine.board.getRoom(0),"east");

        int [] position = engine.player.getPosition();
        int [] roomCoordinates = engine.board.getRoom(1).getRoomCoordinates();

        assertArrayEquals(roomCoordinates, position);
    }

    @Test
    public void testPlayerUpdatePositionWest(){
        engine.player.updatePosition(engine.board.getRoom(0),"east");
        engine.player.updatePosition(engine.board.getRoom(1),"west");

        int [] position = engine.player.getPosition();
        int [] roomCoordinates = engine.board.getRoom(0).getRoomCoordinates();


        assertArrayEquals(roomCoordinates, position);
    }

    @Test
    public void testPlayerUpdatePositionNorth(){
        engine.player.updatePosition(engine.board.getRoom(0),"east");
        engine.player.updatePosition(engine.board.getRoom(1),"north");

        int [] position = engine.player.getPosition();
        int [] roomCoordinates = engine.board.getRoom(2).getRoomCoordinates();

        assertArrayEquals(roomCoordinates, position);
    }

    @Test
    public void testPlayerUpdatePositionSouth(){
        engine.player.updatePosition(engine.board.getRoom(0),"east");
        engine.player.updatePosition(engine.board.getRoom(1),"north");
        engine.player.updatePosition(engine.board.getRoom(2),"south");

        int [] position = engine.player.getPosition();
        int [] roomCoordinates = engine.board.getRoom(1).getRoomCoordinates();

        assertArrayEquals(roomCoordinates, position);
    }

    @Test
    public void testPlayerUpdatePositionInvalidDirection(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);
        engine.player.updatePosition(engine.board.getRoom(0), "weast");

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
        engine.player.updatePosition(engine.board.getRoom(0), "weast");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You cannot go in that direction\r\n", printedString);
    }

    @Test
    public void testCheckInventory(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.player.takeItem(engine.board.getRoom(1), "torch");
        engine.player.checkInventory();

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[1];
        assertEquals(" bob's Inventory: [torch]\r\n", printedString);
    }

    @Test
    public void testPlayerTakeItem(){
        engine.player.takeItem(engine.board.getRoom(1), "torch");
        ArrayList<String> items = engine.player.getItems();
        Assert.assertTrue(items.contains("torch"));
    }

    @Test
    // Code from here: https://stackoverflow.com/questions/8708342/redirect-console-output-to-string-in-java
    public void testPlayerTakeItemNotFoundInRoom(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.player.takeItem(engine.board.getRoom(1), "borch");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("It seems like the room doesn't have that item\r\n", printedString);
    }

    @Test
    public void testPlayerDropsItem(){
        engine.player.updatePosition(engine.board.getRoom(0), "east");
        engine.player.takeItem(engine.board.getRoom(1), "torch");
        Assert.assertTrue(engine.player.getItems().contains("torch"));

        engine.player.dropItem(engine.board.getRoom(1), "torch");
        Assert.assertTrue(engine.board.getRoom(1).getAvailableItems().contains("torch"));
    }

    @Test
    public void testPlayerDropsItemNotFoundInInventory(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.player.dropItem(engine.board.getRoom(0), "torch");

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
        engine.player.takeItem(engine.board.getRoom(2), "torch");
        engine.player.dropItem(engine.board.getRoom(1), "torch");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[1];
        assertEquals(" Can't drop the item, it's already in the room\r\n", printedString);
    }

    @Test
    public void testPlayerUsesItemNotFoundInInventory(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);
        engine.player.useItem(engine.board.getRoom(0), "torch");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You do not have this item\r\n", printedString);
    }

    @Test
    public void testPlayerUsesItemInvalidRoom(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;
        System.setOut(ps);

        engine.player.takeItem(engine.board.getRoom(1), "torch");
        engine.player.useItem(engine.board.getRoom(1), "torch");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[1].trim();
        assertEquals("It appears that the item has no use here", printedString);
    }

    @Test
    public void testPlayerUsesItemWithNoUse(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;
        System.setOut(ps);

        engine.player.takeItem(engine.board.getRoom(4), "knife");
        engine.player.useItem(engine.board.getRoom(4), "knife");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString();
        assertEquals("> Sorry but that item has no use\r\n> ", printedString);
    }

    @Test
    public void testPlayerUsesTorch(){
        engine.player.takeItem(engine.board.getRoom(1), "torch");
        engine.player.useItem(engine.board.getRoom(0), "torch");

        String description = engine.board.getRoom(0).getPrimaryDescription();
        assertEquals("After using the torch, you see a sparkle in one of the cracks. " +
                "After investigating, you see a key", description);
    }

    @Test
    public void testPlayerUsesKey(){
        engine.player.takeItem(engine.board.getRoom(1), "torch");
        engine.player.useItem(engine.board.getRoom(0), "torch");
        engine.player.takeItem(engine.board.getRoom(0), "key");
        engine.player.useItem(engine.board.getRoom(2), "key");

        String description = engine.board.getRoom(2).getPrimaryDescription();
        assertEquals("The door at the north end opened!", description);
    }

    @Test
    public void testPlayerUsesLighter(){
        engine.player.takeItem(engine.board.getRoom(7), "lighter");
        engine.player.useItem(engine.board.getRoom(8), "lighter");

        String description = engine.board.getRoom(8).getPrimaryDescription();
        assertEquals("WOW, the smell of rotten eggs was actually a gas leak " +
                "and the lighter caused the room to combust. The south door was blown down", description);
    }

    @Test
    public void testPlayerUsesCalculator(){
        // Next two lines are from: https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream in = new ByteArrayInputStream("64".getBytes());
        System.setIn(in);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;
        System.setOut(ps);

        try{
            engine.player.takeItem(engine.board.getRoom(6), "calculator");
            engine.player.useItem(engine.board.getRoom(5), "calculator");
        } catch (NoSuchElementException e){
            in = new ByteArrayInputStream("64".getBytes());
            System.setIn(in);
        }


        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[1].trim();
        assertEquals("You have begun the eternal math test, pick your answers wisely!\r\n" +
                "What is the product of the squares of four and two", printedString);
    }

    @Test
    public void testPlayerMathTest(){
        // Next two lines are from: https://bugsdb.com/_en/debug/09bdbc2d248d31d6785ba772ea8689cb
        ByteArrayInputStream in = new ByteArrayInputStream("64".getBytes());
        System.setIn(in);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;
        System.setOut(ps);

        engine.player.mathQuestion("What's 4 times 16", "64");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[1].trim();
        assertEquals("Correct", printedString);
    }
}