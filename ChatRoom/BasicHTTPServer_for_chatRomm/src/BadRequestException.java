

public class BadRequestException extends Exception {
	 public BadRequestException () {
		super("400 Bad Request");
		}
	 public void print() {
		 System.out.println("400 Bad Request");
	 }
	}

