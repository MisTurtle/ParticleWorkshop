package particleworkshop.common.exception;

public class ObjectSerializationException extends Exception {

	private static final long serialVersionUID = -3973801356346127294L;

	public ObjectSerializationException(Exception e)
	{
		super(e.getMessage());
	}
	
}
