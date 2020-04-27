package lasers.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * The model of the lasers safe.  You are free to change this class however
 * you wish, but it should still follow the MVC architecture.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class LasersModel {
    /** the observers who are registered with this model */
    private List<Observer<LasersModel, ModelData>> observers;

    private ArrayList<Laser> laserList = new ArrayList<>();
    private ArrayList<Pillar> pillarList = new ArrayList<>();

    private int rows;
    private int cols;
    private String[][] theGrid;
    private String status;
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
    }    /**
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

            if (laserCount != this.adjacentLaserCount && this.adjacentLaserCount != 9){
                isGood = false;
            }
            return isGood;
        }
    }
    /**
     * constructor for safe (1 parameter)
     * @param filename the mapname
     */
    public LasersModel(String filename) throws FileNotFoundException {
        initSafe(filename);
    }
    /**
     *builds a Safe(2 parameters)
     * @param mapFile the mapFile
     * @param autoPlacement the file of inputs
     */
    public LasersModel(String mapFile, String autoPlacement){
        initSafe(mapFile);
        try{
            File inputFile = new File(autoPlacement);
            Scanner myReader2= new Scanner(inputFile);
            while (myReader2.hasNextLine()){
                String data = myReader2.nextLine();
                String[] dataList = data.split(" ");
                add(Integer.parseInt(dataList[1]),Integer.parseInt((dataList[2])));
            }
        }catch (FileNotFoundException e){
            System.out.print("File cannot be found");
            e.printStackTrace();
            System.exit(0);
        }
    }
    /**
     * builds a grid based off a mapFile
     * @param mapFile the mapFile
     */
    private void initSafe(String mapFile) {
        int MAGIC_NUMBER = 9;
        this.observers = new LinkedList<>();
        int counter = 0;
        File file = new File(mapFile);
        Scanner myReader = null;
        try{
            myReader = new Scanner(file);
            String dimensions =  myReader.nextLine().toString();
            String[] dimGet = dimensions.split(" ");

            this.rows = Integer.parseInt(dimGet[0]);
            this.cols = Integer.parseInt(dimGet[1]);
            //this.gridRow = new ArrayList<>();
            this.theGrid  = new String[this.rows][this.cols];

            int r = 0 ;
            while (myReader.hasNextLine() && r != this.rows) {
                String data = myReader.nextLine();
                String[] dataList = data.split(" ");

                for (int c = 0; c < this.cols; c++) {
                    this.theGrid[r][c] = dataList[c];
                    if (this.theGrid[r][c].equals("0")||
                            this.theGrid[r][c].equals("1")||
                            this.theGrid[r][c].equals("2")||
                            this.theGrid[r][c].equals("3")||
                            this.theGrid[r][c].equals("4")){
                        pillarList.add(new Pillar(r,c,Integer.parseInt(dataList[c])));
                    }else if (this.theGrid[r][c].equals("X")){
                        pillarList.add(new Pillar(r,c,MAGIC_NUMBER));
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
        }
    }

    /**
     * Add a new observer.
     *
     * @param observer the new observer
     */
    public void addObserver(Observer<LasersModel, ModelData > observer) {
        this.observers.add(observer);
    }

    /**
     * Notify observers the model has changed.
     *
     * @param data optional data the model can send to the view
     */
    private void notifyObservers(ModelData data){
        for (Observer<LasersModel, ModelData> observer: observers) {
            observer.update(this, data);
        }
    }

    /**
     * gets a message and returns it
     * @return the status message
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * gets the columns of a grid
     * @return columns
     */
    public int getCols() {
        return this.cols;
    }
    /**
     * gets the rows of a grid
     * @return rows
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * gets the grid
     * @return the grid
     */
    public String[][] getTheGrid() {
        return this.theGrid;
    }
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
        for (int r  = row ; r < this.rows ; r++) {
            if ( r + 1 < this.rows  && (theGrid[r+1][col].equals("*") || theGrid[r+1][col].equals("."))) {
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
        for (int c  = col ; c < this.cols ; c++) {
            if ( c + 1 < this.cols  && (theGrid[row][c + 1].equals("*") || theGrid[row][c + 1].equals("."))) {
                theGrid[row][c + 1] = replaceWith;
            }

            else{
                break;
            }
        }
    }

    /**
     * adds lasers
     * @param row the row
     * @param col the col
     */
    public void add(int row, int col){
        this.theGrid[row][col] = "L";
        laserBeamChange(row,col, "ADD");
        laserList.add( new Laser(row, col));
        this.status = "Laser added at (" + row +", " +col+ ")";
        notifyObservers(null);
    }

    /**
     * creates a help message
     */
    public void help(){
        this.status= "help:\n" +
                "a (row, col) = add a laser to (row, col)\n" +
                "d = display the board\n" +
                "h = show this help message\n" +
                "q = quit application\n" +
                "r (row, col) = remove a laser from (row, col)\n" +
                "v = verify\n";
        notifyObservers(null);
    }

    /**
     * removes a laserBeam
     * @param row the row
     * @param col the col
     */
    public void remove(int row, int col){
        this.theGrid[row][col] = ".";
        this.laserBeamChange(row, col, "REMOVE");
        laserList.removeIf(l -> l.getCol() == col && l.getRow() == row);
        for(Laser l:laserList){
            laserBeamChange(l.getRow(),l.getCol(),"Add");
        }
        this.status = "Laser removed at (" + row +", " +col+ ")";
        notifyObservers(null);
    }

    /**
     * tests the safety of the grid
     */
    public void verify(){
        //laser testing
        this.status = "lasers.model.Safe is fully verified!";
        for (Laser l :laserList){
            if(!l.isValid(this.theGrid)){
                this.status = "Error located at ("+ l.getRow() +","+ l.getCol() +")";
                break;
            }
        }
        for (Pillar p :pillarList){
            if(!p.isValid(this.theGrid)){
                this.status = "Error located at ("+ p.getRow() +","+ p.getCol() +")";
                break;
            }
        }
        notifyObservers(null);
    }
}
