public class PU_Minefield extends PowerUp_Base {
    public PU_Minefield(Base base, boolean suppress)   {
        super(base, suppress);
        this.amount = 1;
        this.cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0)
            System.out.print("Set 2 mines, which explode on contact.");
        else
            System.out.print("-");
    }
    public boolean isSuppressed()       {return suppress;}
    public boolean reduceCooldown()     {return false;}
    public boolean attack(String input) {
        if (amount <= 0 || cooldown > 0)
            return false;
        System.out.print("Place mine: ");
        while (!placeMine()) {System.out.println(printer.notValidMess);}
        printer.printMap();
        while (!placeMine()) {System.out.println(printer.notValidMess);}
        printer.printMap();
        System.out.println("Your mines have been placed. Now you can attack.");
        suppress = true;
        amount--;
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean placeMine() {
        String input;
        System.out.print("Place mine: ");
        input = scan.next();
        if (!validateInput(input))
            return false;
        if (mapMe[transformSign(input.charAt(0))][transformSign(input.charAt(1))] == Printer.empty)
            mapMe[transformSign(input.charAt(0))][transformSign(input.charAt(1))] = Printer.mines;
        else
            return false;
        return true;
    }
}
