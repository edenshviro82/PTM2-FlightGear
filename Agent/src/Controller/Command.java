package Controller;

import java.io.IOException;

public interface Command {
    public void execute(String input) throws IOException, ClassNotFoundException;
}
