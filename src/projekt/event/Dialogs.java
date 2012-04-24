package projekt.event;

/**
 *
 * @author Johannes
 */
public final class Dialogs {

    public static final int MAXMESSAGELENGTH = 100;

    public static final class Story {

        public static final String story1 = "This is a message from the almighty Gods:\nThe gods realized in 2012 that the humans had lost their faith in their superior power. The gods could see into the future, and they foresaw that the humans would walk straight into an eternal life in despair, if nothing is done.\t"
                + "All the gods concluded that something had to be done. Somehow the humans had to be saved from their dark destiny. The gods act and picks a random dude that will infiltrate and save the humanity from within.\t"
                + "The chosen one, Ranglon, begins an epic journey to spread love and defeat evil around the world. At the evils nest where the most horrible and evil creature lives, our hero dies. The gods panic and selected a new messenger...\t"
                + "His name was Dud.";
    }

    public static final class main {
        public static String wellcomeDude = "Welcome to the world of Pokemans! You will find amazing things and explore extremely dangerous dungeons of chaos...\t"
                + "Remember that all of the worlds evil lurks in this dark dungeon. You will probably die just like your predecessor...\t"
                + "Now go! Explore the dungeons if you dare.. the entrance is over there.";
        public static String dungeonEntryDude = "Welcome to this dangerous dungeon. When you get attacked you will have to fight back otherwise you will die.\t"
                + " Your first opponent awaits up ahead!";
        public static String[] hellper = new String[]{
            "Hello there, young Dud! I have been tracking your progress and you are doing well. There is still more evil to defeat.\t"
            + "Further away there is a passage which you cannot pass. To be able to walk through the passage you will have to defeat all the enemies in that area.\t"
            + "Only then will you be able to pass the barrier and continue your journey.\t"
            + "Good luck!"
        };
    }

    public static final class EnemyCalls {
        public static final String[] defeatedEnemy = new String[]{
            "H-how is it possible..? You won...\n I am worthless.0",
            "H-how is it possible..? You won...\n I am worthless.1",
            "H-how is it possible..? You won...\n I am worthless.2",
            "H-how is it possible..? You won...\n I am worthless.3",
            "H-how is it possible..? You won...\n I am worthless.4",
            "H-how is it possible..? You won...\n I am worthless.5",
            "H-how is it possible..? You won...\n I am worthless.6",
            "H-how is it possible..? You won...\n I am worthless.7",
            "H-how is it possible..? You won...\n I am worthless.8",
            "H-how is it possible..? You won...\n I am worthless.9",
            "H-how is it possible..? You won...\n I am worthless.10",
            "H-how is it possible..? You won...\n I am worthless.11",
            "H-how is it possible..? You won...\n I am worthless.12",
            "H-how is it possible..? You won...\n I am worthless.13",
            "H-how is it possible..? You won...\n I am worthless.14",
            "H-how is it possible..? You won...\n I have no purpose.15",
            "Damn it! What? Are you too great for me now?.16"
        };

        public static String[] enemiesAttack = new String[] {
            "Aha, so it is you! The dangerous boy who the boss told us about - You don't seem very dangerous to me, I guess this will be easier than I thought!",
            "Oh, well... I'll give you a helping hand! Muahaahah!",
            "Let's just get on with it!",
            "I've waited my whole life for this opportunity! I won't let you stand in my way!",
            "CHALLENGE ACCEPTED!",
            "OMG, UR LIEK SUCH A N00B! UR going down!!!",
            "Engage battle!!!",
            "Like a BOSS!"
        };
    }
    public static final String[] itemDialog = new String[]{
        "You found an item./",
        "You found a HP-potion/",
        "YOU SHALL NOT PASS...this barrier./",
        "Looks like you can cut here./hihkjhjk"
    };
    public static final String[] characterDialog = new String[]{
        Dialogs.main.dungeonEntryDude, // en annan välkommnare som står innanför porten innuti huset(world11)
        Dialogs.EnemyCalls.enemiesAttack[0], // en enkel strid som attackerar en så fort man kommer nära (obs inga frågetecken: ?)
        Dialogs.main.hellper[0], // berättar om barriärer
        Dialogs.EnemyCalls.enemiesAttack[1], //Byt namn på karaktärer
        "Good4",
        Dialogs.EnemyCalls.enemiesAttack[2],
        "Good6",
        Dialogs.EnemyCalls.enemiesAttack[3],
        "Good8",
        Dialogs.EnemyCalls.enemiesAttack[4],
        "Here you can see a shorter barrier", // efter två jobbiga barriärer
        Dialogs.EnemyCalls.enemiesAttack[5],
        Dialogs.main.dungeonEntryDude, // en annan välkommnare som står innanför porten innuti huset(world11)
        Dialogs.EnemyCalls.enemiesAttack[6],
        Dialogs.main.wellcomeDude, // in lvl world1(intro dude)
        Dialogs.EnemyCalls.enemiesAttack[7],
        "                - Outpost Coverage -\n. Here you are protected from the outer world and can get patched up if you are hurt."
    };
    public static boolean endof = true;
    public static String[] message;
    public static String[] names = new String[]{
        "Tor",
        "Frej",
        "Oden",
        "Thorn",
        "ASSA"
    };

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
