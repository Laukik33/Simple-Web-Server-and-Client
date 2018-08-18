1. Simple web server and client
----------------------------------------------------
A java implementation of Simple HTTP, single threaded client and a Multithreaded server. The client tries to establish a socket connection 
with the server at the port on which the server is running. After successful connection, the client sends a HTTP GET request & ping request
to the server.The format of the GET request & header request is as follows:
                           GET filename.extension HTTP/1.0
                           Host: localhost
                           Connection: close
                           User-agent: Java
                           Accept-language: en
Once the server receives the request, it sends the request to HttpRequest class to process each clients request in a separate thread. The 
HttRequest class extracts the desired filename from the request and checks if the file is present on the server. If the file is present then 
the server sends 200 OK response to the client alongiwth content type and entity body of the file else the server sends 404 NOT FOUND response
to the client if the file is not found on the server. The format of the HTTP response is as follows:
                           HTTP/1.0 200 OK
                           Content type: text/html CRLF
                           Response body: .......(contents
                                          of the file)....
                                          ................
The Client also displays the Round Trip Time(RTT) required for each request. After the response is sent to the client, server closes the connection.


2. Development of Simple web server and client
-------------------------------------------------------
a) Java is the language used to develop the system.	
b) Socket programming is used for client server programming.
c) Server runs at port that is explicitly entered using command line argument or it starts at default port: 8080 if no port nuber is entered.
d) HTML is used for developing default file index.html which is present in server local file system.


3. Implementations
-------------------------------------------------------
1) The server works correctly with requests from a Web browser.
2) The server can serve multiple requests at the same time (multithreaded implementation).
3) The client sends/receives messages to/from the server correctly.
4) The client extracts the status and content of messages from the server correctly.
5) Extracting and displaying connection parameters.
6) Calculate and Display Round Trip Time.
7) Proper closing of the ports with exception handling. 
8) Display/log of proper messages on the server as well as on the client.
9) Code documentation and Readme file.


4. Software Requirements
-------------------------------------------------------
1) Programming language: Java(jdk 1.7)
2) OS: Windows 7 and above
3) Command line Interface: Windows command prompt to run/test the program
4) External packages: No external packages are required other than default Java packages like java.io, java.net and java.util.


5. Directory Structure
-------------------------------------------------------
1) Server: It contains source files for the server implementation along with a default index.html file.
   a) Server.java : Implements and initializes a multithreaded server which initializes a serverSocket to listens to the client requests. Once a client is connected, 
                    the processing is handed over to a separate HttpRequest thread.
   b) HttpRequest.java : Communicates with and processes a client's HTTP request in a separate thread.
   c) index.html : A default html file which is sent to the client in case a GET request contains "/" filepath.

2) Client: It contains source files for the client implementation
   a) Client.java :  Implements a single threaded web client which communicates with the server on a specific ip:port address and requests a file on the server.


6. Steps to compile and run (run on windows command prompt)
-------------------------------------------------------------
1) Open command prompt. 
2) Now set path using the set path command or editing the environment variables on the system. The path should be set to where the jdk folder is installed in 
   your system.
3) Change directories using "cd" command and appending folder's name. cd ServCli will change the directory to ServCli,
4) In ServCli directory, again use "cd" command to go to Server directory. In Server directory, compile all the java files  using  
   javac *.java . This will compile all the classes present in server directory. 
5) Run web server in server directory. If no port is passed, a default 8080 port is used. If port is already in use, a proper error message is displayed.
   
   Default port 8080 will be used if following command is run
         java Server 

   Given port will be used if following command is run
         java Server 3456

6) Now compile the client code using similar steps. 
7) Open a new command prompt. 
8) Set path to where th jdk folder is installedin our system.
9) Use "cd" commands to change directory to Client directory which is present in ServCli directory. 
10) In Client directory, compile the client code using  javac Client.java command.
11) Run client in Client directory by passing at least one argument i.e. serverHost/IP address. Other optional arguments are port and the path of 
    the file to request from the server. If server cannot be reached due to a network issue or incorrect port value, a proper error message is displayed.
    
    Default port 8080 and default file path "/" will be used if following command is run
           java Client localhost
 
    Given port and default file path "/" will be used if following command is run
           java Client localhost 3456

    Default port 8080 and given file path will be used if following command is run
           java Client localhost /filename

12) If the filename exists on the server , the server will return HTTP/1.0 200 OK response with content type and file content. The client will extract 
    the response and display it on the command prompt. 
13) If the filename does not exist on the server then the server will return HTTP/1.0 404 NOT FOUND response with content type and a general error html
    file. 
14) All requested file paths must be relative to the HttpRequest.java class. If not, 404 error will be returned.
