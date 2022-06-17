package view;

import command.Command;
import controller.Controller;
import model.Model;

import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
