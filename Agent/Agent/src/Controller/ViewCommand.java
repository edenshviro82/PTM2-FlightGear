package Controller;

import java.io.IOException;

public abstract class ViewCommand implements Command {
    String name;

    public ViewCommand(String name) {
        this.name = name;
    }

    public abstract void execute(String text) throws IOException;
}
