package mainApp;

import constants.RequestCode;
import fileHandler.AvailablePieceHandler;
import fileHandler.FileSender;
import netscape.javascript.JSObject;
import request.AvailablePieceRequest;
import request.Request;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

public class HandleClientRequest implements Runnable {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    public HandleClientRequest(Socket socket) {
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    Request request = null;
    @Override
    public void run() {
        Request request = null;
        while(true)
        {
            try
            {
                try
                {
                    Object object=ois.readObject();
                    System.out.println(object.getClass());
                    request = (Request)object;
                }
                catch (EOFException e)
                {
                    System.out.println("Client Disconnected..!!");
                    return;
                }
                catch (SocketException e)
                {
                    System.out.println("Client Disconnected..!!");
                    return;
                }
                if(request.getRequestCode().equals(RequestCode.AVAILABLEPIECE_REQUEST))
                {
                    request = (AvailablePieceRequest)request;
                    AvailablePieceHandler availablePieceHandler=new AvailablePieceHandler((AvailablePieceRequest) request);
                    oos.writeObject(availablePieceHandler.getResponse());
                    oos.flush();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();;
            }
        }
    }
}
