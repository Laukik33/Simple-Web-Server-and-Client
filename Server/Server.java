/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



/*
Declaring the variables for socket communication
*/
public final class Server implements Runnable
{
    private ServerSocket socket_serv;                                           //Reference to the serverSocket where server will be started
    private int serv_port;                                                      //Reference to the port where server will be started
    private String serv_host;                                                   //Reference to the hostname of the server where the server will be started
    private final String Default_host = "localhost";                            //Default host where server will be started 
    private final int Default_port = 8080;                                      //Default port value for the server Socket
    
    /**
     *Default constructor if hostname and port number is not provided
     */
    public Server()
    {
        this.serv_host = Default_host;
        this.serv_port = Default_port;
    }
    
    /**
     *Parameterized constructor if a hostname and port number are passed
     * @param svrHost
     * @param port
     */
    public Server(String svrHost, int port)
    {
         this.serv_host = svrHost;
         this.serv_port = port;
    }
    
    /**
     *Parameterized constructor if a port number is passed via command line argument
     * @param port
     */
    public Server(int port)
    {
        this.serv_host = Default_host;
        this.serv_port = port;
    }
    
    /*
       In run method the server accepts connection from client and sends a HTTP request 
       to HttpRequestHandler to futher process. It also started a new thread for each new
       request.
    */
    @Override
    public void run(){
        try {
             
        
            InetAddress  serv_inet = InetAddress.getByName(serv_host);                        //gets inet address of the server
            System.out.println("<SERVER> IP address of the server: " + serv_inet);            // The ip_address at which server starts
                     
            socket_serv = new ServerSocket(serv_port, 0, serv_inet);                          //Initializing server Socket using serverInet and serverPort and timeout value for the server is set to infinite
           
            
            System.out.println("<SERVER> Server started at hostName : " + socket_serv.getInetAddress() + " port number : " +socket_serv.getLocalPort());
            
            
            int clientID = 0;                                                                //providing each client an identifier starting with zero
          
            //multithreading
            while(true){
                  
                     Socket cli_sock = socket_serv.accept();                                 //wait for a client to get connected
                     System.out.println("<SERVER - CLIENT" + clientID + "> Connection established with client with HostName: " + cli_sock.getInetAddress() + "  Port No. :" + cli_sock.getPort());      //new client has been connected to this server
                     System.out.println("<SERVER> Ping Command received from : "+cli_sock.getInetAddress() );    //Acknowledging ping command request from the client
                     HttpRequest request =  new HttpRequest(cli_sock, clientID);                                //Passing client socket and client ID to HttpRequest handler object
                     Thread thread = new Thread(request);                                        //Initialzing a new thread for newly connected client to RequestHandler in a separate thread
                     thread.start();                                                             //Starting the thread
                     clientID++;                                                                 //increment clientID for the next client
                 } 
                }  
        /*
        Handling exceptions inside a catch block
        */
        catch(UnknownHostException e){
            System.out.println("<SERVER> Host entered is not valid");
        }
        catch(SocketTimeoutException s){
             System.out.println("Socket Timed out!");
                }

                catch (IOException ex) {
                     Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                 }
        /*
        Closing Server Socket
        */
   finally {
           try {
              if(socket_serv!=null){
                 socket_serv.close();
} 
           }catch(IOException e){
         System.err.println("[SERVER]> EXCEPTION in closing the server socket." + e);
}
}
}
    /*
    main method executes the Server 
    */
    
    public static void main (String[] args) throws Exception
    {
       int port = 8080;   // initialize port with default port number
       
       if(args.length == 1)    // Checking if port number is provided through command line argument
       {
         try  {
            port = Integer.parseInt(args[0]);     //Checking if port is an integer
       }  
         catch(NumberFormatException nfe){
              System.out.println("<SERVER> Port Number is invalid. Server Stating at Default Port");       
         }
    }
        System.out.println("<SERVER> Server Starting on port "+ port);
        
         Server s = new Server(port);               //Constructing the server object
    
         new Thread(s).start();                     //start Server in a new thread
        
}
     

}
