package Command;

import java.util.Map;

import Interpeter.Token;

public class VarCommand extends Command {
	
	public static final int numOfArgs = 2;

	private Map<String, Token> symbolTable;
	private Command key;
	private Command value;
	
	public VarCommand(Map<String, Token> symbolTable, Command key, Command value) {
		this.value = value;
		this.key = key;
		this.symbolTable = symbolTable;
	}
	
	@Override
	public Token execute() throws Exception {
		Token v = value.execute();
		String k = key.execute().asString();
		symbolTable.put(k, v);
		return v;
	}

	@Override
	public void validate() throws Exception {
		String k = key.execute().asString();
		if(symbolTable.containsKey(k))
			throw new Exception("Redefine symbol " + k);
		
		try {
			value.execute().asString();
		}catch (Exception e) {
			throw new Exception("Trying to assign invalid value to symbol " + k);
		}
		
		this.execute();
	}

}
