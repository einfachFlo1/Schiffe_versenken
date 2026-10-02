public class PU_Torpedo extends PowerUp_Base {
    public PU_Torpedo(Base base, boolean suppress)  {
        super(base, suppress);
        amount = 1;
        cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0)
            System.out.print("Platziere einen Torpedo, um eine gesammte Reihe/Spalte zu treffen.");
        else
            System.out.print("-");
    }
    public boolean isSuppressed()       {return suppress;}
    public boolean reduceCooldown()     {return false;}
    public boolean attack(String input) {
        if (amount <= 0)
            return false;
        System.out.print(printer.powerUp1Mess);
        input = scan.next();
        if (!validateIsLetter(input.charAt(0)) && !validateIsNumber(input.charAt(0)) && input.length() > 1)
            return false;
        String output = "";
        try {
            sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        if (input.charAt(0) < 97 || input.charAt(0) > 106) {
            for (int counter = 0; counter < 11; counter++)
                //noinspection ControlFlowStatementWithoutBraces,StringConcatenationInLoop
                output = output + (char) (counter + 97) + input.charAt(0) + Printer.empty;
        } else if (input.charAt(0) < 48 || input.charAt(0) > 57) {
            for (int counter = 0; counter < 11; counter++)
                //noinspection StringConcatenationInLoop, ControlFlowStatementWithoutBraces
                output = output + input.charAt(0) + (char) (counter + 48) + Printer.empty;
        } else {
            System.out.println(printer.notValidMess);
            return false;}
        network.sendSignal(Printer.torpedo);
        network.sendSignal(output);
        powerUpHelper1(output);
        try {
            sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        amount--;
        return true;
    }
    public boolean defend(String input) {
        System.out.println(printer.powerUpUsedMess);
        input = network.receiveSignal();
        for (int x = transformSign(input.charAt(0)), y = transformSign(input.charAt(1)); input.length() > 3; input = input.substring(3), x = transformSign(input.charAt(0)), y = transformSign(input.charAt(1))) {
            if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
                placeAttack(x, y, false);
            } else network.sendSignal(Printer.dismiss);}
        return true;
    }
}
