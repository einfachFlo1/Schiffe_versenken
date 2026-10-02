import java.io.IOException;
import java.util.Scanner;

public class Base extends Thread{
    public char[][]         mapMe;
    public char[][]         mapEnemy;
    public char[][]         mapOrigin;
    public Scanner          scan;
    public Network          network;
    public Printer          printer;
    public PowerUp_Base     powerUp;
    public Settings         settings;
    public boolean          shieldIsActive;
    protected char          indexB;

    public Base() {}
    @SuppressWarnings("InstantiatingAThreadWithDefaultRunMethod")
    public Base(@SuppressWarnings("unused") Boolean base) {
        this.mapMe      = new char[10][10];
        this.mapEnemy   = new char[10][10];
        this.printer    = new Printer(this);
        this.network    = new Network(printer);
        this.scan       = new Scanner(System.in);
        this.powerUp    = new PowerUp_Base(this, false);
        this.settings   = new Settings(this, false, false, false);
        for (int outer = 0; outer != 10; outer++) {
            for (int inner = 0; inner != 10; inner++)
                mapMe[outer][inner] = 32;
            for (int inner = 0; inner != 10; inner++)
                mapEnemy[outer][inner] = 32;}
        try {network.buildConnection(); sleep(700);
        } catch (IOException | InterruptedException e) {throw new RuntimeException(e);}
        shieldIsActive = false;
        indexB  = 1;
    }

    //Game loop
    public void         gameBegin()             {
        System.out.println("The battle begins!\nthese are the battlefields:");
        printer.printMap();
        try {sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        settings.setShips(new char[10][10]);
        settings.setPowerUp(powerUp);
        printer.printMap();
        System.out.println("Let the battle begin!");
        try { sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        if (network.role != 1) {                                                                                        //Condition deciding, who starts
            String order = Integer.toString(((int) (Math.random() * 10)) % 2);
            network.sendSignal(order);
            if ("0".equals(order)) {
                defend("");
                network.sendSignal(Printer.cont);}
        } else {
            if ("1".equals(network.receiveSignal())) {
                defend("");
                network.sendSignal(Printer.cont);}}
        while (true) {                                                                                                  //Main game loop
            attack("");
            if (gameOver(0)) break;
            shieldIsActive = powerUp.reduceCooldown();
            defend("");
            if (gameOver(1)) break;}
        try { network.closeConnection(); } catch (IOException e) {throw new RuntimeException(e);}
    }
    private boolean     gameOver(int qualifier) {
            if (qualifier == 1) {                                                                                           //Checks, if i lost
                for (char[] outer : mapMe)
                    for (char inner : outer)
                        if (inner != ' ' && inner != Printer.miss && inner != Printer.hit) {
                            network.sendSignal(Printer.cont);
                            return false;}
                network.sendSignal(Printer.end);
                powerUp.shieldIsActive = shieldIsActive;
                System.out.println("You've lost...");
                return true;
            } else {                                                                                                        //Checks, if enemy lost
                if (network.receiveSignal().equals(Printer.end)) {
                    System.out.println("You won!");
                    return true;}}
            return false;
        }

    //Helper methods
    private boolean     checkDestroyed(int x, int y)                {
        for (int outer = 0; outer < 10; outer++)
            for (int inner = 0; inner < 10; inner++)
                if ((int) mapMe[outer][inner] == (int) mapMe[x][y] && (outer != x || inner != y))
                    return true;
        return false;
    }
    protected boolean   placeAttack(int x, int y, boolean attack)   {
        if (attack) {
            String input = network.receiveSignal();
            switch (input) {
                case Printer.missSignal -> mapEnemy[x][y] = Printer.miss;
                case Printer.hitSignal -> {
                    mapEnemy[x][y] = Printer.hit;
                    return true;}
                case Printer.destroyedSignal -> {
                    System.out.println("A ship has been destroyed");
                    mapEnemy[x][y] = Printer.hit;
                    return true;}
                case Printer.shieldSignal -> mapEnemy[x][y] = Printer.shield;
                case ("" + Printer.bombHit) -> mapEnemy[x][y] = Printer.bombHit;
                case Printer.mine -> {mapEnemy[x][y] = Printer.bombHit; input = network.receiveSignal();
                    x = transformSign(input.charAt(0));
                    y = transformSign(input.charAt(1));
                    for (int x2 = x - 2, y2 = y - 2; x2 <= x + 2; x2++, y2++)
                        if (x2 >= 0 && x2 <= 9 && y2 >= 0 && y2 <= 9)
                            placeAttack(x2, y2, false);
                    for (int x2 = x - 2, y2 = y + 2; x2 <= x + 2; x2++, y2--)
                        if (x2 >= 0 && x2 <= 9 && y2 >= 0 && y2 <= 9)
                            placeAttack(x2, y2, false);
                    return true;}}
        } else {
            if (mapMe[x][y] == Printer.mines) {
                mapMe[x][y] = Printer.bombHit;
                network.sendSignal(Printer.mine);
                network.sendSignal("" + ((char) (x + 97)) + ((char) (y + 48)));
                for (int x2 = x - 2, y2 = y - 2; x2 <= x + 2; x2++, y2++)
                    if (x2 >= 0 && x2 <= 9 && y2 >= 0 && y2 <= 9)
                        placeAttack(x2, y2, true);
                for (int x2 = x - 2, y2 = y + 2; x2 <= x + 2; x2++, y2--)
                    if (x2 >= 0 && x2 <= 9 && y2 >= 0 && y2 <= 9)
                        placeAttack(x2, y2, true);
            } else if (mapMe[x][y] == Printer.bombHit) {
                network.sendSignal("" + Printer.bombHit);
            } else if (mapMe[x][y] == Printer.empty || mapMe[x][y] == Printer.miss) {
                mapMe[x][y] = Printer.miss;
                network.sendSignal(Printer.missSignal);
            } else if (mapMe[x][y] == Printer.hit) {
                network.sendSignal(Printer.hitSignal);
            } else {
                if (shieldIsActive) {
                    if (mapMe[x][y] >= 1 && mapMe[x][y] <= 4)
                        mapMe[x][y] = (char)((int)mapMe[x][y] + 10);
                    network.sendSignal(Printer.shieldSignal);
                    return false;}
                if (!checkDestroyed(x, y)) {
                    System.out.println("A ship has been destroyed");
                    network.sendSignal(Printer.destroyedSignal);
                } else {
                    network.sendSignal(Printer.hitSignal);}
                mapMe[x][y] = Printer.hit;
                return true;}}
        return false;
    }
    protected boolean   validateInput(String input)                 {
        return (input.length() == 2 && validateIsLetter(input.charAt(0)) && validateIsNumber(input.charAt(1)));
    }
    protected boolean   validateIsNumber(char input)                {return (input >= 48 && input <= 57);}
    protected boolean   validateIsLetter(char input)                {
        return (input >= 97 && input <= 106) || (input >= 65 && input <= 74);
    }
    protected int       transformSign(char sign)                    {
        if (sign >= 48 && sign <= 57)
            return (sign - 48);
        else if (sign >= 97 && sign <= 106)
            return (sign - 97);
        else if (sign >= 65 && sign <= 74)
            return (sign - 65);
        return 0;
    }

    //Placing ships
    private int         getID(String boat)                                                  {
        String piece = Printer.pieces + "";
        if (boat.equals(Printer.dismiss))
            return 0;
        return boat.length() - boat.replace(piece, "").length();
    }
    public String       buildBoat(int size)                                                 {
        String boat = Printer.pieces  + " | " +  Printer.pieces;
        for (int counter = 2; counter < size; counter++)
            //noinspection StringConcatenationInLoop
            boat = boat + " | "  + Printer.pieces;
        if (size == 0)
            return Printer.dismiss;
        return boat;
    }
    public void         placePieces(String boat1, String boat2, String boat3, String boat4) {
        int     id;
        String  inputStart;
        String  inputEnd;
        System.out.print("Now, place your ships. You can choose from:\n" + "1.) " + boat1 + "\n2.) " + boat2 + "\n3.) " + boat3 + "\n4.) " + boat4);
        System.out.print("\nYou can place the ships by entering the start- and end position: ");
        while (!boat1.equals(Printer.dismiss) || !boat2.equals(Printer.dismiss) || !boat3.equals(Printer.dismiss) || !boat4.equals(Printer.dismiss)) {                                  //loops, while not all boats are placed
            inputStart   = scan.next();
            inputEnd     = scan.next();
            if (validateInput(inputStart) && validateInput(inputEnd) && !(inputStart.charAt(0) != inputEnd.charAt(0) && inputStart.charAt(1) != inputEnd.charAt(1))) {                                                 //Checks distance between start and end, to identify the boat
                id = 1 + Math.abs((transformSign(inputStart.charAt(0)) - transformSign(inputEnd.charAt(0))) - (transformSign(inputStart.charAt(1)) - transformSign(inputEnd.charAt(1))));
                if (id == getID(boat1) && !boat1.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat1 = Printer.dismiss;
                } else if (id == getID(boat2) && !boat2.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat2 = Printer.dismiss;
                } else if (id == getID(boat3) && !boat3.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat3 = Printer.dismiss;
                } else if (id == getID(boat4) && !boat4.equals(Printer.dismiss)) {
                    if (placeDots(transformSign(inputStart.charAt(0)), transformSign(inputStart.charAt(1)), transformSign(inputEnd.charAt(0)), transformSign(inputEnd.charAt(1))))
                        boat4 = Printer.dismiss;
                } else {
                    System.out.println(printer.notValidMess);
                    placePieces(boat1, boat2, boat3, boat4);
                    break;}
                printer.printMap();
                System.out.println("A ship has been placed!");
            } else {
                System.out.println("Invalid entry, please try again.");
                placePieces(boat1, boat2, boat3, boat4);
                break;}
            if (!boat1.equals(boat2) || !boat1.equals(boat3) || !boat1.equals(boat4)) {
                System.out.print("Still open:\n" + "\n 1.) " + boat1 + "\n 2.) " + boat2 + "\n 3.) " + boat3 + "\n 4.) " + boat4 + "\n");
                System.out.print("place your next ship. ");}}
    }
    protected boolean   placeDots(int start0, int start1, int end0, int end1)               {
        int runV1 = start1;
        int runV2 = end1;
        int runH1 = start0;
        int runH2 = end0;
        if (start0 > end0) {                                                                                            //Potentially swaps coordinates, if start > end
            runH1 = end0;
            runH2 = start0;
        } else if (start1 > end1) {
            runV1 = end1;
            runV2 = start1;}
        int cpyV1 = runV1;
        int cpyH1 = runH1;
        for (; runH1 <= runH2; runH1++) {                                                                               //Checks, if boats are placeable
            for (; runV1 <= runV2; runV1++)
                if (mapMe[runH1][runV1] != Printer.empty) {
                    System.out.println(printer.notValidMess);
                    return false;}
            runV1 = cpyV1;}
        runH1 = cpyH1;
        for (; runH1 <= runH2; runH1++) {                                                                               //Places boats
            for (; runV1 <= runV2; runV1++)
                mapMe[runH1][runV1] = indexB;
            runV1 = cpyV1;}
        indexB++;
        return true;
    }

    //Moves
    public boolean      attack(String input) {
        System.out.print("Your time to attack. Choose a field or write \"power\", to use a powerUp: ");
        input = scan.next();
        if (!powerUp.attack(input)) {
            if (!validateInput(input))
                attack("");
            else {
                int x = transformSign(input.charAt(0));
                int y = transformSign(input.charAt(1));
                if (mapEnemy[x][y] == Printer.hit || mapEnemy[x][y] == Printer.miss) {
                    System.out.println(printer.notValidMess);
                    attack("");
                } else {
                    network.sendSignal(input);
                    if (placeAttack(x, y, true)) System.out.println(printer.hitMess);
                    else System.out.println(printer.missedMess);
                    try {sleep(500);} catch (InterruptedException e) {throw new RuntimeException(e);}
                    printer.printMap();}}}
        return true;
    }
    public boolean      defend(String input) {
        Thread printDots = new Printer(this);
        System.out.print("Your enemy attacks now.");
        printDots.start();
        input = network.receiveSignal();
        printDots.interrupt();
        System.out.println();
        if (!powerUp.defend(input)) {
            int x = transformSign(input.charAt(0));
            int y = transformSign(input.charAt(1));
            if (placeAttack(x, y, false)) System.out.println(printer.hitMess);
            else System.out.println(printer.missedMess);
            try {sleep(500);} catch (InterruptedException e) {throw new RuntimeException(e);}}
        printer.printMap();
        return true;
    }

    //Getter
    public char[][]     getMapMe()      {return mapMe;}
    public char[][]     getMapEnemy()   {return mapEnemy;}
}