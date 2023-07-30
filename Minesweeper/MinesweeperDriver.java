

import java.util.Scanner;

/**
 * The driver class for the minesweeper game.
 * The class that you run to actually play the game.
 * Author - Kevin Howard
 */
public class MinesweeperDriver {

    /**
     * The main class of the Driver class.
     *
     * @param args The command line arguments.
     */
    public static void main (String[] args) {
        try {
            //String seedPath = args[0];
            Scanner stdIn = new Scanner(System.in);
            MinesweeperGame myGame = new MinesweeperGame(stdIn, "test1.txt");
            // MinesweeperGame myGame = new MinesweeperGame(stdIn, "seedPath");
            myGame.play();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        } // try
    } // main
} // MinesweeperDriver
