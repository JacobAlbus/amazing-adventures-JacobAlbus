package student.adventure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Player {
    private int[] position;
    private ArrayList<String> items;
    private String name;

    public Player(String playerName){
        this.name = playerName;
        position = new int[2];
        items = new ArrayList<>();
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
            System.out.print("> ");
        } else{
            System.out.println("It seems like the room doesn't have that item");
            System.out.print("> ");
        }
    }

    /**
     * Prints out all the items currently in player's inventory
     */
    public void checkInventory(){
        System.out.println(name + "'s Inventory: " + items);
        System.out.print("> ");
    }

    /**
     * Removes item from player and puts it in room if it is,
     * if item is in player inventory and not in room
     * @param room player is currently in
     * @param item given item that game checks is in player's inventory and not in room
     */
    public void dropItem(Room room, String item){
        if(items.contains(item) && !room.getAvailableItems().contains(item)){
            items.remove(item);
            room.addAvailableItem(item);
            System.out.print("> ");
        } else if(room.getAvailableItems().contains(item)){
            System.out.println("Can't drop the item, it's already in the room");
            System.out.print("> ");
        } else{
            System.out.println("It seems like you don't have that item");
            System.out.print("> ");
        }

    }

    /**
     * Uses item if it is in player's inventory
     * @param item item that the player will use
     */
    public void useItem(Room room, String item){
        //ArrayList<String> itemsInGame = new ArrayList<>(Arrays.asList("torch"));
        if(items.contains(item)){
            switch(item){
                case "torch":
                    int[] torchCoords = {0, 0};
                    if(isRoomCorrect(room, torchCoords)){
                        room.setPrimaryDescription(room.getSecondaryDescription());
                        room.addAvailableItem(room.getUnavailbleItems().get(0));
                        items.remove(item);
                    }
                    break;
                case "key":
                    int[] keyCoords = {1, 1};
                    if(isRoomCorrect(room, keyCoords)){
                        room.setPrimaryDescription(room.getSecondaryDescription());
                        room.addAvailableDoors(room.getUnavailableDoors().get(0));
                        room.removeUnavailableDoors(room.getUnavailableDoors().get(0));
                        items.remove(item);
                    }
                    break;
                case "calculator":
                    int[] calcCoords = {2, 2};
                    if(isRoomCorrect(room, calcCoords)){
                        if(didPlayerAceMathTest()){
                            room.setPrimaryDescription(room.getSecondaryDescription());
                            room.addAvailableDoors(room.getUnavailableDoors().get(0));
                            room.removeUnavailableDoors(room.getUnavailableDoors().get(0));
                            items.remove(item);
                        } else{
                            System.out.println("Better try again!");
                        }
                    }
                    break;
                case "lighter":
                    int[] lighterCoords = {4, 2};
                    if(isRoomCorrect(room, lighterCoords)){
                        room.setPrimaryDescription(room.getSecondaryDescription());
                        room.addAvailableDoors(room.getUnavailableDoors().get(0));
                        room.removeUnavailableDoors(room.getUnavailableDoors().get(0));
                        items.remove(item);
                    }
                    break;
                default:
                    System.out.println("Sorry but that item has no use");
                    System.out.print("> ");
                    break;
            }

        } else {
            System.out.println("You do not have this item");
            System.out.print("> ");
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
            System.out.print("> ");
        }

    }

    /**
     * Checks if player is using item in correct room
     * called in useItem
     * @param room room player is currently in
     * @param coords coords the player should be using the item in
     * @return true if player is in correct room
     */
    private boolean isRoomCorrect(Room room, int[] coords){
        boolean isRoomCorrect = false;
        if(Arrays.equals(room.getRoomCoordinates(), coords)){
            isRoomCorrect = true;
        } else {
            System.out.println("It appears that the item has no use here");
            System.out.print("> ");
        }

        return isRoomCorrect;
    }

    /**
     * Simulates a math test with basic problems; returns boolean depending on whether or not player passes test
     * called in useItem for calculator
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
     * called in didPlayerAceMathTest
     * @param question the question being asked of the player
     * @param answer correct answer to question
     * @return 1 if player is right, 0 otherwise
     */
    private int mathQuestion(String question, String answer){
        Scanner mathTestInput = new Scanner(System.in);
        System.out.println(question);
        printInputPrompt();

        String input = mathTestInput.nextLine().trim();
        if(input.equals(answer)){
            System.out.println("Correct");
            return 1;
        } else {
            System.out.println("Wrong");
            return 0;
        }
    }

    public static void printInputPrompt(){
        System.out.print("> ");
    }

}
