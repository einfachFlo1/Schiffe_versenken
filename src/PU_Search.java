public class PU_Search extends PowerUp_Base {
    public PU_Search(Base base, boolean suppress)   {
        super(base, suppress);
        this.amount = 2;
        this.cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0) {
            if (cooldown > 0)
                System.out.print(Printer.BLACK);
            System.out.print("Place a searching rocket, which finds ships in a 2 space radius.");
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
        if (amount <= 0 || cooldown > 0)
            return false;
        System.out.println("Place your searching Rocket.");
        input = scan.next();
        if (!validateInput(input)) {
            return false;}
        String output;
        try {
            sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        network.sendSignal(Printer.search);
        network.sendSignal(input);
        output = network.receiveSignal();
        placeAttack(transformSign(output.charAt(0)), transformSign(output.charAt(1)), true);
        try {
            sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        amount--;
        cooldown = 3;
        return true;
    }
    public boolean defend(String input) {
        System.out.println(printer.pUUsed);
        powerUpSearch(network.receiveSignal(), 0);
        return true;
    }

    private void powerUpSearch(String input, int distance) {
        for (int x = transformSign(input.charAt(0)) - distance; x <= transformSign(input.charAt(0)) + distance; x++)
            for (int y = transformSign(input.charAt(1)) - distance; y <= transformSign(input.charAt(1)) + distance; y++) {
                if (x >= 0 && y >= 0 && x <= 9 && y <= 9)
                    if (mapMe[x][y] >= 1 && mapMe[x][y] <= 4) {
                        network.sendSignal(("" + ((char) (x + 97)) + ((char) (y + 48))));
                        placeAttack(x, y, false);
                        System.out.println(printer.hitMess);
                        return;}}
        if (distance < 2) {
            powerUpSearch(input, distance + 1);
            return;}
        network.sendSignal(input);
        placeAttack(transformSign(input.charAt(0)), transformSign(input.charAt(1)), false);
    }
}
