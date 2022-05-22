package Command;

import java.util.HashMap;
import java.util.Map;

import Expression.Expression;
import Interpeter.Token;

public class CommandFactory {

	private final Map<String, CommandHandler> cmds = new HashMap<>();
	private final CommandHandler openHandler;
	private final CommandHandler printHandler;
	private final CommandHandler varHandler;

	private static class CommandHandler {
		public final CommandCreator creator;
		public final int numOfArgs;
		
		public CommandHandler(CommandCreator creator, int numOfArgs) {
			this.creator = creator;
			this.numOfArgs = numOfArgs;
		}
	}
	
	private static interface CommandCreator {
		public Command create(Command[] args);
	}
	
	public CommandFactory(Map<String, Token> symbolTable) {		
		varHandler = new CommandHandler((args) -> new VarCommand(symbolTable, args[0], args[1]), VarCommand.numOfArgs);
		openHandler = new CommandHandler((args) -> new OpenCommand(args[0], args[1]), OpenCommand.numOfArgs);
		printHandler = new CommandHandler((args) -> new PrintCommand(args), PrintCommand.numOfArgs);
		
		cmds.put("Open", openHandler);
		cmds.put("Print", printHandler);
		cmds.put("Var", varHandler);

	}
	
	public boolean isCommandExists(String name) {
		return cmds.containsKey(name);
	}
	
	public int getCommandNumOfArgs(String name) throws Exception {
		if(!cmds.containsKey(name))
			throw new Exception("Invalid command " + name);
		
		return cmds.get(name).numOfArgs;
	}
	public Command getCommand(String name, Command[] args) throws Exception {
		if(!cmds.containsKey(name))
			throw new Exception("Invalid command " + name);
		
		return cmds.get(name).creator.create(args);
	}
	
	public Command wrapExpression(Expression exp) {
		return new ExpressionAsCommand(exp);
	}
	
}
