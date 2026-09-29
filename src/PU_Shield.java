public class PU_Shield extends PowerUp_Base {
    public PU_Shield(Base base, boolean suppress)   {
        super(base, suppress);
        this.amount = 2;
        this.cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0) {
            if (cooldown > 0)
                System.out.print(Printer.BLACK);
            System.out.print("Löse einen Schild aus, der dich 3 Runden lang schützt");
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
        if (cooldown > 0)
            return true;
        for (int outer = 0; outer != mapMe.length; outer++)
            for (int inner = 0; inner != mapMe[outer].length; inner++)
                if (mapMe[outer][inner] >= 11 && mapMe[outer][inner] <= 14)
                    mapMe[outer][inner] = (char) ((int) mapMe[outer][inner] - 10);
        return false;
    }
    public boolean attack(String input) {
        if (amount <= 0 || cooldown > 0)
            return false;
        suppress = true;
        amount--;
        cooldown = 4;
        return false;
    }
}
