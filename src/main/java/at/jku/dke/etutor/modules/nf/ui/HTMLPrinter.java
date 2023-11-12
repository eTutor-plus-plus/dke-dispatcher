package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.*;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.Relation;
import at.jku.dke.etutor.modules.nf.report.ErrorReport;
import at.jku.dke.etutor.modules.nf.report.ErrorReportGroup;
import at.jku.dke.etutor.modules.nf.report.Report;
import at.jku.dke.etutor.modules.nf.report.ReportAtomType;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

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
	
	private static MessageSource messageSource;

	@SuppressWarnings("static-access")
	@Required
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public static String printDecomposeStep(IdentifiedRelation relation, boolean showSplitButton, boolean showRemoveButton, boolean editable, Locale locale) throws IOException{
		String attributesString = new String();
		String[] attributes = relation.getAttributesArray();
		StringBuilder out = new StringBuilder();

		for (int i=0; i<attributes.length; i++){
			attributesString = attributesString.concat(attributes[i]);
			if (i<attributes.length -1){
				attributesString = attributesString.concat(" ");
			}
		}

		out.append("<table class='decompose_step' cellpadding='1px' cellspacing='1px' style='margin-left:" + ((relation.getID().length()/2) * OFFSET) + "px;'>" + LINE_SEP);
		out.append("	<tr>" + LINE_SEP);
		out.append("		<td class='arrow_td'>" + LINE_SEP);
		//out.append("			<input id='" + relation.getID() + "_SHOW_HIDE' name='relation' type='image' src='%%RESOURCES%%/down_arrow.gif' onClick=\"show_hide('" + relation.getID() + "', '%%RESOURCES%%')\" />" + LINE_SEP);
		out.append("			<img id='" + relation.getID() + "_SHOW_HIDE' onclick=\"show_hide('" + relation.getID() + "', '%%RESOURCES%%');\" src='%%RESOURCES%%/down_arrow.gif' />" + LINE_SEP);
		out.append("		</td>" + LINE_SEP);
		out.append("		<td>" + LINE_SEP);
		out.append("			<table class='relation_table' cellpadding='2px' cellspacing='2px'>" + LINE_SEP);
		out.append("				<tr>" + LINE_SEP);
		out.append("					<td class='label_td'><b>R" + relation.getID() + "</b>:</td>" + LINE_SEP);
		out.append("					<td class='attributes_td'>" + attributesString + "</td>" + LINE_SEP);

		if (showSplitButton || showRemoveButton){
			out.append("					<td class='button_td'>" + LINE_SEP);
			String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
			String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
			if (showSplitButton){
				String specifyattributesMessage = messageSource.getMessage("showsplitrelationpopup.specifyattributes", null, locale);
				String noattributesMessage = messageSource.getMessage("showsplitrelationpopup.noattributes", null, locale);
				out.append("						<input type='button' value='" + messageSource.getMessage("printdecomposestep.split", null, locale) + "' class='decompose_button' onClick=\"javascript:showSplitRelationPopup('" + relation.getID() + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + specifyattributesMessage + "', '" + noattributesMessage + "');\" />" + LINE_SEP);
			}
			if (showRemoveButton){
				String removerelationsMessage = messageSource.getMessage("showdelsubrelationspopup.removerelations", null, locale);
				out.append("						<input type='button' value='" + messageSource.getMessage("printdecomposestep.remove", null, locale) + "' class='decompose_button' onClick=\"javascript:showDelSubRelationsPopup('" + relation.getID() + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + removerelationsMessage + "');\" />" + LINE_SEP);
			}
			out.append("					</td>" + LINE_SEP);
		}

		out.append("				</tr>" + LINE_SEP);
		out.append("			</table>" + LINE_SEP);
		out.append("		</td>" + LINE_SEP);

		out.append("	</tr>" + LINE_SEP);

		out.append("	<tr>" + LINE_SEP);
		out.append("		<td class='arrow_td'></td>" + LINE_SEP);
		out.append("		<td>" + LINE_SEP);
		out.append("			<table class='relation_table' cellpadding='2px' cellspacing='2px' id='" + relation.getID() + "_CONTENT'>" + LINE_SEP);

		out.append(printDependenciesRow(relation.getFunctionalDependencies(), relation.getID(), 4, editable, locale));
		out.append(printKeysRow(relation.getMinimalKeys(), relation.getID(), 4, editable, locale));

		out.append("			</table>" + LINE_SEP);
		out.append("		</td>" + LINE_SEP);
		out.append("	</tr>" + LINE_SEP);
		out.append("</table>" + LINE_SEP);
		return out.toString();
	}

	public static String printReport(Report report, int displayIndent, int codeIndent, Locale locale) throws IOException{
		Vector errorReports;
		Vector errorReportGroups;
		ErrorReport currErrorReport;
		ErrorReportGroup currErrorReportGroup;
		StringBuilder out = new StringBuilder();

		out.append("<div style='margin-left:" + displayIndent*OFFSET + "px;'>" + LINE_SEP);
		out.append("	<table class='report_table' cellpadding='0px' cellspacing='0px'>" + LINE_SEP);

		//PRINT PROLOGUE
		if (report.showPrologue()){
			out.append("		<tr>" + LINE_SEP);
			out.append("			<td>" + LINE_SEP);
			out.append("				<div class='section'>" + LINE_SEP);
			out.append("					<div class='section_caption'>" + messageSource.getMessage("printreport.result", null, locale) + "</div>" + LINE_SEP);
			out.append("					<div class='section_content'>" + report.getPrologue() + "</div>" + LINE_SEP);
			out.append("				</div>" + LINE_SEP);
			out.append("			</td>" + LINE_SEP);
			out.append("		</tr>" + LINE_SEP);
		}

		//PRINT ERRORS
		errorReports = report.getErrorReports();
		errorReportGroups = report.getErrorReportGroups();
		
		if ((errorReports.size() > 0) || (errorReportGroups.size() > 0)){
			out.append("		<tr>" + LINE_SEP);
			out.append("			<td>" + LINE_SEP);
			out.append("				<div class='section'>" + LINE_SEP);
			out.append("					<div class='section_caption'>" + messageSource.getMessage("printreport.reports", null, locale) + "</div>" + LINE_SEP);
			out.append("					<div class='section_content'>" + LINE_SEP);
			
			//PRINT ERROR REPORTS
			if (errorReports.size() > 0) {
				for (int i=0; i<errorReports.size(); i++){
					currErrorReport = (ErrorReport)errorReports.get(i);
					out.append(printErrorReport(currErrorReport, displayIndent, codeIndent));
					if (i < errorReports.size()-1){
						out.append("						<div class='gap'></div>" + LINE_SEP);
					}
				}
			}

			//PRINT ERROR REPORT GROUPS
			if (errorReportGroups.size() > 0) {
				for (int i=0; i<errorReportGroups.size(); i++){
					currErrorReportGroup = (ErrorReportGroup)errorReportGroups.get(i);
					out.append(printErrorReportGroup(currErrorReportGroup, displayIndent + 1, codeIndent));
					if (i < errorReports.size()-1){
						out.append("						<div class='gap'></div>" + LINE_SEP);
					}
				}
			}

			out.append("					</div>" + LINE_SEP);
			out.append("				</div>" + LINE_SEP);
			out.append("			</td>" + LINE_SEP);
			out.append("		</tr>" + LINE_SEP);
		}

		out.append("	<table>" + LINE_SEP);
		out.append("</div>" + LINE_SEP);
		
		return out.toString();
	}
	
	public static String printErrorReport(ErrorReport errorReport, int displayIndent, int codeIndent) throws IOException{
		StringBuilder out = new StringBuilder();
		out.append("<div class='error_report' style='margin-left:" + displayIndent*OFFSET + "px;'>" + LINE_SEP);
		out.append("	<table class='error_report_table' cellspacing='0px' cellpadding='0px'>" + LINE_SEP);

		String textClass = "";
		if (errorReport.getType().equals(ReportAtomType.INFO)){
			textClass = "info_text";
		
		} else if (errorReport.getType().equals(ReportAtomType.WARNING)){
			textClass = "warning_text";

		} else if (errorReport.getType().equals(ReportAtomType.ERROR)){
			textClass = "error_text";
		}

		if (errorReport.showError()){
			out.append("		<tr>" + LINE_SEP);
			out.append("			<td class='error_report_caption'>" + errorReport.getType() + ":</td>" + LINE_SEP);
			out.append("			<td class='error_report_content'><div class='" + textClass + "'>" + errorReport.getError() + "</div></td>" + LINE_SEP);
			out.append("		</tr>" + LINE_SEP);				
		}

		if (errorReport.showErrorDescription()){
			out.append("		<tr>" + LINE_SEP);				
			out.append("			<td class='error_report_caption'>Description:</td>" + LINE_SEP);
			out.append("			<td class='error_report_content'>" + errorReport.getDescription() + "</td>" + LINE_SEP);
			out.append("		</tr>" + LINE_SEP);				
		}

		if (errorReport.showHint()){
			out.append("		<tr>" + LINE_SEP);				
			out.append("			<td class='error_report_caption'>Hint:</td>" + LINE_SEP);
			out.append("			<td class='error_report_content'>" + errorReport.getHint() + "</td>" + LINE_SEP);
			out.append("		</tr>" + LINE_SEP);				
		}

		out.append("	</table>" + LINE_SEP);
		out.append("</div>" + LINE_SEP);
		return out.toString();
	}
	
	public static String printErrorReportGroup(ErrorReportGroup group, int displayIndent, int codeIndent) throws IOException{
		Vector errorReports = group.getErrorReports();
		Vector subGroups = group.getSubErrorReportGroups();
		StringBuilder out = new StringBuilder();

		out.append("<div class='error_report_group'>" + LINE_SEP);
		out.append("	<div class='error_report_group_caption'>" + group.getHeader() + "</div>" + LINE_SEP);
		out.append("	<div class='error_report_group_content' style='margin-left:" + displayIndent*OFFSET + "px;'>" + LINE_SEP);

		if (errorReports.size() > 0){
			for (int i=0; i<errorReports.size(); i++){
				out.append(printErrorReport((ErrorReport)errorReports.get(i), 0, codeIndent));
				if (i < errorReports.size()-1){
					out.append("						<div class='gap'></div>" + LINE_SEP);
				}
			}
		}

		if (subGroups.size() > 0){
			for (int i=0; i<subGroups.size(); i++){
				out.append(printErrorReportGroup((ErrorReportGroup)subGroups.get(i), 1, codeIndent));
				if (i < errorReports.size()-1){
					out.append("						<div class='gap'></div>" + LINE_SEP);
				}
			}
		}

		out.append("	</div>" + LINE_SEP);
		out.append("</div>" + LINE_SEP);
		return out.toString();
	}

	public static String printParameters(Collection relations, int indent) throws IOException {
		IdentifiedRelation relation;
		Iterator relationsIterator = relations.iterator();
		StringBuilder out = new StringBuilder();
		while (relationsIterator.hasNext()) {
			relation = (IdentifiedRelation)relationsIterator.next();
			out.append(printParameters(relation, indent));
		}
		return out.toString();
	}
	
	public static String printParameters(Relation relation, int indent) throws IOException {
		String keys;
		String offset;
		String attributes;
		String dependencies;
		String relationID;
		StringBuilder out = new StringBuilder();
		
		Iterator keysIterator;
		Iterator attributesIterator;
		Iterator dependenciesIterator;

		if (relation instanceof IdentifiedRelation) {
			relationID = ((IdentifiedRelation)relation).getID();
		} else {
			relationID = "";
		}
		
		offset = getOffset(indent);

		keys = new String();
		keysIterator = relation.iterMinimalKeys();
		while (keysIterator.hasNext()) {
			keys = keys.concat(keysIterator.next() + ";");
		}

		attributes = new String();
		attributesIterator = relation.iterAttributes();
		while (attributesIterator.hasNext()) {
			attributes = attributes.concat(attributesIterator.next() + ";");
		}

		dependencies = new String();
		dependenciesIterator = relation.iterFunctionalDependencies();
		while (dependenciesIterator.hasNext()) {
			dependencies = dependencies.concat(dependenciesIterator.next() + ";");
		}

		out.append(offset);
		out.append("<input");
		out.append(" type='hidden'");
		out.append(" id='" + relationID + "_ATTRIBUTES" + "'");
		out.append(" name='" + relationID + "_ATTRIBUTES" + "'");
		out.append(" value='" + attributes + "'/>");
		out.append(LINE_SEP);

		out.append(offset);
		out.append("<input");
		out.append(" type='hidden'");
		out.append(" id='" + relationID + "_KEYS" + "'");
		out.append(" name='" + relationID + "_KEYS" + "'");
		out.append(" value='" + keys + "'/>");
		out.append(LINE_SEP);

		out.append(offset);
		out.append("<input");
		out.append(" type='hidden'");
		out.append(" id='" + relationID + "_DEPENDENCIES" + "'");
		out.append(" name='" + relationID + "_DEPENDENCIES" + "'");
		out.append(" value='" + dependencies + "'/>");
		out.append(LINE_SEP);
		return out.toString();
	}

	public static String printKeysRow(Collection keys, String relationID, int indent, boolean editable, Locale locale) throws IOException  {
		return printKeysRow(keys, relationID, messageSource.getMessage("printkeysrow.title", null, locale), indent, editable, locale);
	}
	
	public static String printKeysRow(Collection keys, String relationID, String title, int indent, boolean editable, Locale locale) throws IOException  {
		String offset;
		String content;
		String addFunction;
		String delFunction;
		StringBuilder out = new StringBuilder();

		offset = getOffset(indent);

		content = new String();
		for (int i = 0; i < keys.toArray().length; i++) {
			if (i != 0) {
				content = content.concat("<br>");
			}
			content = content.concat(keys.toArray()[i].toString());
		}

		String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
		String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
		String newkeyMessage = messageSource.getMessage("showaddkeypopup.newkey", null, locale);
		String noattributesMessage = messageSource.getMessage("showaddkeypopup.noattributes", null, locale);
		addFunction = "onClick=\"javascript:showAddKeyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + newkeyMessage + "', '" + noattributesMessage + "');\"";
		String nokeysMessage = messageSource.getMessage("showdelkeypopup.nokeys", null, locale);
		String selectkeysMessage = messageSource.getMessage("showdelkeypopup.selectkeys", null, locale);
		delFunction = "onClick=\"javascript:showDelKeyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + nokeysMessage + "', '" + selectkeysMessage + "');\"";

		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td class='label_td'>" + title + ":</td>" + LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset + "	<td class='content_td' nowrap>" + content + "</td>" + LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset + "	<td class='button_td'>" + LINE_SEP);
		if (editable){
			out.append(offset + "		<input class='button' type='button' value='+' " + addFunction + " />" + LINE_SEP);
			out.append(offset + "		<input class='button' type='button' value='-' " + delFunction + " />" + LINE_SEP);
		}
		out.append(offset + "	</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		return out.toString();
	}

	public static String printDependenciesRow(Collection dependencies, String relationID, int indent, boolean editable, Locale locale) throws IOException {
		return printDependenciesRow(dependencies, relationID, messageSource.getMessage("printdependenciesrow.title", null, locale), indent, editable, locale);
	}

	public static String printDependenciesRow(Collection dependencies, String relationID, String title, int indent, boolean editable, Locale locale) throws IOException {
		String content;
		String offset;
		String addFunction;
		String delFunction;
		StringBuilder out = new StringBuilder();

		offset = getOffset(indent);

		content = new String();
		for (int i = 0; i < dependencies.toArray().length; i++) {
			if (i != 0) {
				content = content.concat("<br>");
			}
			content = content.concat(dependencies.toArray()[i].toString().replaceAll("->", "&rarr;"));
		}

		String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
		String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
		String newdependencyMessage = messageSource.getMessage("showadddependencypopup.newdependency", null, locale);
		String noattributesMessage = messageSource.getMessage("showadddependencypopup.noattributes", null, locale);
		addFunction = "onClick=\"javascript:showAddDependencyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + newdependencyMessage + "', '" + noattributesMessage + "');\"";
		String nodependenciesMessage = messageSource.getMessage("showdeldependencypopup.nodependencies", null, locale);
		String removedependenciesMessage = messageSource.getMessage("showdeldependencypopup.removedependencies", null, locale);
		delFunction = "onClick=\"javascript:showDelDependencyPopup('" + relationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + nodependenciesMessage + "', '" + removedependenciesMessage + "');\"";

		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td class='label_td'>" + title + ":</td>" + LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset + "	<td class='content_td' nowrap>" + content + "</td>" + LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset + "	<td class='button_td'>" + LINE_SEP);
		if (editable){
			out.append(offset + "		<input class='button' type='button' value='+' " + addFunction + ">" + LINE_SEP);
			out.append(offset + "		<input class='button' type='button' value='-' " + delFunction + ">" + LINE_SEP);
		}
		out.append(offset + "	</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		return out.toString();
	}
	
	public static String printDependenciesWithNormalformRow(Relation relation, String title, int indent, Locale locale) throws IOException {
		String content;
		String offset;
		NormalformAnalyzerConfig normalformAnalyzerConfig;
		Collection dependencies;
		NormalformAnalysis analysis;
		StringBuilder out = new StringBuilder();
		
		normalformAnalyzerConfig = new NormalformAnalyzerConfig();
		normalformAnalyzerConfig.setCorrectMinimalKeys(KeysDeterminator.determineMinimalKeys(relation));
		normalformAnalyzerConfig.setRelation(relation);
		analysis = NormalformAnalyzer.analyze(normalformAnalyzerConfig);
		dependencies = relation.getFunctionalDependencies();
		
		offset = getOffset(indent);
		
		content = new String();
		for (int i = 0; i < dependencies.toArray().length; i++) {
			content = content.concat(offset + "<tr>" + LINE_SEP);
			content = content.concat(offset + "	<td>" + LINE_SEP);
			content = content.concat(dependencies.toArray()[i].toString().replaceAll("->", "&rarr;"));
			content = content.concat(offset + "	</td>" + LINE_SEP);
			content = content.concat(offset + "	<td>" + LINE_SEP);
			content = content.concat(getNormalform((FunctionalDependency)dependencies.toArray()[i], analysis, locale));
			content = content.concat(offset + "	</td>" + LINE_SEP);
			content = content.concat(offset + "</tr>" + LINE_SEP);
		}

		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td class='label_td'>" + title + ":</td>" + LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset + "	<td class='content_td' nowrap>" + LINE_SEP);
		out.append(offset + "		<table>" + LINE_SEP);
		out.append(content);
		out.append(offset + "		</table>" + LINE_SEP);
		out.append(offset + "	</td>" + LINE_SEP);
		//out.write(offset + "	<td></td>" + LINE_SEP);
		out.append(offset + "	<td class='button_td'>" + LINE_SEP);
		out.append(offset + "	</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		return out.toString();
	}

	public static String getNormalform(FunctionalDependency dependency, NormalformAnalysis analysis, Locale locale) {
		Vector v;
		NormalformViolation violation;
		
		v = analysis.getFirstNormalformViolations();
		for (int i = 0; i < v.size(); i++) {
			violation = (NormalformViolation)v.get(i);
			if (violation.getFunctionalDependency().equals(dependency)) {
				return "";
			}
		}
		
		v = analysis.getSecondNormalformViolations();
		for (int i = 0; i < v.size(); i++) {
			violation = (NormalformViolation)v.get(i);
			if (violation.getFunctionalDependency().equals(dependency)) {
				return messageSource.getMessage("getnormalform.first", null, locale);
			}
		}
		
		v = analysis.getThirdNormalformViolations();
		for (int i = 0; i < v.size(); i++) {
			violation = (NormalformViolation)v.get(i);
			if (violation.getFunctionalDependency().equals(dependency)) {
				return messageSource.getMessage("getnormalform.second", null, locale);
			}
		}
		
		v = analysis.getBoyceCottNormalformViolations();
		for (int i = 0; i < v.size(); i++) {
			violation = (NormalformViolation)v.get(i);
			if (violation.getFunctionalDependency().equals(dependency)) {
				return messageSource.getMessage("getnormalform.third", null, locale);
			}
		}
		
		return messageSource.getMessage("getnormalform.boycecodd", null, locale);
	}
	
	public static String printAttributesRow(Collection attributes, String baseRelationID, String currRelationID, int indent, boolean editable, Locale locale) throws IOException {
		return printAttributesRow(attributes, baseRelationID, currRelationID, "Attributes", indent, editable, locale);
	}
	
	public static String printAttributesRow(Collection attributes, String baseRelationID, String currRelationID, String title, int indent, boolean editable, Locale locale) throws IOException {
		String offset;
		String content;
		String addFunction;
		String delFunction;
		StringBuilder out = new StringBuilder();

		offset = getOffset(indent);

		content = new String();
		for (int i = 0; i < attributes.toArray().length; i++) {
			if (i != 0) {
				content = content.concat("<br>");
			}
			content = content.concat(attributes.toArray()[i].toString());
		}
		
		String okMessage =  messageSource.getMessage("showpopup.ok", null, locale);
		String cancelMessage = messageSource.getMessage("showpopup.cancel", null, locale);
		String selectattributeMessage = messageSource.getMessage("showaddattributepopup.selectattribute", null, locale);
		String noattributesMessage = messageSource.getMessage("showaddattributepopup.noattributes", null, locale);
		addFunction = "onClick=\"javascript:showAddAttributePopup('" + baseRelationID + "', '" + currRelationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + selectattributeMessage + "', '" + noattributesMessage + "');\"";
		String noattributesMessage2 = messageSource.getMessage("showdelattributepopup.noattributes", null, locale);
		String selectremoveattributeMessage = messageSource.getMessage("showdelattributepopup.selectremoveattribute", null, locale);
		delFunction = "onClick=\"javascript:showDelAttributePopup('" + currRelationID + "', '%%IDPREFIX%%', '" + okMessage + "', '" + cancelMessage + "', '" + noattributesMessage2 + "', '" + selectremoveattributeMessage + "');\"";

		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td class='label_td'>" + title + ":</td>" + LINE_SEP);
		out.append(offset + "	<td class='content_td' nowrap>" + content + "</td>" + LINE_SEP);
		out.append(offset + "	<td class='button_td'>" + LINE_SEP);
		if (editable){
			out.append(offset + "		<input class='button' type='button' value='+' " + addFunction + " />" + LINE_SEP);
			out.append(offset + "		<input class='button' type='button' value='-' " + delFunction + " />" + LINE_SEP);
		}
		out.append(offset + "	</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		return out.toString();
	}

	public static String printMaxLostRow(RDBDSpecification spec, int indent, Locale locale) throws IOException {
		String offset;
		String content;
		int maxLost;
		int nrDependencies;
		StringBuilder out = new StringBuilder();

		if (spec instanceof NormalizationSpecification) {
			maxLost = ((NormalizationSpecification)spec).getMaxLostDependencies();
			nrDependencies = ((NormalizationSpecification)spec).getBaseRelation().getFunctionalDependencies().size();
		} else if (spec instanceof DecomposeSpecification) {
			maxLost = ((DecomposeSpecification)spec).getMaxLostDependencies();
			nrDependencies = ((DecomposeSpecification)spec).getBaseRelation().getFunctionalDependencies().size();
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
		
		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td class='label_td'>" + messageSource.getMessage("printmaxlostrow.maxlostdependencies", null, locale) + ":</td>" + LINE_SEP);
		out.append(offset + "	<td class='content_td' nowrap>" + content + "</td>" + LINE_SEP);
		out.append(offset + "	<td class='button_td'>" + LINE_SEP);
		out.append(offset + "	</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		return out.toString();
	}
	
	public static String printTargetLevelRow(RDBDSpecification spec, int indent, Locale locale) throws IOException {
		String offset;
		String content;
		NormalformLevel level;
		StringBuilder out = new StringBuilder();
		
		if (spec instanceof NormalizationSpecification) {
			level = ((NormalizationSpecification)spec).getTargetLevel();
		} else if (spec instanceof DecomposeSpecification) {
			level = ((DecomposeSpecification)spec).getTargetLevel();
		} else {
			return out.toString();
		}
		
		offset = getOffset(indent);

		if (NormalformLevel.FIRST.equals(level)){
			content = messageSource.getMessage("printtargetlevelrow.first", null, locale);
		} else if (NormalformLevel.SECOND.equals(level)){
			content = messageSource.getMessage("printtargetlevelrow.second", null, locale);
		} else if (NormalformLevel.THIRD.equals(level)){
			content = messageSource.getMessage("printtargetlevelrow.third", null, locale);
		} else if (NormalformLevel.BOYCE_CODD.equals(level)){
			content = messageSource.getMessage("printtargetlevelrow.boycecodd", null, locale);
		} else {
			content = "";
		}
		
		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td class='label_td'>" + messageSource.getMessage("printtargetlevelrow.normalform", null, locale) + ":</td>" + LINE_SEP);
		out.append(offset + "	<td class='content_td' nowrap>" + content + "</td>" + LINE_SEP);
		out.append(offset + "	<td class='button_td'>" + LINE_SEP);
		out.append(offset + "	</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		return out.toString();
	}
	
	public static String printAssignmentForRBR(RBRSpecification spec, int indent, Locale locale) throws IOException {
		String relationAttributes;
		String dependencies;
		String baseAttributes;
		String offset;
		Iterator it;
		boolean first;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		relationAttributes = new String();
		first = true;
		it = spec.getBaseRelation().iterAttributes();
		while (it.hasNext()){
			relationAttributes = relationAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}
		dependencies = new String();
		first = true;
		it = spec.getBaseRelation().getFunctionalDependencies().iterator();
		while (it.hasNext()){
			dependencies = dependencies.concat((first ? "" : ", ") + it.next().toString().replaceAll("->", "&rarr;"));
			first = false;
		}
		baseAttributes = new String();
		first = true;
		it = spec.getBaseAttributes().iterator();
		while (it.hasNext()){
			baseAttributes = baseAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}

		out.append(offset + "<p>" + LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset + "	Berechnen Sie die funktionalen Abh&auml;ngigkeiten <strong>F<sub><code>S</code></sub></strong> " + LINE_SEP);
			out.append(offset + "	f&uuml;r das Subschema <strong>S</strong> der Relation <strong>R</strong> " + LINE_SEP);
			out.append(offset + "	mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong>." + LINE_SEP);
		} else {
			out.append(offset + "	Let be <strong>R</strong> a relation scheme with functional dependencies <strong>F</strong>. " + LINE_SEP);
			out.append(offset + "	Determine the set of functional dependencies <strong>F<sub><code>S</code></sub></strong> " + LINE_SEP);
			out.append(offset + "	for the sub-scheme <strong>S</strong>." + LINE_SEP);
		}
		out.append(offset + "</p>" + LINE_SEP);
		out.append(offset + "<table rules=\"none\" frame=\"void\">" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>R</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>{" + relationAttributes + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>F</strong> = </td>" + LINE_SEP);
		out.append(offset + "		<td>{" + dependencies + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>S</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>(" + baseAttributes + ")</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForAttributeClosure(AttributeClosureSpecification spec, int indent, Locale locale) throws IOException {
		String relationAttributes;
		String dependencies;
		String baseAttributes;
		String offset;
		Iterator it;
		boolean first;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		relationAttributes = new String();
		first = true;
		it = spec.getBaseRelation().iterAttributes();
		while (it.hasNext()){
			relationAttributes = relationAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}
		dependencies = new String();
		first = true;
		it = spec.getBaseRelation().getFunctionalDependencies().iterator();
		while (it.hasNext()){
			dependencies = dependencies.concat((first ? "" : ", ") + it.next().toString().replaceAll("->", "&rarr;"));
			first = false;
		}
		baseAttributes = new String();
		first = true;
		it = spec.getBaseAttributes().iterator();
		while (it.hasNext()){
			baseAttributes = baseAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}

		out.append(offset + "<p>" + LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset + "	Berechnen Sie die H&uuml;lle der Attribut Kombination <strong>A</strong> " + LINE_SEP);
			out.append(offset + "	bez&uuml;glich der Menge an Funktionalen Abh&auml;ngigkeiten <strong>F</strong> " + LINE_SEP);
			out.append(offset + "	der Relation <strong>R</strong>." + LINE_SEP);
		} else {
			out.append(offset + "	Determine the attribute closure of the set of attributes <strong>A</strong> " + LINE_SEP);
			out.append(offset + "	with respect to relation scheme <strong>R</strong> and the set of " + LINE_SEP);
			out.append(offset + "	functional dependencies <strong>F</strong>." + LINE_SEP);
		}
		out.append(offset + "</p>" + LINE_SEP);
		out.append(offset + "<table rules=\"none\" frame=\"void\">" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>R</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>{" + relationAttributes + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>F</strong> = </td>" + LINE_SEP);
		out.append(offset + "		<td>{" + dependencies + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>A</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>(" + baseAttributes + ")</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForNormalization(NormalizationSpecification spec, int indent, Locale locale) throws IOException {
		String relationAttributes;
		String dependencies;
		String offset;
		Iterator it;
		boolean first;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		relationAttributes = new String();
		first = true;
		it = spec.getBaseRelation().iterAttributes();
		while (it.hasNext()){
			relationAttributes = relationAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}
		dependencies = new String();
		first = true;
		it = spec.getBaseRelation().getFunctionalDependencies().iterator();
		while (it.hasNext()){
			dependencies = dependencies.concat((first ? "" : ", ") + it.next().toString().replaceAll("->", "&rarr;"));
			first = false;
		}

		out.append(offset + "<p>" + LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset + "	Finden Sie eine <b>verlustfreie Zerlegung</b> der Relation " + LINE_SEP);
			out.append(offset + "	<strong>" + spec.getBaseRelation().getName() + "</strong> mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong> in " + LINE_SEP);
			out.append(offset + "	<strong>");
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset + "erster ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset + "zweiter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset + "dritter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset + "Boyce-Codd ");
			}
			out.append(offset + "	Normalform</strong>. Geben Sie f&uuml;r jede Teilrelation die Schl&uuml;ssel " + LINE_SEP);
			out.append(offset + "	und die von <strong>F</strong> ableitbaren Funktionalen Abh&auml;ngigkeiten an.  " + LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset + "	Sie d&uuml;rfen bei der Zerlegung <b>keine</b> Funktionale Abh&auml;ngigkeit verlieren!" + LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset + "	Die Zerlegung muss <b>nicht ab&auml;ngigkeitstreu</b> sein. " + LINE_SEP);
			} else {
				out.append(offset + "	Sie d&uuml;rfen bei der Zerlegung maximal <b>" + spec.getMaxLostDependencies() + "</b> Funktionale " + LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset + "	Abh&auml;ngigkeit " + LINE_SEP);
				} else {
					out.append(offset + "	Abh&auml;ngigkeiten " + LINE_SEP);
				}
				out.append(offset + "	verlieren!" + LINE_SEP);
			}
		} else {
			out.append(offset + "	Find a <b>lossless decomposition</b> of relation " + LINE_SEP);
			out.append(offset + "	<strong>" + spec.getBaseRelation().getName() + "</strong> with function dependencies <strong>F</strong>. " + LINE_SEP);
			out.append(offset + "	The decomposition must be in <strong>");
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset + "first ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset + "second ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset + "third ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset + "Boyce-Codd ");
			}
			out.append(offset + "	normal form</strong>. Specify keys and functional dependencies derived from <strong>F</strong> for each relation fragment. " + LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset + "	You may not loose <b>any </b> functional dependency!" + LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset + "	The decomposition does not have to be <b>dependency preserving</b>. " + LINE_SEP);
			} else {
				out.append(offset + "	At most <b>" + spec.getMaxLostDependencies() + "</b> " + LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset + "	functinal dependency " + LINE_SEP);
				} else {
					out.append(offset + "	functional dependencies " + LINE_SEP);
				}
				out.append(offset + "	may be lost during decomposition!" + LINE_SEP);
			}
		}
		out.append(offset + "</p>" + LINE_SEP);
		out.append(offset + "<table rules=\"none\" frame=\"void\">" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>R</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>{" + relationAttributes + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>F</strong> = </td>" + LINE_SEP);
		out.append(offset + "		<td>{" + dependencies + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);	
		return out.toString();
	}

	public static String printAssignmentForDecompose(DecomposeSpecification spec, int indent, Locale locale) throws IOException {
		String relationAttributes;
		String dependencies;
		String offset;
		Iterator it;
		boolean first;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		relationAttributes = new String();
		first = true;
		it = spec.getBaseRelation().iterAttributes();
		while (it.hasNext()){
			relationAttributes = relationAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}
		dependencies = new String();
		first = true;
		it = spec.getBaseRelation().getFunctionalDependencies().iterator();
		while (it.hasNext()){
			dependencies = dependencies.concat((first ? "" : ", ") + it.next().toString().replaceAll("->", "&rarr;"));
			first = false;
		}

		out.append(offset + "<p>" + LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset + "	Wenden Sie den Decompose Algorithmus an, um eine <b>verlustfreie Zerlegung</b> der Relation " + LINE_SEP);
			out.append(offset + "	<strong>" + spec.getBaseRelation().getName() + "</strong> mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong> in " + LINE_SEP);
			out.append(offset + "	<strong>");
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset + "erster ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset + "zweiter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset + "dritter ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset + "Boyce-Codd ");
			}
			out.append(offset + "	Normalform</strong> zu finden. Geben Sie f&uuml;r jede Teilrelation die Schl&uuml;ssel " + LINE_SEP);
			out.append(offset + "	und die von <strong>F</strong> ableitbaren Funktionalen Abh&auml;ngigkeiten an.  " + LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset + "	Sie d&uuml;rfen bei der Zerlegung <b>keine</b> Funktionale Abh&auml;ngigkeit verlieren!" + LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset + "	Die Zerlegung muss <b>nicht ab&auml;ngigkeitstreu</b> sein. " + LINE_SEP);
			} else {
				out.append(offset + "	Sie d&uuml;rfen bei der Zerlegung maximal <b>" + spec.getMaxLostDependencies() + "</b> Funktionale " + LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset + "	Abh&auml;ngigkeit " + LINE_SEP);
				} else {
					out.append(offset + "	Abh&auml;ngigkeiten " + LINE_SEP);
				}
				out.append(offset + "	verlieren!" + LINE_SEP);
			}
		} else {
			out.append(offset + "	Let be <strong>" + spec.getBaseRelation().getName() + "</strong> a relation scheme with a set of functional dependencies <strong>F</strong>. Determine a decomposition of <strong>" + spec.getBaseRelation().getName() + " into " + LINE_SEP);
			if (spec.getTargetLevel().equals(NormalformLevel.FIRST)){
				out.append(offset + "first ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.SECOND)){
				out.append(offset + "second ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.THIRD)){
				out.append(offset + "third ");
			} else if (spec.getTargetLevel().equals(NormalformLevel.BOYCE_CODD)){
				out.append(offset + "Boyce-Codd ");
			}
			out.append(offset + "	normal form</strong> by means of the DECOMPOSE algorithm. Specify for each sub-scheme attributes, keys, and functional dependencies. " + LINE_SEP);
			if (spec.getMaxLostDependencies() == 0){
				out.append(offset + "	You may not loose <b>any </b> functional dependency!" + LINE_SEP);
			} else if (spec.getMaxLostDependencies() >= spec.getBaseRelation().getFunctionalDependencies().size()){
				out.append(offset + "	Note that functional dependencies may be lost during decomposition. " + LINE_SEP);
			} else {
				out.append(offset + "	At most <b>" + spec.getMaxLostDependencies() + "</b> " + LINE_SEP);
				if (spec.getMaxLostDependencies() == 1){
					out.append(offset + "	functinal dependency " + LINE_SEP);
				} else {
					out.append(offset + "	functional dependencies " + LINE_SEP);
				}
				out.append(offset + "	may be lost during decomposition!" + LINE_SEP);
			}
		}
		out.append(offset + "</p>" + LINE_SEP);
		out.append(offset + "<table rules=\"none\" frame=\"void\">" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>R</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>{" + relationAttributes + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>F</strong> = </td>" + LINE_SEP);
		out.append(offset + "		<td>{" + dependencies + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForKeysDetermination(IdentifiedRelation spec, int indent, Locale locale) throws IOException {
		String relationAttributes;
		String dependencies;
		String offset;
		Iterator it;
		boolean first;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		relationAttributes = new String();
		first = true;
		it = spec.iterAttributes();
		while (it.hasNext()){
			relationAttributes = relationAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}
		dependencies = new String();
		first = true;
		it = spec.getFunctionalDependencies().iterator();
		while (it.hasNext()){
			dependencies = dependencies.concat((first ? "" : ", ") + it.next().toString().replaceAll("->", "&rarr;"));
			first = false;
		}

		out.append(offset + "<p>" + LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset + "	Berechnen Sie alle Schl&uuml;ssel der Relation <strong>R</strong> auf " + LINE_SEP);
			out.append(offset + "	Basis der Funktionalen Abh&auml;ngigkeiten <strong>F</strong>. " + LINE_SEP);
		} else {
			out.append(offset + "	Let be <strong>R</strong> a relation scheme with a set of functional " + LINE_SEP);
			out.append(offset + "	dependencies <strong>F</strong>. Determine all keys of <strong>R</strong>. " + LINE_SEP);
		}
		out.append(offset + "</p>" + LINE_SEP);
		out.append(offset + "<table rules=\"none\" frame=\"void\">" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>R</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>{" + relationAttributes + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>F</strong> = </td>" + LINE_SEP);
		out.append(offset + "		<td>{" + dependencies + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForMinimalCover(IdentifiedRelation spec, int indent, Locale locale) throws IOException {
		String dependencies;
		String offset;
		Iterator it;
		boolean first;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		dependencies = new String();
		first = true;
		it = spec.getFunctionalDependencies().iterator();
		while (it.hasNext()){
			dependencies = dependencies.concat((first ? "" : ", ") + it.next().toString().replaceAll("->", "&rarr;"));
			first = false;
		}

		out.append(offset + "<p>" + LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset + "	Geben Sie f&uuml;r die Menge <b>F</b> an Funktionalen Abh&auml;ngigkeiten eine minimale  " + LINE_SEP);
			out.append(offset + "	�berdeckung an. Streichen Sie alle redundanten Funktionalen Abh&auml;ngigkeiten " + LINE_SEP);
			out.append(offset + "	und alle redundanten Attribute in den linken Seiten der Funktionalen Abh&auml;ngigkeiten. " + LINE_SEP);
		} else {
			out.append(offset + "	Indicate a minimal cover for <b>F</b>. Eliminate all redundant functional " + LINE_SEP);
			out.append(offset + "	dependencies and redundand attributes at left hand sides of functional dependencies. " + LINE_SEP);
		}
		out.append(offset + "</p>" + LINE_SEP);
		out.append(offset + "<table rules=\"none\" frame=\"void\">" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>F</strong> = </td>" + LINE_SEP);
		out.append(offset + "		<td>{" + dependencies + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);
		out.append(offset + "<p>" + LINE_SEP);
		return out.toString();
	}

	public static String printAssignmentForNormalformDetermination(Relation spec, int indent, Locale locale) throws IOException {
		String relationAttributes;
		String dependencies;
		String offset;
		Iterator it;
		boolean first;
		StringBuilder out = new StringBuilder();
		
		offset = getOffset(indent);
		relationAttributes = new String();
		first = true;
		it = spec.iterAttributes();
		while (it.hasNext()){
			relationAttributes = relationAttributes.concat((first ? "" : "&nbsp;") + it.next());
			first = false;
		}
		dependencies = new String();
		first = true;
		it = spec.getFunctionalDependencies().iterator();
		while (it.hasNext()){
			dependencies = dependencies.concat((first ? "" : ", ") + it.next().toString().replaceAll("->", "&rarr;"));
			first = false;
		}

		out.append(offset + "<p>" + LINE_SEP);
		if (locale.equals(Locale.GERMAN) || locale.equals(Locale.GERMANY)) {
			out.append(offset + "	Geben Sie an, in welcher Normalform sich die Relation <strong>R</strong> " + LINE_SEP);
			out.append(offset + "	mit den Funktionalen Abh&auml;ngigkeiten <strong>F</strong> befindet. " + LINE_SEP);
			out.append(offset + "	Geben Sie weiters f&uuml;r jede Funktionale Abh&auml;ngigkeit <strong>F<sub>i</sub></strong> " + LINE_SEP);
			out.append(offset + "	an, welche Normalform durch <strong>F<sub>i</sub></strong> verletzt wird. " + LINE_SEP);
		} else {
			out.append(offset + "	Determine the highest normal form that is fulfilled in relation scheme " + LINE_SEP);
			out.append(offset + "	<strong>R</strong> with the set of functional dependencies <strong>F</strong>. " + LINE_SEP);
			out.append(offset + "	Further, determine for each functional dependency <strong>F<sub>i</sub></strong> " + LINE_SEP);
			out.append(offset + "	the normal form that is violated by <strong>F<sub>i</sub></strong>. " + LINE_SEP);
		}
		out.append(offset + "</p>" + LINE_SEP);
		out.append(offset + "<table rules=\"none\" frame=\"void\">" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>R</strong></td>" + LINE_SEP);
		out.append(offset + "		<td>{" + relationAttributes + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "	<tr>" + LINE_SEP);
		out.append(offset + "		<td><strong>F</strong> = </td>" + LINE_SEP);
		out.append(offset + "		<td>{" + dependencies + "}</td>" + LINE_SEP);
		out.append(offset + "	</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);	
		return out.toString();
	}
	
	public static String printSpecificationSyntax(SpecificationParser parser, int indent, Locale locale) throws IOException {
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
		out.append(offset + "<table>" + LINE_SEP);
		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.specification", null, locale) + "</span></td>" + LINE_SEP);
		out.append(offset + "	<td>=</td>" + LINE_SEP);
		out.append(offset + "	<td>" + LINE_SEP);
		out.append(offset + "		" + (qualRel != null ? "<span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attributes", null, locale) + "</span> " : "") + LINE_SEP);
		out.append(offset + "		" + (qualDep != null ? "<span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.dependencies", null, locale) + "</span> " : "") + LINE_SEP);
		out.append(offset + "		" + (qualBase != null ? "<span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.baseattributes", null, locale) + "</span></td>" : "") + LINE_SEP);
		out.append(offset + "	</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		if (qualRel != null) {
			out.append(offset + "<tr>" + LINE_SEP);
			out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.attributes", null, locale) + "</span></td>" + LINE_SEP);
			out.append(offset + "	<td>=</td>" + LINE_SEP);
			out.append(offset + "	<td>&quot;<span class='spec_atom'>" + qualRel + "</span>&quot; &quot;<span class='spec_atom'>{</span>&quot; [ <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attributelist", null, locale) + "</span> ] &quot;<span class='spec_atom'>}</span>&quot;</td>" + LINE_SEP);
			out.append(offset + "</tr>" + LINE_SEP);
		}
		if (qualBase != null) {
			out.append(offset + "<tr>" + LINE_SEP);
			out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.baseattributes", null, locale) + "</span></td>" + LINE_SEP);
			out.append(offset + "	<td>=</td>" + LINE_SEP);
			out.append(offset + "	<td>&quot;<span class='spec_atom'>" + qualBase + "</span>&quot; &quot;<span class='spec_atom'>{</span>&quot; [ <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attributelist", null, locale) + "</span> ] &quot;<span class='spec_atom'>}</span>&quot;</td>" + LINE_SEP);
			out.append(offset + "</tr>" + LINE_SEP);
		}
		if (qualDep != null) {
			out.append(offset + "<tr>" + LINE_SEP);
			out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.dependencies", null, locale) + "</span></td>" + LINE_SEP);
			out.append(offset + "	<td>=</td>" + LINE_SEP);
			out.append(offset + "	<td>&quot;<span class='spec_atom'>" + qualDep + "</span>&quot; &quot;<span class='spec_atom'>{</span>&quot; [ <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.dependencylist", null, locale) + "</span> ] &quot;<span class='spec_atom'>}</span>&quot;</td>" + LINE_SEP);
			out.append(offset + "</tr>" + LINE_SEP);
			out.append(offset + "<tr>" + LINE_SEP);
			out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.dependencylist", null, locale) + "</span></td>" + LINE_SEP);
			out.append(offset + "	<td>=</td>" + LINE_SEP);
			out.append(offset + "	<td><span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.dependency", null, locale) + "</span> { &quot;, &quot; <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.dependency", null, locale) + "</span> }</td>" + LINE_SEP);
			out.append(offset + "</tr>" + LINE_SEP);
			out.append(offset + "<tr>" + LINE_SEP);
			out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.dependency", null, locale) + "</span></td>" + LINE_SEP);
			out.append(offset + "	<td>=</td>" + LINE_SEP);
			out.append(offset + "	<td><span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attributelist", null, locale) + "</span> &quot;<span class='spec_atom'><code>-&gt;</code></span>&quot; <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attributelist", null, locale) + "</span></td>" + LINE_SEP);
			out.append(offset + "</tr>" + LINE_SEP);
		}
		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.attributelist", null, locale) + "</span></td>" + LINE_SEP);
		out.append(offset + "	<td>=</td>" + LINE_SEP);
		out.append(offset + "	<td><span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attribute", null, locale) + "</span> { &quot;&nbsp;&quot; <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.attribute", null, locale) + "</span> }</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.attribute", null, locale) + "</span></td>" + LINE_SEP);
		out.append(offset + "	<td>=</td>" + LINE_SEP);
		out.append(offset + "	<td><span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.character", null, locale) + "</span> { <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.character", null, locale) + "</span> }</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		out.append(offset + "<tr>" + LINE_SEP);
		out.append(offset + "	<td><span class='spec_def'>" + messageSource.getMessage("printspecificationsyntax.character", null, locale) + "</span></td>" + LINE_SEP);
		out.append(offset + "	<td>=</td>" + LINE_SEP);
		out.append(offset + "	<td><span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.letter", null, locale) + "</span> | <span class='spec_def_ref'>" + messageSource.getMessage("printspecificationsyntax.digit", null, locale) + "</span> | &quot;<span class='spec_atom'>_</span>&quot;</td>" + LINE_SEP);
		out.append(offset + "</tr>" + LINE_SEP);
		out.append(offset + "</table>" + LINE_SEP);
		return out.toString();
	}

	public static void createHTMLSiteForReport(Report report){

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
	
	public static void createHTMLSiteForDecompose(Collection relations, Locale locale){

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
			Iterator i = relations.iterator();
			while (i.hasNext()){
				writer.println(printParameters((IdentifiedRelation)i.next(), 2));
			}

			writer.println("	</form>");
			
			i = relations.iterator();
			IdentifiedRelation currRelation;
			boolean isInnerNode;			
			while (i.hasNext()){
				currRelation = (IdentifiedRelation)i.next();
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

	public static void testDecompose(){
		Key key;
		TreeSet relations = new TreeSet(new IdentifiedRelationComparator());
		IdentifiedRelation relation;
		FunctionalDependency dependency;
		
		//CREATE RELATION 1
		relation = new IdentifiedRelation();
		try{
			relation.setID("1");
		} catch (Exception e){
			e.printStackTrace();
		}
		relation.setName("R1");
		relation.addAttribute("A");
		relation.addAttribute("B");
		relation.addAttribute("C");
		relation.addAttribute("D");
		 
		dependency = new FunctionalDependency();
		dependency.addLHSAttribute("A"); 
		dependency.addRHSAttribute("B"); 
		dependency.addRHSAttribute("C"); 
		relation.addFunctionalDependency(dependency); 
		 
		key = new Key();
		key.addAttribute("A");
		key.addAttribute("D");
		relation.addMinimalKey(key);
		
		relations.add(relation);

		//CREATE RELATION 2
		relation = new IdentifiedRelation();
		try{
			relation.setID("2");
		} catch (Exception e){
			e.printStackTrace();
		}
		relation.setName("R2");
		relation.addAttribute("A");
		relation.addAttribute("B");
		relation.addAttribute("C");
		relation.addAttribute("D");
		 
		dependency = new FunctionalDependency();
		dependency.addLHSAttribute("A"); 
		dependency.addRHSAttribute("B"); 
		dependency.addRHSAttribute("C"); 
		relation.addFunctionalDependency(dependency); 
		 
		key = new Key();
		key.addAttribute("A");
		key.addAttribute("D");
		relation.addMinimalKey(key);
		
		relations.add(relation);

		//CREATE RELATION 1.1
		relation = new IdentifiedRelation();
		try{
			relation.setID("1.1");
		} catch (Exception e){
			e.printStackTrace();
		}
		relation.setName("R1.1");
		relation.addAttribute("A");
		relation.addAttribute("B");
		relation.addAttribute("C");
		relation.addAttribute("D");
		 
		dependency = new FunctionalDependency();
		dependency.addLHSAttribute("A"); 
		dependency.addRHSAttribute("B"); 
		dependency.addRHSAttribute("C"); 
		relation.addFunctionalDependency(dependency); 
		 
		key = new Key();
		key.addAttribute("A");
		key.addAttribute("D");
		relation.addMinimalKey(key);
		
		relations.add(relation);

		//CREATE RELATION 1.2
		relation = new IdentifiedRelation();
		try{
			relation.setID("1.2");
		} catch (Exception e){
			e.printStackTrace();
		}
		relation.setName("R1.2");
		relation.addAttribute("A");
		relation.addAttribute("B");
		relation.addAttribute("C");
		relation.addAttribute("D");
		 
		dependency = new FunctionalDependency();
		dependency.addLHSAttribute("A"); 
		dependency.addRHSAttribute("B"); 
		dependency.addRHSAttribute("C"); 
		relation.addFunctionalDependency(dependency); 
		 
		key = new Key();
		key.addAttribute("A");
		key.addAttribute("D");
		relation.addMinimalKey(key);
		
		relations.add(relation);

		//CREATE RELATION 1.1.1
		relation = new IdentifiedRelation();
		try{
			relation.setID("1.1.1");
		} catch (Exception e){
			e.printStackTrace();
		}
		relation.setName("R1.1.1");
		relation.addAttribute("A");
		relation.addAttribute("B");
		relation.addAttribute("C");
		relation.addAttribute("D");
		 
		dependency = new FunctionalDependency();
		dependency.addLHSAttribute("A"); 
		dependency.addRHSAttribute("B"); 
		dependency.addRHSAttribute("C"); 
		relation.addFunctionalDependency(dependency); 
		 
		key = new Key();
		key.addAttribute("A");
		key.addAttribute("D");
		relation.addMinimalKey(key);
		
		relations.add(relation);

		//CREATE RELATION 1.1.2
		relation = new IdentifiedRelation();
		try{
			relation.setID("1.1.2");
		} catch (Exception e){
			e.printStackTrace();
		}
		relation.setName("R1.1.2");
		relation.addAttribute("A");
		relation.addAttribute("B");
		relation.addAttribute("C");
		relation.addAttribute("D");
		 
		dependency = new FunctionalDependency();
		dependency.addLHSAttribute("A"); 
		dependency.addRHSAttribute("B"); 
		dependency.addRHSAttribute("C"); 
		relation.addFunctionalDependency(dependency); 
		 
		key = new Key();
		key.addAttribute("A");
		key.addAttribute("D");
		relation.addMinimalKey(key);
		
		relations.add(relation);

		
		createHTMLSiteForDecompose(relations, Locale.ENGLISH);		
	}

	public static void testReport(){
		Report report = new Report();
		report.setPrologue("Congratulations! Everything is correct.");
		report.setShowPrologue(true);
		
		//ERROR REPORT 1
		ErrorReport er1 = new ErrorReport();
		er1.setHint("Hint 1");
		er1.setError("Error 1");
		er1.setDescription("Description 1");
		
		er1.setShowHint(true);
		er1.setShowError(true);
		er1.setShowErrorDescription(true);
		report.addErrorReport(er1);		

		//ERROR REPORT 2
		ErrorReport er2 = new ErrorReport();
		er2.setHint("Hint 2");
		er2.setError("Error 2");
		er2.setDescription("Description 2");
		
		er2.setShowHint(true);
		er2.setShowError(true);
		er2.setShowErrorDescription(true);
		report.addErrorReport(er2);		

		//ERROR REPORT GROUP 1
		ErrorReportGroup erg1 = new ErrorReportGroup();
		erg1.setHeader("Header for ERG 1");

		ErrorReport er1_1 = new ErrorReport();
		er1_1.setHint("Hint 1.1");
		er1_1.setError("Error 1.1");
		er1_1.setDescription("Description 1.1");
		
		er1_1.setShowHint(true);
		er1_1.setShowError(true);
		er1_1.setShowErrorDescription(true);
		erg1.addErrorReport(er1_1);

		ErrorReport er1_2 = new ErrorReport();
		er1_2.setHint("Hint 1.2");
		er1_2.setError("Error 1.2");
		er1_2.setDescription("Description 1.2");
		
		er1_2.setShowHint(true);
		er1_2.setShowError(true);
		er1_2.setShowErrorDescription(true);
		erg1.addErrorReport(er1_2);
		
		//ERROR REPORT GROUP 1.1
		ErrorReportGroup erg1_1 = new ErrorReportGroup();
		erg1_1.setHeader("Header for ERG 1.1");

		ErrorReport er1_1_1 = new ErrorReport();
		er1_1_1.setHint("Hint 1.1.1");
		er1_1_1.setError("Error 1.1.1");
		er1_1_1.setDescription("Description 1.1.1");
		
		er1_1_1.setShowHint(true);
		er1_1_1.setShowError(true);
		er1_1_1.setShowErrorDescription(true);
		erg1_1.addErrorReport(er1_1_1);
		erg1.addSubErrorReportGroup(erg1_1);

		report.addErrorReportGroup(erg1);



		//ERROR REPORT GROUP 2
		ErrorReportGroup erg2 = new ErrorReportGroup();
		erg2.setHeader("Header for ERG 2");

		ErrorReport er2_1 = new ErrorReport();
		er2_1.setHint("Hint 2.1");
		er2_1.setError("Error 2.1");
		er2_1.setDescription("Description 2.1");
		
		er2_1.setShowHint(true);
		er2_1.setShowError(true);
		er2_1.setShowErrorDescription(true);
		erg2.addErrorReport(er2_1);

		ErrorReport er2_2 = new ErrorReport();
		er2_2.setHint("Hint 2.2");
		er2_2.setError("Error 2.2");
		er2_2.setDescription("Description 2.2");
		
		er2_2.setShowHint(true);
		er2_2.setShowError(true);
		er2_2.setShowErrorDescription(true);
		erg2.addErrorReport(er2_2);

		report.addErrorReportGroup(erg2);		


		
		createHTMLSiteForReport(report);
	}

	private static String getOffset(int indent) {
		String offset;
		offset = new String();
		for (int i = 0; i < indent; i++) {
			offset = offset.concat("\t");
		}
		return offset;
	}
	public static void main(String[] args){
		//testDecompose();
	}
}
