package Controller;

import java.io.IOException;
import java.io.OutputStream;

public abstract class ViewCommand implements Command {
    String name;

    public ViewCommand(String name) {
        this.name = name;
    }

    public abstract void execute(String text) throws IOException;
}
