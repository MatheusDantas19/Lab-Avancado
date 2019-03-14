import java.io.InputStream;
import java.util.Scanner;
public class RecepcaoCliente implements Runnable {
    private InputStream servidor;
    public RecepcaoCliente(InputStream servidor) {
        this.servidor = servidor;
    }
    public void run() {
        Scanner leia = new Scanner(this.servidor);
        while (leia.hasNextLine()) {
            System.out.println(leia.nextLine());
        }
    }
}