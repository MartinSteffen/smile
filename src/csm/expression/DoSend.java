package csm.expression;

import java.util.Set;

import csm.ExpressionEnvironment;


public final class DoSend extends Action {

	public final String event;
	public final Expression<Integer> value;

	public DoSend(String event, Expression<Integer> value) {
		assert event != null;
		assert value != null;

		this.event = event;
		this.value = value;
	}

	@Override
	public String prettyprint() {
		return "send (" + this.event + ", " + this.value.prettyprint()
			+ ')';
	}

	public String firstUndefinedVar(Set<String> dict) {
		return this.value.firstUndefinedVar(dict);

	}

	@Override
	public final ExpressionEnvironment[] evaluate(ExpressionEnvironment pre) {
		final ExpressionEnvironment post = new ExpressionEnvironment(pre);
		post.sendEventName = this.event;
		post.sendEventValue = this.value.evaluate(post);
		ExpressionEnvironment[] result = new ExpressionEnvironment[1];
		result[0] = post;
		return result;
	}

}
