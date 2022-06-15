package view;

import command.Command;

import java.util.Observable;

public class View extends Observable  implements viewIF{

    String selection =null;



    @Override
    public void shutDown() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void listOfTasks() {

    }

    @Override
    public void listOfThreds() {

    }

    @Override
    public void listOfActiveAgents() {

    }
}
