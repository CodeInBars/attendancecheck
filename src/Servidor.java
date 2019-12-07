
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {

	static final int PORT = 8080;


    public static void main(String[] args) throws IOException {

    	ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server levantado...");


        //Se bloquea hasta que sucede la conexi�n
        Socket socket = s.accept();
        try {
        	new ClientHandler(socket);
        } catch (IOException e) {
            // Si hay alg�n fallo, se cierra el socket.
            socket.close();
        }
        s.close();


    }
}
