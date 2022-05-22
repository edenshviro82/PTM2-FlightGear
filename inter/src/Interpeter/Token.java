package Interpeter;

public class Token {
	
	private String value;
	
	public Token(String value) {
		this.value = value;
	}
	
	public Token(Double value) {
		this.value = value.toString();
	}
	
	public Token() {
		// Can be use for returning void value
		this.value = null;
	}
	
	public boolean isContainsValue() {
		return this.value != null;
	}
	
	public boolean isDouble() {
		try {
			Double.parseDouble(value);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public double asDouble() throws NumberFormatException, NullPointerException {
		return Double.parseDouble(this.value);
	}
	
	public String asString() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return value != null ? value.toString() : "null";
	}
	
}
