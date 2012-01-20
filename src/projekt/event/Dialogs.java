package projekt.event;

/**
 *
 * @author johannes 
 */
public final class Dialogs {

	public static final int MAXMESSAGELENGTH = 100;

	public static final class Begin {

		public static final String story1 = "message. \" 1893 the gods realized that they lost contact withe the humans. The gods have to save the humans from an eternal life in horror and evil. The gods decides to act and picks a random dude that will do their mission and experience an awful fate.\"\t"
				+ "\"Ranglon, the chosen one, begins an epic journey around the world to spread love and defeat evil. At the evils nest, where the most horrible creature lives -  Fizerlang, our hero dies. The Gods panic and choose a new messenger..\"\t"
				+ "\"Dud!\"";
		public static final String sayHello = "\thej! jag \nheter johannes\n och detta ar ett test pa en lite langre strang, denna text ligger i en statisk klass: Dialogs.Begin.sayHello...\tNytt medelande som ocksa kan vara intressant att lasa :=)";
		public static final String TALK ="\tHej jag ar en Player som du kan prata med!";
	}
	public static boolean endof = true;
	public static String[] message;

	public static void initDialog(String mes) {
		//if(Dialogs.message.length >MAXMESSAGELENGTH)
		// TODO fix splitting of the message
		if (endof) {
			System.out.println("\nDialog:");
			Keys.status = "Dialog";
			Dialogs.message = mes.split("\t");
			Dialogs.endof = false;
		}
	}
	private static int ordning = 0;

	public static int getIndex() {
		return ordning;
	}

	public static String nextMessage() {
		String m = "";
		if (Dialogs.message != null && ordning < Dialogs.message.length) {
			Dialogs.ordning++;
			int cur = Dialogs.ordning - 1;
			System.out.println(Dialogs.message[cur]);
			if (Dialogs.ordning >= Dialogs.message.length) {
				Dialogs.endof = true;
				Dialogs.ordning = 0;
				Dialogs.message = null;
				if (Keys.status.equals("Dialog")) {
					Keys.status = "";
				}
				return "";
			}
			return Dialogs.message[cur];
		}
		Dialogs.message = null;
		Dialogs.endof = true;
		if (Keys.status.equals("Dialog")) {
			Keys.status = "";
		}
		Dialogs.ordning = 0;
		return "";
	}
}
