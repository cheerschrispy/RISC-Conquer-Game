package edu.duke.ece651.server;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {
    @Test
    void connectionTest() throws Exception {
        Client c1 = new Client("c");
        Client c2 = new Client("b");
        Client c3 = new Client("a");
        c1.start();
        TimeUnit.SECONDS.sleep(1);
        c2.start();
        TimeUnit.SECONDS.sleep(1);
        c3.start();
    }
}
