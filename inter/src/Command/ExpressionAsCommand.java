package Command;

import Expression.Expression;
import Interpeter.Token;

public class ExpressionAsCommand extends Command {

	public static final int numOfArgs = 1;
	private Expression exp;
	
	public ExpressionAsCommand(Expression exp) {
		this.exp = exp;
	}
	
	@Override
	public Token execute() throws Exception {
		Token returnValue = new Token(this.exp.calculate());
		return returnValue;
	}

	@Override
	public void validate() throws Exception {
		try {
			this.exp.calculate();
		}catch(Exception e) {
			throw new Exception("Given Expression is invalid");
		}
	}

}
