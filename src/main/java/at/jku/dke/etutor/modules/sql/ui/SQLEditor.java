package at.jku.dke.etutor.modules.sql.ui;

public class SQLEditor /* implements Editor*/ {

	/* @Override
	public HashMap<String, Object> preparePerformTask(
			HashMap<String, Object> passedAttributes,
			HashMap<String, Object> passedParameters, Resource[] resources) {
		HashMap<String, Object> sessionAttributes = new HashMap<String, Object>();

		String query = "";
		if (resources != null) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(resources[0].getContent().getInputStream()));
				String thisLine;
				while ((thisLine = br.readLine()) != null) {
					query = query.concat(thisLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			query = (String) passedParameters.get("query");
			if (query == null) {
				query = "";
			}
		}

		sessionAttributes.put("SQL_QUERY_"
				+ passedAttributes.get(SQLConstants.ATTR_TASK_ID), query);
		sessionAttributes.put("submission", query);
		return sessionAttributes;
	}

	@Override
	public HashMap<String, Object> prepareAuthorTask(
			HashMap<String, Object> passedAttributes,
			HashMap<String, Object> passedParameters, Resource[] resources,
			Serializable exerciseInfo) throws Exception {
		HashMap<String, Object> sessionAttributes = new HashMap<String, Object>();

		SQLExerciseBean sqlExerciseBean;
		if (passedParameters != null) {
			sqlExerciseBean = (SQLExerciseBean) passedAttributes
					.get("exerciseBean");
			sqlExerciseBean.setQuery(passedParameters.get("query").toString());
			sqlExerciseBean.setSelected_trial_db(passedParameters.get(
					"selected_trial_db").toString());
			sqlExerciseBean.setSelected_submission_db(passedParameters.get(
					"selected_submission_db").toString());
			sessionAttributes.put("authoringEnd", true);
		} else {
			// first call -> set exercise bean
			sqlExerciseBean = (SQLExerciseBean) exerciseInfo;
			sessionAttributes.put("authoringEnd", false);
		}
		sessionAttributes.put("exerciseBean", sqlExerciseBean);
		return sessionAttributes;
	}
	
	@Override
	public HashMap<String, Object> initPerformTask(int exerciseId)
			throws Exception {
		// does nothing
		return null;
	}

	 */
}
