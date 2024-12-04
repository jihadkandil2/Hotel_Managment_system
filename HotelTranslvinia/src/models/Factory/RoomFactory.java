package models.Factory;

import models.ProxyFiles.ProxyRoomDatabase;
import models.ProxyFiles.RoomDataBaseService;
import models.Room;

import java.util.List;

public class RoomFactory {

    public static Room CreateRoomType(String roomType)
    {
        switch (roomType.toLowerCase()) {
            case "single":
                return new SingleRoom();
            case "double":
                return new DoubleRoom();
            case "triple":
                return new TripleRoom();
            default:
                throw new IllegalArgumentException("Invalid room type: " + roomType);
        }
    }
<<<<<<< HEAD
=======



>>>>>>> e7b41273bd99bb0d7767bad6d51a9eaff9a283d6
}
