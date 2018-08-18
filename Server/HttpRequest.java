
import java.net.Socket;
import java.io.*;
import java.util.StringTokenizer;


final public class HttpRequest implements Runnable{
    /*
    Defining variables used for request processing
    */
    final static String CRLF = "\r\n";           //carriage return line feed
    private final String SP = " ";               //status line part separator
    private Socket cli_sock;                     //reference to the client's socket
    private int clientID;                       //unique identifier passed by the server to identify client

    /**
     *
     * @param cli_sock
     */

    /**
     *Parameterized constructor to set client Socket and client ID
     * @param cs
     * @param cID
     */
    public HttpRequest(Socket cs, int cID) {
        this.cli_sock = cs;
        this.clientID = cID;    
    }
    
    
    /*
    calls the processRequest method
    */
    @Override
    public void run() {
        
        try {
            processRequest();                   //calls the processrequest method
        } catch(IOException e){
            System.out.println(e);
        }
    }
    
    /*
    processRequest method handles the processing of each client's request 
    */
    private void processRequest()  throws IOException
    {
        InputStreamReader is = null;           //reads data received over socket's input stream
        DataOutputStream os = null;            //writes data to socket's output stream
        FileInputStream fis = null;            //reads file from local file system
       try{
        is = new InputStreamReader(cli_sock.getInputStream());   //gets a reference to CLientsocket's inputStream
        os = new DataOutputStream(cli_sock.getOutputStream());   //gets a eference to Clientsocket's outputstream
     
        BufferedReader br = new BufferedReader(is);                //creating a bufferedreader object to buffer the input received
     
        String requestLine = br.readLine();                       //reads the request from socket's inputstream
     
        System.out.println("");
         
        System.out.println("<SERVER> Received request:"+requestLine+CRLF);    //Displaying the GET request received from the client
        
        String headerLine = null;                                             //Get and display the header lines 
        while((headerLine = br.readLine()).length() != 0) 
        {
              System.out.println(headerLine + CRLF);
        }
        
        //Extract the filename from the request line 
        StringTokenizer tokens = new StringTokenizer(requestLine);            
        tokens.nextToken();
        String fileName = tokens.nextToken();                                 //get filename from the request
        
        if(fileName.indexOf("/")!=0)                                         //check if fileName starts with a forward slash
        {                                                                   //if not add a forward slash and make it relative to the current file Name
            fileName = "/" + fileName;
        }
        
        System.out.println("<SERVER - CLIENT" + clientID+"> Requested File Name: "+ fileName); 
        
        if(fileName.equals("/"))                                            //If requested file name is null or requesting  a default index file
        {
            System.out.println("<SERVER - CLIENT" + clientID + "> Respond with default /index.html file");
            
            fileName = fileName + "index.html";                              //set filename to the default index file
            
        }
        
        //prepend a "." so that file name is within the current directory
        
        fileName = "." + fileName;
        
        //Now contruct the response message
             
             String statusLine;
             String contentTypeLine;
             String entityBody;
             
             File file = new File(fileName);                                    //initialize a file object using filename
            try{ 
             //check if file with filename exists on the server
             if(file.isFile() && file.exists()){
                 statusLine = "HTTP/1.0" + SP + "200" +SP + "OK" + CRLF;             //we will send a 200 OK response as requested file exists on server
                 contentTypeLine = "Content-type:" + contentType(fileName) + CRLF;   //write content type header line
                  
                 os.writeBytes(statusLine);                                         //send the status line
                    
                 os.writeBytes(contentTypeLine);                                    //send the content type line
                 
                 os.writeBytes(CRLF);                                              //send a blank line to indicate  the end of the header lines
                 
                 fis =new FileInputStream(file);                                   //open the requested file
                 
                 sendBytes(fis, os);                                               //send the entity body
                 
                 fis.close();                                                      //close file inputstream
                 
                 System.out.println("<SERVER - CLIENT"+clientID+"> Sending Response with status line: " + statusLine);
             
                 System.out.println("<SERVER - CLIENT"+clientID+"> HTTP Response sent");
             }
             else { 
                 statusLine = "HTTP/1.0" + SP + "404" + SP + "Not Found" + CRLF;       //if file does not exists on the server then we will send 404 Not found response
                 contentTypeLine = "Content-type:" + contentType(fileName) + CRLF;     //send the content type of the requested file
                 entityBody = "<HTML>" + "\n" +                                      //send the entitybody in the form of html as 404 error file
                              "<HEAD>" + "\n" +
                                   "<TITLE> Error 404 </TITLE>" + "\n" +
                              "</HEAD>" + "\n" + 
                              "<BODY> 404. The Requested file does not exists </BODY>" + "\n" +
                              "</HTML>";
                 
                 os.writeBytes(statusLine);                                            //sends the status line
                 
                 os.writeBytes(contentTypeLine);                                       //send the content type line
                 
                 os.writeBytes(CRLF);                                                 //send a blank line to indicate  the end of the header lines           
                 
                 os.writeBytes(entityBody);                                           //send the entity body
                 
                 System.out.println("<SERVER - CLIENT"+clientID+"> Sending Response with status line: " + statusLine);
             
                 System.out.println("<SERVER - CLIENT"+clientID+"> HTTP Response sent");
             }
            
        } catch(FileNotFoundException e){
            
            System.err.println("<SERVER - CLIENT" + clientID + "> EXCEPTION: Requested file " + fileName + "does not exist");   //Handling the exceptions
             
        } catch(Exception e){
            
             System.err.println("<SERVER - CLIENT" + clientID +"> EXCEPTION in processing request " + e.getMessage());
             
        } 
        }catch(IOException e){
            System.err.println("[SERVER - CLIENT"+clientID+"]> EXCEPTION in processing request." + e.getMessage());
        }
        finally {
            /*
            Closing strems and sockets
            */
            try {
                 if (fis!= null) {
                      fis.close();
                 }
                 if (is!= null) {
                      is.close();
                 }
                 if (os!= null){
                     os.close();
                 }
                 if (cli_sock!= null){
                     cli_sock.close();
                     System.out.println("<SERVER - CLIENT"+clientID+"> Closing the connection.\n");
                 }
            } catch(IOException e   ) {
                 System.err.println("<SERVER - CLIENT" + clientID + "> EXCEPTION in closing resources" + e);
            }
        }
    }
        
    
        /*
        method sendBytes is used for sending the file to the client
        */
        private static void sendBytes(FileInputStream fis, OutputStream os) 
                throws Exception
        {
            //Construct a 1K buffer to hold bytes on their way to the socket
            byte[] buffer = new byte[1024];
            int bytes = 0;
            
            //Copy requested file into the socket's output stream
            while((bytes = fis.read(buffer))!= -1) { 
                os.write(buffer, 0 , bytes);
            }
           
        }
        
        /*
        Get Content-type of the file using its extension
        */
        private static String contentType(String fileName)
        {
            //check if file type is html 
            if(fileName.endsWith(".htm") || fileName.endsWith(".html")){
                return "text/html";
            }
            
            //otherwise return a binary file
            return "application/octet-stream";
        }
    
}
