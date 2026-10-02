public class PU_JammingDev extends PowerUp_Base {
    Base base;
    public PU_JammingDev(Base base, boolean suppress)   {
        super(base, suppress);
        this.base = base;
        amount = 2;
        cooldown = 0;
    }

    @Override
    public void printMessage()          {
        if (amount > 0) {
            if (cooldown > 0)
                System.out.print(Printer.BLACK);
            System.out.print("Use the jamming device, to destroy an enemies PowerUp.");
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
        System.out.println("A powerUp has been stolen. Now you can attack.");
        network.sendSignal(Printer.jamming);
        suppress = true;
        amount--;
        cooldown = 4;
        return false;
    }
    public boolean defend(String input) {
        System.out.println(printer.pUUsed);
        switch ((((int)(Math.random() * 10)) % 3) + 1) {
            case 1:
                if (base.powerUp.pu1.amount > 0) {
                    base.powerUp.pu1.amount--;
                    base.defend("");
                    return true;}
            case 2:
                if (base.powerUp.pu1.amount > 0) {
                    base.powerUp.pu2.amount--;
                    base.defend("");
                    return true;}
            case 3:
                if (base.powerUp.pu1.amount > 0) {
                    base.powerUp.pu3.amount--;
                    base.defend("");
                    return true;}
            case 4:
                if (base.powerUp.pu1.amount > 0) {
                    base.powerUp.pu2.amount--;
                    base.attack("");
                    return true;}
            case 5:
                if (base.powerUp.pu1.amount > 0) {
                    base.powerUp.pu1.amount--;
                    base.defend("");
                    return true;}
        }
        return false;
    }
}
