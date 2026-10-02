public class Printer extends Thread{
    private final Base engine;

    //Colors
    public static final String BLACK       = "\u001B[30m";
    public static final String RESET       = "\u001B[0m";
    private static final String RED        = "\u001B[31m";
    private static final String GREEN       = "\u001B[32m";
    private static final String YELLOW      = "\u001B[33m";
    private static final String MAGENTA     = "\u001B[35m";
    private static final String CYAN        = "\u001B[36m";
    private static final String BLACK_BACK  = "\u001B[40m";
    private static final String RED_BACK    = "\u001B[41m";
    private static final String BLUE_BACK   = "\u001B[44m";
    private static final String WHITE_BACK  = "\u001B[47m";

    //Game Pieces (Interchangeable)
    static final char hit                   = '⊗';
    static final char miss                  = '≈';
    static final char pieces                = '■';
    static final char shield                = '▽';
    static final char mines                 = '✶';

    //Constants
    static final char bombHit               = '$';
    static final char empty                 = ' ';
    static final String dismiss             = "-";
    static final String hitSignal           = "X";
    static final String destroyedSignal     = "XX";
    static final String shieldSignal        = "S";
    static final String missSignal          = "O";
    static final String cont                = "C";
    static final String end                 = "L";
    static final String torpedo             = "torpedo";
    static final String fragBomb            = "fragBomb";
    static final String search              = "search";
    static final String jamming             = "jam";
    static final String multi               = "multi";
    static final String critical            = "critical";
    static final String relocate            = "relocate";
    static final String mine                = "mine";

    //Print strings
    String errorMess                        = "An error occurred.";
    String hitMess                          = "Hit!";
    String missedMess                       = "Missed!";
    String notValidMess                     = "Invalid input.";
    String pUUsed                           = "A powerUp has been used.";

    public Printer(Base engine) {
        this.engine = engine;
    }

    //Helper
    public void     printIP(String address)     {
        System.out.println("Your IP: " + GREEN + address + RESET + "\nEnter on Client to connect.");
    }
    public void     printPowerUpSelection()     {//
        System.out.println("1.) MultiHit:\n - as long as you hit your enemies ships, you can keep on attacking");
        System.out.println("2.) Shield:\n - shield all your ships for 3 rounds");
        System.out.println("3.) FragmentationBomb:\n - place a bomb, which randomly hits fields in its radius");
        System.out.println("4.) Torpedo:\n - bomb one entire row/column");
        System.out.println("5.) JammingDevice:\n - destroy a random powerUp your enemy owns");
        System.out.println("6.) SearchingMissile:\n - place a missile, which finds ships in a radius of 2");
        System.out.println("7.) MineField:\n - place 2 mines, which explode when hit by your enemy");
        System.out.println("8.) CriticalHit:\n - if the next attack hits a ship, it is immediately destroyed");
        System.out.println("9.) Relocate:\n - relocate one of your ships to another place");
    }

    //Print waiting-dots
    public void     run()                                   {
        try {
            printDots();
        } catch (InterruptedException _) {}
    }
    public void     printDots() throws InterruptedException {
        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.print(".");
            //noinspection BusyWait
            sleep(700);}
    }

    //Output map
    public void     printMap()                                                  {
        char[][] mapMe = engine.getMapMe();
        char[][] mapEnemy = engine.getMapEnemy();
        System.out.println("     Your Battlefield:                                                               Enemies Battlefield:");
        System.out.println("     ╥ " + BLACK_BACK + " 0 " + RESET + " ╥ " + BLACK_BACK + " 1 " + RESET + " ╥ " + BLACK_BACK + " 2 " + RESET + " ╥ " + BLACK_BACK + " 3 " + RESET + " ╥ " + BLACK_BACK + " 4 " + RESET + " ╥ " + BLACK_BACK + " 5 " + RESET + " ╥ " + BLACK_BACK + " 6 " + RESET + " ╥ " + BLACK_BACK + " 7 " + RESET + " ╥ " + BLACK_BACK + " 8 " + RESET + " ╥ " + BLACK_BACK + " 9 " + RESET + " ╥                   ╥ " + BLACK_BACK + " 0 " + RESET + " ╥ " + BLACK_BACK + " 1 " + RESET + " ╥ " + BLACK_BACK + " 2 " + RESET + " ╥ " + BLACK_BACK + " 3 " + RESET + " ╥ " + BLACK_BACK + " 4 " + RESET + " ╥ " + BLACK_BACK + " 5 " + RESET + " ╥ " + BLACK_BACK + " 6 " + RESET + " ╥ " + BLACK_BACK + " 7 " + RESET + " ╥ " + BLACK_BACK + " 8 " + RESET + " ╥ " + BLACK_BACK + " 9 " + RESET + " ╥");
        System.out.println("    ╭╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣                  ╭╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣ ");
        for (int outer = 0; outer != mapMe.length; outer++) {
            System.out.print( BLACK_BACK + " " + ((char) (outer + 65)) + " " + RESET + " │║ ");
            for (int inner = 0; inner != mapMe[outer].length; inner++)
                if (inner < mapMe[outer].length - 1)
                    printBoat(mapMe[outer][inner], mapMe[outer][inner + 1]);
                else printBoat(mapMe[outer][inner], empty);
            System.out.print("             " + BLACK_BACK + " " + ((char) (outer + 65)) + " " + RESET + " │║ ");
            for (int inner = 0; inner != mapEnemy[outer].length; inner++)
                if (inner < mapEnemy[outer].length - 1)
                    printBoat(mapEnemy[outer][inner], mapEnemy[outer][inner + 1]);
                else printBoat(mapEnemy[outer][inner], empty);
            if (outer < mapMe.length - 1) {
                System.out.print("\n");
                printBordersHor(outer, mapMe);
                System.out.print("              ");
                printBordersHor(outer, mapEnemy);
                System.out.print("\n");
            } else
                System.out.println("\n    ╰╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝                  ╰╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝ ");}
    }
    private void    printBoat(char field, char fieldNext)                       {
        switch (field) {
            case hit:
                System.out.print(RED_BACK + BLACK + " " + field + " " + RESET);
                printBordersVer(field, fieldNext, RED_BACK);
                break;
            case miss:
                System.out.print(BLUE_BACK + " " + field + " " + RESET);
                printBordersVer(field, fieldNext, BLUE_BACK);
                break;
            case 1:
                System.out.print(YELLOW + "[" + pieces + "]" + RESET);
                printBordersVer(field, fieldNext, RESET);
                break;
            case 2:
                System.out.print(CYAN + "[" + pieces + "]" + RESET);
                printBordersVer(field, fieldNext, RESET);
                break;
            case 3:
                System.out.print(MAGENTA + "[" + pieces + "]" + RESET);
                printBordersVer(field, fieldNext, RESET);
                break;
            case 4:
                System.out.print(GREEN + "[" + pieces + "]" + RESET);
                printBordersVer(field, fieldNext, RESET);
                break;
            case 5, 6:
                System.out.print(RED + "[" + pieces + "]" + RESET);
                printBordersVer(field, fieldNext, RESET);
                break;
            case shield, 11, 12, 13, 14:
                System.out.print(WHITE_BACK + BLACK + " " + shield + " " + RESET);
                printBordersVer(field, fieldNext, WHITE_BACK);
                break;
            case mines:
                System.out.print(RED + "⟨" + mines + "⟩" + RESET);
                printBordersVer(field, fieldNext, RESET);
                break;
            case bombHit:
                System.out.print(RED + BLUE_BACK + "⟨" + mines + "⟩" + RESET);
                printBordersVer(field, fieldNext, BLUE_BACK);
                break;
            default:
                System.out.print("   ");
                printBordersVer(field, fieldNext, RESET);
        }
    }
    private boolean printBomb(char field, char fieldNext)                       {
        return (field == Printer.miss && fieldNext == Printer.bombHit) || (fieldNext == Printer.miss && field == Printer.bombHit) || (fieldNext == Printer.bombHit && field == Printer.bombHit);
    }
    private void    printBordersVer(char field, char fieldNext, String color)   {
        if (field == fieldNext || printBomb(field, fieldNext))
            System.out.print(color + " ║ " + RESET);
        else
            System.out.print(RESET + " ║ ");
    }
    private void    printBordersHor(int outer, char[][] map)                    {
        System.out.print(" ╞══╪╬═");
        for (int inner = 0; inner < map[outer].length; inner++) {
            if (inner < map[outer].length - 1) {
                if (map[outer][inner] == hit && map[outer + 1][inner] == hit && map[outer][inner + 1] == hit && map[outer + 1][inner + 1] == hit)
                    System.out.print(RED_BACK + "════╬═" + RESET);
                else if ((map[outer][inner] == miss && map[outer + 1][inner] == miss && map[outer][inner + 1] == miss && map[outer + 1][inner + 1] == miss) || (printBomb(map[outer][inner], map[outer][inner + 1]) && printBomb(map[outer][inner], map[outer + 1][inner + 1]) && printBomb(map[outer][inner], map[outer + 1][inner])))
                    System.out.print(BLUE_BACK + "════╬═" + RESET);
                else if (map[outer][inner] == shield && map[outer + 1][inner] == shield && map[outer][inner + 1] == shield && map[outer + 1][inner + 1] == shield || ((map[outer][inner] >= 11 && map[outer][inner] <= 14) && (map[outer][inner + 1] >= 11 && map[outer][inner + 1] <= 14) && (map[outer + 1][inner] >= 11 && map[outer + 1][inner] <= 14) && (map[outer + 1][inner + 1] >= 11 && map[outer + 1][inner + 1] <= 14)))
                    System.out.print(WHITE_BACK + "════╬═" + RESET);
                else {
                    if (map[outer][inner] == hit && map[outer + 1][inner] == hit)
                        System.out.print(RED_BACK + "═══" + RESET + "═╬═");
                    else if ((map[outer][inner] == miss && map[outer + 1][inner] == miss) || printBomb(map[outer][inner], map[outer + 1][inner]))
                        System.out.print(BLUE_BACK + "═══" + RESET + "═╬═");
                    else if (map[outer][inner] == shield && map[outer + 1][inner] == shield || ((map[outer][inner] >= 11 && map[outer][inner] <= 14) && (map[outer + 1][inner] >= 11 && map[outer + 1][inner] <= 14)))
                        System.out.print(WHITE_BACK + "═══" + RESET + "═╬═");
                    else
                        System.out.print("════╬═");
                }
            } else {
                if (map[outer][inner] == hit && map[outer + 1][inner] == hit)
                    System.out.print(RED_BACK + "═══" + RESET + "═╣");
                else if (map[outer][inner] == miss && map[outer + 1][inner] == miss)
                    System.out.print(BLUE_BACK + "═══" + RESET + "═╣");
                else if (map[outer][inner] == shield && map[outer + 1][inner] == shield || ((map[outer][inner] >= 11 && map[outer][inner] <= 14) && (map[outer + 1][inner] >= 11 && map[outer + 1][inner] <= 14)))
                    System.out.print(WHITE_BACK + "═══" + RESET + "═╬═");
                else
                    System.out.print("════╣");
            }
        }
    }
}