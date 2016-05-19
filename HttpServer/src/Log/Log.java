package Log;

import java.util.Date;

/**
 * Created by Andersson on 18/05/16.
 */

public class Log {

    private static Date date;

    public Log(){

        date = new Date();
    }

    public static void i(String TAG , String message){

        System.out.println(date.toString() + ":Information: " + TAG + ": " + message);

    }
    public static void e(String TAG , String message){

        System.err.println(date.toString() + ":Error: " + TAG + ": " + message);

    }
    public static void d(String TAG , String message){

        System.out.println(date.toString() + ":Debug: " + TAG + ": " + message);

    }
    public static void v(String TAG , String message){

        System.out.println(date.toString() + ":Verbose: " + TAG + ": " + message);

    }
    public static void W(String TAG , String message){

        System.err.println(date.toString() + ":Warning: " + TAG + ": " + message);

    }
}
