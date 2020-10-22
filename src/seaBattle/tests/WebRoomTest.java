package seaBattle.tests;


import org.junit.jupiter.api.Test;
import seaBattle.rooms.WebRoom;

import java.util.ArrayList;

class WebRoomTest {
    @Test
    void getPrinted() {
        ArrayList<WebRoom> rooms = new ArrayList<>();
        rooms.add(new WebRoom("Dima", null));
        rooms.add(new WebRoom("Diasefma", null));
        rooms.add(new WebRoom("Disfsafdfma", null, "fejif"));
        rooms.add(new WebRoom("Diasdfasdfma", null, "fjieijf"));
        rooms.add(new WebRoom("afefasfsdDima", null));
        rooms.add(new WebRoom("sadfsafsdfasdfsdf", null, "jfieiff"));
        for (String string: WebRoom.getPrinted(rooms)) System.out.println(string);
    }

    @Test
    void checkPsw() {
        WebRoom room = new WebRoom("Name", null, "1234");
        System.out.println(room.tryConnect(null, "0000"));
        System.out.println(room.tryConnect(null, "1234"));
    }
}