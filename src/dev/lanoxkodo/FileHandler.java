package dev.lanoxkodo;

public class FileHandler {

	private VersaLeash versaleash;
	
	FileHandler(VersaLeash vl)
	{
		versaleash = vl;
		versaleash.saveDefaultConfig();
	}
	
	protected boolean isEnabled(String property)
	{
		return versaleash.getConfig().getBoolean(property);
	}
}
