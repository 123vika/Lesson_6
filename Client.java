import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;



class alwaysListen implements Runnable {

    String msgFromServer;

    private Thread t;
    private String threadName;

   // DataOutputStream out;
    DataInputStream in;
    Socket socket;






    alwaysListen(String threadName ,Socket socket,DataInputStream in){

        this.in = in;
        this.socket = socket;
        this.threadName = threadName;

    }

    public void run() {



        while (true){
            if (!socket.isClosed()){

                try{
                    msgFromServer = in.readUTF();
                }
                catch(Exception e1){
                    System.out.println("problem with input.");
                }


                System.out.println(msgFromServer);
            }
            else {
                break;
            }
        }


        System.out.println("Thread " +  threadName + " exiting.");
    }
    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}









public class Client {
    public static void main(String[] args) throws IOException {

        System.out.println("before connected");
        Socket socket = new Socket("Localhost", 80);
        System.out.println(" Client connected! ");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());


        alwaysListen aL = new alwaysListen("always",socket,in);
        aL.start();

        Scanner console = new Scanner(System.in);
        while (true){
            if (!socket.isClosed()){
                String msg = console.nextLine();
                out.writeUTF(msg);
                if (msg.equals( " q ")){
                    break;
                }

               // String msgFromServer = in.readUTF();
               // System.out.println(msgFromServer);
            }
            else {
                break;
            }
        }
    }
}
