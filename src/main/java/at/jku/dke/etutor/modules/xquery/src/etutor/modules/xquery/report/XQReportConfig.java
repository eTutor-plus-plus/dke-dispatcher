package etutor.modules.xquery.report;

/**
 * @author nitsche
 */
public class XQReportConfig {
	
	private boolean includesGrading;
	private String mode;
	private int diagnoseLevel;

	public XQReportConfig() {
		super();
		this.includesGrading = false;
		this.diagnoseLevel = XQFeedback.DIAGNOSE_LOW;
		this.mode = "";
	}
	
	/**
     * A flag used for reporting later on. This tells,
     * whether the information of the grading should be included in the report.
     * 
     * @return true if grading information will be included when reporting, otherwise false.
     */
    public boolean includesGrading() {
        return this.includesGrading;
    }

    /**
     * Sets a flag which is used for reporting later on.
     * This tells, whether the information of the grading should be included in the report.
     * 
     * @param includesGrading true if grading information should be included when reporting, 
     *            otherwise false.
     */
    public void setIncludesGrading(boolean includesGrading) {
        this.includesGrading = includesGrading;
    }
	
    /**
     * @return The submission mode.
     */
    public String getMode() {
		return mode;
	}
    
	/**
	 * @param mode Specifies the submission mode.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	/**
	 * @return The diagnose level.
	 */
	public int getDiagnoseLevel() {
		return diagnoseLevel;
	}
	
	/**
	 * @param diagnoseLevel Specifies the diagnose level, which affects how detailed the generated
     *            messages are. 
	 */
	public void setDiagnoseLevel(int diagnoseLevel) {
		this.diagnoseLevel = diagnoseLevel;
	}
}
