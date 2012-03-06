package projekt.event;

/**
 *
 * @author johannes
 */
public final class Dialogs {

    public static final int MAXMESSAGELENGTH = 100;

    public static final class Story {

        public static final String story1 = "message. \" 1893 the gods realized that they lost contact with the humans. The gods have to save the humans from an eternal life in horror and evil. The gods decides to act and picks a random dude that will do their mission and experience an awful fate.\"\t"
                + "\"Ranglon, the chosen one, begins an epic journey around the world to spread love and defeat evil. At the evils nest, where the most horrible creature lives -  Fizerlang, our hero dies. The Gods panic and choose a new messenger..\"\t"
                + "\"Dud!\"";
    }

    public static final class main {

        public static final String story1 = "message. \" 1893 the gods realized that they lost contact with the humans. The gods have to save the humans from an eternal life in horror and evil. The gods decides to act and picks a random dude that will do their mission and experience an awful fate.\"\t"
                + "\"Ranglon, the chosen one, begins an epic journey around the world to spread love and defeat evil. At the evils nest, where the most horrible creature lives -  Fizerlang, our hero dies. The Gods panic and choose a new messenger..\"\t"
                + "\"Dud!\"";
        public static final String sayHello = "hej! jag \nheter johannes\n och detta ar ett test pa en lite langre strang,\t"
                + "denna text ligger i en statisk klass:\nDialogs.Begin.sayHello...\t"
                + "Nytt medelande som ocksa kan vara intressant att lasa :=)";
        public static final String TALK = "Hej jag ar en Player som du kan prata med!";
        public static String wellcomeDude = "Wellcome to the world of pokemans, here you will find amsing stuff and explore extreamly dangerous dungeon's of caos...\t"
                + "Rememder it is very dangerous in there and you need to stay focus all the time to survive!\n If not, you will propobly die as the one before you did...\t"
                + "Now, go and explore the dugneon's for your self and se what awaits you, you yust need to enter that door over there.";
        public static String dungeonEntryDude = "Wellcome to the dungeon, this is a very dangerous place and you need to be carefull. When someone attacks you, upp with your guard and fight back, only then you will win.\t"
                + " Your first opponent waits for you!";
        public static String[] hellper = new String[]{
            "Hello you'r doing well, now when you have come this far there is some more waiting. \t"
            + "Litle further away you can se a green line that you can't pass until you have completed all levels of dungeon's in this section.\t"
            + "Only then you'l pass the barrier and can continue, good lock."
        };
    }

    public static final class EnemyCalls {

        public static String firstAttack = "So you are the dangerous boy my boss have told me about? Loks like it's goint to be easier than i thought.\t"
                + "Do you know how to knock yourself yet?\n What an amature you are come here so i can kick you!"
                + "Well lets get on with it, shall we?";
    }
    public static final String[] itemDialog = new String[]{
        "Only item is here!"
    };
    public static final String[] characterDialog = new String[]{
        Dialogs.main.dungeonEntryDude, // en annan wälkomnare som står innanför porten innuti huset(world11)
        Dialogs.EnemyCalls.firstAttack, // en enkel strid som attakerar en så fort man kommer nära
        Dialogs.main.hellper[0], // berättar om barriärer
        "evil3",
        "good4",
        "evil5",
        "good6",
        "evil7",
        "good8",
        "evil9",
        "Here you can se a shorter barrier", // efeter två jobbiga barriärer
        "evil11",
        "good12",
        "evil13",
        Dialogs.main.wellcomeDude, // in lvl world1(intro dude)
        "evil15",
        "                - Outpost Coverage -\n Here you are safe, we protect you from danger and heal you when you need it."
    };
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
