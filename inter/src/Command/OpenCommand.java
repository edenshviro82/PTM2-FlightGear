package Command;

import Interpeter.Token;

public class OpenCommand extends Command {

	public static final int numOfArgs = 2;
	
	private Command ip;
	private Command port;
	
	public OpenCommand(Command ip, Command port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	public Token execute() throws Exception {
		String ip = this.ip.execute().asString();
		int port = (int)Math.floor(this.port.execute().asDouble());
		System.out.println("Opened tcp connection to " + ip + ":" + port);
		return new Token();
	}

	@Override
	public void validate() throws Exception {
		try{
			this.port.execute().asDouble();
		}catch(Exception e) {
			throw new Exception("Invalid port value for Open command  " + port + ". " + e.getMessage());
		}
		
		try{
			this.ip.execute().asString();
		}catch(Exception e) {
			throw new Exception("Invalid ip value for Open command  " + ip + ". " + e.getMessage());
		}
	}

}
