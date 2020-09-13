package student.adventure;

import java.util.ArrayList;
import java.util.Arrays;

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
            System.out.println(items);
            System.out.println(room.getAvailableItems());
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
     * @param item item that the plaer will use
     */
    public void useItem(String item){
        System.out.println("Used" + item);
        System.out.print("> ");
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

}
