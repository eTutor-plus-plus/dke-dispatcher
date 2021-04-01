package at.jku.dke.etutor.modules.sql;

import java.util.HashMap;
import java.util.Vector;

public class SQLExerciseComparison {

	private String conn1Name;
	private String conn2Name;
	private Vector exercisesReturningIdenticalRows;
	private HashMap exercisesReturningZeroRows;
	
	
	public SQLExerciseComparison(){
		this.conn1Name = "Connection 1";
		this.conn2Name = "Connection 2";
		this.exercisesReturningIdenticalRows = new Vector();
		this.exercisesReturningZeroRows = new HashMap();
	}
	
	public void addExercisesReturningIdenticalRows(String id){
		this.exercisesReturningIdenticalRows.add(id);
	}
	
	public Vector getExercisesReturningIdenticalRows(){
		return this.exercisesReturningIdenticalRows;
	}
	
	public void addZeroRowsReturningExercise(String id, boolean forConn1, boolean forConn2){
		this.exercisesReturningZeroRows.put(id, new boolean[]{forConn1, forConn2});
	}
	
	public HashMap getZeroRowsReturningExercises(){
		return this.exercisesReturningZeroRows;
	}
	
	public boolean returnsExerciseZeroRowsOnConn1(String id){
		if (this.exercisesReturningZeroRows.containsKey(id)){
			return ((boolean[])this.exercisesReturningZeroRows.get(id))[0];
		} else {
			return false;
		}
	}
	
	public boolean returnsExerciseZeroRowsOnConn2(String id){
		if (this.exercisesReturningZeroRows.containsKey(id)){
			return ((boolean[])this.exercisesReturningZeroRows.get(id))[1];
		} else {
			return false;
		}
	}

	public String[] getExercisesReturningZeroRows(){
		return (String[])this.exercisesReturningZeroRows.keySet().toArray(new String[0]);
	}
	
	public String getConn1Name() {
		return this.conn1Name;
	}

	public void setConn1Name(String conn1Name) {
		this.conn1Name = conn1Name;
	}

	public String getConn2Name() {
		return this.conn2Name;
	}

	public void setConn2Name(String conn2Name) {
		this.conn2Name = conn2Name;
	}

	public String printHTML(){
		String exerciseID;
		StringBuffer html = new StringBuffer();
		String[] exercisesReturningZeroRows = this.getExercisesReturningZeroRows();

		html.append("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd'>");
		html.append("<html>");
		html.append("<head>");
		html.append("	<meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>");
		html.append("	<title>SQL Examples Comparison</title>");
		html.append("</head>");
		html.append("<body>");
		html.append("	<h3>Exercises returning zero rows:</h3>");
		
		if (this.exercisesReturningZeroRows.size() == 0){
			html.append("	<p>No such exercises found!</p>");
		} else {
			html.append("	<table>");
			html.append("		<thead>");
			html.append("			<tr>");
			html.append("				<th align='center'>Exercise ID</th>");
			html.append("				<th align='center'>" + this.getConn1Name() + "</th>");
			html.append("				<th align='center'>" + this.getConn2Name() + "</th>");
			html.append("			</tr>");
			html.append("		</thead>");
			html.append("		<tbody>");
			
			for (int i=0; i<this.exercisesReturningZeroRows.size(); i++){
				exerciseID = exercisesReturningZeroRows[i];
				
				html.append("			<tr>");
				html.append("				<td align='center'>" + exerciseID + "</td>");
				html.append("				<td align='center'>" + this.returnsExerciseZeroRowsOnConn1(exerciseID) + "</td>");
				html.append("				<td align='center'>" + this.returnsExerciseZeroRowsOnConn1(exerciseID) + "</td>");
				html.append("			</tr>");
				html.append("		</tbody>");
				html.append("	</table>");
			}
		}
		
		

		html.append("	<h3>Exercises returning identical rows:</h3>");
		html.append("	<p>");
		
		if (this.exercisesReturningIdenticalRows.size() == 0){
			html.append("No such exercises found!");
		} else {
			for (int j=0; j<this.exercisesReturningIdenticalRows.size(); j++){
				html.append(this.exercisesReturningIdenticalRows.get(j) + "; ");
			}
		}
		html.append("	</p>");
		html.append("</body>");
		html.append("</html>");
		
		
		return html.toString();
	}
}
