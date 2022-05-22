package Interpeter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import Command.Command;
import Command.CommandFactory;
import Command.ConstantValue;
import Expression.Expression;
import Expression.ExpressionFactory;

public class Parser {

	private final Lexer lexer;
	private final ExpressionFactory expsFactory;
	private final CommandFactory cmdsFactory;
	private final Map<String, Token> symbolTable;
	public Parser(Lexer lexer, ExpressionFactory expsFactory, CommandFactory cmdsFactory, Map<String, Token> symbolTable) {
		this.lexer = lexer;
		this.expsFactory = expsFactory;
		this.cmdsFactory = cmdsFactory;
		this.symbolTable = symbolTable;
	}

	public boolean isDouble(String token) {
		try {
			Double.parseDouble(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected Command[] getArgsForCommand(String cmdName, Stack<Command> stack) throws Exception {
		// if stack.size() < numOfArgs -> Exception invalid expression
		int numOfArgs = cmdsFactory.getCommandNumOfArgs(cmdName);
		
		if(numOfArgs < Integer.MAX_VALUE && stack.size() < numOfArgs)
			throw new Exception("Command " + cmdName + " expected " + numOfArgs + ", recived " + stack.size());
		
		List<Command> args = new LinkedList<>();
		for (int i = 0; i < numOfArgs && !stack.isEmpty(); i++) {
			Command arg = stack.pop();
			
			if(cmdName != "Var")
				arg = symbolResolv(arg);
			
			args.add(arg);
		}
		
		return args.toArray(new Command[0]);
	}

	protected void validateAndPushCommand(Stack<Command> stack, Command cmd) throws Exception {
		cmd.validate();
		stack.push(cmd);
	}
	
	protected Token symbolResolv(Token key) {
		if(!key.isContainsValue())
			return key;
		if(key.isDouble())
			return key;
		return symbolTable.getOrDefault(key.asString(), key);
	}
	
	protected Command symbolResolv(Command cmd) throws Exception {
		if(!(cmd instanceof ConstantValue))
			return cmd;
		
		return new ConstantValue(symbolResolv(cmd.execute()));
	}
	
	public Command parse(String line) throws Exception {

		// Set the lexer date (code)
		lexer.setData(line);

		Stack<Command> cmds = new Stack<>();
		List<Token> tokens = lexer.getAllTokens();

		// Every command is prefix - we would like to use it as a postfix.
		Collections.reverse(tokens); 

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			if (!token.isContainsValue())
				throw new Exception("Invalid token");
			
			String tokenStr = token.asString();
			// Replace symbols			
			if (cmdsFactory.isCommandExists(tokenStr)) {
				// The token is command
				Command[] args = getArgsForCommand(tokenStr, cmds);
				Command cmd = cmdsFactory.getCommand(tokenStr, args);
				validateAndPushCommand(cmds, cmd);
				continue;
			}

			if (expsFactory.isExpressionExists(tokenStr)) {
				// We support only binary expressions.
				if(cmds.size() < 2)
					throw new Exception("Expression " + tokenStr + " expected 2 arguments" + ", recived " + cmds.size());
				
				Command left = symbolResolv(cmds.pop());
				Command right = symbolResolv(cmds.pop());
				
				Expression exp = expsFactory.getExpression(tokenStr, left, right);
				Command cmd = cmdsFactory.wrapExpression(exp);
				validateAndPushCommand(cmds, cmd);
				continue;
			}
			
			// Probably it is just a constant value (String / Double)
			Command cmd = new ConstantValue(token);
			validateAndPushCommand(cmds, cmd);
		
		}

		if(cmds.size() != 1)
			throw new Exception("Invalid syntax");
		return cmds.pop();
	}

}
