public class PowerUp_Base extends Base {
    int                     amount;
    int                     cooldown;
    protected boolean       suppress;
    public PowerUp_Base     pu1;
    public PowerUp_Base     pu2;
    public PowerUp_Base     pu3;

    PU_MultiHit multiHit;
    PU_Shield       shield;
    PU_FragBomb     fragBomb;
    PU_Torpedo      torpedo;
    PU_JammingDev   jammingDev;
    PU_Search       search;
    PU_Minefield    minefield;
    PU_Critical     critical;
    PU_Relocate     relocate;

    public PowerUp_Base() {}
    @SuppressWarnings("InstantiatingAThreadWithDefaultRunMethod")
    public PowerUp_Base(Base base, boolean suppress)    {
        this.mapMe      = base.mapMe;
        this.mapEnemy   = base.mapEnemy;
        this.network    = base.network;
        this.scan       = base.scan;
        this.printer    = base.printer;
        this.suppress   = false;
        if (!suppress) {
            multiHit    = new PU_MultiHit(base, true);
            shield      = new PU_Shield(base, true);
            fragBomb    = new PU_FragBomb(base, true);
            torpedo     = new PU_Torpedo(base, true);
            jammingDev  = new PU_JammingDev(base, true);
            search      = new PU_Search(base, true);
            minefield   = new PU_Minefield(base, true);
            critical    = new PU_Critical(base, true);
            relocate    = new PU_Relocate(base, true);}
    }

    @Override
    public boolean attack(String input) {
        if (input.equals("power")) {
            if (isSuppressed())
                return false;
            printMessage();
            input = scan.next();
            if (!validateIsNumber(input.charAt(1)))
                return false;
            switch (input.charAt(1)) {
                case '1' -> {return pu1.attack("");}
                case '2' -> {return pu2.attack("");}
                case '3' -> {return pu3.attack("");}
                default -> {return false;}
            }}
        return false;
    }
    public boolean defend(String input) {
        pu1.shieldIsActive = shieldIsActive;
        pu2.shieldIsActive = shieldIsActive;
        pu3.shieldIsActive = shieldIsActive;
        return switch (input) {
            case Printer.multi -> multiHit.defend("");
            case Printer.fragBomb -> fragBomb.defend("");
            case Printer.torpedo -> torpedo.defend("");
            case Printer.jamming -> jammingDev.defend("");
            case Printer.search -> search.defend("");
            case Printer.mine -> minefield.defend("");
            case Printer.critical -> critical.defend("");
            case Printer.relocate -> relocate.defend("");
            default -> false;
        };
    }

    //PowerUps helper
    public void         unSuppress()                    {suppress = false;}
    public void         chosePowerUp(int index, int id) {
        if (index == 1) {
            switch (id) {
                case 1: pu1 = multiHit;break;
                case 2: pu1 = shield;break;
                case 3: pu1 = fragBomb;break;
                case 4: pu1 = torpedo;break;
                case 5: pu1 = jammingDev;break;
                case 6: pu1 = search;break;
                case 7: pu1 = minefield;break;
                case 8: pu1 = critical;break;
                case 9: relocate.setShips(mapOrigin);pu1 = relocate;break;}}
        if (index == 2) {
            switch (id) {
                case 1: pu2 = multiHit;break;
                case 2: pu2 = shield;break;
                case 3: pu2 = fragBomb;break;
                case 4: pu2 = torpedo;break;
                case 5: pu2 = jammingDev;break;
                case 6: pu2 = search;break;
                case 7: pu2 = minefield;break;
                case 8: pu2 = critical;break;
                case 9: relocate.setShips(mapOrigin);pu2 = relocate;break;}}
        if (index == 3) {
            switch (id) {
                case 1: pu3 = multiHit;break;
                case 2: pu3 = shield;break;
                case 3: pu3 = fragBomb;break;
                case 4: pu3 = torpedo;break;
                case 5: pu3 = jammingDev;break;
                case 6: pu3 = search;break;
                case 7: pu3 = minefield;break;
                case 8: pu3 = critical;break;
                case 9: relocate.setShips(mapOrigin);pu3 = relocate;break;}}
    }
    public void         printMessage()                  {
        System.out.print("PowerUp active, chose one and enter it's index.");
        System.out.print("\n(1) ");
        pu1.printMessage();
        System.out.print("\n(2) ");
        pu2.printMessage();
        System.out.print("\n(3) ");
        pu3.printMessage();
        System.out.println("(default num) back to regular attack");
    }
    public boolean      isSuppressed()                  {
        if  (pu1.isSuppressed() || pu2.isSuppressed() || pu3.isSuppressed()) {
            System.out.println("PowerUps are suppressed");
            return true; }
        if (pu1.amount == 0 && pu2.amount == 0 && pu3.amount == 0) {
            System.out.println("No more PowerUps left");
            return true;}
        return false;
    }
    public boolean      reduceCooldown()                {
        pu1.unSuppress();
        pu2.unSuppress();
        pu3.unSuppress();
        if (pu1.reduceCooldown() || pu2.reduceCooldown() || pu3.reduceCooldown()) {
            shieldIsActive = true;
            pu1.shieldIsActive = true;
            pu2.shieldIsActive = true;
            pu3.shieldIsActive = true;
            return true;}
        return false;
    }
    protected void      powerUpHelper1(String hitList)  {
        for (int x = transformSign(hitList.charAt(0)), y = transformSign(hitList.charAt(1)); hitList.length() > 3; hitList = hitList.substring(3), x = transformSign(hitList.charAt(0)), y = transformSign(hitList.charAt(1)))
            if (x >= 0 && y >= 0 && x <= 9 && y <= 9)
                placeAttack(x, y, true);
    }
}