import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class Cliente {
    private String host;
    private int porta;
    public static void main(String[] args) throws UnknownHostException, IOException {
        new Cliente("127.2.1.1", 2020).run();
    }
    public Cliente (String host, int porta) {
        this.host = host;
        this.porta = porta;
    }
    public void run() throws UnknownHostException, IOException {
        Socket client = new Socket(this.host, this.porta);
        System.out.println("O CLIENTE ENTROU NO SERVIDOR!!!");
        RecepcaoCliente rece = new RecepcaoCliente(client.getInputStream());
        new Thread(rece).start();
        Scanner leia = new Scanner(System.in);
        PrintStream out = new PrintStream(client.getOutputStream());
        while (leia.hasNextLine()) {
            out.println(leia.nextLine());
        }
        leia.close();
        client.close();        
    }
}