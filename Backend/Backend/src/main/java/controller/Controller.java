package controller;
import command.Command;
import model.Model;
import view.View;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Observer {
    Model m ;
    View v ;
    HashMap<String,Command> commandMap;
    Commands c ;
    ExecutorService es ;
    private boolean stop ;
    public Controller(Model m, View v){
        this.m=m;
        m.addObserver(this);
        this.v=v;
        v.addObserver(this);
        this.es = Executors.newFixedThreadPool(5);
        this.c = new Commands(m,v);
    }
    @Override
    public void update(Observable o, Object arg) {

    }

}
