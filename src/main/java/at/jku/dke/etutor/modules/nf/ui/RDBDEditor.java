package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.core.ws.types.Resource;
import at.jku.dke.etutor.modules.nf.*;
import at.jku.dke.etutor.modules.nf.analysis.KeysDeterminator;
import at.jku.dke.etutor.modules.nf.exercises.RDBDExercisesManager;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;
import etutor.core.evaluation.TaskNotFoundException;
import etutor.core.ui.Editor;
import etutor.core.utils.WsUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.io.Serializable;
import java.util.*;

public class RDBDEditor  implements MessageSourceAware, Editor {
	protected final int rdbdType;
	private MessageSource messageSource;

	public RDBDEditor(int rdbdType) {
		this.rdbdType = rdbdType;
	}

	@Required
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public HashMap<String, Object> preparePerformTask(
			HashMap<String, Object> passedAttributes,
			HashMap<String, Object> passedParameters, Resource[] resources)
			throws Exception {
		HashMap<String, Object> sessionAttributes = new HashMap<String, Object>();

		String msg;
		String command = "";
		String relationID = null;
		String[] attributes;
		String[] keysToDelete;
		String[] attributesToDelete;
		String[] dependenciesToDelete;
		String[] rhsAttributes;
		String[] lhsAttributes;
		String[] subRelation1Attributes;
		String[] subRelation2Attributes;
		Object relations;
		int parameterAttempts = -1;

		// fetch submission possibly needed for manipulating relations;
		// not all submissions are Collections (see Normalform Determination!)
		int exerciseId = Integer.parseInt(passedAttributes.get(
				RDBDConstants.ATT_EXERCISE_ID).toString());
		relations = passedAttributes.get(RDBDConstants
				.calcSubmissionIDFor(exerciseId));

		int sessionAttempts = Integer.parseInt(passedAttributes.get(
				"numberAttempts").toString());
		if (passedParameters.get("numberAttempts") != null) {
			parameterAttempts = Integer.parseInt(passedParameters.get(
					"numberAttempts").toString());
		}
		if (parameterAttempts > -1) {
			if ((sessionAttempts - 1) < parameterAttempts) {
				for (int i = 1; i <= parameterAttempts; i++) {
					if (passedParameters.get(i + "command") != null) {
						command = passedParameters.get(i + "command")
								.toString();
					}
					keysToDelete = WsUtils.splitMultiValues(passedParameters
							.get(i + "delKey"));
					attributes = WsUtils.splitMultiValues(passedParameters
							.get(i + "attribute"));
					dependenciesToDelete = WsUtils
							.splitMultiValues(passedParameters.get(i
									+ "delDependency"));
					lhsAttributes = WsUtils.splitMultiValues(passedParameters
							.get(i + "lhsAttribute"));
					rhsAttributes = WsUtils.splitMultiValues(passedParameters
							.get(i + "rhsAttribute"));
					attributesToDelete = WsUtils
							.splitMultiValues(passedParameters.get(i
									+ "delAttribute"));
					if (passedParameters.get(i + RDBDConstants.PARAM_REL_ID) != null) {
						relationID = passedParameters.get(
								i + RDBDConstants.PARAM_REL_ID).toString();
					}
					subRelation1Attributes = WsUtils
							.splitMultiValues(passedParameters
									.get(i
											+ RDBDConstants.PARAM_SUB_RELATION_1_ATTRIBUTE));
					subRelation2Attributes = WsUtils
							.splitMultiValues(passedParameters
									.get(i
											+ RDBDConstants.PARAM_SUB_RELATION_2_ATTRIBUTE));

					executeCommand(relations, command, relationID, attributes,
							keysToDelete, attributesToDelete,
							dependenciesToDelete, rhsAttributes, lhsAttributes,
							subRelation1Attributes, subRelation2Attributes);
					saveCommandsInSession(sessionAttributes, i, command,
							relationID, attributes, keysToDelete,
							attributesToDelete, dependenciesToDelete,
							rhsAttributes, lhsAttributes,
							subRelation1Attributes, subRelation2Attributes);
				}
				sessionAttributes.put("numberAttempts", parameterAttempts + 1);
			}
		}

		// fetch all request parameters possibly needed
		if (passedParameters.get("command") != null) {
			command = passedParameters.get("command").toString();
		}
		keysToDelete = WsUtils.splitMultiValues(passedParameters.get("delKey"));
		attributes = WsUtils
				.splitMultiValues(passedParameters.get("attribute"));
		dependenciesToDelete = WsUtils.splitMultiValues(passedParameters
				.get("delDependency"));
		lhsAttributes = WsUtils.splitMultiValues(passedParameters
				.get("lhsAttribute"));
		rhsAttributes = WsUtils.splitMultiValues(passedParameters
				.get("rhsAttribute"));
		attributesToDelete = WsUtils.splitMultiValues(passedParameters
				.get("delAttribute"));
		if (passedParameters.get(RDBDConstants.PARAM_REL_ID) != null) {
			relationID = passedParameters.get(RDBDConstants.PARAM_REL_ID)
					.toString();
		}
		subRelation1Attributes = WsUtils.splitMultiValues(passedParameters
				.get(RDBDConstants.PARAM_SUB_RELATION_1_ATTRIBUTE));
		subRelation2Attributes = WsUtils.splitMultiValues(passedParameters
				.get(RDBDConstants.PARAM_SUB_RELATION_2_ATTRIBUTE));

		executeCommand(relations, command, relationID, attributes,
				keysToDelete, attributesToDelete, dependenciesToDelete,
				rhsAttributes, lhsAttributes, subRelation1Attributes,
				subRelation2Attributes);
		saveCommandsInSession(sessionAttributes, parameterAttempts + 1,
				command, relationID, attributes, keysToDelete,
				attributesToDelete, dependenciesToDelete, rhsAttributes,
				lhsAttributes, subRelation1Attributes, subRelation2Attributes);
		return sessionAttributes;
	}

	private void saveCommandsInSession(
			HashMap<String, Object> sessionAttributes, int attemptNumber,
			String command, String relationID, String[] attributes,
			String[] keysToDelete, String[] attributesToDelete,
			String[] dependenciesToDelete, String[] rhsAttributes,
			String[] lhsAttributes, String[] subRelation1Attributes,
			String[] subRelation2Attributes) {
		// save commands in session

		if (command != null) {
			sessionAttributes.put(attemptNumber + "command", command);
		}
		if (keysToDelete != null) {
			sessionAttributes.put(attemptNumber + "delKey", WsUtils
					.combineMultiValues(keysToDelete));
		}
		if (attributes != null) {
			sessionAttributes.put(attemptNumber + "attribute", WsUtils
					.combineMultiValues(attributes));
		}
		if (dependenciesToDelete != null) {
			sessionAttributes.put(attemptNumber + "delDependency", WsUtils
					.combineMultiValues(dependenciesToDelete));
		}
		if (lhsAttributes != null) {
			sessionAttributes.put(attemptNumber + "lhsAttribute", WsUtils
					.combineMultiValues(lhsAttributes));
		}
		if (rhsAttributes != null) {
			sessionAttributes.put(attemptNumber + "rhsAttribute", WsUtils
					.combineMultiValues(rhsAttributes));
		}
		if (attributesToDelete != null) {
			sessionAttributes.put(attemptNumber + "delAttribute", WsUtils
					.combineMultiValues(attributesToDelete));
		}
		if (relationID != null) {
			sessionAttributes.put(attemptNumber + RDBDConstants.PARAM_REL_ID,
					relationID);
		}
		if (subRelation1Attributes != null) {
			sessionAttributes.put(attemptNumber
					+ RDBDConstants.PARAM_SUB_RELATION_1_ATTRIBUTE, WsUtils
					.combineMultiValues(subRelation1Attributes));
		}
		if (subRelation2Attributes != null) {
			sessionAttributes.put(attemptNumber
					+ RDBDConstants.PARAM_SUB_RELATION_2_ATTRIBUTE, WsUtils
					.combineMultiValues(subRelation2Attributes));
		}
	}
	
	private Relation getFirstRelation(Object relations) {
		if (relations instanceof TreeSet) {
			return ((TreeSet<Relation>) relations).first();
		} else {
			return ((Vector<Relation>) relations).get(0);
		}
	}

	private void executeCommand(Object relations, String command,
			String relationID, String[] attributes, String[] keysToDelete,
			String[] attributesToDelete, String[] dependenciesToDelete,
			String[] rhsAttributes, String[] lhsAttributes,
			String[] subRelation1Attributes, String[] subRelation2Attributes)
			throws Exception {
		// process commands w.r.t. relation manipulations, relations object must
		// be a Collection
		if (RDBDConstants.CMD_NEW_REL.equals(command)) {
			RDBDHelper.newRelation((Collection) relations);
		} else if (RDBDConstants.CMD_DEL_REL.equals(command)) {
			RDBDHelper.delRelation((Collection) relations, relationID);
		} else if (RDBDConstants.CMD_ADD_ATT.equals(command)) {
			if ((relationID == null) || (relationID.isEmpty())) {
				RDBDHelper.addAttribute(getFirstRelation(relations),
						attributes);
			} else {
				RDBDHelper.addAttribute((Collection) relations, relationID,
						attributes);
			}
		} else if (RDBDConstants.CMD_DEL_ATT.equals(command)) {
			if ((relationID == null) || (relationID.isEmpty())) {
				RDBDHelper.delAttributes(
						getFirstRelation(relations),
						attributesToDelete);
			} else {
				RDBDHelper.delAttributes((Collection) relations, relationID,
						attributesToDelete);
			}
		} else if (RDBDConstants.CMD_ADD_DEP.equals(command)) {
			if ((relationID == null) || (relationID.isEmpty())) {
				
				RDBDHelper.addDependency(
						getFirstRelation(relations), rhsAttributes,
						lhsAttributes);
			} else {
				RDBDHelper.addDependency((Collection) relations, relationID,
						rhsAttributes, lhsAttributes);
			}
		} else if (RDBDConstants.CMD_DEL_DEP.equals(command)) {
			if ((relationID == null) || (relationID.isEmpty())) {
				RDBDHelper.delDependencies(getFirstRelation(relations), rhsAttributes, lhsAttributes,
						dependenciesToDelete);
			} else {
				RDBDHelper.delDependencies((Collection) relations, relationID,
						rhsAttributes, lhsAttributes, dependenciesToDelete);
			}
		} else if (RDBDConstants.CMD_ADD_KEY.equals(command)) {
			if ((relationID == null) || (relationID.isEmpty())) {
				RDBDHelper.addKey(getFirstRelation(relations),
						attributes);
			} else {
				RDBDHelper.addKey((Collection) relations, relationID,
						attributes);
			}
		} else if (RDBDConstants.CMD_DEL_KEY.equals(command)) {
			if ((relationID == null) || (relationID.isEmpty())) {
				RDBDHelper.delKeys(getFirstRelation(relations),
						attributes, keysToDelete);
			} else {
				RDBDHelper.delKeys((Collection) relations, relationID,
						attributes, keysToDelete);
			}
		} else if (RDBDConstants.CMD_SPLIT_REL.equals(command)) {
			RDBDHelper.splitRelation((Collection) relations, relationID,
					subRelation1Attributes, subRelation2Attributes);
		} else if (RDBDConstants.CMD_DEL_SUB_RELATIONS.equals(command)) {
			RDBDHelper.delSubRelations((Collection) relations, relationID);
		}
	}

	@Override
	public HashMap<String, Object> prepareAuthorTask(
			HashMap<String, Object> passedAttributes,
			HashMap<String, Object> passedParameters, Resource[] resources,
			Serializable exerciseInfo) throws Exception {
		HashMap<String, Object> sessionAttributes = new HashMap<String, Object>();

		SpecificationEditor rdbdExerciseBean;
		SpecificationParser parser;
		RDBDSpecification specTmp;
		Relation relationTmp;
		String errMsg = null;
		int maxLost;
		Locale locale = (Locale) passedAttributes.get("locale");
		if (passedParameters != null) {
			rdbdExerciseBean = (SpecificationEditor) passedAttributes
					.get("exerciseBean");
			parser = rdbdExerciseBean.getParser();
			specTmp = rdbdExerciseBean.getSpecTmp();
			relationTmp = RDBDHelper.getRelation(specTmp, rdbdExerciseBean
					.getRdbdType());
			if (passedParameters.containsKey(RDBDConstants.PARAM_SPEC_TXT)) {
				rdbdExerciseBean.setSpecText(passedParameters.get(
						RDBDConstants.PARAM_SPEC_TXT).toString());
			}
			if (passedParameters
					.containsKey(RDBDConstants.PARAM_MAX_LOST_DEPENDENCIES)) {
				rdbdExerciseBean.setMaxLostDependencies(passedParameters.get(
						RDBDConstants.PARAM_MAX_LOST_DEPENDENCIES).toString());
			}
			if ("1".equals(passedParameters
					.get(RDBDConstants.PARAM_NORMALFORM_LEVEL))) {
				rdbdExerciseBean.setTargetLevel(NormalformLevel.FIRST);
			} else if ("2".equals(passedParameters
					.get(RDBDConstants.PARAM_NORMALFORM_LEVEL))) {
				rdbdExerciseBean.setTargetLevel(NormalformLevel.SECOND);
			} else if ("3".equals(passedParameters
					.get(RDBDConstants.PARAM_NORMALFORM_LEVEL))) {
				rdbdExerciseBean.setTargetLevel(NormalformLevel.THIRD);
			} else if ("4".equals(passedParameters
					.get(RDBDConstants.PARAM_NORMALFORM_LEVEL))) {
				rdbdExerciseBean.setTargetLevel(NormalformLevel.BOYCE_CODD);
			}

			if ("yes".equals(passedParameters.get("reset"))) {
				specTmp = RDBDHelper.clone(rdbdExerciseBean.getSpec(),
						rdbdExerciseBean.getRdbdType());
				rdbdExerciseBean.setSpecTmp(specTmp);
				rdbdExerciseBean.setSpecText(parser.getText(specTmp));
				if (specTmp instanceof DecomposeSpecification) {
					rdbdExerciseBean
							.setTargetLevel(((DecomposeSpecification) specTmp)
									.getTargetLevel());
					rdbdExerciseBean.setMaxLostDependencies(Integer
							.toString(((DecomposeSpecification) specTmp)
									.getMaxLostDependencies()));
					rdbdExerciseBean
							.setTargetLevel(((DecomposeSpecification) specTmp)
									.getTargetLevel());
				} else if (rdbdExerciseBean.getSpecTmp() instanceof NormalizationSpecification) {
					rdbdExerciseBean
							.setTargetLevel(((NormalizationSpecification) specTmp)
									.getTargetLevel());
					rdbdExerciseBean.setMaxLostDependencies(Integer
							.toString(((NormalizationSpecification) specTmp)
									.getMaxLostDependencies()));
					rdbdExerciseBean
							.setTargetLevel(((NormalizationSpecification) specTmp)
									.getTargetLevel());
				}
				rdbdExerciseBean.setMsg(messageSource.getMessage("rdbdeditor.specificationreset", null, locale));
			} else {

				maxLost = -1;
				try {
					parser.parse(rdbdExerciseBean.getSpecText());
					if (rdbdExerciseBean.getMaxLostDependencies() != null
							&& rdbdExerciseBean.getMaxLostDependencies() != null
							&& !rdbdExerciseBean.getMaxLostDependencies().trim().isEmpty()) {
						maxLost = Integer.parseInt(rdbdExerciseBean
								.getMaxLostDependencies());
						if (maxLost < 0) {
							errMsg = messageSource.getMessage("rdbdeditor.invalidlostdependencies", new Object[]{maxLost}, locale);
						}
					}
				} catch (SpecificationParserException e) {
					errMsg = e.getMessage();
				} catch (NumberFormatException e) {
					errMsg = messageSource.getMessage("rdbdeditor.invalidlostdependencies", new Object[]{maxLost}, locale);
				}
				if (errMsg != null) {
					rdbdExerciseBean.setMsg(errMsg);
				} else {
					if (parser.getRelationAttributes() != null) {
						relationTmp.setAttributes(parser
								.getRelationAttributes());
					}
					if (parser.getDependencies() != null) {
						relationTmp.setFunctionalDependencies(parser
								.getDependencies());
					}
					if (parser.getBaseAttributes() != null) {
						RDBDHelper.setBaseAttributes(specTmp, parser
								.getBaseAttributes());
					}
					if (rdbdExerciseBean.getTargetLevel() != null) {
						if (specTmp instanceof DecomposeSpecification) {
							((DecomposeSpecification) specTmp)
									.setTargetLevel(rdbdExerciseBean
											.getTargetLevel());
						} else if (specTmp instanceof NormalizationSpecification) {
							((NormalizationSpecification) specTmp)
									.setTargetLevel(rdbdExerciseBean
											.getTargetLevel());
						}
					}
					if (maxLost > -1) {
						if (specTmp instanceof DecomposeSpecification) {
							((DecomposeSpecification) specTmp)
									.setMaxLostDependencies(maxLost);
						} else if (specTmp instanceof NormalizationSpecification) {
							((NormalizationSpecification) specTmp)
									.setMaxLostDependencies(maxLost);
						}
					}
					rdbdExerciseBean
							.setMsg(messageSource.getMessage("rdbdeditor.specificationparsed", null, locale));
				}
			}
			boolean authoringEnd;
			if (("yes".equals(passedParameters.get("preview")) || ("yes"
					.equals(passedParameters.get("reset"))))) {
				authoringEnd = false;
			} else {
				authoringEnd = true;
			}
			sessionAttributes.put("authoringEnd", authoringEnd);
		} else {
			// first call -> set exercise bean
			rdbdExerciseBean = (SpecificationEditor) exerciseInfo;
			sessionAttributes.put("authoringEnd", false);
		}
		sessionAttributes.put("exerciseBean", rdbdExerciseBean);
		return sessionAttributes;
	}

	@Override
	public HashMap<String, Object> initPerformTask(int exerciseId)
			throws Exception {
		HashMap<String, Object> sessionAttributes = new HashMap<String, Object>();

		Serializable specification = RDBDExercisesManager
				.fetchSpecification(exerciseId);
		if (specification == null) {
			throw new TaskNotFoundException("Requested Exercise with id " + exerciseId + " not found in database!");
		}
		sessionAttributes.put(RDBDConstants.calcSpecificationIDFor(exerciseId),
				specification);
		Serializable submission;
		IdentifiedRelation relation = null;

		if (rdbdType == RDBDConstants.TYPE_NORMALIZATION) {
			submission = new TreeSet<>(new IdentifiedRelationComparator());

			relation = new IdentifiedRelation();
			relation.setID("1");
			relation.setName("R1");
			((Collection) submission).add(relation);
		} else if (rdbdType == RDBDConstants.TYPE_DECOMPOSE) {
			submission = new TreeSet<>(new IdentifiedRelationComparator());
		} else if (rdbdType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION) {
			int lastID = 1;
			Iterator dependenciesIterator;
			submission = new NormalformDeterminationSubmission();

			dependenciesIterator = ((Relation) specification).iterFunctionalDependencies();
			while (dependenciesIterator.hasNext()) {
				((NormalformDeterminationSubmission) submission)
						.setDependencyID((FunctionalDependency) dependenciesIterator.next(), lastID);
				lastID = lastID + 1;
			}
		} else {
			submission = new Vector<>();
			relation = new IdentifiedRelation();
			relation.setID("1");
			relation.setName("R1");
			((Collection) submission).add(relation);
		}
		sessionAttributes.put(RDBDConstants.calcSubmissionIDFor(exerciseId),
				submission);

		if (rdbdType == RDBDConstants.TYPE_KEYS_DETERMINATION) {
			if (relation != null) {
				relation.setAttributes(((Relation) specification)
						.getAttributes());
			}
		} else if (rdbdType == RDBDConstants.TYPE_MINIMAL_COVER) {
			if (relation != null) {
				relation.setAttributes(((Relation) specification)
						.getAttributes());
			}
		} else if (rdbdType == RDBDConstants.TYPE_RBR) {
			if (relation != null) {
				relation.setAttributes(((RBRSpecification) specification)
						.getBaseAttributes());
			}
		} else if (rdbdType == RDBDConstants.TYPE_DECOMPOSE) {
			relation = ((DecomposeSpecification) specification)
					.getBaseRelation();
			if ((relation.getMinimalKeys() == null)
					|| (relation.getMinimalKeys().isEmpty())) {
				relation.setMinimalKeys(KeysDeterminator
						.determineMinimalKeys(relation));
			}
		} else if (rdbdType == RDBDConstants.TYPE_NORMALIZATION) {
			relation = ((NormalizationSpecification) specification)
					.getBaseRelation();
			if ((relation.getMinimalKeys() == null)
					|| (relation.getMinimalKeys().isEmpty())) {
				relation.setMinimalKeys(KeysDeterminator
						.determineMinimalKeys(relation));
			}
		}

		return sessionAttributes;
	}
}
