package lasers.ptui;

import lasers.model.LasersModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author RIT CS
 * @author YOUR NAME HERE
 */
public class ControllerPTUI {
    /**
     * The UI's connection to the lasers.lasers.model
     */
    private LasersModel model;

    private LasersPTUI view;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     *
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     *
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) {
        try (Scanner userIn = new Scanner(System.in)) {

            System.out.flush();
            System.out.print("cmd> ");
            while (userIn.hasNextLine()) {
                String input = userIn.nextLine();
                ArrayList<String> inputList = new ArrayList<String>(Arrays.asList(input.split(" ")));
                if (inputList.get(0).substring(0, 1).equals("a")) {
                    try {
                        int row = Integer.parseInt(inputList.get(1));
                        int col = Integer.parseInt(inputList.get(2));
                        this.model.add(row, col);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.print("Illegal command, or incorrect input\n");
                    }
                } else if (inputList.get(0).substring(0, 1).equals("d")) {
                    update(this.model, null);//todo fix
                } else if (inputList.get(0).substring(0, 1).equals("h")) {
                    this.model.help();
                } else if (inputList.get(0).substring(0, 1).equals("q")) {
                    break;
                } else if (inputList.get(0).substring(0, 1).equals("r")) {
                    try {
                        int row = Integer.parseInt(inputList.get(1));
                        int col = Integer.parseInt(inputList.get(2));
                        this.model.remove(row, col);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.print("Illegal command, or incorrect input\n");
                    }
                } else if (inputList.get(0).substring(0, 1).equals("v")) {
                    this.model.verify();
                } else {
                    System.out.print("UNKNOWN COMMAND\n");

                }
                System.out.flush();

                System.out.print("cmd> ");

            }
        }
    }
    public void display (LasersModel safe) {

        System.out.print("  ");
        for(int c = 0; c < safe.getCols(); c++){
            System.out.print(c + " ");
        }
        System.out.print("\n  ");
        for(int i = 0; i < safe.getCols(); i++){
            System.out.print("--");
        }
        System.out.print("\n");
        for(int r = 0; r < safe.getRows(); r++){
            StringBuilder builder = new StringBuilder();
            for (int c= 0; c < safe.getCols();c++){
                builder.append(safe.getTheGrid()[r][c]);
                builder.append(" ");
            }
            System.out.print(r%10+"|"+builder.toString()+"\n");
        }
        System.out.print(this.model.getStatus()+"\n");
    }
}
