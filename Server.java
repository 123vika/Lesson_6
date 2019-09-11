import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

    class RunnableDemo implements Runnable {
        private Thread t;
        private String threadName;

        RunnableDemo( String name) {
            threadName = name;
            System.out.println("Creating " +  threadName );
        }
        public void run() {
            System.out.println("Running " +  threadName );
            System.out.println(" server ");

            try (ServerSocket srv = new ServerSocket(80)){
                Socket socket =  srv.accept();


                //System.out.println(" server1 ");

                if (socket !=null){
                    System.out.println(" Client + " + socket.getInetAddress()+ " connected ");
                }
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                ScanForServer sfc = new  ScanForServer("scan for server",socket, out);
                sfc.start();

                while (true) {
                    String messageFromClient = in.readUTF();
                    if (messageFromClient.equals(" q ")){
                        System.out.println(" Connection closed! ");
                        socket.close();
                        break;
                    }
                    out.writeUTF(" From server: " + messageFromClient);
                }
            }

            catch (IOException e){
                e.printStackTrace();
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

class ScanForServer implements Runnable {
    private Thread t;
    private String threadName;
    Socket socket;
    DataOutputStream out;
    ScanForServer( String name,Socket socket,DataOutputStream out ) {
        threadName  = name;
        this.socket = socket;
        this.out    = out;
        System.out.println("Creating " +  threadName );

    }
    public void run() {
        Scanner console = new Scanner(System.in);
        while (true){
            if (!socket.isClosed()){
                String msg = console.nextLine();
                try{
                    out.writeUTF(msg);
                    System.out.println("try send" + msg);
                } catch (Exception e1){
                    System.out.println("problem with output");
                }

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

public class Server {
    public static void main(String[] args) {
        RunnableDemo R1 = new RunnableDemo( "Thread-1");
        R1.start();

    }
}