package student.adventure;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
    private int[] position = {1, 1};
    private ArrayList<String> items = new ArrayList<>();
    private String name;
    private GameBoard board;

    public Player(String playerName) throws IOException {
        this.name = playerName;

        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/resources/Rooms.json"));

            board = gson.fromJson(reader, GameBoard.class);
            reader.close();
        } catch (NullPointerException e) {
            throw new NullPointerException("The json file passed is null");
        } catch (IOException e) {
            throw new IOException("The specified file does not exist");
        }
    }

    public int[] getPosition(){
        return position;
    }

    public void setPosition(int[] position){
        this.position = position;
    }

    public ArrayList<String> getItems(){
        return items;
    }

    /**
     * Removes item from room and adds to player inventory if it is in room and not in player's inventory
     * @param room room player is currently in
     * @param item given item that game checks is in room
     */
    public void takeItem(Room room, String item){
        if(room.getAvailableItems().contains(item)){
            items.add(item);
            room.getAvailableItems().remove(item);
            printInputPrompt();
        } else{
            System.out.println("It seems like the room doesn't have that item");
            printInputPrompt();
        }
    }

    /**
     * Removes item from player and puts it in room if it is,
     * If item is in player inventory and not in room
     * @param room player is currently in
     * @param item given item that game checks is in player's inventory and not in room
     */
    public void dropItem(Room room, String item){
        if(items.contains(item) && !room.getAvailableItems().contains(item)){
            items.remove(item);
            room.addAvailableItem(item);
            printInputPrompt();
        } else if(room.getAvailableItems().contains(item)){
            System.out.println("Can't drop the item, it's already in the room");
            printInputPrompt();
        } else{
            System.out.println("It seems like you don't have that item");
            printInputPrompt();
        }

    }

    /**
     * Uses item if it is in player's inventory
     * @param item item that the player will use
     */
    public void useItem(Room room, String item){
        if(items.contains(item)){
            switch(item){
                case "torch":
                    int[] torchCoords = board.getRoom(0).getRoomCoordinates();
                    if(isRoomCorrectForItemUse(room, torchCoords)){
                        room.setPrimaryDescription(room.getSecondaryDescription());
                        room.addAvailableItem(room.getUnavailbleItems().get(0));
                        items.remove(item);
                        room.printRoomMessage();
                    }
                    break;
                case "key":
                    int[] keyCoords = board.getRoom(2).getRoomCoordinates();
                    if(isRoomCorrectForItemUse(room, keyCoords)){
                        room.setPrimaryDescription(room.getSecondaryDescription());
                        room.addAvailableDoors(room.getUnavailableDoors().get(0));
                        room.removeUnavailableDoors(room.getUnavailableDoors().get(0));
                        items.remove(item);
                        room.printRoomMessage();
                    }
                    break;
                case "calculator":
                    int[] calcCoords = board.getRoom(5).getRoomCoordinates();
                    if(isRoomCorrectForItemUse(room, calcCoords)){
                        if(didPlayerAceMathTest()){
                            room.setPrimaryDescription(room.getSecondaryDescription());
                            room.addAvailableDoors(room.getUnavailableDoors().get(0));
                            room.removeUnavailableDoors(room.getUnavailableDoors().get(0));
                            items.remove(item);
                            room.printRoomMessage();
                        } else{
                            System.out.println("Better try again!");
                        }
                    }
                    break;
                case "lighter":
                    int[] lighterCoords = board.getRoom(8).getRoomCoordinates();
                    if(isRoomCorrectForItemUse(room, lighterCoords)){
                        room.setPrimaryDescription(room.getSecondaryDescription());
                        room.addAvailableDoors(room.getUnavailableDoors().get(0));
                        room.removeUnavailableDoors(room.getUnavailableDoors().get(0));
                        items.remove(item);
                        room.printRoomMessage();
                    }
                    break;
                default:
                    System.out.println("Sorry but that item has no use");
                    printInputPrompt();
                    break;
            }

        } else {
            System.out.println("You do not have this item");
            printInputPrompt();
        }
    }

    /**
     * Updates player position relative to current room location
     * Only updates if given direction is valid for given room
     * @param room Room object player is currently in
     * @param direction given direction user wants player to go
     */
    public void updatePosition(Room room, String direction){
        String[] directions = {"east", "north", "west", "south"};

        if(Arrays.asList(directions).contains(direction) && room.getAvailableDoors().contains(direction)){
            switch(direction){
                case "east":
                    position[0] += 1;
                    break;
                case "north":
                    position[1] += 1;
                    break;
                case "west":
                    position[0] -= 1;
                    break;
                case "south":
                    position[1] -= 1;
                    break;
            }
        } else{
            System.out.println("You cannot go in that direction");
            printInputPrompt();
        }

    }

    /**
     * Prints out all the items currently in player's inventory
     */
    public void checkInventory(){
        System.out.println(name + "'s Inventory: " + items);
        printInputPrompt();
    }

    /**
     * Checks if the room item is used in, is a valid room for that item to be used
     * @param room Room object in which useItem() is called in
     * @param coords valid coords for item to be used in
     * @return boolean depending on wheter or not room is valid for item use
     */
    private boolean isRoomCorrectForItemUse(Room room, int[] coords){
        boolean isRoomCorrect = false;
        if(Arrays.equals(room.getRoomCoordinates(), coords)){
            isRoomCorrect = true;
        } else {
            System.out.println("It appears that the item has no use here");
            printInputPrompt();
        }

        return isRoomCorrect;
    }

    /**
     * Simulates a math test with basic problems; returns boolean depending on whether or not player passes test
     * @return boolean representing whether or not player passed test
     */
    private boolean didPlayerAceMathTest(){
        int numCorrect = 0;

        System.out.println("You have begun the eternal math test, pick your answers wisely!");

        numCorrect += this.mathQuestion("What is the product of the squares of four and two", "64");
        numCorrect += this.mathQuestion("Give me pi to the first 3 digits", "3.14");
        numCorrect += this.mathQuestion("What is the standard representation " +
                "for the square root of negative one", "i");
        numCorrect += this.mathQuestion("What is the cube of the sixth element " +
                "in the set of all natural numbers", "216");
        numCorrect += this.mathQuestion("What is the factorial of 0", "1");

        if(numCorrect >= 4){
            System.out.println("Congratulations, you have passed the test");
            return true;
        } else {
            System.out.println("You failed stupid, try again");
            return false;
        }

    }

    /**
     * Asks player a math question, takes input, and determines if it's correct
     * Made public for testing purposes
     * @param question the question being asked of the player
     * @param correctAnswer correct answer to question
     * @return 1 if player is right, 0 otherwise
     */
    public int mathQuestion(String question, String correctAnswer){
        System.out.println(question);
        printInputPrompt();

        Scanner playerInput = new Scanner(System.in);
        String playerAnswer = playerInput.nextLine();

        if(playerAnswer.equals(correctAnswer)){
            System.out.println("Correct");
            return 1;
        } else {
            System.out.println("Wrong");
            return 0;
        }

    }

    private void printInputPrompt(){
        System.out.print("> ");
    }
}