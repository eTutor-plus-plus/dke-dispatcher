package at.jku.dke.etutor.modules.ra2sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import at.jku.dke.etutor.modules.ra2sql.model.*;

// todo: relational algebra(RA) to(2) SQL => ra2sql

public class SQLBuilder {

	public SQLBuilder() {
		super();
	}

	public String buildSQLQuery(Expression exp, Connection conn) throws SQLException, IllegalArgumentException, NullPointerException {
		String query = new String();

		if (exp != null){
			if (conn != null){
				exp.calculateSchema(conn);
				query = query.concat("SELECT DISTINCT * ");
				query = query.concat("FROM (");
				query = query.concat(this.printSQLQuery(exp));
				query = query.concat(")");
				query = query.concat(" AS subquery");
			} else {
				throw new NullPointerException("Connection is null");
			}
		} else {
			throw new NullPointerException("Expression is null");
		}
		
		return query;
	}

	private String printSQLQuery(Expression exp) {
		boolean first;
		Iterator iter;
		String query = new String();
		String leftQuery = new String();
		String rightQuery = new String();

		if (exp != null) {
			if (exp instanceof UnaryOperator) {
				leftQuery = this.printSQLQuery(((UnaryOperator)exp).getExpression());
				if (exp instanceof Selection) {
					Selection sel = (Selection)exp;
					query = query.concat("SELECT " + this.printSchema(sel) + " ");
					query = query.concat("FROM (");
					query = query.concat(leftQuery);
					query = query.concat(") AS selectionSubquery ");
					query = query.concat("WHERE ");

					first = true;
					iter = sel.iterComparisons();
					while (iter.hasNext()) {
						if (first) {
							first = false;
						} else {
							query = query.concat(" AND ");
						}
						query = query.concat(this.printComparison((Comparison)iter.next(), "", ""));
					}
				} else if (exp instanceof Projection) {
					query = query.concat("SELECT " + this.printSchema(exp) + " ");
					if(leftQuery.contains(" ")){
						query = query.concat("FROM (");
						query = query.concat(leftQuery);
						query = query.concat(") AS projectionSubquery ");
					}else {
						query = query.concat("FROM");
						query = query.concat(leftQuery);
					}
				} else if (exp instanceof Renaming) {
					query = query.concat("SELECT " + this.printSchema(exp) + " ");
					query = query.concat("FROM (");
					query = query.concat(leftQuery);
					query = query.concat(") AS renamingSubquery ");
				}
			}
			if (exp instanceof BinaryOperator) {
				leftQuery = this.printSQLQuery(((BinaryOperator)exp).getLeftExpression());
				rightQuery = this.printSQLQuery(((BinaryOperator)exp).getRightExpression());
				if ((exp instanceof Join)
					|| (exp instanceof LeftSemiJoin)
					|| (exp instanceof RightSemiJoin)) {
					query = query.concat("SELECT " + this.printSchema(exp) + " ");
					query = query.concat("FROM (");
					query = query.concat(leftQuery);
					query = query.concat(") AS joinLeftSideSubquery NATURAL JOIN (");
					query = query.concat(rightQuery);
					query = query.concat(") as joinRightSideSubquery");
				}
				if (exp instanceof Minus) {
					query = query.concat("SELECT " + this.printSchema(exp) + " ");
					query = query.concat("FROM (");
					query = query.concat(leftQuery);
					query = query.concat(") AS minusLeftSideSubquery MINUS (");
					query = query.concat(rightQuery);
					query = query.concat(")");
				}
				if (exp instanceof Division) {
					query = query.concat("SELECT " + this.printSchema(exp) + " ");
					query = query.concat("FROM (");
					query = query.concat(leftQuery);
					query = query.concat(") AS naJoinLeftSideSubquery NATURAL JOIN (");
					query = query.concat(rightQuery);
					query = query.concat(") AS naJoinRightSideSubquery ");
					query = query.concat("GROUP BY " + this.printSchema(exp) + " ");
					query = query.concat("HAVING COUNT(*) = (SELECT COUNT(*) FROM (");
					query = query.concat(rightQuery + ") AS havingSubquery ) ");
				}
				if (exp instanceof OuterJoin) {
					query = query.concat("SELECT " + this.printSchema(exp) + " ");
					query = query.concat("FROM (");
					query = query.concat(leftQuery);
					query = query.concat(") AS naFullJoinLeftSideSubquery NATURAL FULL OUTER JOIN (");
					query = query.concat(rightQuery);
					query = query.concat(") AS naFullJoinRightSideSubquery ");
				}

			}
			if (exp instanceof Relation) {
				query = query.concat("SELECT " + this.printSchema(exp) + " ");
				query = query.concat("FROM ");
				query = query.concat(((Relation)exp).getName());
				query = query.concat("");
			}
			if (exp instanceof CartesianProduct) {
				query = query.concat("SELECT " + this.printSchema(exp) + " ");
				query = query.concat("FROM (");
				query = query.concat(leftQuery);
				query = query.concat(") AS naFullOuterLeftSideSubQu NATURAL FULL OUTER JOIN (");
				query = query.concat(rightQuery);
				query = query.concat(") AS naFullOuterRightSideSubQu ");
			}
			if (exp instanceof Intersection) {
				query = query.concat("SELECT " + this.printSchema(exp) + " ");
				query = query.concat("FROM (");
				query = query.concat(leftQuery);
				query = query.concat(") AS intersectLeftSubQu INTERSECT (");
				query = query.concat(rightQuery);
				query = query.concat(")");
			}
			if (exp instanceof Union) {
				query = query.concat("SELECT " + this.printSchema(exp) + " ");
				query = query.concat("FROM (");
				query = query.concat(leftQuery);
				query = query.concat(") AS unionLeftSubQu UNION (");
				query = query.concat(rightQuery);
				query = query.concat(")");
			}
			if (exp instanceof ThetaJoin) {
				ThetaJoin thetaJoin = (ThetaJoin)exp;

				query = query.concat("SELECT " + this.printSchema(exp) + " ");
				query = query.concat("FROM (");
				query = query.concat(leftQuery);
				query = query.concat(") AS LS, (");
				query = query.concat(rightQuery);
				query = query.concat(") AS RS ");
				query = query.concat("WHERE ");
				first = true;
				iter = thetaJoin.iterComparisons();
				while (iter.hasNext()) {
					if (first) {
						first = false;
					} else {
						query = query.concat(" AND ");
					}
					query = query.concat(this.printComparison((Comparison)iter.next(), "LS", "RS"));
				}
			}
		}

		return query;
	}

	private String printComparison(Comparison comp, String leftPrefix, String rightPrefix) {
		String comparison = new String();

		if (comp != null) {
			if (comp.getLeftValueType().equals(ComparisonValueType.DATE)) {
				comparison = comparison.concat("'");
				if ((leftPrefix != null) && (leftPrefix.length() != 0)) {
					comparison = comparison.concat(leftPrefix + ".");
				}
				comparison = comparison.concat(comp.getLeftValue());
				comparison = comparison.concat("'");
			}
			if (comp.getLeftValueType().equals(ComparisonValueType.LITERAL)) {
				comparison = comparison.concat("'");
				if ((leftPrefix != null) && (leftPrefix.length() != 0)) {
					comparison = comparison.concat(leftPrefix + ".");
				}
				comparison = comparison.concat(comp.getLeftValue());
				comparison = comparison.concat("'");
			}
			if (comp.getLeftValueType().equals(ComparisonValueType.ATTRIBUTE)) {
				if ((leftPrefix != null) && (leftPrefix.length() != 0)) {
					comparison = comparison.concat(leftPrefix + ".");
				}
				comparison = comparison.concat(comp.getLeftValue());
			}
			if (comp.getLeftValueType().equals(ComparisonValueType.NUMBER)) {
				if ((leftPrefix != null) && (leftPrefix.length() != 0)) {
					comparison = comparison.concat(leftPrefix + ".");
				}
				comparison = comparison.concat(comp.getLeftValue());
			}

			comparison = comparison.concat(" " + comp.getOperator().toString() + " ");

			if (comp.getRightValueType().equals(ComparisonValueType.DATE)) {
				comparison = comparison.concat("'");
				if ((rightPrefix != null) && (rightPrefix.length() != 0)) {
					comparison = comparison.concat(rightPrefix + ".");
				}
				comparison = comparison.concat(comp.getRightValue());
				comparison = comparison.concat("'");
			}
			if (comp.getRightValueType().equals(ComparisonValueType.LITERAL)) {
				comparison = comparison.concat("'");
				if ((rightPrefix != null) && (rightPrefix.length() != 0)) {
					comparison = comparison.concat(rightPrefix + ".");
				}
				comparison = comparison.concat(comp.getRightValue());
				comparison = comparison.concat("'");
			}
			if (comp.getRightValueType().equals(ComparisonValueType.ATTRIBUTE)) {
				if ((rightPrefix != null) && (rightPrefix.length() != 0)) {
					comparison = comparison.concat(rightPrefix + ".");
				}
				comparison = comparison.concat(comp.getRightValue());
			}
			if (comp.getRightValueType().equals(ComparisonValueType.NUMBER)) {
				if ((rightPrefix != null) && (rightPrefix.length() != 0)) {
					comparison = comparison.concat(rightPrefix + ".");
				}
				comparison = comparison.concat(comp.getRightValue());
			}
		}

		return comparison;
	}

	private String printSchema(Expression exp) {
		boolean first;
		String attSet;
		String attribute;
		Renaming renaming;
		Iterator attributes;

		if (exp != null) {
			first = true;
			attSet = new String();
			attributes = exp.iterSchemaAttributes();

			while (attributes.hasNext()) {
				attribute = attributes.next().toString().toUpperCase().trim();

				if (first) {
					first = false;
				} else {
					attSet = attSet.concat(", ");
				}

				if (exp instanceof Renaming) {
					if (((Renaming)exp).isAlias(attribute)) {
						attSet = attSet.concat(((Renaming)exp).getAttributeForAlias(attribute));
						attSet = attSet.concat(" AS ");
						attSet = attSet.concat(attribute);
					} else {
						attSet = attSet.concat(attribute);
					}
				} else {
					attSet = attSet.concat(attribute);
				}
			}
			return attSet;
		} else {
			return null;
		}
	}
}