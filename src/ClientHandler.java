


// Un servidor que usa el multihilo
// para manejar cualquier número de clientes.
import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


/**
 * Servidor el cual usa multihilos para tratar con un n�mero de clientes.
 */
public class ClientHandler extends Thread {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private DataOutputStream salida;
    private static Random rnd = new Random();
    private static Date d = new Date();
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    private static ArrayList<String> hashes = new ArrayList<>();

    public ClientHandler(Socket s)throws IOException {

        socket = s;
        input= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        writer = new BufferedWriter(new FileWriter("registros.txt", true));
    	reader = new BufferedReader(new FileReader("registros.txt"));




        start();
    }

    @Override
    public void run() {
    	boolean flag = true;
        try {
            while (true) {



            	int num = rnd.nextInt(10);
            	createHashes(hashes, num);

            	int numero = rnd.nextInt(2);
            	String cad = "  " + hashes.get(0) + "  " + hashes.get(1) + "  " + hashes.get(2);
            	output.println("Posicion" +" "+numero + " " + cad);

            	String str = input.readLine();

                if (str.equals("END")) {

                	break;

                }else if(str.equals(hashes.get(numero))) {

                	System.out.println("Hash Correcto");

                	String er = reader.readLine();
                	while(er!=null){

                		String[] cadena = er.split("--");

						Date regist = format.parse(cadena[1]);
						Date today = new Date();

                		if(cadena[0].equalsIgnoreCase(socket.getInetAddress().toString())){
                			if(regist.getYear() >= today.getYear()){
                				if(regist.getMonth() >= today.getMonth()){
                					if(regist.getDay() > today.getDay()){

                					}else{
                						flag = false;
                					}
                				}else{
                					flag = false;
                				}

                			}else{
                				flag = false;
                			}
                		}else{
                			flag = false;
                		}
                		er = reader.readLine();

                	}
                	reader.close();
                	if(flag){
                		System.out.println("Fecha Registro: " + format.format(d.getTime()));
                		writer.write(socket.getInetAddress() + "--" +format.format(d.getTime()));
                    	writer.newLine();
                    	writer.flush();
                    	writer.close();
                	}else{
                		System.out.println("IP Repetida");
                	}

                	break;

                }else {

                	System.out.println("Hash incorrecto");
                	break;
                }

            }
            System.out.println("Cerrando...");
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
            System.err.println("I/O Exception");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket no cerrado");
            }
        }
    }

    public void createHashes(ArrayList<String> hash, int num) {

    	int numero = rnd.nextInt(50);
    	char[] letras = {'A','b','C','d','E','f','G','h','I','j','K','l','M','n','O','p','Q','r','S','t','U','v','W','x','Y','z'};


    	for(int x = 0; x<3; x++) {
    		String cad = "";

    		for(int i = 0; i<num; i++) {

        		int post = rnd.nextInt(letras.length);
        		cad += letras[post] + "" + post;
        	}

    		hash.add(cad);
    	}

    }
}
