import com.google.gson.Gson;
import student.adventure.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameEngine {
    private GameBoard board;
    private Player player;
    private Scanner gameMaster = new Scanner(System.in);
    private final String[] DIRECTIONS = {"east", "north", "west", "south"};

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
    }

    public GameEngine(){
        this.initializeWorld();

        while(true){
            Room room = findPlayerCurrentRoom();
            printRoomMessage(room);

            String input = gameMaster.nextLine().toLowerCase();
            if(input.equals("exit") || input.equals("quit")){
                break;
            } else if(Arrays.asList(DIRECTIONS).contains(input) && room.getAvailableDoors().contains(input)){
                player.updatePosition(input);
            } else{
                System.out.println("Silly mortal, the player is incapable of such commands! " +
                                   "Input a proper command this time.");
            }

        }
    }

    private void initializeWorld(){
        System.out.println("Hello player! What would you like to name your adventurer?");
        String name = gameMaster.nextLine();
        player = new Player(name);

        System.out.println(".\n" + ".\n" + ".\n.");

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

    private void printRoomMessage(Room room){
        System.out.println(room.getDescription());

        String availableDoors = "";
        for(String direction: room.getAvailableDoors()){
            availableDoors += direction + "  ";
        }

        System.out.println("Direction: " + availableDoors);

        String items = "";
        for(String direction: room.getItems()){
            items += direction + "  ";
        }

        System.out.println("Items:" + items);
        System.out.print("> ");
    }

    private void computeUserInputs(){

    }

    private Room findPlayerCurrentRoom(){
        for(int i = 0; i < board.getBoardSize(); i++){
            Room room = board.getRoom(i);
            if(Arrays.equals(player.getPosition(), room.getRoomCoordinates())){
                return room;
            }
        }
        System.out.println("It seems you've wandered off the path of destiny. " +
                            "You have been returned to the first room.");
        return board.getRoom(0);
    }
}