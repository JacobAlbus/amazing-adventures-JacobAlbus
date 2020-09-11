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

    public void grabItem(String item){
        item = item.toLowerCase();
        items.add(item);
    }

    public void dropItem(String item){
        item = item.toLowerCase();
        items.remove(item);
    }

    public void updatePosition(String direction){
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
    }

}
