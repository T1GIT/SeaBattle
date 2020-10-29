package seaBattle.tests;


import org.junit.jupiter.api.Test;
import seaBattle.rooms.types.WebRoom;

class RoomTest {
    @Test
    void checkPsw() {
        WebRoom room = new WebRoom(null, 2, WebRoom.security.hash("1234"));
        System.out.println(room.checkPsw(WebRoom.security.hash("0000")));
        System.out.println(room.checkPsw(WebRoom.security.hash("1234")));
    }
}