package com.company;

import java.util.List;

public class Commands {

    // the shared state of all commands
    private class SharedState {

    }

    DefaultIO dio;
    private SharedState sharedState = new SharedState();

    public class setAileron extends Command {

        @Override
        public void execute() {

        }
    }
}
