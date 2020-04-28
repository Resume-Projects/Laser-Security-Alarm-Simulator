package lasers.ptui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import lasers.model.LasersModel;
import lasers.model.ModelData;
import lasers.model.Observer;

/**
 * This class represents the view portion of the plain text UI.  It
 * is initialized first, followed by the controller (ControllerPTUI).
 * You should create the model here, and then implement the update method.
 *
 * @author Sean Strout @ RIT CS
 * @author rl2939@g.rit.edu
 */
public class LasersPTUI implements Observer<LasersModel, ModelData> {
    /** The UI's connection to the model */
    private LasersModel model;
    private ControllerPTUI controller;

    /**
     * Construct the PTUI.  Create the lasers.lasers.model and initialize the view.
     * @param filename the safe file name
     * @throws FileNotFoundException if file not found
     */
    public LasersPTUI(String filename) throws FileNotFoundException {
        try {
            System.out.print(filename);
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
        this.controller = new ControllerPTUI(this.model);
    }

    /**
     * Accessor for the model the PTUI create.
     *
     * @return the model
     */
    public LasersModel getModel() { return this.model; }

    @Override
    public void update(LasersModel model, ModelData data) {
        display(model);
    }



    /**
     * displays the output of the model
     * @param safe safe
     */
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
