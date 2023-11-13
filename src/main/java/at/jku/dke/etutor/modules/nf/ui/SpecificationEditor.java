package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.RDBDSpecification;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import java.io.Serializable;
import java.util.Locale;

/**
 * This class is designed for enabling the manipulation of RDBD exercise specifications 
 * in a user interface (JSP). It provides all properties needed for collecting user 
 * input, which especially includes the specification itself, an additional specification 
 * for being able to reset or apply changes and language information. Textual 
 * representations of specifications can be set by the user as well, which should
 * ease the input of specifications.
 *  
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class SpecificationEditor implements MessageSourceAware, Serializable {
	
	private static final long serialVersionUID = -5266530270165646510L;
	private String msg;
	private String specText;
	private NormalformLevel targetLevel;
	private String maxLostDependencies;
	private SpecificationParser parser;
	private RDBDSpecification spec;
	private RDBDSpecification specTmp;
	private Locale locale;
	private final int rdbdType;
	private static MessageSource messageSource;

	public SpecificationEditor(int rdbdType) throws MalformedRelationIDException {
		this.rdbdType = rdbdType;
		this.msg = "";
		this.specText = "";
		this.locale = Locale.ENGLISH;
	}
	
	@Required
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public String getMsg() {
		return msg;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setMsg(String msg) {
		if (msg != null) {
			this.msg = msg;
		}
	}

	public RDBDSpecification getSpec() {
		return spec;
	}

	public void setSpec(RDBDSpecification spec) {
		check(spec);
		this.spec = spec;
	}

	public String getSpecText() {
		return specText;
	}

	public void setSpecText(String specText) {
		if (specText != null) {
			this.specText = specText;
		}
	}

	public RDBDSpecification getSpecTmp() {
		return specTmp;
	}

	public void setSpecTmp(RDBDSpecification specTmp) {
		check(spec);
		this.specTmp = specTmp;
	}

	public SpecificationParser getParser() {
		return parser;
	}

	public void setParser(SpecificationParser parser) {
		this.parser = parser;
	}
	
	public int getRdbdType() {
		return rdbdType;
	}

	protected void check(RDBDSpecification spec) {
		String msg;
		if (!RDBDHelper.isOfRdbdType(spec, this.rdbdType)) {
			msg = "The passed specification should be of a type appropriate for RDBD type "+ this.rdbdType + ". ";
			msg += (spec == null ? "The specification is null." : "Passed type: " + spec.getClass().getName());
			throw new RuntimeException(msg);
		}
	}
	
	public String getMaxLostDependencies() {
		if (maxLostDependencies == null) {
			return "";
		}
		return maxLostDependencies;
	}

	public void setMaxLostDependencies(String maxLostDependencies) {
		this.maxLostDependencies = maxLostDependencies;
	}

	public NormalformLevel getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(NormalformLevel normalform) {
		this.targetLevel = normalform;
	}
}