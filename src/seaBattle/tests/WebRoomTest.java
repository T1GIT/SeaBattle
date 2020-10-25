package seaBattle.tests;


import org.junit.jupiter.api.Test;
import seaBattle.rooms.WebRoom;

class WebRoomTest {
    @Test
    void checkPsw() {
        WebRoom room = new WebRoom("Name", null, WebRoom.security.hash("1234"));
        System.out.println(room.tryConnect(null, WebRoom.security.hash("0000")));
        System.out.println(room.tryConnect(null, WebRoom.security.hash("1234")));
    }
}