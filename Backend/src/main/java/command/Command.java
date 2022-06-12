package command;

import java.io.IOException;

public interface Command {
    void execute(String input) throws IOException, ClassNotFoundException;
}
