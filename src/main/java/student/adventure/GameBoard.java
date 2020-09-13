package student.adventure;

import java.util.List;

public class GameBoard {
    protected List<Room> rooms;

    public GameBoard(List<Room> rooms){
        this.rooms = rooms;
    }

    public int getBoardSize(){
        return rooms.size();
    }

    public Room getRoom(int i){
        return rooms.get(i);
    }

}
