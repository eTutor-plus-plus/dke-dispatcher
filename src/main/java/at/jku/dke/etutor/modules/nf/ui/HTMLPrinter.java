package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.NFConstants;
import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformViolation;
import at.jku.dke.etutor.modules.nf.analysis.normalization.KeysDeterminator;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.report.ErrorReport;
import at.jku.dke.etutor.modules.nf.report.ErrorReportGroup;
import at.jku.dke.etutor.modules.nf.report.NFReport;
import at.jku.dke.etutor.modules.nf.report.ReportAtomType;
import at.jku.dke.etutor.modules.nf.specification.AttributeClosureSpecification;
import at.jku.dke.etutor.modules.nf.specification.DecomposeSpecification;
import at.jku.dke.etutor.modules.nf.specification.NFSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalizationSpecification;
import at.jku.dke.etutor.modules.nf.specification.RBRSpecification;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * Helper class for printing all types of specification implementations
 * of the RDBD module.
 * 
 * @author Georg Nitsche (10.01.2006)
 * @author Christian Fischer (25.12.2009)
 *
 */
public class HTMLPrinter {

	public static final String LINE_SEP = System.getProperty("line.separator");
	public static final int OFFSET = 15;
	
	private static final MessageSource MESSAGE_SOURCE;

	@SuppressWarnings("static-access")
	// @Required // deprecated (Gerald Wimmer, 2024-01-03)
	public void setMessageSource(MessageSource messageSource) {
		// this.messageSource = messageSource;
	}

	static {
		ResourceBundleMessageSource msgSrc = new ResourceBundleMessageSource();
		msgSrc.setBasename(NFConstants.MESSAGE_SOURCE_PATH);
		MESSAGE_SOURCE = msgSrc;
	}

	public static String printDecomposeStep(IdentifiedRelation relation, boolean showSplitButton, boolean showRemoveButton, boolean editable, Locale locale) {
		StringJoiner attributesJoiner = new StringJoiner(" ");

		for (String s : relation.getAttributes()) {
			attributesJoiner.add(s);
		}

		String attributesString = attributesJoiner.toString();

		StringBuilder out = new StringBuilder();

		out.append("<table class='decompose_step' cellpadding='1px' cellspacing='1px' style='margin-left:").append((relation.getID().length() / 2) * OFFSET).append("px;'>").append(LINE_SEP);
		out.append("	<tr>").append(LINE_SEP);
		out.append("		<td class='arrow_td'>").append(LINE_SEP);
		//out.append("			<input id='" + relation.getID() + "_SHOW_HIDE' name='relation' type='image' src='%%RESOURCES%%/down_arrow.gif' onClick=\"show_hide('" + relation.getID() + "', '%%RESOURCES%%')\" />" + LINE_SEP);
		out.append("			<img id='").append(relation.getID()).append("_SHOW_HIDE' onclick=\"show_hide('").append(relation.getID()).append("', '%%RESOURCES%%');\" src='%%RESOURCES%%/down_arrow.gif' />").append(LINE_SEP);
		out.append("		</td>").append(LINE_SEP);
		out.append("		<td>").append(LINE_SEP);
		out.append("			<table class='relation_table' cellpadding='2px' cellspacing='2px'>").append(LINE_SEP);
		out.append("				<tr>").append(LINE_SEP);
		out.append("					<td class='label_td'><b>").append(relation.getID()).append("</b>:</td>").append(LINE_SEP);
		out.append("					<td class='attributes_td'>").append(attributesString).append("</td>").append(LINE_SEP);

		if (showSplitButton || showRemoveButton){
			out.append("					<td class='button_td'>").append(LINE_SEP);
			String okMessage =  MESSAGE_SOURCE.getMessage("showpopup.ok", null, locale);
			String cancelMessage = MESSAGE_SOURCE.getMessage("showpopup.cancel", null, locale);
			if (showSplitButton){
				String specifyattributesMessage = MESSAGE_SOURCE.getMessage("showsplitrelationpopup.specifyattributes", null, locale);
				String noattributesMessage = MESSAGE_SOURCE.getMessage("showsplitrelationpopup.noattributes", null, locale);
				out.append("						<input type='button' value='").append(MESSAGE_SOURCE.getMessage("printdecomposestep.split", null, locale)).append("' class='decompose_button' onClick=\"javascript:showSplitRelationPopup('").append(relation.getID()).append("', '%%IDPREFIX%%', '").append(okMessage).append("', '").append(cancelMessage).append("', '").append(specifyattributesMessage).append("', '").append(noattributesMessage).append("');\" />").append(LINE_SEP);
			}
			if (showRemoveButton){
				String removerelationsMessage = MESSAGE_SOURCE.getMessage("showdelsubrelationspopup.removerelations", null, locale);
				out.append("						<input type='button' value='").append(MESSAGE_SOURCE.getMessage("printdecomposestep.remove", null, locale)).append("' class='decompose_button' onClick=\"javascript:showDelSubRelationsPopup('").append(relation.getID()).append("', '%%IDPREFIX%%', '").append(okMessage).append("', '").append(cancelMessage).append("', '").append(removerelationsMessage).append("');\" />").append(LINE_SEP);
			}
			out.append("					</td>").append(LINE_SEP);
		}

		out.append("				</tr>").append(LINE_SEP);
		out.append("			</table>").append(LINE_SEP);
		out.append("		</td>").append(LINE_SEP);

		out.append("	</tr>").append(LINE_SEP);

		out.append("	<tr>").append(LINE_SEP);
		out.append("		<td class='arrow_td'></td>").append(LINE_SEP);
		out.append("		<td>").append(LINE_SEP);
		out.append("			<table class='relation_table' cellpadding='2px' cellspacing='2px' id='").append(relation.getID()).append("_CONTENT'>").append(LINE_SEP);

		out.append(printDependenciesRow(relation.getFunctionalDependencies(), relation.getID(), 4, editable, locale));
		out.append(printKeysRow(relation.getMinimalKeys(), relation.getID(), 4, editable, locale));

		out.append("			</table>").append(LINE_SEP);
		out.append("		</td>").append(LINE_SEP);
		out.append("	</tr>").append(LINE_SEP);
		out.append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printReport(NFReport report, int displayIndent, int codeIndent, Locale locale) {
		StringBuilder out = new StringBuilder();

		out.append("<div style='margin-left:").append(displayIndent * OFFSET).append("px;'>").append(LINE_SEP);
		out.append("	<table class='report_table' cellpadding='0px' cellspacing='0px'>").append(LINE_SEP);

		//PRINT PROLOGUE
		if (report.showPrologue()){
			out.append("		<tr>").append(LINE_SEP);
			out.append("			<td>").append(LINE_SEP);
			out.append("				<div class='section'>").append(LINE_SEP);
			out.append("					<div class='section_caption'>").append(MESSAGE_SOURCE.getMessage("printreport.result", null, locale)).append("</div>").append(LINE_SEP);
			out.append("					<div class='section_content'>").append(report.getPrologue()).append("</div>").append(LINE_SEP);
			out.append("				</div>").append(LINE_SEP);
			out.append("			</td>").append(LINE_SEP);
			out.append("		</tr>").append(LINE_SEP);
		}

		//PRINT ERRORS
		List<ErrorReport> errorReports = report.getErrorReports();
		List<ErrorReportGroup> errorReportGroups = report.getErrorReportGroups();
		
		if ((!errorReports.isEmpty()) || (!errorReportGroups.isEmpty())){
			out.append("		<tr>").append(LINE_SEP);
			out.append("			<td>").append(LINE_SEP);
			out.append("				<div class='section'>").append(LINE_SEP);
			out.append("					<div class='section_caption'>").append(MESSAGE_SOURCE.getMessage("printreport.reports", null, locale)).append("</div>").append(LINE_SEP);
			out.append("					<div class='section_content'>").append(LINE_SEP);
			
			//PRINT ERROR REPORTS
			if (!errorReports.isEmpty()) {
				StringJoiner errorReportJoiner = new StringJoiner("						<div class='gap'></div>" + LINE_SEP);
				for (ErrorReport currErrorReport: errorReports) {
					errorReportJoiner.add(printErrorReport(currErrorReport, displayIndent, codeIndent));
				}
				out.append(errorReportJoiner);
			}

			//PRINT ERROR REPORT GROUPS
			if (!errorReportGroups.isEmpty()) {
				StringJoiner errorReportGroupJoiner = new StringJoiner("						<div class='gap'></div>" + LINE_SEP);
				for (ErrorReportGroup currErrorReportGroup : errorReportGroups) {
					out.append(printErrorReportGroup(currErrorReportGroup, displayIndent + 1, codeIndent));
				}
				out.append(errorReportGroupJoiner);
			}

			out.append("					</div>").append(LINE_SEP);
			out.append("				</div>").append(LINE_SEP);
			out.append("			</td>").append(LINE_SEP);
			out.append("		</tr>").append(LINE_SEP);
		}

		out.append("	<table>").append(LINE_SEP);
		out.append("</div>").append(LINE_SEP);
		
		return out.toString();
	}
	
	public static String printErrorReport(ErrorReport errorReport, int displayIndent, int codeIndent) {
		StringBuilder out = new StringBuilder();
		out.append("<div class='error_report' style='margin-left:").append(displayIndent * OFFSET).append("px;'>").append(LINE_SEP);
		out.append("	<table class='error_report_table' cellspacing='0px' cellpadding='0px'>").append(LINE_SEP);

		String textClass = "";
		if (errorReport.getType().equals(ReportAtomType.INFO)){
			textClass = "info_text";
		
		} else if (errorReport.getType().equals(ReportAtomType.WARNING)){
			textClass = "warning_text";

		} else if (errorReport.getType().equals(ReportAtomType.ERROR)){
			textClass = "error_text";
		}

		if (errorReport.showError()){
			out.append("		<tr>").append(LINE_SEP);
			out.append("			<td class='error_report_caption'>").append(errorReport.getType()).append(":</td>").append(LINE_SEP);
			out.append("			<td class='error_report_content'><div class='").append(textClass).append("'>").append(errorReport.getError()).append("</div></td>").append(LINE_SEP);
			out.append("		</tr>").append(LINE_SEP);
		}

		if (errorReport.showErrorDescription()){
			out.append("		<tr>").append(LINE_SEP);
			out.append("			<td class='error_report_caption'>Description:</td>").append(LINE_SEP);
			out.append("			<td class='error_report_content'>").append(errorReport.getDescription()).append("</td>").append(LINE_SEP);
			out.append("		</tr>").append(LINE_SEP);
		}

		if (errorReport.showHint()){
			out.append("		<tr>").append(LINE_SEP);
			out.append("			<td class='error_report_caption'>Hint:</td>").append(LINE_SEP);
			out.append("			<td class='error_report_content'>").append(errorReport.getHint()).append("</td>").append(LINE_SEP);
			out.append("		</tr>").append(LINE_SEP);
		}

		out.append("	</table>").append(LINE_SEP);
		out.append("</div>").append(LINE_SEP);
		return out.toString();
	}
	
	public static String printErrorReportGroup(ErrorReportGroup group, int displayIndent, int codeIndent) {
		List<ErrorReport> errorReports = group.getErrorReports();
		List<ErrorReportGroup> subGroups = group.getSubErrorReportGroups();
		StringBuilder out = new StringBuilder();

		out.append("<div class='error_report_group'>").append(LINE_SEP);
		out.append("	<div class='error_report_group_caption'>").append(group.getHeader()).append("</div>").append(LINE_SEP);
		out.append("	<div class='error_report_group_content' style='margin-left:").append(displayIndent * OFFSET).append("px;'>").append(LINE_SEP);

		if (!errorReports.isEmpty()) {
			StringJoiner errorReportsJoiner = new StringJoiner("						<div class='gap'></div>" + LINE_SEP);

			for (ErrorReport e : errorReports) {
				errorReportsJoiner.add(printErrorReport(e, 0, codeIndent));
			}

			out.append(errorReportsJoiner);
		}

		if (!subGroups.isEmpty()) {
			StringJoiner subgroupsJoiner = new StringJoiner("						<div class='gap'></div>" + LINE_SEP);

			for (ErrorReportGroup e : subGroups){
				subgroupsJoiner.add(printErrorReportGroup(e, 1, codeIndent));
			}

			out.append(subgroupsJoiner);
		}

		out.append("	</div>").append(LINE_SEP);
		out.append("</div>").append(LINE_SEP);
		return out.toString();
	}

	public static String printParameters(Collection<IdentifiedRelation> relations, int indent) {
		StringBuilder out = new StringBuilder();

		for (IdentifiedRelation relation : relations) {
			out.append(printParameters(relation, indent));
		}

		return out.toString();
	}
	
	public static String printParameters(Relation relation, int indent) {
		String relationID = "";

		if (relation instanceof IdentifiedRelation) {
			relationID = ((IdentifiedRelation)relation).getID();
		}
		
		String offset = getOffset(indent);

		StringBuilder keys = new StringBuilder();
		for (Key k : relation.getMinimalKeys()) {
			keys.append(k).append(";");
		}

		StringBuilder attributes = new StringBuilder();
		for (String a : relation.getAttributes()) {
			attributes.append(a).append(";");
		}

		StringBuilder dependencies = new StringBuilder();
		for (FunctionalDependency fd : relation.getFunctionalDependencies()) {
			dependencies.append(fd).append(";");
		}

		StringBuilder out = new StringBuilder();
		out.append(offset);
		out.append("<input");
		out.append(" type='hidden'");
		out.append(" id='").append(relationID).append("_ATTRIBUTES").append("'");
		out.append(" name='").append(relationID).append("_ATTRIBUTES").append("'");
		out.append(" value='").append(attributes).append("'/>");
		out.append(LINE_SEP);

		out.append(offset);
		out.append("<input");
		out.append(" type='hidden'");
		out.append(" id='").append(relationID).append("_KEYS").append("'");
		out.append(" name='").append(relationID).append("_KEYS").append("'");
		out.append(" value='").append(keys).append("'/>");
		out.append(LINE_SEP);

		out.append(offset);
		out.append("<input");
		out.append(" type='hidden'");
		out.append(" id='").append(relationID).append("_DEPENDENCIES").append("'");
		out.append(" name='").append(relationID).append("_DEPENDENCIES").append("'");
		out.append(" value='").append(dependencies).append("'/>");
		out.append(LINE_SEP);

		return out.toString();
	}

	public static String printKeysRow(Collection<Key> keys, String relationID, int indent, boolean editable, Locale locale) {
		return printKeysRow(keys, relationID, MESSAGE_SOURCE.getMessage("printkeysrow.title", null, locale), indent, editable, locale);
	}
	
	public static String printKeysRow(Collection<Key> keys, String relationID, String title, int indent, boolean editable, Locale locale) {
		String offset = getOffset(indent);

		StringJoiner contentJoiner = new StringJoiner("<br>");
		for (Object key : keys.toArray()) {
			contentJoiner.add(key.toString());
		}
		String content = contentJoiner.toString();

		String okMessage =  MESSAGE_SOURCE.getMessage("showpopup.ok", null, locale);
		String cancelMessage = MESSAGE_SOURCE.getMessage("showpopup.cancel", null, locale);
		String newkeyMessage = MESSAGE_SOURCE.getMessage("showaddkeypopup.newkey", null, locale);
		String noattributesMessage = MESSAGE_SOURCE.getMessage("showaddkeypopup.noattributes", null, locale);
		String addFunction = "onClick=\"javascript:showAddKeyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + newkeyMessage + "', '" + noattributesMessage + "');\"";
		String nokeysMessage = MESSAGE_SOURCE.getMessage("showdelkeypopup.nokeys", null, locale);
		String selectkeysMessage = MESSAGE_SOURCE.getMessage("showdelkeypopup.selectkeys", null, locale);
		String delFunction = "onClick=\"javascript:showDelKeyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + nokeysMessage + "', '" + selectkeysMessage + "');\"";

		StringBuilder out = new StringBuilder();

		out.append(generateButtons(offset, title, content, addFunction, delFunction, editable));

		return out.toString();
	}

	public static String printDependenciesRow(Collection<FunctionalDependency> dependencies, String relationID, int indent, boolean editable, Locale locale) {
		return printDependenciesRow(dependencies, relationID, MESSAGE_SOURCE.getMessage("printdependenciesrow.title", null, locale), indent, editable, locale);
	}

	public static String printDependenciesRow(Collection<FunctionalDependency> dependencies, String relationID, String title, int indent, boolean editable, Locale locale) {
		String offset = getOffset(indent);

		StringJoiner contentJoiner = new StringJoiner("<br>");
		for (Object dependency : dependencies.toArray()) {
			contentJoiner.add(dependency.toString().replaceAll("->", "&rarr;"));
		}
		String content = contentJoiner.toString();

		String okMessage =  MESSAGE_SOURCE.getMessage("showpopup.ok", null, locale);
		String cancelMessage = MESSAGE_SOURCE.getMessage("showpopup.cancel", null, locale);
		String newdependencyMessage = MESSAGE_SOURCE.getMessage("showadddependencypopup.newdependency", null, locale);
		String noattributesMessage = MESSAGE_SOURCE.getMessage("showadddependencypopup.noattributes", null, locale);
		String addFunction = "onClick=\"javascript:showAddDependencyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + newdependencyMessage + "', '" + noattributesMessage + "');\"";
		String nodependenciesMessage = MESSAGE_SOURCE.getMessage("showdeldependencypopup.nodependencies", null, locale);
		String removedependenciesMessage = MESSAGE_SOURCE.getMessage("showdeldependencypopup.removedependencies", null, locale);
		String delFunction = "onClick=\"javascript:showDelDependencyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + nodependenciesMessage + "', '" + removedependenciesMessage + "');\"";

		StringBuilder out = new StringBuilder();
		out.append(generateButtons(offset, title, content, addFunction, delFunction, editable));

		return out.toString();
	}
	
	public static String printDependenciesWithNormalformRow(Relation relation, String title, int indent, Locale locale) {
		StringBuilder out = new StringBuilder();
		
		NormalformAnalyzerConfig normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		normalformAnalyzerConfig.setCorrectMinimalKeys(KeysDeterminator.determineMinimalKeys(relation));
		normalformAnalyzerConfig.setRelation(relation);
		NormalformAnalysis analysis = NormalformAnalyzer.analyze(normalformAnalyzerConfig);
		Collection<FunctionalDependency> dependencies = relation.getFunctionalDependencies();
		
		String offset = getOffset(indent);
		
		String content = "";
		for (Object dependency : dependencies.toArray()) {
			content = content.concat(offset + "<tr>" + LINE_SEP);
			content = content.concat(offset + "	<td>" + LINE_SEP);
			content = content.concat(dependency.toString().replaceAll("->", "&rarr;"));
			content = content.concat(offset + "	</td>" + LINE_SEP);
			content = content.concat(offset + "	<td>" + LINE_SEP);
			content = content.concat(getNormalform((FunctionalDependency)dependency, analysis, locale));
			content = content.concat(offset + "	</td>" + LINE_SEP);
			content = content.concat(offset + "</tr>" + LINE_SEP);
		}

		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td class='label_td'>").append(title).append(":</td>").append(LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset).append("	<td class='content_td' nowrap>").append(LINE_SEP);
		out.append(offset).append("		<table>").append(LINE_SEP);
		out.append(content);
		out.append(offset).append("		</table>").append(LINE_SEP);
		out.append(offset).append("	</td>").append(LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset).append("	<td class='button_td'>").append(LINE_SEP);
		out.append(offset).append("	</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		return out.toString();
	}

	public static String getNormalform(FunctionalDependency dependency, NormalformAnalysis analysis, Locale locale) {
        for (NormalformViolation violation : analysis.getFirstNormalformViolations()) {
            if (violation.getFunctionalDependency().equals(dependency)) {
                return "";
            }
        }

        for (NormalformViolation violation : analysis.getSecondNormalformViolations()) {
            if (violation.getFunctionalDependency().equals(dependency)) {
                return MESSAGE_SOURCE.getMessage("getnormalform.first", null, locale);
            }
        }

        for (NormalformViolation violation : analysis.getThirdNormalformViolations()) {
            if (violation.getFunctionalDependency().equals(dependency)) {
                return MESSAGE_SOURCE.getMessage("getnormalform.second", null, locale);
            }
        }

        for (NormalformViolation violation : analysis.getBoyceCoddNormalformViolations()) {
            if (violation.getFunctionalDependency().equals(dependency)) {
                return MESSAGE_SOURCE.getMessage("getnormalform.third", null, locale);
            }
        }
		
		return MESSAGE_SOURCE.getMessage("getnormalform.boycecodd", null, locale);
	}
	
	public static String printAttributesRow(Collection<String> attributes, String baseRelationID, String currRelationID, int indent, boolean editable, Locale locale) {
		return printAttributesRow(attributes, baseRelationID, currRelationID, "Attributes", indent, editable, locale);
	}
	
	public static String printAttributesRow(Collection<String> attributes, String baseRelationID, String currRelationID, String title, int indent, boolean editable, Locale locale) {
		String offset = getOffset(indent);

		StringJoiner contentJoiner = new StringJoiner("<br>");
		for (Object attribute : attributes.toArray()) {
			contentJoiner.add(attribute.toString());
		}
		String content = contentJoiner.toString();
		
		String okMessage =  MESSAGE_SOURCE.getMessage("showpopup.ok", null, locale);
		String cancelMessage = MESSAGE_SOURCE.getMessage("showpopup.cancel", null, locale);
		String selectattributeMessage = MESSAGE_SOURCE.getMessage("showaddattributepopup.selectattribute", null, locale);
		String noattributesMessage = MESSAGE_SOURCE.getMessage("showaddattributepopup.noattributes", null, locale);
		String addFunction = "onClick=\"javascript:showAddAttributePopup('" + baseRelationID + "', '" + currRelationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + selectattributeMessage + "', '" + noattributesMessage + "');\"";
		String noattributesMessage2 = MESSAGE_SOURCE.getMessage("showdelattributepopup.noattributes", null, locale);
		String selectremoveattributeMessage = MESSAGE_SOURCE.getMessage("showdelattributepopup.selectremoveattribute", null, locale);
		String delFunction = "onClick=\"javascript:showDelAttributePopup('" + currRelationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + noattributesMessage2 + "', '" + selectremoveattributeMessage + "');\"";

		StringBuilder out = new StringBuilder();

		out.append(generateButtons(offset, title, content, addFunction, delFunction, editable));

		return out.toString();
	}

	public static String printMaxLostRow(NFSpecification spec, int indent, Locale locale) {
		String offset;
		String content;
		int maxLost;
		int nrDependencies;
		StringBuilder out = new StringBuilder();

		if (spec instanceof NormalizationSpecification) {
			maxLost = ((NormalizationSpecification)spec).getMaxLostDependencies();
			nrDependencies = spec.getBaseRelation().getFunctionalDependencies().size();
		} else if (spec instanceof DecomposeSpecification) {
			maxLost = ((DecomposeSpecification)spec).getMaxLostDependencies();
			nrDependencies = spec.getBaseRelation().getFunctionalDependencies().size();
		} else {
			return out.toString();
		}
		
		offset = getOffset(indent);
		
		if (maxLost > nrDependencies) {
			content = MESSAGE_SOURCE.getMessage("printmaxlostrow.unlimited", null, locale)+" (" + maxLost + " " + MESSAGE_SOURCE.getMessage("printmaxlostrow.exceedsdependencies", null, locale) + ", " + nrDependencies + ")";
		} else if (maxLost == nrDependencies) {
			content = MESSAGE_SOURCE.getMessage("printmaxlostrow.unlimited", null, locale)+" (" + maxLost + " " + MESSAGE_SOURCE.getMessage("printmaxlostrow.equalsdependencies", null, locale) + ", " + nrDependencies + ")";
		} else {
			content = Integer.toString(maxLost);
		}
		
		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td class='label_td'>").append(MESSAGE_SOURCE.getMessage("printmaxlostrow.maxlostdependencies", null, locale)).append(":</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='content_td' nowrap>").append(content).append("</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='button_td'>").append(LINE_SEP);
		out.append(offset).append("	</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		return out.toString();
	}
	
	public static String printTargetLevelRow(NormalizationSpecification spec, int indent, Locale locale) {
		if (spec == null) {
			return "";

		}

		NormalformLevel level = spec.getTargetLevel();

		String offset = getOffset(indent);

		String content = switch (level) {
			case FIRST -> MESSAGE_SOURCE.getMessage("printtargetlevelrow.first", null, locale);
			case SECOND -> MESSAGE_SOURCE.getMessage("printtargetlevelrow.second", null, locale);
			case THIRD -> MESSAGE_SOURCE.getMessage("printtargetlevelrow.third", null, locale);
			case BOYCE_CODD -> MESSAGE_SOURCE.getMessage("printtargetlevelrow.boycecodd", null, locale);
			default -> "";
		};

		StringBuilder out = new StringBuilder();

		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td class='label_td'>").append(MESSAGE_SOURCE.getMessage("printtargetlevelrow.normalform", null, locale)).append(":</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='content_td' nowrap>").append(content).append("</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='button_td'>").append(LINE_SEP);
		out.append(offset).append("	</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		return out.toString();
	}
	
	public static String printAssignmentForRBR(RBRSpecification spec, int indent, Locale locale) {
		String offset = getOffset(indent);

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Berechnen Sie die funktionalen Abhängigkeiten ").append(LINE_SEP);
			out.append(offset).append("	für das Subschema <strong>S</strong> der Relation <strong>").append(spec.getBaseRelation().getName()).append("</strong> ").append(LINE_SEP);
			out.append(offset).append("	mit den Funktionalen Abhängigkeiten gekennzeichnet durch das Präfix <strong>-></strong>.").append(LINE_SEP);
		} else {
			out.append(offset).append("	Let <strong>").append(spec.getBaseRelation().getName()).append("</strong> be a relation scheme with functional dependencies prefixed with <strong>-></strong>. ").append(LINE_SEP);
			out.append(offset).append("	Determine the set of functional dependencies ").append(LINE_SEP);
			out.append(offset).append("	for the sub-scheme <strong>S</strong>.").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(generateAttributeSetTableRowHTML(offset, spec.getBaseRelation().getName(), spec.getBaseRelation().getAttributes()));
		out.append(generateFunctionalDependencySetTableRowHTML(offset, spec.getBaseRelation().getFunctionalDependencies()));
		out.append(generateAttributeSetTableRowHTML(offset, "S", spec.getBaseAttributes()));
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForAttributeClosure(AttributeClosureSpecification spec, int indent, Locale locale) {
		String offset = getOffset(indent);

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Berechnen Sie die Hülle der Attribut-Kombination <strong>A</strong> ").append(LINE_SEP);
			out.append(offset).append("	bezüglich der Menge an Funktionalen Abhängigkeiten gekennzeichnet durch das Präfix <strong>-></strong> ").append(LINE_SEP);
			out.append(offset).append("	der Relation <strong>").append(spec.getBaseRelation().getName()).append("</strong>.").append(LINE_SEP);
		} else {
			out.append(offset).append("	Determine the attribute closure of the set of attributes <strong>A</strong> ").append(LINE_SEP);
			out.append(offset).append("	with respect to relation scheme <strong>").append(spec.getBaseRelation().getName()).append("</strong> and the set of ").append(LINE_SEP);
			out.append(offset).append("	functional dependencies prefixed with <strong>-></strong>.").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(generateAttributeSetTableRowHTML(offset, spec.getBaseRelation().getName(), spec.getBaseRelation().getAttributes()));
		out.append(generateFunctionalDependencySetTableRowHTML(offset, spec.getBaseRelation().getFunctionalDependencies()));
		out.append(generateAttributeSetTableRowHTML(offset, "A", spec.getBaseAttributes()));
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForNormalization(NormalizationSpecification spec, int indent, Locale locale) {
		String offset = getOffset(indent);

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Finden Sie eine <strong>verlustfreie Zerlegung</strong> der Relation ").append(LINE_SEP);
			out.append(offset).append("	<strong>").append(spec.getBaseRelation().getName()).append("</strong> mit den Funktionalen Abhängigkeiten gekennzeichnet durch das Präfix <strong>-></strong> in ").append(LINE_SEP);
			out.append(offset).append("	<strong>");
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset).append("erster ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset).append("zweiter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset).append("dritter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset).append("Boyce-Codd ");
			}
			out.append(offset).append("	Normalform</strong>. Geben Sie für jede Teilrelation in dieser Reihenfolge an:<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -Einen einzigartigen Relations-Namen (Suffix <strong>:</strong>),<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -Die Attribute in runden Klammern (kein Präfix vor der öffnenden Klammer),<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -Die ableitbaren Funktionalen Abhängigkeiten in runden Klammern (Präfix vor der öffnenden Klammer <strong>-></strong>) und<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -Die Schlüssel in runden Klammern (Präfix vor der öffnenden Klammer <strong>#</strong>).<br><br>").append(LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset).append("	Sie dürfen bei der Zerlegung <strong>keine</strong> Funktionale Abhängigkeit verlieren!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	Die Zerlegung muss <strong>nicht abhängigkeitstreu</strong> sein. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	Sie dürfen bei der Zerlegung maximal <strong>").append(spec.getMaxLostDependencies()).append("</strong> Funktionale ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	Abhängigkeit ").append(LINE_SEP);
				} else {
					out.append(offset).append("	Abhängigkeiten ").append(LINE_SEP);
				}
				out.append(offset).append("	verlieren!").append(LINE_SEP);
			}
		} else {
			out.append(offset).append("	Find a <strong>lossless decomposition</strong> of relation ").append(LINE_SEP);
			out.append(offset).append("	<strong>").append(spec.getBaseRelation().getName()).append("</strong> with functional dependencies prefixed with <strong>-></strong>. ").append(LINE_SEP);
			out.append(offset).append("	The decomposition must be in <strong>");
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset).append("first ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset).append("second ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset).append("third ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset).append("Boyce-Codd ");
			}
			out.append(offset).append("	normal form</strong>. For each relation fragment, specify (in this order):<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -A unique relation name (suffixed with <strong>:</strong>),<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -Its attributes in parentheses (no prefix before the opening parenthesis),<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -Its functional dependencies in parentheses (opening parenthesis prefixed with ->) and<br>").append(LINE_SEP);
			out.append(offset).append("&nbsp".repeat(4)).append(" -Its keys in parentheses (opening parenthesis prefixed with #).<br><br>").append(LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset).append("	You may not lose <strong>any </strong> functional dependency!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	The decomposition does not have to be <strong>dependency preserving</strong>. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	At most <strong>").append(spec.getMaxLostDependencies()).append("</strong> ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	functional dependency ").append(LINE_SEP);
				} else {
					out.append(offset).append("	functional dependencies ").append(LINE_SEP);
				}
				out.append(offset).append("	may be lost during decomposition!").append(LINE_SEP);
			}
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(generateAttributeSetTableRowHTML(offset, spec.getBaseRelation().getName(), spec.getBaseRelation().getAttributes()));
		out.append(generateFunctionalDependencySetTableRowHTML(offset, spec.getBaseRelation().getFunctionalDependencies()));
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForDecompose(DecomposeSpecification spec, int indent, Locale locale) {
		String offset = getOffset(indent);

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Wenden Sie den Decompose Algorithmus an, um eine <strong>verlustfreie Zerlegung</strong> der Relation ").append(LINE_SEP);
			out.append(offset).append("	<strong>").append(spec.getBaseRelation().getName()).append("</strong> mit den Funktionalen Abhängigkeiten gekennzeichnet durch das Präfix <strong>-></strong> in ").append(LINE_SEP);
			out.append(offset).append("	<strong>");
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset).append("erster ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset).append("zweiter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset).append("dritter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset).append("Boyce-Codd ");
			}
			out.append(offset).append("	Normalform</strong> zu finden. Geben Sie für jede Teilrelation die Schlüssel (Präfix <strong>#</strong>) ").append(LINE_SEP);
			out.append(offset).append("	und die ableitbaren Funktionalen Abhängigkeiten (Präfix <strong>-></strong>) an. ").append(LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset).append("	Sie dürfen bei der Zerlegung <strong>keine</strong> Funktionale Abhängigkeit verlieren!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	Die Zerlegung muss <strong>nicht abhängigkeitstreu</strong> sein. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	Sie dürfen bei der Zerlegung maximal <strong>").append(spec.getMaxLostDependencies()).append("</strong> Funktionale ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	Abhängigkeit ").append(LINE_SEP);
				} else {
					out.append(offset).append("	Abhängigkeiten ").append(LINE_SEP);
				}
				out.append(offset).append("	verlieren!").append(LINE_SEP);
			}
		} else {
			out.append(offset).append("	Let <strong>").append(spec.getBaseRelation().getName()).append("</strong> be a relation scheme with a set of functional dependencies prefixed with <strong>-></strong>. Determine a decomposition of <strong>").append(spec.getBaseRelation().getName()).append(" into ").append(LINE_SEP);
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset).append("first ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset).append("second ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset).append("third ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset).append("Boyce-Codd ");
			}
			out.append(offset).append("	normal form</strong> by means of the DECOMPOSE algorithm. Specify for each sub-scheme attributes, keys, and functional dependencies. ").append(LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset).append("	You may not lose <strong>any </strong> functional dependency!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	Note that functional dependencies may be lost during decomposition. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	At most <strong>").append(spec.getMaxLostDependencies()).append("</strong> ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	functional dependency ").append(LINE_SEP);
				} else {
					out.append(offset).append("	functional dependencies ").append(LINE_SEP);
				}
				out.append(offset).append("	may be lost during decomposition!").append(LINE_SEP);
			}
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(generateAttributeSetTableRowHTML(offset, spec.getBaseRelation().getName(), spec.getBaseRelation().getAttributes()));
		generateFunctionalDependencySetTableRowHTML(offset, spec.getBaseRelation().getFunctionalDependencies());
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForKeysDetermination(IdentifiedRelation spec, int indent, Locale locale) {
		String offset = getOffset(indent);

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Berechnen Sie alle Schlüssel der Relation <strong>").append(spec.getName()).append("</strong> auf ").append(LINE_SEP);
			out.append(offset).append("	Basis der Funktionalen Abhängigkeiten gekennzeichnet durch das Präfix <strong>-></strong>. ").append(LINE_SEP);
		} else {
			out.append(offset).append("	Let <strong>").append(spec.getName()).append("</strong> be a relation scheme with a set of functional ").append(LINE_SEP);
			out.append(offset).append("	dependencies prefixed with <strong>-></strong>. Determine all keys of <strong>").append(spec.getName()).append("</strong>. ").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(generateAttributeSetTableRowHTML(offset, spec.getName(), spec.getAttributes()));
		out.append(generateFunctionalDependencySetTableRowHTML(offset, spec.getFunctionalDependencies()));
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForMinimalCover(IdentifiedRelation spec, int indent, Locale locale) {
		String offset = getOffset(indent);

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Geben Sie für die Menge an Funktionalen Abhängigkeiten gekennzeichnet durch das Präfix <strong>-></strong> eine minimale  ").append(LINE_SEP);
			out.append(offset).append("	Überdeckung an. Streichen Sie alle redundanten Funktionalen Abhängigkeiten ").append(LINE_SEP);
			out.append(offset).append("	und alle redundanten Attribute in den linken Seiten der Funktionalen Abhängigkeiten. ").append(LINE_SEP);
		} else {
			out.append(offset).append("	Determine a minimal cover for the set of functional dependencies prefixed with <strong>-></strong>. Eliminate all redundant functional ").append(LINE_SEP);
			out.append(offset).append("	dependencies and redundant attributes at left hand sides of functional dependencies. ").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(generateFunctionalDependencySetTableRowHTML(offset, spec.getFunctionalDependencies()));
		out.append(offset).append("</table>").append(LINE_SEP);
		out.append(offset).append("<p>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForNormalFormDetermination(Relation spec, int indent, Locale locale) {
		String offset = getOffset(indent);

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Geben Sie an, in welcher Normalform sich die Relation <strong>").append(spec.getName()).append("</strong> ").append(LINE_SEP);
			out.append(offset).append("	mit den Funktionalen Abhängigkeiten gekennzeichnet durch das Präfix <strong>-></strong> befindet. ").append(LINE_SEP);
			out.append(offset).append("	Geben Sie weiters für jede Funktionale Abhängigkeit ").append(LINE_SEP);
			out.append(offset).append("	an, welche Normalform durch diese verletzt wird. ").append(LINE_SEP);
		} else {
			out.append(offset).append("	Determine the highest normal form that is fulfilled in relation scheme ").append(LINE_SEP);
			out.append(offset).append("	<strong>").append(spec.getName()).append("</strong> with the set of functional dependencies prefixed with <strong>-></strong>. ").append(LINE_SEP);
			out.append(offset).append("	Further, determine for each functional dependency ").append(LINE_SEP);
			out.append(offset).append("	the normal form that is violated by it. ").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(generateAttributeSetTableRowHTML(offset, spec.getName(), spec.getAttributes())).append(LINE_SEP);
		out.append(generateFunctionalDependencySetTableRowHTML(offset, spec.getFunctionalDependencies()));
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static void createHTMLSiteForDecompose(Collection<IdentifiedRelation> relations, Locale locale) {
		try (
			OutputStream out = new FileOutputStream("D:/DecomposeTest/decompose.html");
			PrintWriter writer = new PrintWriter(out)
		) {
			writer.println("<html>");

			writer.println("<head>");
			
			writer.println("	<link rel='stylesheet' type='text/css' href='./css/popup.css'>");
			writer.println("	<link rel='stylesheet' type='text/css' href='./css/etutor.css'>");
			writer.println("	<link rel='stylesheet' type='text/css' href='./css/relation.css'>");
			writer.println("	<link rel='stylesheet' type='text/css' href='./css/decompose.css'>");
	
			writer.println("	<script language='javascript' type='text/javascript' src='./js/popup.js'></script>");
			writer.println("	<script language='javascript' type='text/javascript' src='./js/decompose.js'></script>");
			writer.println("	<script language='javascript' type='text/javascript' src='./js/general_commands.js'></script>");
			
			writer.println("</head>");
			writer.println("<body>");
			
			writer.println("	<form id='commandForm' method='POST' action='/etutor/modules/rdbd/RDBDEditor'>");

			for (IdentifiedRelation i : relations){
				writer.println(printParameters(i, 2));
			}

			writer.println("	</form>");

			boolean isInnerNode;
			for (IdentifiedRelation currRelation : relations){
				isInnerNode = NFHelper.isInnerNode(currRelation.getID(), relations);
				writer.println(printDecomposeStep(currRelation, !isInnerNode, isInnerNode, true, locale));
			}
			
			writer.println("</body>");
			writer.println("</html>");

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private static String generateAttributeSetTableRowHTML(String offset, String setName, Collection<String> attributes) {
		StringBuilder out = new StringBuilder();

		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>").append(setName).append("</strong>: </td>").append(LINE_SEP);
		out.append(offset).append("		<td>(").append(generateSetHTML(attributes, ",&nbsp;")).append(")</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);

		return out.toString();
	}

	private static String generateFunctionalDependencySetTableRowHTML(String offset, Collection<FunctionalDependency> dependencies) {
		StringBuilder out = new StringBuilder();

		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>-></strong> </td>").append(LINE_SEP);
		out.append(offset).append("		<td>(").append(generateSetHTML(dependencies, "; ")).append(")</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);

		return out.toString();
	}

	private static String generateSetHTML(Collection<?> elements, String delimiter) {
		StringJoiner joiner = new StringJoiner(delimiter);

		for (Object element : elements) {
			joiner.add(element.toString());
		}

		return joiner.toString();
	}

	private static String generateButtons(String offset, String title, String content, String addFunction, String delFunction, boolean editable) {
		StringBuilder out = new StringBuilder();

		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td class='label_td'>").append(title).append(":</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='content_td' nowrap>").append(content).append("</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='button_td'>").append(LINE_SEP);
		if (editable){
			out.append(offset).append("		<input class='button' type='button' value='+' ").append(addFunction).append(" />").append(LINE_SEP);
			out.append(offset).append("		<input class='button' type='button' value='-' ").append(delFunction).append(" />").append(LINE_SEP);
		}
		out.append(offset).append("	</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);

		return  out.toString();
	}

	private static String getOffset(int indent) {
		return "\t".repeat(Math.max(0, indent));
	}
}
