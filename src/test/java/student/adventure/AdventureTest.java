package student.adventure;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AdventureTest {
    GameBoard board;
    Player player;
    GameEngine engine;

    @Before
    public void setUp() {
        engine = new GameEngine("Bobby");
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
    public void testProcessInputsExamie(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(board.getRoom(0), "examine", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split("'")[0];
        assertEquals("You", printedString);
    }

    @Test
    public void testProcessInputsTake(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(board.getRoom(0), "take", "foo");

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

        engine.processInputs(board.getRoom(0), "drop", "foo");

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

        engine.processInputs(board.getRoom(0), "use", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("Usedfoo\r\n", printedString);
    }

    @Test
    public void testProcessInputsGo(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(board.getRoom(0), "go", "t");

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

        engine.processInputs(board.getRoom(0), "check", "foo");

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("Bobby's Inventory: []\r\n", printedString);
    }

    @Test
    public void testProcessInputsHelp(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        engine.processInputs(board.getRoom(0), "help", "foo");

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

        engine.processInputs(board.getRoom(0), "yuppie", "foo");

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

        engine.printRoomMessage(board.getRoom(0));

        System.out.flush();
        System.setOut(old);

        String printedString = baos.toString().split(">")[0];
        assertEquals("You're in a dark room with one visible door." +
                                "\r\nDirection: east \r\nItems: \r\n", printedString);
    }
}