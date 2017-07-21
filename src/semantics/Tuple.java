package semantics;

/*
 * allgemeiner Tupeltyp
 */
public class Tuple<L, R> {
	public final L l;

	public final R r;

	Tuple(L l, R r) {
		this.l = l;
		this.r = r;
	}
}
