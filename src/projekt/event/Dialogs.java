package projekt.event;

/**
 *
 * @author Johannes
 */
public final class Dialogs {

    public static final int MAXMESSAGELENGTH = 100;

    public static final class Story {

        public static final String story1 = "message. \"\"\"\n The gods realized in 1893 that the humans had lost their faith in a superior power. As the future could see into the future, the humans would walk straight into an eternal life in despair, if nothing is done. All the gods concluded that something had to be done. Somehow the humans had to be saved from their dark destiny. The gods act and picks a random dude that will infiltrate and save the humanity from within.\n\"\"\"\t"
                + "\"\"\"\nThe chosen one, Ranglon, begins an epic journey to spread love and defeat evil around the world. At the evils nest where the most horrible and evil creature lives, our hero dies. The gods panic and chose a new messenger\n\"\"\"\t"
                + "\"\"\"\nDud.\n\"\"\"";
    }

    public static final class main {
        public static String wellcomeDude = "Welcome to the world of Pokèmans! You will find amazing things and explore extremely dangerous dungeons of chaos...\t"
                + "Remember that all of the worlds evil lurks in the dark dungeon. You will probably die just as your precedor...\t"
                + "Now go! Explore the dungeons if you dare.. the entrance is over there.";
        public static String dungeonEntryDude = "Welcome to the dangerous dungeon. When you get attacked you will have to fight back, otherwise you will die and rot.\t"
                + " Your first opponent awaits up ahead!";
        public static String[] hellper = new String[]{
            "Hello there, young Dud! I have been tracking your progress, you are doing well. When you finally have come this far there is yet more evil to defeat. \t"
            + "Further away there is a passage of which you cannot pass. To be able to walk through the passage you will have to defeat all the enemies in the current dungeon."
            + "Only then you'll be able to pass the barrier continue your journey. Good luck."
        };
    }

    public static final class EnemyCalls {
        public static String defeatedEnemy = "H-how is it possible..? You won...\n I am worthless.";

        public static String firstAttack = "Aha, so it is you are the dangerous boy of whom my boss have told me about - You don't seem very dangerous to me, I guess it'll be easier than I thought kicking your ass!\t"
                + " Do you know how to knock yourself yet-\n What an amateur you are come here so I can kick you!\t"
                + " Well lets get on with it, shall we-";
    }
    public static final String[] itemDialog = new String[]{
        "Only item is here!"
    };
    public static final String[] characterDialog = new String[]{
        Dialogs.main.dungeonEntryDude, // en annan välkommnare som står innanför porten innuti huset(world11)
        Dialogs.EnemyCalls.firstAttack, // en enkel strid som attakerar en så fort man kommer nära (obs inga frågetecken: ?)
        Dialogs.main.hellper[0], // berättar om barriärer
        "Evil3",
        "Good4",
        "Evil5",
        "Good6",
        "Evil7",
        "Good8",
        "Evil9",
        "Here you can see a shorter barrier", // effeter två jobbiga barriärer
        "Evil11",
        Dialogs.main.dungeonEntryDude, // en annan välkommnare som står innanför porten innuti huset(world11)
        "Evil13",
        Dialogs.main.wellcomeDude, // in lvl world1(intro dude)
        "Evil15",
        "                - Outpost Coverage -\nHere we will protect you from the outer world and patch you up when you get hurt."
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
