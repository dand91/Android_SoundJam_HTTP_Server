package Main;

import Handler.MusicAppHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class HttpServerMain {

    public static void main(String[] args) throws Exception {

        Enumeration InterFaceList = NetworkInterface.getNetworkInterfaces();
        int k = 0;
        ArrayList<InetAddress> list = new ArrayList<InetAddress>();

        while (InterFaceList.hasMoreElements()) {

            NetworkInterface n = (NetworkInterface) InterFaceList.nextElement();
            Enumeration addressList = n.getInetAddresses();

            while (addressList.hasMoreElements()) {
                InetAddress i = (InetAddress) addressList.nextElement();
                list.add(i);
                System.out.println(">" + k + ": " + i.getHostAddress());
                k++;
            }
        }

        int nbr = k + 1;
        int port = 0;

        while (nbr >= k) {

            try {

                System.out.println(">Which address? Input number.");
                Scanner desc = new Scanner(System.in);
                nbr = desc.nextInt();
                System.out.println(">Which port? Input number.");
                desc = new Scanner(System.in);
                port = desc.nextInt();

            } catch (Exception e1) {

                System.out.println(">Not a valid input.");

            }
        }

        System.out.println(">Initiating server");

        HttpServer server = HttpServer.create(new InetSocketAddress(list.get(nbr), port), 0);
        HttpContext context = server.createContext("/test", new MusicAppHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

    }
}

