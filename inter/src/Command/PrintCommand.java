package Command;

import Interpeter.Token;

public class PrintCommand extends Command {

	public static final int numOfArgs = Integer.MAX_VALUE;
	
	private Command[] args;
	
	public PrintCommand(Command[] args) {
		this.args = args;
	}
	
	@Override
	public Token execute() throws Exception {
		for(Command arg: args) {
			System.out.print(arg.execute().asString() + " ");
		}
		System.out.println("");
		return new Token();
	}

	@Override
	public void validate() throws Exception {}

}
