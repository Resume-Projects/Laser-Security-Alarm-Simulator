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
 * @author rl2939@g.rit.edu
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
        this.model.input(inputFile);
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
                    this.model.display();
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
}
