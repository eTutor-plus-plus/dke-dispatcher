package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.nf.RDBDConstants;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalform.*;
import at.jku.dke.etutor.modules.nf.analysis.normalformdetermination.NormalformDeterminationAnalysis;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.NormalformLevelComparator;
import org.springframework.context.MessageSource;

import java.util.Iterator;
import java.util.Locale;

public class NormalformReporter {

	public static Report report(NormalformDeterminationAnalysis analysis, DefaultGrading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		Report report = new Report();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
		if (config.getAction().equals(RDBDConstants.EVAL_ACTION_SUBMIT)){
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("normalformreporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("normalformreporter.notcorrectsolution", null, locale));
			}
			prologue.append(messageSource.getMessage("normalformreporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));
			if (grading != null){
				if (grading.getPoints() == 1){
					prologue.append(" ").append(messageSource.getMessage("normalformreporter.point", null, locale)).append(" ");
				} else {
					prologue.append(" ").append(messageSource.getMessage("normalformreporter.points", null, locale)).append(" ");
				}
				prologue.append(messageSource.getMessage("normalformreporter.yoursolution", null, locale));
			}
		} else {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("normalformreporter.correct", null, locale));
			} else {
				prologue.append(messageSource.getMessage("normalformreporter.notcorrect", null, locale));
			}
		}
		report.setPrologue(prologue.toString());
		
		if (!config.getAction().equals(RDBDConstants.EVAL_ACTION_CHECK)){
			//REPORT OVERALL_NF_LEVEL 
			if (!analysis.getOverallNormalformLevel().equals(analysis.getSubmittedLevel())){
				report.addErrorReport(createNormalformLevelDeterminationErrorReport(analysis, config, messageSource, locale));
			}

			//REPORT NF_LEVEL_VIOLATIONS
			if (!analysis.getWrongLeveledDependencies().isEmpty()){
				report.addErrorReport(createNormalformLevelViolationErrorReport(analysis, config, messageSource, locale));
			}
		}
		
		//CONFIGURE REPORT
		report.setShowPrologue(true);

		return report;	
	}

	public static ErrorReport createNormalformLevelDeterminationErrorReport(NormalformDeterminationAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();

		//SET ERROR
		report.setError(messageSource.getMessage("normalformreporter.incorrectnormalform", null, locale));
		
		//SET HINT
		
		//SET DESCRIPTION
		if ((config.getDiagnoseLevel() == 1) || (config.getDiagnoseLevel() == 2)){
			report.setDescription(messageSource.getMessage("normalformreporter.normalformnotcorrect", new Object[]{analysis.getSubmittedLevel()}, locale));
		}
		
		if (config.getDiagnoseLevel() == 3){
			report.setDescription(messageSource.getMessage("normalformreporter.normalformdoesnotmatch", new Object[]{normalformLevelToString(analysis.getSubmittedLevel(), messageSource, locale), normalformLevelToString(analysis.getOverallNormalformLevel(), messageSource, locale)}, locale));
		}

		//CONFIGURE REPORT
		report.setShowError(true);
		report.setShowHint(false);
		report.setShowErrorDescription(config.getDiagnoseLevel() > 0);

		return report;
	}

	public static ErrorReport createNormalformLevelViolationErrorReport(NormalformDeterminationAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		
		//SET ERROR
		report.setError(messageSource.getMessage("normalformreporter.incorrectnormalformviolations", null, locale));
		
		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 1){
			description.append(messageSource.getMessage("normalformreporter.dependencynotviolatesnormalform", null, locale));
		}
		
		if (config.getDiagnoseLevel() == 2){
			description.append(analysis.wrongLeveledDependenciesCount());
			if (analysis.wrongLeveledDependenciesCount() == 1){
				description.append(" ").append(messageSource.getMessage("normalformreporter.dependencydoes", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("normalformreporter.dependenciesdo", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("normalformreporter.notviolatenormalform", null, locale));
		}

		if (config.getDiagnoseLevel() == 3){
			description.append(messageSource.getMessage("normalformreporter.violatenotspecifiednormalform", null, locale));
			description.append("<table rules='cols' border='1' style='margin-top:5px;'>");
			description.append("	<tr>");
			description.append("		<td style='border-bottom:solid;border-bottom-width:thin;padding-left:10px;padding-right:10px'><i>").append(messageSource.getMessage("normalformreporter.functionaldependency", null, locale)).append("</i></td>");
			description.append("		<td style='border-bottom:solid;border-bottom-width:thin;padding-left:10px;padding-right:10px'><i>").append(messageSource.getMessage("normalformreporter.violatednormalform", null, locale)).append("</i></td>");
			description.append("		<td style='border-bottom:solid;border-bottom-width:thin;padding-left:10px;padding-right:10px'><i>").append(messageSource.getMessage("normalformreporter.yoursolution", null, locale)).append("</i></td>");
			description.append("	</tr>");
			
			Object[] entry;
			Iterator<Object[]> iter = analysis.iterWrongLeveledDependencies();
			while (iter.hasNext()){
				entry = iter.next();
				
				description.append("	<tr>");
				description.append("		<td align='center'>").append(entry[0].toString().replaceAll("->", "&rarr;")).append("</td>");
				description.append("		<td align='center'>").append(normalformLevelToString((NormalformLevel) entry[1], messageSource, locale)).append("</td>");
				description.append("		<td align='center'>").append(normalformLevelToString((NormalformLevel) entry[2], messageSource, locale)).append("</td>");
				description.append("	</tr>");
			}
			description.append("</table>");
		}

		report.setDescription(description.toString());

		//CONFIGURE REPORT
		report.setShowHint(false);
		report.setShowError(true);
		report.setShowErrorDescription(config.getDiagnoseLevel() > 0);

		return report;
	}

	public static ErrorReport createNormalformErrorReport(NormalformAnalysis analysis, NormalformReporterConfig config, MessageSource messageSource, Locale locale){
		boolean first;
		boolean appendLineBreak = false;

        Iterator<String> it;
		Iterator<? extends NormalformViolation> violations;

		String currElemID;

		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		NormalformLevelComparator comparator = new NormalformLevelComparator();

        //SET ERROR
		report.setError(messageSource.getMessage("normalformreporter.insufficientnormalformlevel", new Object[]{normalformLevelToString(config.getDesiredNormalformLevel(), messageSource, locale),  normalformLevelToString(analysis.getOverallNormalformLevel(), messageSource, locale)}, locale));
		
		//SET HINT

		//SET DESCRIPTION

		//REPORTING FIRST NORMALFORM VIOLATIONS IF NECESSARY
		if (comparator.compare(config.getDesiredNormalformLevel(), NormalformLevel.FIRST) >= 0){
			if (!analysis.getFirstNormalformViolations().isEmpty()){
				appendLineBreak = true;
			}

			first = true; 
			violations = analysis.getFirstNormalformViolations().iterator();
			while (violations.hasNext()){
				if (first){
					first = false;
				} else {
					description.append("<br><br>");
				}

				FirstNormalformViolation firstNFViolation = (FirstNormalformViolation)violations.next();
				description.append(messageSource.getMessage("normalformreporter.dependencyviolatesnormalform", new Object[]{firstNFViolation.getFunctionalDependency()}, locale));
			}
		}

		//REPORTING SECOND NORMALFORM VIOLATIONS IF NECESSARY
		if (comparator.compare(config.getDesiredNormalformLevel(), NormalformLevel.SECOND) >= 0){
			if (!analysis.getSecondNormalformViolations().isEmpty()){
				if (appendLineBreak){
					description.append("<br><br>");
				}
				appendLineBreak = true;
			}

			first = true;
			violations = analysis.getSecondNormalformViolations().iterator();
			while (violations.hasNext()){
				if (first){
					first = false;
				} else {
					description.append("<br><br>");
				}

				SecondNormalformViolation secondNFViolation = (SecondNormalformViolation)violations.next();
				description.append(messageSource.getMessage("normalformreporter.violatesnormalform", new Object[]{secondNFViolation.getFunctionalDependency(), "second"}, locale));

				if (config.getDiagnoseLevel() == 0){
					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.rightsidenotprim", null, locale));
					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.leftsidecompriseskey", null, locale));
				}

				if (config.getDiagnoseLevel() == 1){ 
					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.rightsidecomprises", new Object[]{secondNFViolation.nonPrimRHSAttributesCount()}, locale));
					if (secondNFViolation.nonPrimRHSAttributesCount() == 1){
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributenotprim", null, locale));
					} else {
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributesnotprim", null, locale));
					}

					description.append("<br>");

					description.append(messageSource.getMessage("normalformreporter.leftsidepartialkey", null, locale));
				}

				if ((config.getDiagnoseLevel() == 2) || (config.getDiagnoseLevel() == 3)){
					currElemID = RDBDHelper.getNextElementID();
					description.append("<br>");
					description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
					description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
					description.append("<p>").append(messageSource.getMessage("normalformreporter.nonprimrightside", null, locale)).append("</p>");
					description.append("<table border='2' rules='all'>");

					it = secondNFViolation.iterNonPrimRHSAttributes();
					while (it.hasNext()){
						description.append("<tr><td>");
						description.append(it.next());
						description.append("</td></tr>");
					}
					description.append("</table>");
					description.append("</body></html>");
					description.append("\"></input>");

					description.append(messageSource.getMessage("normalformreporter.rightsidecomprises", new Object[]{" <a href=\"javascript:openWindow('" + currElemID + "')\">" + secondNFViolation.nonPrimRHSAttributesCount()}, locale));

					if (secondNFViolation.nonPrimRHSAttributesCount() == 1){
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributenotprim", null, locale)).append("</a>.");
					} else {
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributesnotprim", null, locale)).append("</a>.");
					}

					currElemID = RDBDHelper.getNextElementID();
					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.leftsidepartialkey", null, locale));
					
				}
			}
		}

		//REPORTING THIRD NORMALFORM VIOLATIONS IF NECESSARY
		if (comparator.compare(config.getDesiredNormalformLevel(), NormalformLevel.THIRD) >= 0){
			if (!analysis.getThirdNormalformViolations().isEmpty()){
				if (appendLineBreak){
					description.append("<br><br>");
				}
				appendLineBreak = true;
			}

			first = true;
			violations = analysis.getThirdNormalformViolations().iterator();
			while (violations.hasNext()){
				if (first){
					first = false;
				} else {
					description.append("<br><br>");
				}

				ThirdNormalformViolation thirdNFViolation = (ThirdNormalformViolation)violations.next();
				description.append(messageSource.getMessage("normalformreporter.violatesnormalform", new Object[]{thirdNFViolation.getFunctionalDependency(), "third"}, locale));

				if (config.getDiagnoseLevel() == 0){
					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.rightsidenotprim", null, locale));
					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.leftsidenotsuperkey", null, locale));
				}

				if (config.getDiagnoseLevel() == 1){
					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.rightsidecomprises", new Object[]{thirdNFViolation.nonPrimRHSAttributesCount()}, locale));
					if (thirdNFViolation.nonPrimRHSAttributesCount() == 1){
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributenotprim", null, locale));
					} else {
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributesnotprim", null, locale));
					}

					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.leftsidenotsuperkey", null, locale));
				}

				if ((config.getDiagnoseLevel() == 2) || (config.getDiagnoseLevel() == 3)){
					currElemID = RDBDHelper.getNextElementID();
					description.append("<br>");
					description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
					description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
					description.append("<p>").append(messageSource.getMessage("normalformreporter.nonprimrightside", null, locale)).append("</p>");
					description.append("<table border='2' rules='all'>");

					it = thirdNFViolation.iterNonPrimRHSAttributes();
					while (it.hasNext()){
						description.append("<tr><td>");
						description.append(it.next());
						description.append("</td></tr>");
					}
					description.append("</table>");
					description.append("</body></html>");
					description.append("\"></input>");

					description.append(messageSource.getMessage("normalformreporter.rightsidecomprises", new Object[]{"<a href=\"javascript:openWindow('" + currElemID + "')\">" + thirdNFViolation.nonPrimRHSAttributesCount()}, locale));

					if (thirdNFViolation.nonPrimRHSAttributesCount() == 1){
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributenotprim", null, locale)).append("</a>.");
					} else {
						description.append(" ").append(messageSource.getMessage("normalformreporter.attributesnotprim", null, locale)).append("</a>.");
					}

					description.append("<br>");
					description.append(messageSource.getMessage("normalformreporter.leftsidenotsuperkey", null, locale));
				}
			}
		}

		//REPORTING BOYCE COTT NORMALFORM VIOLATIONS IF NECESSARY
		if (comparator.compare(config.getDesiredNormalformLevel(), NormalformLevel.BOYCE_CODD) >= 0){
			if (!analysis.getBoyceCoddNormalformViolations().isEmpty()){
				if (appendLineBreak){
					description.append("<br><br>");
				}
			}

			first = true;
			violations = analysis.getBoyceCoddNormalformViolations().iterator();
			while (violations.hasNext()){
				if (first){
					first = false;
				} else {
					description.append("<br><br>");
				}

				BoyceCoddNormalformViolation boyceCoddNFViolation = (BoyceCoddNormalformViolation)violations.next();
				description.append(messageSource.getMessage("normalformreporter.violatesnormalform", new Object[]{boyceCoddNFViolation.getFunctionalDependency(), "boyce cott"}, locale));
				description.append("<br>");
				description.append(messageSource.getMessage("normalformreporter.leftsidenotsuperkey", null, locale));
			}
		}

		report.setDescription(description.toString());

		//CONFIGURE REPORT
		report.setShowHint(false);
		report.setShowError(true);
		report.setShowErrorDescription(!report.getDescription().isEmpty());

		return report;
	}
	
	public static String normalformLevelToString(NormalformLevel level, MessageSource messageSource, Locale locale){
		if (level == null){
			return messageSource.getMessage("normalformreporter.none", null, locale);
		} else if (level.equals(NormalformLevel.FIRST)){
			return messageSource.getMessage("normalformreporter.first", null, locale);
		} else if (level.equals(NormalformLevel.SECOND)){
			return messageSource.getMessage("normalformreporter.second", null, locale);
		} else if (level.equals(NormalformLevel.THIRD)){
			return messageSource.getMessage("normalformreporter.third", null, locale);
		} else if (level.equals(NormalformLevel.BOYCE_CODD)){
			return messageSource.getMessage("normalformreporter.boycecodd", null, locale);
		} else {
			return "";
		}
	}
}