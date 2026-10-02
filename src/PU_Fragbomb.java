public class PU_Fragbomb extends PowerUp_Base {
    public PU_Fragbomb(Base base, boolean suppress) {
        super(base, suppress);
        amount = 1;
        cooldown = 0;
    }

    @Override
    public void printMessage()                      {
        if (amount > 0)
            System.out.print("Platziere eine Bombe, die zufällige Felder um sich herrum trifft.");
        else
            System.out.print("-");
    }
    public boolean isSuppressed()                   {return suppress;}
    public boolean reduceCooldown()                 {return false;}
    public boolean attack(String input)             {
        if (amount <= 0)
            return false;
        System.out.print(printer.powerUp2Mess);
        input = scan.next();
        try {
            sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        String output = input + Printer.empty;
        String strive = "";
        if (!validateInput(input)) {
            System.out.println(printer.notValidMess);
            return false;}
        for (int counter = 0; counter < 11;) {
            //noinspection ControlFlowStatementWithoutBraces,StringConcatenationInLoop
            strive = strive + powerUpHelperFrag(input.charAt(0)) + powerUpHelperFrag(input.charAt(1));
            if (!output.contains(strive) && !strive.equals(input)) {
                //noinspection ControlFlowStatementWithoutBraces,StringConcatenationInLoop
                output = output + strive + Printer.empty;
                counter++;}
            strive = "";}
        network.sendSignal(Printer.fragBomb);
        network.sendSignal(output);
        powerUpHelper1(output);
        try {
            sleep(700);} catch (InterruptedException e) {throw new RuntimeException(e);}
        printer.printMap();
        amount--;
        return true;
    }
    public boolean defend(String input)             {
        System.out.println(printer.powerUpUsedMess);
        input = network.receiveSignal();
        for (int x = transformSign(input.charAt(0)), y = transformSign(input.charAt(1)); input.length() > 3; input = input.substring(3), x = transformSign(input.charAt(0)), y = transformSign(input.charAt(1))) {
            if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
                placeAttack(x, y, false);
            } else network.sendSignal(Printer.dismiss);}
        return true;
    }

    protected char powerUpHelperFrag(char input)    {
        int num = ((int)(Math.random() * 10)) % 3;
        if (((int)(Math.random() * 10)) % 2 == 0) {
            return (char) ((input - num));
        } else {
            return (char) ((input + num));}
    }

}
