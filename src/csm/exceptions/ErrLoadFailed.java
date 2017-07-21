package csm.exceptions;

public class ErrLoadFailed extends CSMEditException {

	public final CSMEditException cause;
	public final int id;

	public ErrLoadFailed(int id, CSMEditException cause) {
		super("in component " + id + ": " + cause.getMessage());
		this.cause = cause;
		this.id = id;
	}

	public ErrLoadFailed(int id, String cause) {
		super("in component " + id + ": " + cause);
		this.cause = null;
		this.id = id;
	}
}
