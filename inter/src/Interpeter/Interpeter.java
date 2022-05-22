package Interpeter;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import Command.Command;
import Command.CommandFactory;
import Expression.ExpressionFactory;

public class Interpeter {

	public static void run(Queue<Command> cmds) throws Exception {
		while(!cmds.isEmpty())
			cmds.poll().execute().asString();
	}
	
	public static Queue<Command> getCommands(File code) throws Exception {
		/*
		 * We assume each line is a command. each token separate by whitespace
		 */
		Map<String, Token> symbolTable = new HashMap<>();
		Parser p = new Parser(new Lexer(), new ExpressionFactory(), new CommandFactory(symbolTable), symbolTable);
		Scanner in = new Scanner(code);
		Queue<Command> cmds = new LinkedList<>();
		
		for(int index=1;in.hasNextLine();index++) {
			String line = in.nextLine().trim();
			
			if(line.length() == 0)
				continue;
			
			try {
				Command cmd = p.parse(line);
				cmds.add(cmd);
			} catch (Exception e) {
				in.close();
				throw new Exception("[ERROR] at line " + index + ": " + e.getMessage());
			}
		}
		
		in.close();
		
		return cmds;
		
		
	}
	
	public static void main(String[] args) {
		
		try {
			Queue<Command> cmds = getCommands(new File("./src/resources/code.ptm"));
			run(cmds);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
}
