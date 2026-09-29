import java.util.Scanner;

public class Settings extends Base {
    private final boolean language;
    private final boolean powerUps;
    private final boolean shipDistribution;
    private final boolean ships;
    private final Base    base;
    Scanner scan;

    public Settings(Base base, boolean language, boolean powerUps, boolean shipDistribution, boolean ships) {
        this.base               = base;
        this.language           = language;
        this.powerUps           = powerUps;
        this.shipDistribution   = shipDistribution;
        this.ships              = ships;
        scan = new Scanner(System.in);
    }

    public void setLanguage(String language)        {
        if (!this.language) {
            System.out.print("German (default) or english (E)?: ");
            base.printer.setLanguage(scan.next());
        } else
            base.printer.setLanguage(language);
    }
    public void setPowerUp(PowerUp_Base powerUps)   {
        int id1;
        int id2;
        int id3;
        if (!this.powerUps) {
            base.printer.printPowerUpSelection();
            System.out.print("Chose your powerUps by entering its ID:\nI:   - ");
            id1 = scan.nextInt();
            base.powerUp.chosePowerUp(1, id1);
            System.out.print("II:  - ");
            id2 = scan.nextInt();
            while (id2 == id1) {
                System.out.println("IDs können nur einmalig verwendet werden\nII:  - ");
                id2 = scan.nextInt();}
            base.powerUp.chosePowerUp(2, id2);
            System.out.print("III: - ");
            id3 = scan.nextInt();
            while (id3 == id1 || id3 == id2) {
                System.out.println("IDs können nur einmalig verwendet werden\nIII: - ");
                id3 = scan.nextInt();}
            base.powerUp.chosePowerUp(3, id3);
        } else
            base.powerUp = powerUps;
    }
    public int[] setShipDistribution(int[] boats)   {
        if (!this.shipDistribution) {
            int[] newBoats = new int[4];
            System.out.println(base.printer.boatsExplMess);
            for (int counter = 0; counter < 4; counter++)
                for (newBoats[counter] = scan.nextLine().charAt(0) - 48; !(newBoats[counter] <= 6 && boats[counter] >= 2) && !(boats[counter] == 0); boats[counter] = scan.nextLine().charAt(0) - 48)
                    System.out.println(base.printer.notValidMess);
            if (newBoats[0] + newBoats[1] + newBoats[2] + newBoats[3] != 15) {
                System.out.println(base.printer.notValidMess);
                return setShipDistribution(new int[4]);
            } else
                return newBoats;
        } else
            return boats;
    }
    public void setShips(char[][] ships)            {
        if (!this.ships) {
            int[] boats = setShipDistribution(new int[4]);
            base.placePieces(buildBoat(boats[0]), buildBoat(boats[1]), buildBoat(boats[2]), buildBoat(boats[3]));
        } else
            base.mapMe = ships;
        base.powerUp.mapOrigin = base.mapMe;
    }
}
