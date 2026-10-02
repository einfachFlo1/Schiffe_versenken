public class PU_MultiHit extends PowerUp_Base {
    public PU_MultiHit(Base base, boolean suppress)     {
        super(base, suppress);
        amount = 2;
        cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0) {
            if (cooldown > 0)
                System.out.print(Printer.BLACK);
            System.out.print("While hitting an enemies ship, you are allowed to shoot again.");
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
        if (amount == 0 || cooldown > 0)
            return false;
        network.sendSignal(Printer.multi);
        while (true) {
            System.out.print("Which field do you attack?: ");
            input = scan.next();
            if (!validateInput(input))
                continue;
            network.sendSignal(input);
            if (!placeAttack(transformSign(input.charAt(0)), transformSign(input.charAt(1)), true)) {
                break;}
            System.out.println(printer.hitMess);
            printer.printMap();}
        System.out.println(printer.missedMess);
        printer.printMap();
        amount--;
        cooldown = 4;
        return true;
    }
    public boolean defend(String input) {
        System.out.println(printer.pUUsed);
        input = network.receiveSignal();
        while (placeAttack(transformSign(input.charAt(0)), transformSign(input.charAt(1)), false)) {
            System.out.println(printer.hitMess);
            input = network.receiveSignal();
        }
        System.out.println(printer.missedMess);
        return true;
    }
}
