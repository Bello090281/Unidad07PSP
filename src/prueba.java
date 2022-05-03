
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author Bello
 */
public class prueba {

    private static FileInputStream fe = null; //fichero de entrada
    private static FileOutputStream fs = null; //fichero de salida

    public static void cifrarFichero(String file, SecretKey clave) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        int bytesLeidos;
// Encrypt cipher
        Cipher cifrador = Cipher.getInstance("Rijndael");
        cifrador.init(Cipher.ENCRYPT_MODE, clave);

        byte[] buffer = new byte[1000]; //array de bytes
        byte[] bufferCifrado;

        fe = new FileInputStream(file); //objeto fichero de entrada
        fs = new FileOutputStream(file + ".cifrado"); //fichero de salida

        bytesLeidos = fe.read(buffer, 0, 1000);
        while (bytesLeidos != -1) {//mientras no se llegue al final del fichero
//Realizamos la escritura cifrado en el fichero de salida
            bufferCifrado = cifrador.update(buffer, 0, bytesLeidos);
            fs.write(bufferCifrado);
            bytesLeidos = fe.read(buffer, 0, 1000);
        }//while (bytesLeidos != -1)
        bufferCifrado = cifrador.doFinal(); //Completa el cifrado
        fs.write(bufferCifrado); //Graba el final del texto cifrado, si lo hay
        System.out.println("Cadena cifrada correctamente.");//Mostramos mensaje advertencia.

//Cerrar los recursos utilizados
        fe.close();
        fs.close();
    }

    private static void descifrarFichero(String file1, SecretKey clave, String file2) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        int bytesLeidos;

//Creamos una instancia para realizar el descifrado
        Cipher cifrador = Cipher.getInstance("Rijndael");
        cifrador.init(Cipher.DECRYPT_MODE, clave);

//Fichero de entrada será el archivo cifrado y la salida será en texto plano
        fe = new FileInputStream(file1);
        fs = new FileOutputStream(file2);
        byte[] bufferClaro;
        byte[] buffer = new byte[1000]; //array de bytes
        bytesLeidos = fe.read(buffer, 0, 1000);
        while (bytesLeidos != -1) {//mientras no se llegue al final del fichero.
//pasa texto cifrado al cifrador y lo descifra, asignándolo a bufferClaro.
            bufferClaro = cifrador.update(buffer, 0, bytesLeidos);
            fs.write(bufferClaro); //Graba el texto claro en fichero.
            bytesLeidos = fe.read(buffer, 0, 1000);
        }//while (bytesLeidos != -1).
        bufferClaro = cifrador.doFinal(); //Completa el descifrado.
        fs.write(bufferClaro); //Graba el final del texto claro, si lo hay.

//Cerramos los recursos utilizados.
        fe.close();
        fs.close();
    }//

    public static void muestraContenido(String archivo) throws FileNotFoundException, IOException {
        String cadena;
//Recibimos le fichero a leer.
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        System.out.println("La cadena que disponemos en el fichero es: ");
//Bucle wile para mostrar la cadena.
        while ((cadena = b.readLine()) != null) {
            System.out.println(cadena);
        }//while (cadena =null).
        b.close();
    }

    //Con este método lo que podemos realizar es la creacion de una cadena aleatoria con la longitud que deseamos. Para insertarla en el fichero "fichero".
    //En mi ejemplo la cadena la introduzco manualmente. 
    /*
  public static String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int posicion = (int) (Math.random() * 27 + 1);
            char caracterAleatorio = banco.charAt(posicion);
            cadena += caracterAleatorio;
        }
        return cadena;
    }*/
   
    public static void main(String[] Args) {
//Recursos que utilizaremos
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String usuario, password;

//Realizamos la lectura del usuario y password y realizamos la encriptación y desencriptación
        try {
            System.out.println("Introduce el usuario: ");
            usuario = reader.readLine();
            System.out.println("Introduce el password: ");
            password = reader.readLine();

//Generamos la clave a partir del usuario y el password
            KeyGenerator key_gen = KeyGenerator.getInstance("Rijndael");
            SecureRandom secure = new SecureRandom();
            StringBuilder cadenaClave = new StringBuilder();

// La clave a generar se calcula en base al usuario y password introducido, con una longitud de 128 bits.
            cadenaClave.append(usuario).append(password);
            secure.setSeed(cadenaClave.toString().getBytes());
            key_gen.init(128, secure);//Longitud de clave

// Generamos la clave.
            SecretKey key = key_gen.generateKey();

//Cifrado de fichero en base a la clave creada
            cifrarFichero("fichero", key);

//Descifrado del fichero en base a la clave creada.
            descifrarFichero("fichero.cifrado", key, "fichero.descifrado");
//Mostramos los datos del fichero que pasamos por parametro.            
            muestraContenido("fichero.descifrado");
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }//Método main
}//Clase 
