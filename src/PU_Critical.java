public class PU_Critical extends PowerUp_Base {
    public PU_Critical(Base base, boolean suppress)     {
        super(base, suppress);
        amount = 2;
        cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0) {
            if (cooldown > 0)
                System.out.print(Printer.BLACK);
            System.out.print("The next ship you hit is immediately destroyed.");
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
        System.out.println("Place your attack!");
        network.sendSignal(Printer.critical);
        String output;
        input = scan.next();
        if (!validateInput(input)) {
            System.out.println(printer.notValidMess);
            return false;
        } else {
            network.sendSignal(input);
            if (placeAttack(transformSign(input.charAt(0)), transformSign(input.charAt(1)), true)) {
                output = network.receiveSignal();
                while (!output.equals(Printer.missSignal)) {
                    mapEnemy[transformSign(output.charAt(0))][transformSign(output.charAt(1))] = Printer.hit;
                    output = network.receiveSignal();}
            } else { network.receiveSignal(); }
            printer.printMap();
            suppress = true;
            amount--;
            cooldown = 4;
            return true;
        }
    }
    public boolean defend(String input) {
        System.out.println(printer.pUUsed);
        input = network.receiveSignal();
        int x = transformSign(input.charAt(0));
        int y = transformSign(input.charAt(1));
        char destroy = mapMe[x][y];
        if (placeAttack(x, y, false))
            for (int outer = 0; outer < mapMe.length; outer++)
                for (int inner = 0; inner < mapMe[outer].length; inner++)
                    if (mapMe[outer][inner] == destroy) {
                        mapMe[outer][inner] = Printer.hit;
                        network.sendSignal("" + ((char)(outer + 97)) + ((char)(inner + 48)));
                    }
        network.sendSignal(Printer.missSignal);
        return true;
    }
}
