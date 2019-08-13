
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Request {

	String requestfile;
	String encoded;
	String key;

	Request(Socket connection) throws IOException, BadRequestException, NoSuchAlgorithmException {

		Scanner input = new Scanner(connection.getInputStream());
		String filename = new String(input.nextLine());
		String[] parts = filename.split(" ");
		if (parts.length != 3) {
			throw new BadRequestException();
		}
		requestfile = parts[1]; // this string will be /
		if (requestfile.equals("/")) {
			requestfile = "/LabFirstHtmlPage.html";
		}

		encoded = "non";
		key = "non";
		while (true) { // if try hasNextLine(), it is infinite loop as long as the socket is open

			// System.out.println("hello");

			String line = new String(input.nextLine());
			if (line.equals("")) {
				break;
			}
			String[] parts2 = line.split(" ");

			if (parts2[0].contains("Sec-WebSocket-Key:")) { // if use == instead of contains, wont work
				// web socket, handshake.
				key = parts2[1] + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
				// System.out.println(key);
			}

		}

		if (!key.equals("non")) {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] messageDigest = md.digest(key.getBytes());
			encoded = Base64.getEncoder().encodeToString(messageDigest);
			// System.out.println(encoded);
		}

	}

}
