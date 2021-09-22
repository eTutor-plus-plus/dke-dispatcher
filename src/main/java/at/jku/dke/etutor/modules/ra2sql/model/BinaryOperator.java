package at.jku.dke.etutor.modules.ra2sql.model;

public interface BinaryOperator extends Expression {

	public Expression getLeftExpression();

	public Expression getRightExpression();

	public boolean setLeftExpression(Expression e);

	public boolean setRightExpression(Expression e);
}
