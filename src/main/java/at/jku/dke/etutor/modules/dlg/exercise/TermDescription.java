/*
 * Created on 27.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package at.jku.dke.etutor.modules.dlg.exercise;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author nitsche
 */
public class TermDescription implements Serializable {
	private String predicate;
	private String term;
	private String position;
	
	/**
	 * 
	 */
	public TermDescription() {
		this.predicate = "";
		this.term = "";
		this.position = "";
	}
	/**
	 * @return Returns the position.
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * @param position The position to set.
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return Returns the predicate.
	 */
	public String getPredicate() {
		return predicate;
	}
	/**
	 * @param predicate The predicate to set.
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	/**
	 * @return Returns the term.
	 */
	public String getTerm() {
		return term;
	}
	/**
	 * @param term The term to set.
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TermDescription that = (TermDescription) o;
		return predicate.equals(that.predicate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(predicate);
	}
}
