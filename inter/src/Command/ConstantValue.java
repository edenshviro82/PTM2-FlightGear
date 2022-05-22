package Command;

import Interpeter.Token;

public class ConstantValue extends Command{

	private Token value;
		
	public ConstantValue(Token value) {
		this.value = value;
	}
	
	@Override
	public Token execute() throws Exception {
		return value;
	}

	@Override
	public void validate() throws Exception {}

}
