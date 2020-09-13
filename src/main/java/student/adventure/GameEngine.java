package student.adventure;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class GameEngine {
    private static GameBoard board;
    private static Player player;
    private static Scanner gameMaster = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Hello player! What would you like to name your adventurer?");
        printInputPrompt();
        String name = gameMaster.nextLine();
        System.out.println(".\n" + ".\n" + ".\n.");

        GameEngine engine = new GameEngine(name);
        engine.gameLoop();
    }

    /**
     * Initializes Player and GameBoard object
     */
    public GameEngine(String name){
        player = new Player(name);

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

    /**
     * Contains main loop for game: prompts player for input then filters and
     * processes input to figure out what player wants to do
     */
    public void gameLoop(){
        Room room = findPlayerCurrentRoom();
        printRoomMessage(room);

        while(true){
            room = findPlayerCurrentRoom();

            ArrayList<String> inputs = filterInputs();
            String action = inputs.get(0);
            String noun = inputs.get(1);

            if(action.equals("exit") || action.equals("quit")){
                break;
            }

            processInputs(room, action, noun);
        }
    }

    /**
     * Prompts user for input and filters it to match the form 'action' + 'noun'
     * @return an arrayList containing an 'action' command (use, go, examine, take, check etc.)
     *         and a 'noun' (usually an item or direction)
     */
    public ArrayList<String> filterInputs(){
        ArrayList<String> actions = new ArrayList<>(Arrays.asList("use", "take", "drop", "go", "check",
                                                                  "examine", "help", "exit", "quit"));
        ArrayList<String> processedInputs = new ArrayList<>();

        String[] playerInputs = gameMaster.nextLine().toLowerCase().split(" ");

        // sees how many different words user inputted, tracks both if more than one, only tracks first otherwise
        String action = playerInputs[0];
        String noun;
        if(playerInputs.length > 1){
            noun = playerInputs[1];
        } else{
            noun = "foo";
        }

        if(actions.contains(action)){
            Collections.addAll(processedInputs, action, noun);
        } else{
            // adds filler to reset input loop
            Collections.addAll(processedInputs, "foo", "bar");
        }

        return processedInputs;
    }

    /**
     * Contains a switch tree that calls certain methods depending on param 'action' from user input
     * @param room the Room object player is currently in
     * @param action command for the player (use, go, examine, take, check etc.)
     * @param noun object or thing the player interacts with (usually an item or direction)
     */
    public void processInputs(Room room, String action, String noun){
        switch(action){
            case "examine":
                printRoomMessage(room);
                break;
            case "take":
                player.takeItem(room, noun);
                break;
            case "drop":
                player.dropItem(room, noun);
                break;
            case "use":
                player.useItem(noun);
                break;
            case "go":
                player.updatePosition(room, noun);
                room = findPlayerCurrentRoom();
                printRoomMessage(room);
                break;
            case "check":
                player.checkInventory();
                break;
            case "help":
                printHelpCommands();
                break;
            default:
                System.out.println("I couldn't understand that command. Input 'help' to see list of commands");
                printInputPrompt();
        }
    }

    /**
     * Prints a formatted message depending on which room player is in
     * @param room the Room object player is currently in
     */
    public void printRoomMessage(Room room){
        System.out.println(room.getPrimaryDescription());

        StringBuilder availableDoors = new StringBuilder();
        for(String direction: room.getAvailableDoors()){
            availableDoors.append(direction);
            availableDoors.append(" ");
        }

        System.out.println("Direction: " + availableDoors.toString());

        StringBuilder items = new StringBuilder();
        for(String item: room.getAvailableItems()){
            items.append(item);
            items.append(" ");
        }

        System.out.println("Items: " + items.toString());
        printInputPrompt();
    }

    /**
     * finds room the player is currently in
     * @return the Room object player is currently in
     */
    public Room findPlayerCurrentRoom(){
        for(int i = 0; i < board.getBoardSize(); i++){
            Room room = board.getRoom(i);
            if(Arrays.equals(player.getPosition(), room.getRoomCoordinates())){
                return room;
            }
        }
        // Returns player to first room if current room isn't found in GameBoard
        System.out.println("It seems you've wandered off the path of destiny. " +
                            "You have been returned to the first room.");
        printInputPrompt();
        return board.getRoom(0);
    }

    /**
     * Prints a list of commands recognized by student.adventure.GameEngine
     */
    public void printHelpCommands(){
        System.out.println("Input go + 'direction' (east, west, north, south) to through corresponding direction \n" +
                "Input take + 'item' to grab item from room \n" +
                "Input use + 'item' to use item in inventory \n" +
                "Input drop + 'item' to drop item from inventory and leave it in room \n" +
                "Input examine to see room information \n" +
                "Input check to see all items currently in inventory \n" +
                "Input exit or quit to stop playing Adventure");
        printInputPrompt();
    }

    public static void  printInputPrompt(){
        System.out.print("> ");
    }
}