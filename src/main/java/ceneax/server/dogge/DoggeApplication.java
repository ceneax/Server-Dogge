package ceneax.server.dogge;

import ceneax.server.dogge.util.HttpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Description: Springboot 入口
 * Author: ceneax
 * Website: ceneax.com
 * Date: 2021/2/28 19:30
 */
@SpringBootApplication
public class DoggeApplication {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(44445);
            while (true) {
                Socket socket = serverSocket.accept();
                new HttpServer(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SpringApplication.run(DoggeApplication.class, args);
    }

}