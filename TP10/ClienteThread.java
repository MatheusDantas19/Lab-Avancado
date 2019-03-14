import java.io.InputStream;
import java.util.Scanner;
public class ClienteThread implements Runnable {
    private InputStream cliente;
    private Servidor servidor;
    public ClienteThread(InputStream cliente, Servidor servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }
    public void run() {
        Scanner leia = new Scanner(this.cliente);
        while (leia.hasNextLine()) {
            servidor.Mensagem(leia.nextLine());
        }
        leia.close();
    }
}