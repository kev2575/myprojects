
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;

/**
 * A class that has useful methods meant to make a minesweeper
 * game, A object of this class represents a game of minesweeper.
 * To play the game make a object of this class and use the play method.
 *
 * author-Kevin Howard
 */
public class MinesweeperGame {
    private boolean[][] mineBoard; // holds the mines.
    private String[][] gameBoard; // holds the characters like mark and guess.
    private int rounds; // holds number of rounds.
    private final Scanner stdIn; // holds scanner object.
    private int numMines; // holds number of mines.
    private int exit; // holds the exit number which makes the game exit.
    private int control; // controls nofog so that the nofog command prints only one board.

    /**
     * A contructor that makes a game of minesweeper.
     * It initializes all the variables with the proper values.
     *
     * @param stdIn a scanner object that is passed through for the
     * game.
     * @param seedPath should give information for the seed file.
     */
    public MinesweeperGame(Scanner stdIn, String seedPath) {
        this.stdIn = stdIn;
        rounds = 0;
        exit = 0;
        control = 0;
        try {
            File configFile = new File(seedPath);
            this.checkSeedFile(configFile);
            Scanner configScanner = new Scanner(configFile);
            int seedRow = Integer.parseInt(configScanner.next());
            int seedCol = Integer.parseInt(configScanner.next());
            gameBoard = new String[seedRow][seedCol];
            mineBoard = new boolean[seedRow][seedCol];
            for (int row = 0; row < gameBoard.length; row++) {
                for (int col = 0; col < gameBoard[0].length; col++) {
                    gameBoard[row][col] = " ";
                    mineBoard[row][col] = false;
                } // for
            } // for
            // puts true in mineboard where there is a mine
            this.numMines = Integer.parseInt(configScanner.next());
            int i = 0;
            while (numMines > i) {
                int mineRow = Integer.parseInt(configScanner.next());
                int mineCol = Integer.parseInt(configScanner.next());
                mineBoard[mineRow][mineCol] = true;
                i++;
            } // while
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("Seed File Not Found Error: " + fnfe.getMessage());
            System.exit(2);
        } // try
    } //MinesweeperGame

    /**
     * Prints the welcome message given in resources.
     */
    public void printWelcome() {
        System.out.println("Hello");
    } // printWelcome

    /**
     * Prints the current contents of the board.
     *
     */
    public void printMineField() {
        System.out.println();
        System.out.println(" Rounds Completed: " + rounds);
        System.out.println();
        // goes through gameboard to set up the board.
        for (int row = 0; row < gameBoard.length; row++) {
            System.out.print(row + " |");
            for (int col = 0; col < gameBoard[0].length; col++) {
                System.out.print(" " + gameBoard[row][col] + " |");
            } // for
            System.out.println();
        } // for
        // prints the numbers for the bottom of the board
        System.out.print("    ");
        for (int col = 0; col < gameBoard[0].length; col++) {
            System.out.print(col + "   ");
        } // for
        System.out.println();
        System.out.println();
    } // printMineField

    /**
     * if the conditions of winning have been met
     * it will return true. players win by marking every square
     * with a mine and revealing the rest.
     *
     * @return true if winning conditons are met. False otherwise.
     */
    public boolean isWon() {
        // loops through mineboard to check the conditions
        for (int row = 0; row < mineBoard.length; row++) {
            for (int col = 0; col < mineBoard[0].length; col++) {
                // if there is a mine that hasn't been marked it returns false.
                if (mineBoard[row][col] == true && !gameBoard[row][col].equals("F")) {
                    return false;
                } // if
                // if there is a spot that is blank returns false.
                if (gameBoard[row][col].equals(" ")) {
                    return false;
                } // if
                // if there isn't a mine and put it has a F it returns false
                if (mineBoard[row][col] == false && gameBoard[row][col].equals("F")) {
                    return false;
                } // if
                if (gameBoard[row][col].equals("?")) {
                    return false;
                }
            } // for
        } // for
        return true;
    } // isWon

    /**
     * prints winning message.
     */
    public void printWin() {
        System.out.println("You win");
            double score = 100.0 * gameBoard.length * gameBoard[0].length / rounds;
            score = Math.round(score * 100.0 ) / 100.0;
            System.out.println(" " + score);
            exit = 3;
    } // printWin

    /**
     * prints losing message.
     */
    public void printLoss() {
        System.out.println("You lose");
    } //printLoss

    /**
     * main game loop that keeps the game going.
     */
    public void play() {
        this.printWelcome();
        while (this.exit == 0) {
            if (control == 0) {
                this.printMineField();
            } // if
            this.promptUser();
            if (this.isWon()) {
                this.printWin();
            } // if
        } //while
    } // play

    /**
     * Method that takes the user input and calls the proper method
     * depending on the user input. If the user does not put in a proper
     * command then a error message will be printed and the game will continue.
     *
     */
    public void promptUser() {
        try {
            this.control = 0;
            System.out.print("minesweeper-alpha: ");
            String fullCommand = stdIn.nextLine();
            Scanner commandScan = new Scanner(fullCommand);
            String command = commandScan.next();
            if (command.equals("reveal") || command.equals("r")) {
                int row = commandScan.nextInt();
                int col = commandScan.nextInt();
                if (commandScan.hasNext()) {
                    this.incorrectNumIndexCommand();
                } else {
                    this.reveal(row, col);
                } // if
            } else if (command.equals("mark") || command.equals("m")) {
                int row = commandScan.nextInt();
                int col = commandScan.nextInt();
                if (commandScan.hasNext()) {
                    this.incorrectNumIndexCommand();
                } else {
                    this.mark(row, col);
                } // if
            } else if (command.equals("guess") || command.equals("g")) {
                int row = commandScan.nextInt();
                int col = commandScan.nextInt();
                if (commandScan.hasNext()) {
                    this.incorrectNumIndexCommand();
                } else {
                    this.guess(row, col);
                } // if
            } else if (command.equals("nofog")) {
                this.nofog();
            } else if (command.equals("help") || command.equals("h")) {
                this.help();
            } else if (command.equals("quit") || command.equals("q")) {
                this.quit();
            } else {
                System.err.println();
                System.err.println("Invalid Command: Command not recognized!");
            } // else
        } catch (NoSuchElementException nsee) {
            this.incorrectNumIndexCommand();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            this.printOutArrayError(aioobe);
        } // try
    } // promptUser

    /**
     * prints out the error for array index out of bounds exceptions.
     *
     * @param aioobe the exception that was caught.
     */
    public void printOutArrayError(ArrayIndexOutOfBoundsException aioobe) {
        System.err.println();
        System.err.println("Invalid Command: " + aioobe.getMessage());
    } // printOutArrayError

    /**
     * Prints out the error message for a invalid command.
     * if the user does not put in the proper number of indexes.
     */
    public void incorrectNumIndexCommand() {
        System.err.println();
        System.err.println("Invalid Command: Command not recognized!");
    } // incorrectCommand

    /**
     * quits the game.
     */
    public void quit() {
        System.out.println();
        System.out.println("Quitting the game...");
        System.out.println("Bye!");
        this.exit = 2;
    } // quit

    /**
     * Prints helpful tips for the user.
     */
    public void help() {
        System.out.println("Commands Available...");
        System.out.println(" - Reveal: r/reveal row col");
        System.out.println(" -   Mark: m/mark   row col");
        System.out.println(" -  Guess: g/guess  row col");
        System.out.println(" -   Help: h/help");
        System.out.println(" -   Quit: q/quit");
        rounds++;
    } // help

    /**
     * puts a question mark on the board where the player thinks there is a mine.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     */
    public void guess(int row, int col) {
        if (this.isInBounds(row, col) == false) {
            System.err.println();
            System.err.println("Invalid Command: index is not in bounds");
        } else {
            gameBoard[row][col] = "?";
            rounds++;
        } // else
    } // guess

    /**
     * puts a F on the board where the player knows there is a mine.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     */
    public void mark (int row, int col) {
        if (this.isInBounds(row, col) == false) {
            System.err.println();
            System.err.println("Invalid Command: index is not in bounds");
        } else {
            gameBoard[row][col] = "F";
            rounds++;
        } // else
    } // mark

    /**
     * puts a number on the board based on the mines near the spot.
     * If you reveal a mine you lose.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     */
    public void reveal(int row, int col) {
        if (mineBoard[row][col] == true) {
            this.printLoss();
            this.exit = 1;
        } // if
        if (this.isInBounds(row, col) == false) {
            System.err.println();
            System.err.println("Invalid Command: index is not in bounds");
        } else {
            int mineNum = this.getNumAdjMines(row, col);
            gameBoard[row][col] = Integer.toString(mineNum);
            rounds++;
        } //else
    } // reveal

    /**
     * Puts arrows around where the mines are.
     */
    public void nofog() {
        System.out.println();
        System.out.println("Rounds Completed: " + rounds);
        System.out.println();
        for (int row = 0; row < gameBoard.length; row++) {
            System.out.print(row + " |");
            for (int col = 0; col < gameBoard[0].length; col++) {
                if (mineBoard[row][col] == true) {
                    System.out.print("<" + gameBoard[row][col] + ">" + "|");
                } else {
                    System.out.print(gameBoard[row][col] + "  |");
                } // else
            } // for
            System.out.println();
        } // for
        System.out.print("    ");
        for (int col = 0; col < gameBoard[0].length; col++) {
            System.out.print(col + "   ");
        } // for
        System.out.println();
        System.out.print("minesweeper-alpha: ");
        this.control = 1;
    } // nofog

    /**
     * Indicates whether or not the square is in the game grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return true if the square is in the game grid; false otherwise
     */
    private boolean isInBounds(int row, int col) {
        if (row < 0 || row >= gameBoard.length) {
            return false;
        } else if (col < 0 || col >= gameBoard[0].length) {
            return false;
        } else {
            return true;
        } // else
    } // isInBounds

    /**
     * Return the number of mines adjacent to the specified
     * square in the grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return the number of adjacent mines
     */
    private int getNumAdjMines(int row, int col) {
        int numOfMines = 0;
        if (this.isInBounds(row + 1, col) && mineBoard[row + 1][col] == true) {
            numOfMines++;
        } // if
        if (this.isInBounds(row - 1, col) && mineBoard[row - 1][col] == true) {
            numOfMines++;
        } // if
        if (this.isInBounds(row, col - 1) && mineBoard[row][col - 1] == true) {
            numOfMines++;
        } // if
        if (this.isInBounds(row, col + 1) && mineBoard[row][col + 1] == true) {
            numOfMines++;
        } // if
        if (this.isInBounds(row - 1, col - 1) && mineBoard[row - 1][col - 1] == true) {
            numOfMines++;
        } // if
        if (this.isInBounds(row - 1, col + 1) && mineBoard[row - 1][col + 1] == true) {
            numOfMines++;
        } // if
        if (this.isInBounds(row + 1, col - 1) && mineBoard[row + 1][col - 1] == true) {
            numOfMines++;
        } // if
        if (this.isInBounds(row + 1, col + 1) && mineBoard[row + 1][col + 1] == true) {
            numOfMines++;
        } // if
        return numOfMines++;
    } // getNumAdjMines

    /**
     * checks to see if the file is malformed. It will print out a error if the bounds
     * for the board are not between 5 and 10. It will print out a error if there isn't enough
     * mines or too many. It will print a error if there is too many mine coordinates or too little.
     * It will print a error if there is something other then integer values or if the file is
     * not found.
     *
     * @param file the file that is being checked for errors.
     */
    public void checkSeedFile(File file) {
        try {
            Scanner testScanner = new Scanner(file);
            int fileError = 0;
            int controlError = 0; //if there is a error this makes sure the right message is printed
            int seedRow = Integer.parseInt(testScanner.next());
            int seedCol = Integer.parseInt(testScanner.next());
            if (seedRow < 5 || seedRow > 10) {
                fileError = 1;
                controlError = 1;
            } // if
            if (seedCol < 5 || seedCol > 10) {
                fileError = 1;
                controlError = 1;
            } // if
            int mines = Integer.parseInt(testScanner.next());
            if (mines < 1 || mines > ((seedRow * seedCol) - 1) && controlError != 1) {
                fileError = 2;
                controlError = 1;
            } // if
            int i = 0;
            // checks to see if there is the right number of mine coordinates.
            while (mines > i) {
                int mineRow = Integer.parseInt(testScanner.next());
                int mineCol = Integer.parseInt(testScanner.next());
                if (mineRow < 0 || mineRow >= seedRow && controlError != 1) {
                    fileError = 3;
                    controlError = 1;
                } // if
                if (mineCol < 0 || mineCol >= seedCol && controlError != 1) {
                    fileError = 3;
                    controlError = 1;
                } // if
                i++;
            } // while
            if (testScanner.hasNext() && controlError != 1) {
                fileError = 3;
            } // if
            this.seedFileMalformedError(fileError);
        } catch (NoSuchElementException nsee) {
            System.err.println();
            System.err.println("Seed File Malformed Error: file is missing elements");
            System.exit(3);
        } catch (FileNotFoundException fnfe) {
            System.err.println();
            System.err.println("Seed File Not Found Error: " + fnfe.getMessage());
            System.exit(2);
        } catch (NumberFormatException nfe) {
            System.err.println();
            System.err.println("Seed File Malformed Error: file must contain integer values");
            System.exit(3);
        } // try
    } // checkSeedFile

    /**
     * prints the error message depending on the error.
     *
     * @param errorNum The number that determines what the error is.
     */
    private void seedFileMalformedError(int errorNum) {
        if (errorNum == 1) {
            System.err.println();
            System.err.println("Seed File Malformed Error: " +
                "Cannot create a mine field with that many rows and/or columns!");
            System.exit(3);
        } // if
        if (errorNum == 2) {
            System.err.println();
            System.err.println("Seed File Malformed Error: number of mines" +
                " must be between 1 and (row*col)-1");
            System.exit(3);
        } // if
        if (errorNum == 3) {
            System.err.println();
            System.err.println("Seed File Malformed Error: must have the"
                + " right number of coordinates");
            System.exit(3);
        } // if
    } // seedFileMalformedError
} //MinesweeperGame
