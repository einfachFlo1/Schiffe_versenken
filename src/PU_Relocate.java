public class PU_Relocate extends PowerUp_Base {
    public PU_Relocate(Base base, boolean suppress)   {
        super(base, suppress);
        this.amount = 2;
        this.cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0) {
            if (cooldown > 0)
                System.out.print(Printer.BLACK);
            System.out.print("Relocate one of your ships.");
            if (amount > 1)
                System.out.print(" (x2).");
            else
                System.out.print(".");
        } else
            System.out.print("-");
        System.out.print(Printer.RESET);
    }
    public boolean isSuppressed()       {return suppress;}
    public boolean reduceCooldown()     {
        cooldown--;
        return false;
    }
    public boolean attack(String input) {
        if (amount == 0 || cooldown < 0)
            return false;
        System.out.println("Which ship do you want to relocate?");
        String ship = scan.next();
        if (!validateInput(ship))
            return false;
        while (!findShip(transformSign(ship.charAt(0)), transformSign(ship.charAt(1)))) {
            System.out.println("Please chose a valid ship.");
            ship = scan.next();}
        System.out.println("Relocate your ship now.");
        String start = scan.next();
        String end = scan.next();
        while (!validateInput(start) || !validateInput(end)) {start = scan.next();end = scan.next();}
        int counter = 0;
        char copy = indexB;
        for (int outer = 0; outer != mapOrigin.length; outer++)
            for (int inner = 0; inner != mapOrigin[outer].length; inner++)
                if (mapOrigin[outer][inner] == indexB)
                    counter++;
        indexB = 6;
        while (true) {
            if (1 + Math.abs((transformSign(start.charAt(0)) - transformSign(end.charAt(0))) - (transformSign(start.charAt(1)) - transformSign(end.charAt(1)))) != counter) {
                System.out.println("Please enter a valid length.");
                while (!validateInput(start) || !validateInput(end)) {start = scan.next();end = scan.next();}
                continue;}
            if (placeDots(transformSign(start.charAt(0)), transformSign(start.charAt(1)), transformSign(end.charAt(0)), transformSign(end.charAt(1))))
                break;
            System.out.println("Please enter a valid Position.");
            while (!validateInput(start) || !validateInput(end)) {start = scan.next();end = scan.next();}}
        indexB = copy;
        for (int outer = 0; outer != mapOrigin.length; outer++)
            for (int inner = 0; inner != mapOrigin[outer].length; inner++)
                if (mapOrigin[outer][inner] == indexB) {
                    if (mapMe[outer][inner] == Printer.hit) setPieces((char) 5);
                    else setPieces(indexB);
                    mapMe[outer][inner] = ' ';
                    mapOrigin[outer][inner] = ' ';}
        setToOrigin(start, end);
        System.out.println("A ship was relocated.");
        printer.printMap();
        suppress = true;
        cooldown = 4;
        amount--;
        return false;
    }

    public void setShips(char[][] mapOrigin)            {
        this.mapOrigin = new char[10][10];
        for (int outer = 0; outer != mapOrigin.length; outer++)
            System.arraycopy(mapOrigin[outer], 0, this.mapOrigin[outer], 0, mapOrigin.length);
    }
    private boolean findShip(int x, int y)              {
        if (mapOrigin[x][y] != ' ') {
            indexB = mapOrigin[x][y];
            for (char[] outer : mapMe)
                for (char inner : outer)
                    if (inner == indexB)
                        return true;
        }
        return false;
    }
    private void setToOrigin(String start, String end)  {
        char[][] copy = mapMe;
        mapMe = mapOrigin;
        mapOrigin = copy;
        placeDots(transformSign(start.charAt(0)), transformSign(start.charAt(1)), transformSign(end.charAt(0)), transformSign(end.charAt(1)));
        copy = mapMe;
        mapMe = mapOrigin;
        mapOrigin = copy;
    }
    private void setPieces(char piece)                  {
        for (int outer = 0; outer != mapMe.length; outer++)
            for (int inner = 0; inner != mapMe[outer].length; inner++)
                if (mapMe[outer][inner] == 6) {
                    mapMe[outer][inner] = piece;
                    return;
                }
    }
}
