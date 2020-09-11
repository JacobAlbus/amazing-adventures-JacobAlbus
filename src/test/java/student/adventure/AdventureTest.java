package student.adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.google.gson.Gson;
import java.io.Reader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;


public class AdventureTest {
    @Test
    public void setUp() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/student/adventure/Rooms.json"));

            GameBoard board = gson.fromJson(reader, GameBoard.class);
            reader.close();
        } catch (NullPointerException e) {
            System.out.println("Null value passed");
        } catch (IOException e) {
            System.out.println("ERORR: File not found!");
        }
    }


    @Test
    public void testPlayerUpdatePosition(){
        Player player = new Player("bob");

        player.updatePosition("east");
        int [] position = player.getPosition();
        int[] arr = {1, 0};

        for(int i = 0; i < arr.length; i++){
            assertEquals(arr[i], position[i]);
        }
    }

    @Test
    public void testUpdatePlayerRoom(){
        Player player = new Player("bob");

        player.updatePosition("east");
    }

}