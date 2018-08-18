

import java.io.*;
import java.net.*;
import java.util.GregorianCalendar;

//Class Client represents single threaded web client

public class Client {

    //main method executes the client

    public static void main(String args[]){
        int port_no = 8080;         // Default port value that should be used
        String server_ipaddr = null; //intialize server IP address 
        String requested_file_name = "/";  // initialize requested file name with default value
        final String carr_ret = "\r\n";     // declaring carriage return line feed
        final String line_separator = " ";  // status line part separator
   
        if(args.length == 1)
        {
            server_ipaddr = args[0];                  //Examine command line arguments for Server IP address
          
        }
            
        else if (args.length == 2){
            server_ipaddr = args[0];                  //First argument is server host
            try{
            port_no = Integer.parseInt(args[1]);      //Second argument is port number
        }
            catch(NumberFormatException nfe){
                System.err.println("<CLIENT> Port number is not valid. Using the default port number");
            
            
            requested_file_name = args[1];             //Requested file name will be the third argument
            }
      }
        else if (args.length == 3){
            server_ipaddr = args[0];                   //First argument is server host
            try{
                port_no = Integer.parseInt(args[1]);   //check if port number is integer
            } 
            catch(NumberFormatException nfe)
            {
              System.err.println("<CLIENT> Port number is not valid. Using the default port number");  
            }
            
            requested_file_name = args[2];              //third argument is file name
        }
        else 
        {
            System.err.println("<CLIENT> Please provide parameters. Server IP address is required");
            System.exit(-1);
            
        }
        
        System.out.println("<CLIENT> Using Server Port: " + port_no);
	System.out.println("<CLIENT> Using FilePath: " + requested_file_name);
	System.out.println("<CLIENT> Requested File: " + requested_file_name);
        
        Socket socket = null;                                   // defining a socket
        
        BufferedReader bis = null;                              //reads data from the sockt's inputstream
        DataOutputStream dos = null;                            //writes data   to the socket's outputstream
       /*
        Socket connection is done in the try block
        */
        
        try{
            InetAddress serverInet = InetAddress.getByName(server_ipaddr);                //gets the peername of the remotehost    
            System.out.println("<CLIENT> Server's Hostname :" + serverInet);                //prints the peername of the remotehost
            System.out.println("<CLIENT> Sending Ping Request to "+ server_ipaddr);       //to send a ping request to the server
            
            long finish = 0;                                                              
            long start = new GregorianCalendar().getTimeInMillis();                       //gets system current time in Milliseconds
            socket = new Socket(serverInet,port_no);     //Connecting to the server 
             
            //get the core protocol used for network communication 
            SocketAddress socketAddress = socket.getRemoteSocketAddress();
              
             
            if (socketAddress instanceof InetSocketAddress) {
            InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
                 if (inetAddress instanceof Inet4Address)
                   System.out.println("<CLIENT> IPv4: " + inetAddress);
                 else if (inetAddress instanceof Inet6Address)
                   System.out.println("<CLIENT> IPv6: " + inetAddress);
            else
             System.err.println("<CLIENT> Not an IP address.");
              } else {
                 System.err.println("<CLIENT> Not an internet protocol socket.");
               }
           
            System.out.println("<CLIENT> Connected to the server at hostname : " + socket.getInetAddress() + " Port no. :" + socket.getPort() + " from port :" + socket.getLocalPort() + " of :" + socket.getLocalAddress());  //displays the host name of the server and the number to which client is connected
            dos = new DataOutputStream(socket.getOutputStream());                                       //gets a reference to the socket's  outputstream
            //Sends Http GET Request and the header lines
            String getRequest = "GET" + line_separator + requested_file_name + line_separator + "HTTP/1.0" + carr_ret;
            String headerLine = "Host: " + server_ipaddr + carr_ret + "Connection: close" + carr_ret + "User-agent: Java"+ carr_ret + "Accept-language: en" + carr_ret;
          
            System.out.println("<CLIENT> HTTP GET request: " + getRequest + headerLine);
          
          
           dos.writeBytes(getRequest);                                                       //sends GET Request
           dos.writeBytes(headerLine);                                                       //sends Header lines
           dos.writeBytes(carr_ret);                                                         //sends an empty line
          
           dos.flush();                                                                      //flush out outputstream
          
           /*
           Now we extract response from the server 
           */
            System.out.println("<CLIENT> Waiting for a response from the server");          
            bis = new BufferedReader(new InputStreamReader(socket.getInputStream()));         //initializing a buffered reader to read server's response   
            String response = bis.readLine();                                                 //reads the response from server and stores it in response
            
            System.out.println("<CLIENT> HTTP Response received " + response);
            
            String Content_typ = bis.readLine();                                              //retrieving  coontent type of the response
            System.out.println("<CLIENT> "+ Content_typ);
            
            System.out.println("<CLIENT> Response Body:");                                      //Retrieving content body
            
            //Begin reading content body
            StringBuilder cont = new StringBuilder();
            String result;
            while((result = bis.readLine())!=null)
            {
                cont.append(result).append("\n");                                              //The content body is stored in ariable called and result and then displayed
                
                System.out.println(result);
            }
            
         /*
            We check the round trip time required for the client's request 
            */   
            
       if(serverInet.isReachable(5000)){
           finish = new GregorianCalendar().getTimeInMillis();
           System.out.println("<CLIENT> Ping RTT: " + (finish - start + "ms"));    
        }else{
           System.out.println(server_ipaddr + "Not Reachable");
            }
                 
      }
        catch(IOException e){
            System.err.println("<CLIENT> EXCEPTION in connecting to the SERVER: " + e.getMessage());
        }
        finally{
             try{
                 //close all resources
                 if(bis != null){                                               //check if inputStreamReader is null or not & close it
                     bis.close();
                 }
                 if(dos!=null){                                                 //check if dataoutputStreamm is null or not & close it
                     dos.close();
                 }
                 if (socket!= null){                                            //closing the socket connection
                      socket.close();
                      System.out.println("<CLIENT> Connection Closed");
                 }
             }catch(Exception e){
                 System.err.println("Exception in closing resource." + e); 
             }
        }
    }
}
   
    