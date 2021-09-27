package etutor.modules.xquery.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import etutor.core.ui.Editor;
import etutor.core.ws.types.Resource;
import etutor.modules.xquery.ParameterException;
import etutor.modules.xquery.XQConstants;
import etutor.modules.xquery.analysis.UrlContentMap;
import etutor.modules.xquery.exercise.XQExerciseBean;

public class XQEditor  implements MessageSourceAware, Editor {
	private static final Logger logger = Logger.getLogger(XQEditor.class);
	private static final String LINE_SEP = System.getProperty("line.separator",
	"\n");
	private MessageSource messageSource;

	@Required
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public HashMap<String, Object> preparePerformTask(
			HashMap<String, Object> passedAttributes,
			HashMap<String, Object> passedParameters, Resource[] resources) {
		HashMap<String, Object> sessionAttributes = new HashMap<String, Object>();

		String query = "";
		if (resources != null) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						resources[0].getContent().getInputStream()));
				String thisLine;
				while ((thisLine = br.readLine()) != null) {
					query = query.concat(thisLine + LINE_SEP);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			query = (String) passedParameters.get(XQConstants.ATTR_QUERY);
			if (query == null) {
				query = "";
			}
		}
		
		logger.log(Priority.DEBUG, XQConstants.ATTR_COMMAND + ": " + passedParameters.get(XQConstants.ATTR_COMMAND));
		if (passedParameters.get(XQConstants.ATTR_COMMAND)!= null && passedParameters.get(XQConstants.ATTR_COMMAND).equals(XQConstants.ACTION_SUBMIT)) {
			sessionAttributes.put(XQConstants.ATTR_ACTION, XQConstants.ACTION_SUBMIT);
		}
		//Modified by Michael Karlinger on May 16, 2011
		if (passedParameters.get(XQConstants.ATTR_COMMAND)!= null && passedParameters.get(XQConstants.ATTR_COMMAND).equals(XQConstants.ACTION_DIAGNOSE)) {
			sessionAttributes.put(XQConstants.ATTR_ACTION, XQConstants.ACTION_DIAGNOSE);
		}
		
		sessionAttributes.put(XQConstants.ATTR_SUBMISSION, query);
		return sessionAttributes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> prepareAuthorTask(
			HashMap<String, Object> passedAttributes,
			HashMap<String, Object> passedParameters, Resource[] resources,
			Serializable exerciseInfo) throws Exception {
		HashMap<String, Object> sessionAttributes = new HashMap<String, Object>();

		XQExerciseBean xqExerciseBean;
		if (passedParameters != null) {
			xqExerciseBean = (XQExerciseBean) passedAttributes
					.get("exerciseBean");
			String msg = null;
			List<String> errors;
			if (passedAttributes.get("errors") == null) {
				errors = new ArrayList<String>();
				sessionAttributes.put("errors", errors);
			} else {
				errors = (List<String>) passedAttributes.get("errors");
			}
			String cmd = null;
			if (passedParameters.get(XQConstants.PARAM_COMMAND) != null) {
				cmd = passedParameters.get(XQConstants.PARAM_COMMAND)
						.toString();
			}
			String query = "";
			if (resources != null) {
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(resources[0].getContent()
									.getInputStream()));
					String thisLine;
					while ((thisLine = br.readLine()) != null) {
						query = query.concat(thisLine + LINE_SEP);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if (passedParameters.get(XQConstants.PARAM_QUERY) != null) {
					query = passedParameters.get(XQConstants.PARAM_QUERY)
							.toString();
				}
			}
			String nodesToAdd = null;
			if (passedParameters.get(XQConstants.PARAM_SORTED_NODE_TO_ADD) != null) {
				nodesToAdd = passedParameters.get(
						XQConstants.PARAM_SORTED_NODE_TO_ADD).toString();
			}
			String nodesIndex = null;
			if (passedParameters.get(XQConstants.PARAM_SORTED_NODE_INDEX) != null) {
				nodesIndex = passedParameters.get(
						XQConstants.PARAM_SORTED_NODE_INDEX).toString();
			}
			String urlToAdd = null;
			if (passedParameters.get(XQConstants.PARAM_URL_TO_ADD) != null) {
				urlToAdd = passedParameters.get(XQConstants.PARAM_URL_TO_ADD)
						.toString();
			}
			String hiddenUrlToAdd = null;
			if (passedParameters.get(XQConstants.PARAM_HIDDEN_URL_TO_ADD) != null) {
				hiddenUrlToAdd = passedParameters.get(
						XQConstants.PARAM_HIDDEN_URL_TO_ADD).toString();
			}
			String urlIndex = null;
			if (passedParameters.get(XQConstants.PARAM_URL_INDEX) != null) {
				urlIndex = passedParameters.get(XQConstants.PARAM_URL_INDEX)
						.toString();
			}

			applyParameters(xqExerciseBean, msg, cmd, query, nodesToAdd,
					nodesIndex, urlToAdd, hiddenUrlToAdd, urlIndex, errors, (Locale) sessionAttributes.get("locale"));
			boolean authoringEnd;
			if ("yes".equals(passedParameters.get("preview"))) {
				authoringEnd = false;
			} else {
				authoringEnd = true;
			}
			sessionAttributes.put("authoringEnd", authoringEnd);
		} else {
			// first call -> set exercise bean
			xqExerciseBean = (XQExerciseBean) exerciseInfo;
			sessionAttributes.put("authoringEnd", false);
		}
		sessionAttributes.put("exerciseBean", xqExerciseBean);
		return sessionAttributes;
	}

	private void applyParameters(XQExerciseBean exercise, String msg,
			String cmd, String query, String nodesToAdd, String nodesIndex,
			String urlToAdd, String hiddenUrlToAdd, String urlIndex,
			List<String> errors, Locale locale) {

		List nodes = exercise.getSortedNodes();
		UrlContentMap urls = exercise.getUrls();

		if (XQConstants.COMMAND_ADD_SORTED_NODES.equals(cmd)) {
			if (nodesToAdd == null || nodesToAdd.trim().length() < 1) {
				msg = messageSource.getMessage("xqeditor.specifyexpression", null, locale);
				errors.add(msg);
			}
			if (errors.size() < 1) {
				nodes = nodes == null ? new ArrayList() : nodes;
				nodes.add(0, nodesToAdd);
				// reset in user interface after adding to list
				nodesToAdd = "";
			}
		} else if (XQConstants.COMMAND_REMOVE_SORTED_NODES.equals(cmd)
				&& nodes != null) {
			try {
				nodes.remove(Integer.parseInt(nodesIndex));
			} catch (NumberFormatException e) {
				msg = "Could not parse " + XQConstants.PARAM_SORTED_NODE_INDEX
						+ " parameter.";
				logger.warn(msg, e);
				msg = messageSource.getMessage("xqeditor.removeexpressionfailed", null, locale);
				errors.add(msg);
			} catch (IndexOutOfBoundsException e) {
				msg = new String();
				msg += "Could not remove expression at " + nodesIndex;
				msg += " from list of XPath expressions.";
				logger.warn(msg, e);
				msg = messageSource.getMessage("xqeditor.removeexpressionfailed", null, locale);
				errors.add(msg);
			}
		} else if (XQConstants.COMMAND_ADD_URL.equals(cmd)) {
			if (urlToAdd == null || urlToAdd.trim().length() < 1) {
				msg = messageSource.getMessage("xqeditor.specifyurl", null, locale);
				errors.add(msg);
			}
			if (hiddenUrlToAdd == null || hiddenUrlToAdd.trim().length() < 1) {
				msg = messageSource.getMessage("xqeditor.specifyhiddenurl", null, locale);
				errors.add(msg);
			}
			if (errors.size() < 1) {
				urls = urls == null ? new UrlContentMap() : urls;
				try {
					urls.addUrlAlias(hiddenUrlToAdd, urlToAdd);
					// reset in user interface after adding to list
					urlToAdd = "";
					hiddenUrlToAdd = "";
				} catch (ParameterException e) {
					errors.add(e.getMessage());
				}
			}
		} else if (XQConstants.COMMAND_REMOVE_URL.equals(cmd) && urls != null) {
			urls.removeUrl(urlIndex);
		}

		exercise.setQuery(query != null ? query : exercise.getQuery());
		exercise.setSortedNodeToAdd(nodesToAdd != null ? nodesToAdd : exercise
				.getSortedNodeToAdd());
		exercise.setUrlToAdd(urlToAdd != null ? urlToAdd : exercise
				.getUrlToAdd());
		exercise.setHiddenUrlToAdd(hiddenUrlToAdd != null ? hiddenUrlToAdd
				: exercise.getHiddenUrlToAdd());
	}

	@Override
	public HashMap<String, Object> initPerformTask(int exerciseId)
			throws Exception {
		// does nothing
		return null;
	}
}
