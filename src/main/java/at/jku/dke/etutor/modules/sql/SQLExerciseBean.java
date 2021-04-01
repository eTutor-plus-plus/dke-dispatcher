package at.jku.dke.etutor.modules.sql;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Georg Nitsche (11.08.2005)
 *
 */
public class SQLExerciseBean implements Serializable {
	
	private String query;
	private String selected_trial_db;
	private String selected_submission_db;
	private Map connections;
	
	public Map getConnections() {
		return connections;
	}
	public void setConnections(Map connections) {
		this.connections = connections;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getSelected_submission_db() {
		return selected_submission_db;
	}
	public void setSelected_submission_db(String selected_submission_db) {
		this.selected_submission_db = selected_submission_db;
	}
	public String getSelected_trial_db() {
		return selected_trial_db;
	}
	public void setSelected_trial_db(String selected_trial_db) {
		this.selected_trial_db = selected_trial_db;
	}
}
