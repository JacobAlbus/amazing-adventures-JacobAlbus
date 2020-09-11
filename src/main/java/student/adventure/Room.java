package student.adventure;

import java.util.ArrayList;

public class Room {
    private int[] roomCoordinates;
    private ArrayList<String> availableDoors;
    private ArrayList<String> unavailableDoors;
    private ArrayList<String> items;
    private String description;

    public Room(int[] roomCoordinates, ArrayList<String> availableDoors,
                ArrayList<String> unavailableDoors, ArrayList<String> items, String description){
        this.roomCoordinates = roomCoordinates;
        this.availableDoors = availableDoors;
        this.unavailableDoors = unavailableDoors;
        this.items = items;
        this.description = description;
    }

    public int[] getRoomCoordinates(){
        return roomCoordinates;
    }

    public void setRoomCoordinates(int[] roomCoordinates){
        this.roomCoordinates = roomCoordinates;
    }

    public ArrayList<String> getAvailableDoors(){
        return availableDoors;
    }

    public void setAvailableDoors(ArrayList<String> doors){
        this.availableDoors = doors;
    }

    public ArrayList<String> getUnavailableDoors(){
        return unavailableDoors;
    }

    public void setUnavailableDoors(ArrayList<String> doors){
        this.unavailableDoors = doors;
    }

    public ArrayList<String> getItems(){
        return items;
    }

    public void setItems(ArrayList<String> items){
        this.items = items;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

}
