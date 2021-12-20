package at.jku.dke.etutor.modules.dlg.exercise;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Class to be used as JavaBean object when specifying datalog exercises.
 * 
 * @author Georg Nitsche (01.02.2006)
 * 
 */
public class DatalogExerciseBean implements Serializable {

	private Integer factsId;
	private Double points;
	private String query;
	private List<String> predicates;
	private List<TermDescription> terms;
	private Map facts;
	private String predicateToAdd;
	private TermDescription termToAdd;

	public DatalogExerciseBean() {
		super();
	}

	/**
	 * Returns the facts which can globally be used as facts base for exercises.
	 * @return a map of <code>String</code> keys and values where 
	 * the ID of a facts base forms the key and the value is a description of 
	 * the facts base.
	 */
	public Map getFacts() {
		return facts;
	}

	/**
	 * Sets the facts which can globally be used as facts base for exercises.
	 * @param facts a map of <code>String</code> keys and values where 
	 * the ID of a facts base forms the key and the value is a description of 
	 * the facts base.
	 */
	public void setFacts(Map facts) {
		this.facts = facts;
	}

	/**
	 * Returns the ID of the facts base selected for the exercise specification
	 * or <code>null</code> if not set. Should be one of the IDs as returned by 
	 * {@link #getFacts()}.
	 * @return the ID of the facts base
	 */
	public Integer getFactsId() {
		return factsId;
	}

	/**
	 * Sets the ID of the facts base for the exercise specification. Should be 
	 * @param factsId the ID of the facts base to set
	 */
	public void setFactsId(Integer factsId) {
		this.factsId = factsId;
	}

	/**
	 * Returns the predicates designated to be required in the result of an
	 * analyzed query in order to be considered as correct.
	 * @return a list of <code>String</code> objects representing the name 
	 * of required predicates
	 */
	public List<String> getPredicates() {
		return predicates;
	}

	/**
	 * Sets the predicates designated to be required in the result of an
	 * analyzed query in order to be considered as correct.
	 * @param predicates a list of <code>String</code> objects representing the name 
	 * of required predicates 
	 */
	public void setPredicates(List<String> predicates) {
		this.predicates = predicates;
	}

	/**
	 * Returns the datalog query which represents the correct solution.
	 * @return the datalog query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Sets the datalog query which represents the correct solution.
	 * @param query the datalog query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Returns the terms which are not to be manipulated in the facts base,
	 * which is done to uncover the attempt of a student to declare the required
	 * facts rather than deriving it by a query. 
	 * @return a list of terms, represented by an array of term descriptions, which
	 * identify the term.
	 */
	public List<TermDescription> getTerms() {
		return terms;
	}

	/** Sets the terms which are not to be manipulated in the facts base,
	 * which is done to uncover the attempt of a student to declare the required
	 * facts rather than deriving it by a query.
	 * @param terms a list of terms, represented by an array of term descriptions, which
	 * identify the term.
	 */
	public void setTerms(List<TermDescription> terms) {
		this.terms = terms;
	}

	/**
	 * Returns the maximum number of points designated for the exercise. This
	 * should not be <code>null</code> and a value greater than zero. The actual
	 * number of points is defined by the eTutor core.
	 * @return the maximum number of points to be reached for the exercise.
	 */
	public Double getPoints() {
		return points;
	}

	/**
	 * Sets the maximum number of points designated for the exercise. This
	 * should not be <code>null</code> and a value greater than zero. The actual
	 * number of points is defined by the eTutor core.
	 * @param points the maximum number of points to be reached for the exercise.
	 */
	public void setPoints(Double points) {
		this.points = points;
	}

	/**
	 * Returns the name of a predicate which a user chooses to add to 
	 * the list of required predicates of an exercise specification.
	 * @return name of a predicate to be added
	 */
	public String getPredicateToAdd() {
		return predicateToAdd;
	}

	/**
	 * Sets the name of a predicate which a user chooses to add to
	 * the list of required predicates of an exercise specification.
	 * @param predicateToAdd name of a predicate to be added
	 */
	public void setPredicateToAdd(String predicateToAdd) {
		this.predicateToAdd = predicateToAdd;
	}

	/**
	 * Returns the term definition which a user chooses to add to 
	 * the list of terms.
	 * @return a term descriptions
	 */
	public TermDescription getTermToAdd() {
		return termToAdd;
	}

	/**
	 * Sets the term definition which a user chooses to add to 
	 * the list of terms.
	 * @param termToAdd a term description
	 */
	public void setTermToAdd(TermDescription termToAdd) {
		this.termToAdd = termToAdd;
	}

	public String toString() {
		String msg;
		String term;
		String pos;
		String predicate;
		TermDescription terms;
		Iterator it;
		Object key;

		msg = new String();
		msg = msg.concat("Datalog exercise specification:\n");
		msg = msg.concat("-------------------------------\n");
		msg = msg.concat("Globally available facts:\n");
		if (this.facts == null || this.facts.size() < 1) {
			msg = msg.concat("\tnone\n");
		} else {
			it = this.facts.keySet().iterator();
			while (it.hasNext()) {
				key = it.next();
				msg = msg.concat("\t" + key + " (" + this.facts.get(key) + ")\n");				
			}
		}
		msg = msg.concat("ID of facts base:\n");
		msg = msg.concat("\t"
				+ (this.factsId != null ? this.factsId.toString() : "none")
				+ "\n");
		msg = msg.concat("Internal points:\n");
		msg = msg.concat("\t"
				+ (this.points != null ? this.points.toString() : "none")
				+ "\n");
		msg = msg.concat("Predicates:\n");
		if (this.predicates == null || this.predicates.size() < 1) {
			msg = msg.concat("\tnone\n");
		} else {
			for (int i = 0; i < this.predicates.size(); i++) {
				msg = msg.concat("\t" + this.predicates.get(i) + "\n");
			}
		}
		msg = msg.concat("Terms:\n");
		if (this.terms == null || this.terms.size() < 1) {
			msg = msg.concat("\tnone\n");
		} else {
			for (int i = 0; i < this.terms.size(); i++) {
				terms = (TermDescription) this.terms.get(i);
				msg = msg.concat("\tPredicate: " + terms.getPredicate() + ", ");
				msg = msg.concat("Pos: " + terms.getPosition() + ", ");
				msg = msg.concat("Term: " + terms.getTerm() + "\n");
			}
		}
		msg = msg.concat("Query:\n");
		msg = msg.concat("\t" + this.query + "\n");

		return msg;
	}
}