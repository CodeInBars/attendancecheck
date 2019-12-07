

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Un simple cliente que env�a lineas de texto al servidor y lee las respuesta de �l.
 */
class ClientThread extends Thread {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private static int counter = 0;
    private int id = counter++;
    private DataOutputStream envio;
    private static int threadsCount = 0;
    private static int PORT = 8080;
    private static Scanner scan = new Scanner(System.in);

    public static int getThreadsCount() {
        return threadsCount;
    }

    public ClientThread(InetAddress addr) {
        System.out.println("Creando el cliente " + id);
        threadsCount++;
        try {
            socket = new Socket(addr, ClientThread.PORT);
        } catch (IOException e) {
            System.err.println("El socket ha fallado");
//Si la creaci�n del socket falla, no hay que limpiar nada.

        }
        try {
        	envio = new DataOutputStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            start();
        } catch (IOException e) {
//Si ocurre cualquier error, el socket debe ser cerrado.
            try {
                socket.close();
            } catch (IOException e2) {
                System.err.println("Socket not closed");
            }
        }
    }

    public void run() {
        try {

            String hashes = input.readLine();
            System.out.println(hashes);

            output.println(scan.nextLine());

        } catch (IOException e) {
            System.err.println("I/O Exception");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket no cerrado");
                threadsCount--;
            }
        }
    }
}
