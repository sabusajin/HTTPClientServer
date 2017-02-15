import java.net.*;
import java.io.*;

public class HttpServer extends Thread{
    private int port;
    private ServerSocket serverSocket;
    public int bufferSize = 20000;

    public static void main(String[] args) {
        // TODO Auto-generated command stub
        
        int[] ports = new int [20];
        
        for (int i = 0; i < args.length; i++ )
        {
            ports [i] = Integer.parseInt(args[i]);
        }
        try
        {       
            HttpServer Thread;
            for (int i=0; i < args.length; i++)
            {       Thread = new HttpServer (ports[i]);
                Thread.start();
            }
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    HttpServer (int inpPort) throws IOException
    {
        port = inpPort;
        System.out.println( "Server is being started on port: " + port + "\n");
        serverSocket = new ServerSocket( port);
        serverSocket.setSoTimeout(180000);
    }
    
    
    public void run()
    {
        while( true )
        {
            try
            {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Connected to " + server.getRemoteSocketAddress() + " through the port " + serverSocket.getLocalPort() +"\n");
                ObjectInputStream in= new ObjectInputStream(server.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
                
                
                
                String cmd = (String) in.readObject();
                String[] Command = cmd.split(" ");
                String command = Command[0];
                String fileName = Command[1];
                String httpVersion = Command[2];
                
                String workingDirectory = System.getProperty("user.dir");
                
                //System.out.println(workingDirectory);
                
                
                if ( command.equals("GET"))
                {
                    
                    File file = new File(workingDirectory, fileName);
                    if ( !(file.isDirectory()) && (file.exists()) )
                    {
                        out.writeObject("200 OK");
                        System.out.println("Requested object is " + file.getName());
                        BufferedReader buffer = new BufferedReader(new FileReader(file));
                        String line = null;
                        while ((line = buffer.readLine())!= null)
                        {
                            out.writeObject(line);
                        }

                        System.out.println("file contents of " + file.getName() + " sent \n");
                        buffer.close();
                    }
                    else
                    {
                        out.writeObject("404 Not Found");
                        out.writeObject("File " + file.getName() + " doesn't exist in the server");
                        System.out.println("File " + file.getName() + " doesn't exist\n");
                    }

                }
                else if ( command.equals("PUT"))
                {
                    //System.out.println(workingDirectory + "\\"+ fileName);
                    
                    
                    
                    try
                    {
                        byte [] buffer = new byte[bufferSize];
                        Object object = in.readObject();
                      
                        FileOutputStream fos = null;
                        if (object instanceof String)
                        {
                            fos = new FileOutputStream(workingDirectory + "\\"+ fileName);
                        }
                        else
                        {
                            throwException("Error!");
                        }

                        Integer bytesRead = 0;
                        do
                        {
                            object = in.readObject();


                            if (!(object instanceof Integer))
                            {
                                throwException("Error!");
                            }

                            bytesRead = (Integer)object;
                            object = in.readObject();
                            if (!(object instanceof byte[]))
                            {
                                throwException("Error!");
                            }

                            buffer = (byte[])object;
                            fos.write(buffer, 0, bytesRead);
                        } while (bytesRead == bufferSize);

                        out.writeObject("HTTP/1.1 200 OK File Created");


                        fos.close();
                    }

                    catch(Exception e)
                    {
                        System.out.println("\n");
                    }
                }

                else
                {
                    System.out.println("Invalid command Passed\n");
                }

                in.close();
                out.close();
                server.close();

            }

            catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out");
                break;
            }

            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }

            catch(Exception e)
            {
                e.printStackTrace();
                break;
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void throwException(String message) throws Exception
    {
        throw new Exception(message);
    }
        
        
        
        
        
        

    

}
