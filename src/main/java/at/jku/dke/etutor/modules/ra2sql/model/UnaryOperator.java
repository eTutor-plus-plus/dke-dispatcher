package at.jku.dke.etutor.modules.ra2sql.model;

public interface UnaryOperator extends Expression {

	public Expression getExpression();

	public boolean setExpression(Expression e);
}
