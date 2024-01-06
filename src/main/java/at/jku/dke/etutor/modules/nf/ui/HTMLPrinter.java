package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzer;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformAnalyzerConfig;
import at.jku.dke.etutor.modules.nf.analysis.normalform.NormalformViolation;
import at.jku.dke.etutor.modules.nf.analysis.normalization.KeysDeterminator;
import at.jku.dke.etutor.modules.nf.i18n.NFMessageSource;
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
import org.springframework.context.MessageSourceAware;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.Vector;

/**
 * Helper class for printing all types of specification implementations
 * of the RDBD module.
 * 
 * @author Georg Nitsche (10.01.2006)
 * @author Christian Fischer (25.12.2009)
 *
 */
public class HTMLPrinter implements MessageSourceAware {

	public static final String LINE_SEP = System.getProperty("line.separator");
	public static final int OFFSET = 15;
	
	private static MessageSource messageSource = new NFMessageSource();

	@SuppressWarnings("static-access")
	// @Required // deprecated (Gerald Wimmer, 2024-01-03)
	public void setMessageSource(MessageSource messageSource) {
		// this.messageSource = messageSource;
	}

	public static String printDecomposeStep(IdentifiedRelation relation, boolean showSplitButton, boolean showRemoveButton, boolean editable, Locale locale) throws IOException{
		StringJoiner attributesJoiner = new StringJoiner(" ");

		for (String s : relation.getAttributesArray()) {
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
		out.append("					<td class='label_td'><b>R").append(relation.getID()).append("</b>:</td>").append(LINE_SEP);
		out.append("					<td class='attributes_td'>").append(attributesString).append("</td>").append(LINE_SEP);

		if (showSplitButton || showRemoveButton){
			out.append("					<td class='button_td'>").append(LINE_SEP);
			String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
			String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
			if (showSplitButton){
				String specifyattributesMessage = messageSource.getMessage("showsplitrelationpopup.specifyattributes", null, locale);
				String noattributesMessage = messageSource.getMessage("showsplitrelationpopup.noattributes", null, locale);
				out.append("						<input type='button' value='").append(messageSource.getMessage("printdecomposestep.split", null, locale)).append("' class='decompose_button' onClick=\"javascript:showSplitRelationPopup('").append(relation.getID()).append("', '%%IDPREFIX%%', '").append(okMessage).append("', '").append(cancelMessage).append("', '").append(specifyattributesMessage).append("', '").append(noattributesMessage).append("');\" />").append(LINE_SEP);
			}
			if (showRemoveButton){
				String removerelationsMessage = messageSource.getMessage("showdelsubrelationspopup.removerelations", null, locale);
				out.append("						<input type='button' value='").append(messageSource.getMessage("printdecomposestep.remove", null, locale)).append("' class='decompose_button' onClick=\"javascript:showDelSubRelationsPopup('").append(relation.getID()).append("', '%%IDPREFIX%%', '").append(okMessage).append("', '").append(cancelMessage).append("', '").append(removerelationsMessage).append("');\" />").append(LINE_SEP);
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

	public static String printReport(NFReport report, int displayIndent, int codeIndent, Locale locale) throws IOException{
		StringBuilder out = new StringBuilder();

		out.append("<div style='margin-left:").append(displayIndent * OFFSET).append("px;'>").append(LINE_SEP);
		out.append("	<table class='report_table' cellpadding='0px' cellspacing='0px'>").append(LINE_SEP);

		//PRINT PROLOGUE
		if (report.showPrologue()){
			out.append("		<tr>").append(LINE_SEP);
			out.append("			<td>").append(LINE_SEP);
			out.append("				<div class='section'>").append(LINE_SEP);
			out.append("					<div class='section_caption'>").append(messageSource.getMessage("printreport.result", null, locale)).append("</div>").append(LINE_SEP);
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
			out.append("					<div class='section_caption'>").append(messageSource.getMessage("printreport.reports", null, locale)).append("</div>").append(LINE_SEP);
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
	
	public static String printErrorReport(ErrorReport errorReport, int displayIndent, int codeIndent) throws IOException{
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
	
	public static String printErrorReportGroup(ErrorReportGroup group, int displayIndent, int codeIndent) throws IOException{
		Vector<ErrorReport> errorReports = group.getErrorReports();
		Vector<ErrorReportGroup> subGroups = group.getSubErrorReportGroups();
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

	public static String printParameters(Collection<IdentifiedRelation> relations, int indent) throws IOException {
		StringBuilder out = new StringBuilder();

		for (IdentifiedRelation relation : relations) {
			out.append(printParameters(relation, indent));
		}

		return out.toString();
	}
	
	public static String printParameters(Relation relation, int indent) throws IOException {
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

	public static String printKeysRow(Collection<Key> keys, String relationID, int indent, boolean editable, Locale locale) throws IOException  {
		return printKeysRow(keys, relationID, messageSource.getMessage("printkeysrow.title", null, locale), indent, editable, locale);
	}
	
	public static String printKeysRow(Collection<Key> keys, String relationID, String title, int indent, boolean editable, Locale locale) throws IOException  {
		String offset = getOffset(indent);

		StringJoiner contentJoiner = new StringJoiner("<br>");
		for (Object key : keys.toArray()) {
			contentJoiner.add(key.toString());
		}
		String content = contentJoiner.toString();

		String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
		String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
		String newkeyMessage = messageSource.getMessage("showaddkeypopup.newkey", null, locale);
		String noattributesMessage = messageSource.getMessage("showaddkeypopup.noattributes", null, locale);
		String addFunction = "onClick=\"javascript:showAddKeyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + newkeyMessage + "', '" + noattributesMessage + "');\"";
		String nokeysMessage = messageSource.getMessage("showdelkeypopup.nokeys", null, locale);
		String selectkeysMessage = messageSource.getMessage("showdelkeypopup.selectkeys", null, locale);
		String delFunction = "onClick=\"javascript:showDelKeyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + nokeysMessage + "', '" + selectkeysMessage + "');\"";

		StringBuilder out = new StringBuilder();

		out.append(generateButtons(offset, title, content, addFunction, delFunction, editable));

		return out.toString();
	}

	public static String printDependenciesRow(Collection<FunctionalDependency> dependencies, String relationID, int indent, boolean editable, Locale locale) throws IOException {
		return printDependenciesRow(dependencies, relationID, messageSource.getMessage("printdependenciesrow.title", null, locale), indent, editable, locale);
	}

	public static String printDependenciesRow(Collection<FunctionalDependency> dependencies, String relationID, String title, int indent, boolean editable, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner contentJoiner = new StringJoiner("<br>");
		for (Object dependency : dependencies.toArray()) {
			contentJoiner.add(dependency.toString().replaceAll("->", "&rarr;"));
		}
		String content = contentJoiner.toString();

		String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
		String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
		String newdependencyMessage = messageSource.getMessage("showadddependencypopup.newdependency", null, locale);
		String noattributesMessage = messageSource.getMessage("showadddependencypopup.noattributes", null, locale);
		String addFunction = "onClick=\"javascript:showAddDependencyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + newdependencyMessage + "', '" + noattributesMessage + "');\"";
		String nodependenciesMessage = messageSource.getMessage("showdeldependencypopup.nodependencies", null, locale);
		String removedependenciesMessage = messageSource.getMessage("showdeldependencypopup.removedependencies", null, locale);
		String delFunction = "onClick=\"javascript:showDelDependencyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + nodependenciesMessage + "', '" + removedependenciesMessage + "');\"";

		StringBuilder out = new StringBuilder();
		out.append(generateButtons(offset, title, content, addFunction, delFunction, editable));

		return out.toString();
	}
	
	public static String printDependenciesWithNormalformRow(Relation relation, String title, int indent, Locale locale) throws IOException {
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
                return messageSource.getMessage("getnormalform.first", null, locale);
            }
        }

        for (NormalformViolation violation : analysis.getThirdNormalformViolations()) {
            if (violation.getFunctionalDependency().equals(dependency)) {
                return messageSource.getMessage("getnormalform.second", null, locale);
            }
        }

        for (NormalformViolation violation : analysis.getBoyceCoddNormalformViolations()) {
            if (violation.getFunctionalDependency().equals(dependency)) {
                return messageSource.getMessage("getnormalform.third", null, locale);
            }
        }
		
		return messageSource.getMessage("getnormalform.boycecodd", null, locale);
	}
	
	public static String printAttributesRow(Collection<String> attributes, String baseRelationID, String currRelationID, int indent, boolean editable, Locale locale) throws IOException {
		return printAttributesRow(attributes, baseRelationID, currRelationID, "Attributes", indent, editable, locale);
	}
	
	public static String printAttributesRow(Collection<String> attributes, String baseRelationID, String currRelationID, String title, int indent, boolean editable, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner contentJoiner = new StringJoiner("<br>");
		for (Object attribute : attributes.toArray()) {
			contentJoiner.add(attribute.toString());
		}
		String content = contentJoiner.toString();
		
		String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
		String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
		String selectattributeMessage = messageSource.getMessage("showaddattributepopup.selectattribute", null, locale);
		String noattributesMessage = messageSource.getMessage("showaddattributepopup.noattributes", null, locale);
		String addFunction = "onClick=\"javascript:showAddAttributePopup('" + baseRelationID + "', '" + currRelationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + selectattributeMessage + "', '" + noattributesMessage + "');\"";
		String noattributesMessage2 = messageSource.getMessage("showdelattributepopup.noattributes", null, locale);
		String selectremoveattributeMessage = messageSource.getMessage("showdelattributepopup.selectremoveattribute", null, locale);
		String delFunction = "onClick=\"javascript:showDelAttributePopup('" + currRelationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + noattributesMessage2 + "', '" + selectremoveattributeMessage + "');\"";

		StringBuilder out = new StringBuilder();

		out.append(generateButtons(offset, title, content, addFunction, delFunction, editable));

		return out.toString();
	}

	public static String printMaxLostRow(NFSpecification spec, int indent, Locale locale) throws IOException {
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
			content = messageSource.getMessage("printmaxlostrow.unlimited", null, locale)+" (" + maxLost + " " + messageSource.getMessage("printmaxlostrow.exceedsdependencies", null, locale) + ", " + nrDependencies + ")";
		} else if (maxLost == nrDependencies) {
			content = messageSource.getMessage("printmaxlostrow.unlimited", null, locale)+" (" + maxLost + " " + messageSource.getMessage("printmaxlostrow.equalsdependencies", null, locale) + ", " + nrDependencies + ")";
		} else {
			content = Integer.toString(maxLost);
		}
		
		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td class='label_td'>").append(messageSource.getMessage("printmaxlostrow.maxlostdependencies", null, locale)).append(":</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='content_td' nowrap>").append(content).append("</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='button_td'>").append(LINE_SEP);
		out.append(offset).append("	</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		return out.toString();
	}
	
	public static String printTargetLevelRow(NFSpecification spec, int indent, Locale locale) throws IOException {
		String offset;
		StringBuilder out = new StringBuilder();

		NormalformLevel level;
		if (spec instanceof NormalizationSpecification) {
			level = ((NormalizationSpecification)spec).getTargetLevel();
		} else if (spec instanceof DecomposeSpecification) {
			level = ((DecomposeSpecification)spec).getTargetLevel();
		} else {
			return out.toString();
		}
		
		offset = getOffset(indent);

		String content = switch (level) {
			case FIRST -> messageSource.getMessage("printtargetlevelrow.first", null, locale);
			case SECOND -> messageSource.getMessage("printtargetlevelrow.second", null, locale);
			case THIRD -> messageSource.getMessage("printtargetlevelrow.third", null, locale);
			case BOYCE_CODD -> messageSource.getMessage("printtargetlevelrow.boycecodd", null, locale);
			default -> "";
		};
		
		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td class='label_td'>").append(messageSource.getMessage("printtargetlevelrow.normalform", null, locale)).append(":</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='content_td' nowrap>").append(content).append("</td>").append(LINE_SEP);
		out.append(offset).append("	<td class='button_td'>").append(LINE_SEP);
		out.append(offset).append("	</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		return out.toString();
	}
	
	public static String printAssignmentForRBR(RBRSpecification spec, int indent, Locale locale) throws IOException {
		String offset;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		StringJoiner relationAttributes = new StringJoiner("&nbsp;");
		for (String a : spec.getBaseRelation().getAttributes()){
			relationAttributes.add(a);
		}

		StringJoiner dependencies = new StringJoiner(", ");
        for (FunctionalDependency functionalDependency : spec.getBaseRelation().getFunctionalDependencies()) {
            dependencies.add(functionalDependency.toString().replaceAll("->", "&rarr;"));
		}

		StringJoiner baseAttributes = new StringJoiner("&nbsp;");
        for (String s : spec.getBaseAttributes()) {
            baseAttributes.add(s);
        }

		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Berechnen Sie die funktionalen Abh&auml;ngigkeiten <strong>F<sub><code>S</code></sub></strong> ").append(LINE_SEP);
			out.append(offset).append("	f&uuml;r das Subschema <strong>S</strong> der Relation <strong>R</strong> ").append(LINE_SEP);
			out.append(offset).append("	mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong>.").append(LINE_SEP);
		} else {
			out.append(offset).append("	Let be <strong>R</strong> a relation scheme with functional dependencies <strong>F</strong>. ").append(LINE_SEP);
			out.append(offset).append("	Determine the set of functional dependencies <strong>F<sub><code>S</code></sub></strong> ").append(LINE_SEP);
			out.append(offset).append("	for the sub-scheme <strong>S</strong>.").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>R</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(relationAttributes).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>F</strong> = </td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(dependencies).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>S</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>(").append(baseAttributes).append(")</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForAttributeClosure(AttributeClosureSpecification spec, int indent, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner attributesJoiner = new StringJoiner("&nbsp;");
		for (String a : spec.getBaseRelation().getAttributes()){
			attributesJoiner.add(a);
		}
		String relationAttributes = attributesJoiner.toString();

		StringJoiner dependenciesJoiner = new StringJoiner(", ");
		for (FunctionalDependency functionalDependency : spec.getBaseRelation().getFunctionalDependencies()) {
            dependenciesJoiner.add(functionalDependency.toString().replaceAll("->", "&rarr;"));
		}
		String dependencies = dependenciesJoiner.toString();

		StringJoiner baseAttributesJoiner = new StringJoiner("&nbsp;");
		for (String s : spec.getBaseAttributes()) {
            baseAttributesJoiner.add(s);
		}
		String baseAttributes = baseAttributesJoiner.toString();

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Berechnen Sie die H&uuml;lle der Attribut Kombination <strong>A</strong> ").append(LINE_SEP);
			out.append(offset).append("	bez&uuml;glich der Menge an Funktionalen Abh&auml;ngigkeiten <strong>F</strong> ").append(LINE_SEP);
			out.append(offset).append("	der Relation <strong>R</strong>.").append(LINE_SEP);
		} else {
			out.append(offset).append("	Determine the attribute closure of the set of attributes <strong>A</strong> ").append(LINE_SEP);
			out.append(offset).append("	with respect to relation scheme <strong>R</strong> and the set of ").append(LINE_SEP);
			out.append(offset).append("	functional dependencies <strong>F</strong>.").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>R</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(relationAttributes).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>F</strong> = </td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(dependencies).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>A</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>(").append(baseAttributes).append(")</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForNormalization(NormalizationSpecification spec, int indent, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner attributesJoiner = new StringJoiner("&nbsp;");
		for (String a : spec.getBaseRelation().getAttributes()){
			attributesJoiner.add(a);
		}
		String relationAttributes = attributesJoiner.toString();

		StringJoiner dependenciesJoiner = new StringJoiner(", ");
		for (FunctionalDependency functionalDependency : spec.getBaseRelation().getFunctionalDependencies()) {
            dependenciesJoiner.add(functionalDependency.toString().replaceAll("->", "&rarr;"));
		}
		String dependencies = dependenciesJoiner.toString();

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Finden Sie eine <b>verlustfreie Zerlegung</b> der Relation ").append(LINE_SEP);
			out.append(offset).append("	<strong>").append(spec.getBaseRelation().getName()).append("</strong> mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong> in ").append(LINE_SEP);
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
			out.append(offset).append("	Normalform</strong>. Geben Sie f&uuml;r jede Teilrelation die Schl&uuml;ssel ").append(LINE_SEP);
			out.append(offset).append("	und die von <strong>F</strong> ableitbaren Funktionalen Abh&auml;ngigkeiten an.  ").append(LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset).append("	Sie d&uuml;rfen bei der Zerlegung <b>keine</b> Funktionale Abh&auml;ngigkeit verlieren!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	Die Zerlegung muss <b>nicht ab&auml;ngigkeitstreu</b> sein. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	Sie d&uuml;rfen bei der Zerlegung maximal <b>").append(spec.getMaxLostDependencies()).append("</b> Funktionale ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	Abh&auml;ngigkeit ").append(LINE_SEP);
				} else {
					out.append(offset).append("	Abh&auml;ngigkeiten ").append(LINE_SEP);
				}
				out.append(offset).append("	verlieren!").append(LINE_SEP);
			}
		} else {
			out.append(offset).append("	Find a <b>lossless decomposition</b> of relation ").append(LINE_SEP);
			out.append(offset).append("	<strong>").append(spec.getBaseRelation().getName()).append("</strong> with function dependencies <strong>F</strong>. ").append(LINE_SEP);
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
			out.append(offset).append("	normal form</strong>. Specify keys and functional dependencies derived from <strong>F</strong> for each relation fragment. ").append(LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset).append("	You may not loose <b>any </b> functional dependency!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	The decomposition does not have to be <b>dependency preserving</b>. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	At most <b>").append(spec.getMaxLostDependencies()).append("</b> ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	functinal dependency ").append(LINE_SEP);
				} else {
					out.append(offset).append("	functional dependencies ").append(LINE_SEP);
				}
				out.append(offset).append("	may be lost during decomposition!").append(LINE_SEP);
			}
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>R</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(relationAttributes).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>F</strong> = </td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(dependencies).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForDecompose(DecomposeSpecification spec, int indent, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner attributesJoiner = new StringJoiner("&nbsp;");
		for (String a : spec.getBaseRelation().getAttributes()){
			attributesJoiner.add(a);
		}
		String relationAttributes = attributesJoiner.toString();

		StringJoiner dependenciesJoiner = new StringJoiner(", ");
		for (FunctionalDependency functionalDependency : spec.getBaseRelation().getFunctionalDependencies()) {
            dependenciesJoiner.add(functionalDependency.toString().replaceAll("->", "&rarr;"));
		}
		String dependencies = dependenciesJoiner.toString();

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Wenden Sie den Decompose Algorithmus an, um eine <b>verlustfreie Zerlegung</b> der Relation ").append(LINE_SEP);
			out.append(offset).append("	<strong>").append(spec.getBaseRelation().getName()).append("</strong> mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong> in ").append(LINE_SEP);
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
			out.append(offset).append("	Normalform</strong> zu finden. Geben Sie f&uuml;r jede Teilrelation die Schl&uuml;ssel ").append(LINE_SEP);
			out.append(offset).append("	und die von <strong>F</strong> ableitbaren Funktionalen Abh&auml;ngigkeiten an.  ").append(LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset).append("	Sie d&uuml;rfen bei der Zerlegung <b>keine</b> Funktionale Abh&auml;ngigkeit verlieren!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	Die Zerlegung muss <b>nicht ab&auml;ngigkeitstreu</b> sein. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	Sie d&uuml;rfen bei der Zerlegung maximal <b>").append(spec.getMaxLostDependencies()).append("</b> Funktionale ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	Abh&auml;ngigkeit ").append(LINE_SEP);
				} else {
					out.append(offset).append("	Abh&auml;ngigkeiten ").append(LINE_SEP);
				}
				out.append(offset).append("	verlieren!").append(LINE_SEP);
			}
		} else {
			out.append(offset).append("	Let be <strong>").append(spec.getBaseRelation().getName()).append("</strong> a relation scheme with a set of functional dependencies <strong>F</strong>. Determine a decomposition of <strong>").append(spec.getBaseRelation().getName()).append(" into ").append(LINE_SEP);
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
				out.append(offset).append("	You may not loose <b>any </b> functional dependency!").append(LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset).append("	Note that functional dependencies may be lost during decomposition. ").append(LINE_SEP);
			} else {
				out.append(offset).append("	At most <b>").append(spec.getMaxLostDependencies()).append("</b> ").append(LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset).append("	functinal dependency ").append(LINE_SEP);
				} else {
					out.append(offset).append("	functional dependencies ").append(LINE_SEP);
				}
				out.append(offset).append("	may be lost during decomposition!").append(LINE_SEP);
			}
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>R</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(relationAttributes).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>F</strong> = </td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(dependencies).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForKeysDetermination(IdentifiedRelation spec, int indent, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner attributesJoiner = new StringJoiner("&nbsp;");
		for (String a : spec.getAttributes()){
			attributesJoiner.add(a);
		}
		String relationAttributes = attributesJoiner.toString();

		StringJoiner dependenciesJoiner = new StringJoiner(", ");
		for (FunctionalDependency functionalDependency : spec.getFunctionalDependencies()) {
            dependenciesJoiner.add(functionalDependency.toString().replaceAll("->", "&rarr;"));
		}
		String dependencies = dependenciesJoiner.toString();

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Berechnen Sie alle Schl&uuml;ssel der Relation <strong>R</strong> auf ").append(LINE_SEP);
			out.append(offset).append("	Basis der Funktionalen Abh&auml;ngigkeiten <strong>F</strong>. ").append(LINE_SEP);
		} else {
			out.append(offset).append("	Let be <strong>R</strong> a relation scheme with a set of functional ").append(LINE_SEP);
			out.append(offset).append("	dependencies <strong>F</strong>. Determine all keys of <strong>R</strong>. ").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>R</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(relationAttributes).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>F</strong> = </td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(dependencies).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForMinimalCover(IdentifiedRelation spec, int indent, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner dependenciesJoiner = new StringJoiner(", ");
		for (FunctionalDependency functionalDependency : spec.getFunctionalDependencies()) {
            dependenciesJoiner.add(functionalDependency.toString().replaceAll("->", "&rarr;"));
		}
		String dependencies = dependenciesJoiner.toString();

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Geben Sie f&uuml;r die Menge <b>F</b> an Funktionalen Abh&auml;ngigkeiten eine minimale  ").append(LINE_SEP);
			out.append(offset).append("	�berdeckung an. Streichen Sie alle redundanten Funktionalen Abh&auml;ngigkeiten ").append(LINE_SEP);
			out.append(offset).append("	und alle redundanten Attribute in den linken Seiten der Funktionalen Abh&auml;ngigkeiten. ").append(LINE_SEP);
		} else {
			out.append(offset).append("	Indicate a minimal cover for <b>F</b>. Eliminate all redundant functional ").append(LINE_SEP);
			out.append(offset).append("	dependencies and redundant attributes at left hand sides of functional dependencies. ").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>F</strong> = </td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(dependencies).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		out.append(offset).append("<p>").append(LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForNormalformDetermination(Relation spec, int indent, Locale locale) throws IOException {
		String offset = getOffset(indent);

		StringJoiner attributesJoiner = new StringJoiner("&nbsp;");
		for (String a : spec.getAttributes()){
			attributesJoiner.add(a);
		}
		String relationAttributes = attributesJoiner.toString();

		StringJoiner dependenciesJoiner = new StringJoiner(", ");
		for (FunctionalDependency functionalDependency : spec.getFunctionalDependencies()) {
            dependenciesJoiner.add(functionalDependency.toString().replaceAll("->", "&rarr;"));
		}
		String dependencies = dependenciesJoiner.toString();

		StringBuilder out = new StringBuilder();
		out.append(offset).append("<p>").append(LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset).append("	Geben Sie an, in welcher Normalform sich die Relation <strong>R</strong> ").append(LINE_SEP);
			out.append(offset).append("	mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong> befindet. ").append(LINE_SEP);
			out.append(offset).append("	Geben Sie weiters f&uuml;r jede Funktionale Abh&auml;ngigkeit <strong>F<sub>i</sub></strong> ").append(LINE_SEP);
			out.append(offset).append("	an, welche Normalform durch <strong>F<sub>i</sub></strong> verletzt wird. ").append(LINE_SEP);
		} else {
			out.append(offset).append("	Determine the highest normal form that is fulfilled in relation scheme ").append(LINE_SEP);
			out.append(offset).append("	<strong>R</strong> with the set of functional dependencies <strong>F</strong>. ").append(LINE_SEP);
			out.append(offset).append("	Further, determine for each functional dependency <strong>F<sub>i</sub></strong> ").append(LINE_SEP);
			out.append(offset).append("	the normal form that is violated by <strong>F<sub>i</sub></strong>. ").append(LINE_SEP);
		}
		out.append(offset).append("</p>").append(LINE_SEP);
		out.append(offset).append("<table rules=\"none\" frame=\"void\">").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>R</strong></td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(relationAttributes).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("	<tr>").append(LINE_SEP);
		out.append(offset).append("		<td><strong>F</strong> = </td>").append(LINE_SEP);
		out.append(offset).append("		<td>{").append(dependencies).append("}</td>").append(LINE_SEP);
		out.append(offset).append("	</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}
	
	/*public static String printSpecificationSyntax(SpecificationParser parser, int indent, Locale locale) throws IOException {
		String offset;
		String qualBase;
		String qualRel;
		String qualDep;
		StringBuilder out = new StringBuilder();
		
		qualBase = parser.getQualifier(SpecificationParser.QUALIFIER_ATTRIBUTES_BASE);
		qualRel = parser.getQualifier(SpecificationParser.QUALIFIER_ATTRIBUTES_RELATION);
		qualDep = parser.getQualifier(SpecificationParser.QUALIFIER_DEPENDENCIES);
		
		if (qualBase == null && qualRel == null && qualDep == null) {
			return out.toString();
		}
		
		offset = getOffset(indent);
		out.append(offset).append("<table>").append(LINE_SEP);
		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.specification", null, locale)).append("</span></td>").append(LINE_SEP);
		out.append(offset).append("	<td>=</td>").append(LINE_SEP);
		out.append(offset).append("	<td>").append(LINE_SEP);
		out.append(offset).append("		").append(qualRel != null ? "<span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attributes", null, locale) + "</span> " : "").append(LINE_SEP);
		out.append(offset).append("		").append(qualDep != null ? "<span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.dependencies", null, locale) + "</span> " : "").append(LINE_SEP);
		out.append(offset).append("		").append(qualBase != null ? "<span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.baseattributes", null, locale) + "</span></td>" : "").append(LINE_SEP);
		out.append(offset).append("	</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		if (qualRel != null) {
			out.append(offset).append("<tr>").append(LINE_SEP);
			out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.attributes", null, locale)).append("</span></td>").append(LINE_SEP);
			out.append(offset).append("	<td>=</td>").append(LINE_SEP);
			out.append(offset).append("	<td>&quot;<span class='spec_atom'>").append(qualRel).append("</span>&quot; &quot;<span class='spec_atom'>{</span>&quot; [ <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.attributelist", null, locale)).append("</span> ] &quot;<span class='spec_atom'>}</span>&quot;</td>").append(LINE_SEP);
			out.append(offset).append("</tr>").append(LINE_SEP);
		}
		if (qualBase != null) {
			out.append(offset).append("<tr>").append(LINE_SEP);
			out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.baseattributes", null, locale)).append("</span></td>").append(LINE_SEP);
			out.append(offset).append("	<td>=</td>").append(LINE_SEP);
			out.append(offset).append("	<td>&quot;<span class='spec_atom'>").append(qualBase).append("</span>&quot; &quot;<span class='spec_atom'>{</span>&quot; [ <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.attributelist", null, locale)).append("</span> ] &quot;<span class='spec_atom'>}</span>&quot;</td>").append(LINE_SEP);
			out.append(offset).append("</tr>").append(LINE_SEP);
		}
		if (qualDep != null) {
			out.append(offset).append("<tr>").append(LINE_SEP);
			out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.dependencies", null, locale)).append("</span></td>").append(LINE_SEP);
			out.append(offset).append("	<td>=</td>").append(LINE_SEP);
			out.append(offset).append("	<td>&quot;<span class='spec_atom'>").append(qualDep).append("</span>&quot; &quot;<span class='spec_atom'>{</span>&quot; [ <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.dependencylist", null, locale)).append("</span> ] &quot;<span class='spec_atom'>}</span>&quot;</td>").append(LINE_SEP);
			out.append(offset).append("</tr>").append(LINE_SEP);
			out.append(offset).append("<tr>").append(LINE_SEP);
			out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.dependencylist", null, locale)).append("</span></td>").append(LINE_SEP);
			out.append(offset).append("	<td>=</td>").append(LINE_SEP);
			out.append(offset).append("	<td><span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.dependency", null, locale)).append("</span> { &quot;, &quot; <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.dependency", null, locale)).append("</span> }</td>").append(LINE_SEP);
			out.append(offset).append("</tr>").append(LINE_SEP);
			out.append(offset).append("<tr>").append(LINE_SEP);
			out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.dependency", null, locale)).append("</span></td>").append(LINE_SEP);
			out.append(offset).append("	<td>=</td>").append(LINE_SEP);
			out.append(offset).append("	<td><span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.attributelist", null, locale)).append("</span> &quot;<span class='spec_atom'><code>-&gt;</code></span>&quot; <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.attributelist", null, locale)).append("</span></td>").append(LINE_SEP);
			out.append(offset).append("</tr>").append(LINE_SEP);
		}
		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.attributelist", null, locale)).append("</span></td>").append(LINE_SEP);
		out.append(offset).append("	<td>=</td>").append(LINE_SEP);
		out.append(offset).append("	<td><span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.attribute", null, locale)).append("</span> { &quot;&nbsp;&quot; <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.attribute", null, locale)).append("</span> }</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.attribute", null, locale)).append("</span></td>").append(LINE_SEP);
		out.append(offset).append("	<td>=</td>").append(LINE_SEP);
		out.append(offset).append("	<td><span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.character", null, locale)).append("</span> { <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.character", null, locale)).append("</span> }</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		out.append(offset).append("<tr>").append(LINE_SEP);
		out.append(offset).append("	<td><span class='spec_def'>").append(messageSource.getMessage("printspecificationsyntax.character", null, locale)).append("</span></td>").append(LINE_SEP);
		out.append(offset).append("	<td>=</td>").append(LINE_SEP);
		out.append(offset).append("	<td><span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.letter", null, locale)).append("</span> | <span class='spec_def_ref'>").append(messageSource.getMessage("printspecificationsyntax.digit", null, locale)).append("</span> | &quot;<span class='spec_atom'>_</span>&quot;</td>").append(LINE_SEP);
		out.append(offset).append("</tr>").append(LINE_SEP);
		out.append(offset).append("</table>").append(LINE_SEP);
		return out.toString();
	}*/

	public static void createHTMLSiteForReport(NFReport report){

		PrintWriter writer = null;
		FileOutputStream out = null;
		try{
			out = new FileOutputStream("D:/report.html");
			writer = new PrintWriter(out);	
			
			writer.println("<html>");

			writer.println("<head>");
			writer.println("	<link rel='stylesheet' type='text/css' href='etutor.css'>");
			writer.println("	<link rel='stylesheet' type='text/css' href='report.css'>");
			writer.println("</head>");
			writer.println("<body>");
				
			//printReport(writer, report, 0, 0);
			
			writer.println("</body>");
			writer.println("</html>");

		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try{
				if (writer != null){
					writer.close();
				}
				if (out != null){
					out.close();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void createHTMLSiteForDecompose(Collection<IdentifiedRelation> relations, Locale locale){

		PrintWriter writer = null;
		FileOutputStream out = null;
		try{
			out = new FileOutputStream("D:/DecomposeTest/decompose.html");
			writer = new PrintWriter(out);	
			
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
				isInnerNode = RDBDHelper.isInnerNode(currRelation.getID(), relations);
				writer.println(printDecomposeStep(currRelation, !isInnerNode, isInnerNode, true, locale));
			}
			
			writer.println("</body>");
			writer.println("</html>");

		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try{
				if (writer != null){
					writer.close();
				}
				if (out != null){
					out.close();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
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
