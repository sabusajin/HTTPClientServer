Please follow the below instructions for running this code-


1. The folder structure should be the same as the one 
   you get while unzipping the Project. That is, server class 
   files should be placed in "Server" folder, and client 
   class files should be placed in "Project" folder.

2. Running the server - 
    
    java HttpServer portno

    eg - java HttpServer 8080

3. Running the client - 
    
    java HttpClient hostname port command filename

    eg - java HttpClient 127.0.0.1 8080 GET abc.txt
         java HttpClient 127.0.0.1 8080 PUT xyz.txt
    
    Note - Please use only the filename, and not the full path.
    Client files should be placed in "Project" folder, 
    and server files should be placed in "Server" folder.

 4. Exiting the server - 
    
    If you want to exit the server during execution, type exit
    in server console.



