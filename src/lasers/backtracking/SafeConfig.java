package lasers.backtracking;

import lasers.model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the lasers.model
 * package and/or incorporate it into another class.
 *
 * @author RIT CS
 * @author Rl2939@g.rit.edu
 */
public class SafeConfig implements Configuration {

    //Total number of rows and columns
    private int maxRows;
    private int maxCols;
    //row and col for the cursor placement
    private int cursorRow;
    private int cursorCol;
    private String[][] theGrid;
    private ArrayList<SafeConfig.Pillar> pillarList = new ArrayList<>();
    private ArrayList<SafeConfig.Laser> laserList = new ArrayList<>();
    private String filename;
    /**
     * Adds a laser
     */
    public class Laser{
        private int row;
        private int col;


        /**
         * Details the laser coordinates
         * @param row the row
         * @param col the column
         */
        public Laser(int row,int col){
            this.row = row;
            this.col = col;
        }

        /**
         * gets the laser row
         * @return the row
         */
        public int getRow() {
            return this.row;
        }

        /**
         * gets the laser col
         * @return the col
         */
        public int getCol() {
            return this.col;
        }

        /**
         * finds if lasers collide with each other
         * @param theGrid the grid
         * @return true or false (pass or fail)
         */
        public boolean isValid(String[][] theGrid) {
            boolean isGood = true;

            int r = this.row;
            int c = this.col;

            // go up until  hit wall or pillar
            while ( isGood &&  r - 1 >= 0  && (theGrid[r-1][col].equals("*") ||
                    theGrid[r-1][col].equals(".") ||
                    theGrid[r-1][col].equals("L"))) {
                isGood = !theGrid[r-1][c].equals("L");
                r--;
            }
            // go down
            r=this.row;
            while ( isGood && r + 1 < theGrid[0].length && (theGrid[r+1][col].equals("*") ||
                    theGrid[r+1][col].equals(".") ||
                    theGrid[r+1][col].equals("L"))) {
                isGood = !theGrid[r+1][c].equals("L");

                r++ ;
            }
            // go left
            while ( isGood && c - 1 >= 0  && (theGrid[row][c-1].equals("*") ||
                    theGrid[row][c-1].equals(".") ||
                    theGrid[row][c-1].equals("L"))) {
                isGood = !theGrid[r][c-1].equals("L");
                c--;
            }

            // go right
            c=this.col;
            while ( isGood && c + 1 < theGrid[1].length  && (theGrid[row][c+1].equals("*") ||
                    theGrid[row][c+1].equals(".") ||
                    theGrid[row][c+1].equals("L"))) {
                isGood = !theGrid[r][c+1].equals("L");
                c++;
            }
            return isGood;
        }
    }
    /**
     * Adds a Pillar
     */
    public class Pillar{
        private int row;
        private int col;
        private int adjacentLaserCount;

        /**
         * Details the Pillar coordinates
         * @param row the row
         * @param col the column
         * @param adjacentLaserCount number of required adjacent lasers
         */
        public Pillar(int row,int col, int adjacentLaserCount){
            this.row = row;
            this.col = col;
            this.adjacentLaserCount = adjacentLaserCount;
        }

        /**
         * gets the laser row
         * @return the row
         */
        public int getRow() {
            return this.row;
        }

        /**
         * gets the laser col
         * @return the col
         */
        public int getCol() {
            return this.col;
        }

        /**
         * gets the number of adjacent lasers
         * @return a number
         */
        public int getAdjacentLaserCount() {
            return this.adjacentLaserCount;
        }

        /**
         * checks to see if the pillars are powered
         * @param theGrid the grid
         * @return true or false statement (pass or fail)
         */
        public boolean isValid(String[][] theGrid) {
            boolean isGood = true;
            int laserCount = 0;
            //if space above, add counter
            if (this.row != 0 && theGrid[this.row-1][this.col].equals("L")){
                laserCount++;
            }
            //space below
            if (this.row != theGrid[0].length-1 && theGrid[this.row+1][this.col].equals("L")){
                laserCount++;
            }
            //space to the left
            if (this.col != 0 && theGrid[this.row][this.col-1].equals("L")){
                laserCount++;
            }
            //space to the right
            if (this.col != theGrid[1].length-1 && theGrid[this.row][this.col+1].equals("L")){
                laserCount++;
            }
            if (this.row == theGrid[0].length-1 && this.col == theGrid[1].length-1 ){
                if (laserCount != this.adjacentLaserCount && this.adjacentLaserCount != 9){
                    isGood = false;
                }
            }else{
                if ((laserCount > this.adjacentLaserCount) && (this.adjacentLaserCount != 9)) {
                    isGood = false;
                }
            }
            return isGood;
        }
    }

    /**
     * The public safe config
     * @param filename the file name
     */
    public SafeConfig(String filename) {
        int MAGIC_NUMBER = 9;
        int counter = 0;
        File file = new File(filename);
        Scanner myReader = null;
        try{
            myReader = new Scanner(file);
            String dimensions =  myReader.nextLine().toString();
            String[] dimGet = dimensions.split(" ");

            this.maxRows = Integer.parseInt(dimGet[0]);
            this.maxCols = Integer.parseInt(dimGet[1]);
            //this.gridRow = new ArrayList<>();
            this.theGrid  = new String[this.maxRows][this.maxCols];

            int r = 0 ;
            while (myReader.hasNextLine() && r != this.maxRows) {
                String data = myReader.nextLine();
                String[] dataList = data.split(" ");

                for (int c = 0; c < this.maxCols; c++) {
                    this.theGrid[r][c] = dataList[c];
                    if (this.theGrid[r][c].equals("0")||
                            this.theGrid[r][c].equals("1")||
                            this.theGrid[r][c].equals("2")||
                            this.theGrid[r][c].equals("3")||
                            this.theGrid[r][c].equals("4")){
                        pillarList.add(new SafeConfig.Pillar(r,c,Integer.parseInt(dataList[c])));
                    }else if (this.theGrid[r][c].equals("X")){
                        pillarList.add(new SafeConfig.Pillar(r,c,MAGIC_NUMBER));
                    }
                }
                r++;
            }
        }
        catch(FileNotFoundException e){
            System.out.print("File cannot be found");
            e.printStackTrace();
            System.exit(0);
        }
        finally {
            myReader.close();
            //set cursor to be off the board initially
            this.cursorRow = 0;
            this.cursorCol = 0;
        }

    }

    /**
     * the copy constructor
     * @param other the public safeConfig
     * @param cursorRow the row
     * @param cursorCol the col
     * @param item the string
     */
    private SafeConfig(SafeConfig other, int cursorRow, int cursorCol, String item){
        this.cursorRow = cursorRow;
        this.cursorCol = cursorCol;

        this.maxRows = other.maxRows;
        this.maxCols = other.maxCols;
        this.theGrid = new String[this.maxRows][this.maxCols];
        this.laserList.addAll(other.laserList);
        this.pillarList.addAll(other.pillarList);
        for (int r = 0; r< this.maxRows; r++){
            System.arraycopy(other.theGrid[r],0,this.theGrid[r],0, this.maxCols);
        }

        if (this.theGrid[this.cursorRow][this.cursorCol].equals(".") ||
                this.theGrid[this.cursorRow][this.cursorCol].equals("*")){
            this.theGrid[this.cursorRow][this.cursorCol] = item;
            if (item.equals("L")){
                this.laserList.add(new Laser(this.cursorRow,this.cursorCol));
                laserBeamChange(this.cursorRow,this.cursorCol,"ADD");
            }
        }
    }

    /**
     * creates a list of successors
     * @return a list of successors
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        List<Configuration> successors = new LinkedList<Configuration>();
        int nextRow = this.cursorRow;
        int nextCol = this.cursorCol+1;
        if (nextCol == maxCols){
            nextCol = 0;
            nextRow++;
        }

        SafeConfig child = new SafeConfig(this, nextRow, nextCol,".");
        successors.add(child);
        if (this.theGrid[this.cursorRow][this.cursorCol].equals(".") ||
                this.theGrid[this.cursorRow][this.cursorCol].equals("*")){
            SafeConfig child2 = new SafeConfig(this, nextRow, nextCol,"L");
            successors.add(child2);
        }
        return successors;
    }

    /**
     * Finds if the configuration is valid
     * @return true or false
     */
    @Override
    public boolean isValid() {

        //laser testing
        //this.status = "lasers.model.Safe is fully verified!";
        for (SafeConfig.Laser l :laserList){
            if(!l.isValid(this.theGrid)){
                //this.status = "Error located at ("+ l.getRow() +","+ l.getCol() +")";
                return false;
            }
        }
        for (SafeConfig.Pillar p :pillarList){
            if(!p.isValid(this.theGrid)){
                //this.status = "Error located at ("+ p.getRow() +","+ p.getCol() +")";
                return false;
            }
        }
        return true;
    }

    /**
     * the goal of the backTracker
     * @return true or false
     */
    @Override
    public boolean isGoal() {
        //  goal is true when cursor is in last row and column
        return this.cursorCol == this.maxCols-1 && this.cursorRow == this.maxRows-1 && this.isValid();
    }

    /**
     * Displays laserBeams
     * @param row row
     * @param col col
     * @param addOrRemove string
     */
    public void laserBeamChange(int row, int col, String addOrRemove){

        String replaceWith = "*" ;
        // Put L in the coordinates.
        if (addOrRemove.toUpperCase().equals("REMOVE")){
            replaceWith = "." ;
        }
        // go up
        for (int r  = row ; r >= 0 ; r--) {
            if ( r - 1 >= 0  && (theGrid[r-1][col].equals("*") || theGrid[r-1][col].equals(".")))
            {
                theGrid[r-1][col] = replaceWith;
            }
            else{
                break;
            }
        }
        // go down
        for (int r  = row ; r < this.maxRows ; r++) {
            if ( r + 1 < this.maxRows  && (theGrid[r+1][col].equals("*") || theGrid[r+1][col].equals("."))) {
                theGrid[r + 1][col] = replaceWith;
            }
            else{
                break;
            }
        }
        // go left
        for (int c  = col ; c >= 0 ; c--) {
            if ( c - 1 >= 0  && (theGrid[row][c-1].equals("*") || theGrid[row][c-1].equals("."))) {
                theGrid[row][c - 1] = replaceWith;
            }
            else{
                break;
            }
        }
        // go right
        for (int c  = col ; c < this.maxCols ; c++) {
            if ( c + 1 < this.maxCols  && (theGrid[row][c + 1].equals("*") || theGrid[row][c + 1].equals("."))) {
                theGrid[row][c + 1] = replaceWith;
            }

            else{
                break;
            }
        }
    }
}
