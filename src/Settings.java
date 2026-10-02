import java.util.Scanner;

public class Settings extends Base {
    private final boolean powerUps;
    private final boolean shipDistribution;
    private final boolean ships;
    private final Base    base;
    Scanner scan;

    public Settings(Base base, boolean powerUps, boolean shipDistribution, boolean ships) {
        this.base               = base;
        this.powerUps           = powerUps;
        this.shipDistribution   = shipDistribution;
        this.ships              = ships;
        scan = new Scanner(System.in);
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
                System.out.println("IDs can only be used once\nII:  - ");
                id2 = scan.nextInt();}
            base.powerUp.chosePowerUp(2, id2);
            System.out.print("III: - ");
            id3 = scan.nextInt();
            while (id3 == id1 || id3 == id2) {
                System.out.println("IDs can only be used once\nIII: - ");
                id3 = scan.nextInt();}
            base.powerUp.chosePowerUp(3, id3);
        } else
            base.powerUp = powerUps;
    }
    public int[] setShipDistribution(int[] boats)   {
        if (!this.shipDistribution) {
            int[] newBoats = new int[4];
            System.out.println("Please enter the lengths of your boats.\nYou have to enter 4 boats. These can vary between 2 to 6 fields. By using 0 you delete one boat.\nOverall your boats have to be equivalent to 15 fields.");
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
