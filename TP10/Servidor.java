import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
	private int porta;
    private List<PrintStream> client1;
	public static void main(String[] args) throws IOException {
        new Servidor(2040).run();
    }
    public Servidor (int porta) {
        this.porta = porta;
        this.client1 = new ArrayList<PrintStream>();
    }
    public void run () throws IOException {
        ServerSocket servidor = new ServerSocket(this.porta);
        System.out.println("PORTA 8080 ABERTA!");
        while (true) {
            Socket client = servidor.accept();
            System.out.println("CONEXAO CLIENTE" +     client.getInetAddress().getHostAddress());
            PrintStream print = new PrintStream(client.getOutputStream());
            this.client1.add(print);
            ClienteThread type =  new ClienteThread(client.getInputStream(), this);
            new Thread(type).start();
        }

    }
    public void Mensagem(String msg) {
       
        for (PrintStream client : this.client1) {
            client.println(msg);
        }
    }
}