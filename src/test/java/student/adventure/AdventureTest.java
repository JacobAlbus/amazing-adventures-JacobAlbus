package student.adventure;

import java.io.*;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdventureTest {
    GameEngine engine;

    @Before
    public void setUp() throws IOException {
        engine = new GameEngine("src/main/java/resources/Rooms.json", "bob");
    }

    @Test
    public void testReadInJsonNullFile() throws IOException {
        try {
            new GameEngine("src/main/java/resources/RoomsNull.json", "bob");
        } catch (NullPointerException e) {
            assertEquals(e.getMessage(), "The json file passed is null");
        }
    }

    @Test
    public void testReadInJsonFileNotFound(){
        try {
            new GameEngine("src/main/java/student/adventure/R.json", "bob");
        } catch (IOException e) {
            assertEquals(e.getMessage(), "The specified file does not exist");
        }
    }

    @Test
    public void testProcessInputsInvalidInput(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "foo", "bar");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("I couldn't understand that command. Input 'help' to see list of commands\r\n", printedString);
    }

    @Test
    public void testProcessInputsExamine(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "examine", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You're in a dark room with one visible door." +
                "\r\nDirection: east \r\nItems:  \r\n", printedString);
    }

    @Test
    public void testProcessInputsTake(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "take", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("It seems like the room doesn't have that item\r\n", printedString);
    }

    @Test
    public void testProcessInputsDrop(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "drop", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("It seems like you don't have that item\r\n", printedString);
    }

    @Test
    public void testProcessInputsUse(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "use", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You do not have this item\r\n", printedString);
    }

    @Test
    public void testProcessInputsGo(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "go", "t");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You cannot go in that direction\r\n", printedString);
    }

    @Test
    public void testProcessInputsCheck(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "check", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("bob's Inventory: []\r\n", printedString);
    }

    @Test
    public void testProcessInputsHelp(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "help", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(Pattern.quote("+"))[0];
        assertEquals("Input go ", printedString);
    }

    @Test
    public void testProcessInputsDefault(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(engine.board.getRoom(0), "yuppie", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("I couldn't understand that command. " +
                             "Input 'help' to see list of commands\r\n", printedString);
    }

    @Test
    public void testPrintRoomMessage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.board.getRoom(0).printRoomMessage();

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You're in a dark room with one visible door." +
                                "\r\nDirection: east \r\nItems:  \r\n", printedString);
    }

    @Test
    public void testFindPlayerCurrentRoom(){
        Room room = engine.findPlayerCurrentRoom();

        assertArrayEquals(room.getRoomCoordinates(), engine.player.getPosition());
    }

    @Test
    public void testFindPlayerCurrentRoomNotFound() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);

            PrintStream old = System.out;

            System.setOut(ps);

            GameEngine invalidJSONEngine = new GameEngine("src/main/java/resources/RoomsInvalidTesting.json",
                                                      "bob");
            invalidJSONEngine.player.updatePosition(invalidJSONEngine.board.getRoom(0), "east");
            System.out.println(invalidJSONEngine.findPlayerCurrentRoom());

            System.out.flush();
            System.setOut(old);

            String printedString = baos.toString().split("> ")[0];
            assertEquals("It seems you've wandered off the path of destiny. " +
                                 "You have been returned to the first room.\r\n", printedString);
    }

    @Test
    public void testPrintOutMap(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);


        engine.printOutMap();

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString();
        assertEquals("1\n\r\n> ", printedString);

    }

    @Test
    public void testPlayerWins(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.player.updatePosition(engine.board.getRoom(0), "east");
        engine.player.takeItem(engine.board.getRoom(1), "torch");
        engine.player.updatePosition(engine.board.getRoom(1), "west");
        engine.player.useItem(engine.board.getRoom(0), "torch");
        engine.player.takeItem(engine.board.getRoom(0), "key");
        engine.player.updatePosition(engine.board.getRoom(0), "east");
        engine.player.updatePosition(engine.board.getRoom(1), "north");
        engine.player.useItem(engine.board.getRoom(2), "key");
        engine.player.updatePosition(engine.board.getRoom(2), "north");
        engine.player.updatePosition(engine.board.getRoom(3), "north");
        engine.player.takeItem(engine.board.getRoom(6), "calculator");
        engine.player.updatePosition(engine.board.getRoom(6), "south");
        engine.player.updatePosition(engine.board.getRoom(3), "east");
        engine.board.getRoom(5).addAvailableDoors("east");
        engine.player.updatePosition(engine.board.getRoom(5), "east");
        engine.player.takeItem(engine.board.getRoom(7), "lighter");
        engine.player.updatePosition(engine.board.getRoom(7), "east");
        engine.player.useItem(engine.board.getRoom(8), "lighter");
        engine.player.updatePosition(engine.board.getRoom(8), "south");
        engine.player.updatePosition(engine.board.getRoom(9), "south");
        engine.processInputs(engine.findPlayerCurrentRoom(), "examine", "");

        engine.gameLoop();
        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[9];
        assertEquals(" You win! Play again to venture back into your dorm\r\n", printedString);

    }

}