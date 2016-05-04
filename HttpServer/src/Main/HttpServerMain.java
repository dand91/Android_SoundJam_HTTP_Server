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
	
	public class HttpServerMain {

	    public static void main(String[] args) throws Exception {
	    	
	    	Enumeration e = NetworkInterface.getNetworkInterfaces();
	    	int k = 0;
	    	ArrayList<InetAddress> list = new ArrayList<InetAddress>();

	    	while(e.hasMoreElements()){
	    	    NetworkInterface n = (NetworkInterface) e.nextElement();
	    	    Enumeration ee = n.getInetAddresses();
	    	    
	    	    while (ee.hasMoreElements())
	    	    {
	    	        InetAddress i = (InetAddress) ee.nextElement();
	    	        list.add(i);
	    	        System.out.println(k + ": " + i.getHostAddress());
	    	        k++;
	    	    }
	    	}
	    	
	    	int nbr = k + 1;
	    	
	    	while(nbr >= k){
	    		
	    		try{
	    			
	    		System.out.println("Which address? Input number. Max: " + (k-1));
	    		Scanner desc1 = new Scanner(System.in);
	    		nbr = desc1.nextInt();

	    		}catch(Exception e1){
	    			
		    		System.out.println("Not a valid input.");
	    			
	    		}
	    	}
	    	
        	System.out.println("Initiating server");
	        HttpServer server = HttpServer.create(new InetSocketAddress(list.get(nbr),1234), 0);
	        HttpContext context = server.createContext("/test", new MusicAppHandler());
	        //context.getFilters().add(new ParameterFilter());
	        server.setExecutor(null);
	        server.start();
	        
	    }
	}

