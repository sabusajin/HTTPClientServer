import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class HttpClient {

    public static void main(String[] args) throws Exception{
        // TODO Auto-generated method stub
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String command = args[2];
        String fileName = args[3];
        int fileSize = 6400000;
        String workingDirectory = System.getProperty("user.dir") + "\\" + fileName;
        
        try 
        {
            Socket socket = new Socket(host,port);
            if (socket!=null)
            {
                System.out.println("\n" + "Connected to Server with port " + port);
            }
            
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            
            if (command.equals("GET"))
            {
                out.writeObject(command + " " + fileName + " HTTP/1.1");
                out.writeObject("Host:" + host);
                out.flush();
                
                String response = (String) in.readObject();
                System.out.println("HTTP Status: " + response + "\n");
                
                if (response.equals("200 OK"))
                {
                    System.out.println("The contents of the requested file is given below \n \n");
                    
                    try 
                    {
                        String output = "";
                        while (output!=null)
                        {
                            output = (String) in.readObject();
                            System.out.println(output);
                            
                        }
                        
                    }
                    catch (EOFException e)
                    {
                        System.out.println("End of File Reached\n Contents read successfully! ");
                    }

                }
                else if (response.equals("404 Not Found"))
                {
                    System.out.println("404 NOT FOUND \n Sorry! Requested File " + fileName + "not found in the Server");
                }
                
                
            }
            else if (command.equals("PUT"))
            {
                File file = new File(workingDirectory);
                if (!(file.isDirectory()) && (file.exists()))
                {
                    out.writeObject(command + " " + fileName + " HTTP/1.1");
                    System.out.println("The file is being sent");
                    out.writeObject(file.getName());
                    
                    
                    FileInputStream f = new FileInputStream(file);
                    byte [] buffer = new byte[fileSize];
                    int bytes = f.read(buffer);
                    
                    try
                    {
                        if (bytes > 0)
                        {

                            out.writeObject(bytes);
                            out.writeObject(Arrays.copyOf(buffer, buffer.length));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    
                    System.out.println(("File successfully sent!"));
                    
                    try{

                        String response = "";

                        while (response != null)
                        {
                        System.out.println("Response from server: \n" + response + "\n");
                        response = (String) in.readObject();
                        }

                    }
                    catch (EOFException ex1) {

                    System.out.println(); 

                }
                    
                }
                else
                {
                    System.out.println("File not found!");
                    out.writeObject("File not found");
                }
                
                    
           }
            else 
            {
                System.out.println("Server not found - Check your address");
                out.writeObject("Server not found");
                
            }
            out.close();
            in.close();
            socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        

    }

}
