package muargus.resources;

/**
 * Enumeration used for context sensitive help.
 *
 * @author Statistics Netherlands
 */
public enum ContextHelp {

    MAIN_FRAME("muargus.view.MainFrameView", "MainFrame"),
    
    OPEN_MICRODATA("muargus.view.OpenMicrodataView", "OpenMicrodata"),
    
    SPECIFY_METADATA("muargus.view.SpecifyMetadataView", "SpecifyMetaFile"),
    SELECT_COMBINATIONS("muargus.view.SelectCombinationsView", "SpecifyCombinations"),
    
    SHOW_TABLE_COLLECTION("muargus.view.ShowTableCollectionView", "ModifyShowTables"),
    GLOBAL_RECODE("muargus.view.GlobalRecodeView", "ModifyGlobalRecode"),
    PRAM_SPECIFICATION("muargus.view.PramSpecificationView", "ModifyPRAMSpecification"),
    INDIVIDUAL_RISK_SPECIFICATION("muargus.view.RiskSpecificationView", "ModifyIndividualRiskSpecification"),
    HOUSEHOLD_RISK_SPECIFICATION("muargus.view.RiskSpecificationView", "ModifyHouseholdRiskSpecification"),
    MODIFY_NUMERICAL_VARIABLES("muargus.view.ModifyNumericalVariablesView", "ModifyNumericalVariables"),
    MICROAGGREGATION("muargus.view.MicroaggregationView", "ModifyNumericalMicroAggregation"),
    NUMERICAL_RANK_SWAPPING("muargus.view.NumericalRankSwappingView", "ModifyNumericalRankSwapping"),
    SYNTHETIC_DATA("muargus.view.SyntheticDataView", "ModifySyntheticData"),
    
    MAKE_PROTECTED_FILE("muargus.view.MakeProtectedFileView", "OutputMakeProtectedFile"),
    VIEW_REPORT("muargus.view.ViewReportView", "OutputViewReport"),
    
    NEWS("muargus.view.ViewReportView", "HelpNews"),
    ABOUT("muargus.view.AboutView", "HelpAbout");

    public String nameddest;
    public String className;

    /**
     * Constructor of the ContextHelp class.
     *
     * @param className String containing the classname.
     * @param name String containing the named destination.
     */
    private ContextHelp(String className, String name) {
        this.nameddest = name;
        this.className = className;
    }

    /**
     * Gets the named destination using the classname.
     *
     * @param className String containing the classname.
     * @return String containing the named destination.
     */
    public static String fromClassName(String className) {
        for (ContextHelp help : ContextHelp.values()) {
            if (help.className.equals(className)) {
                return help.nameddest;
            }
        }
        return null;
    }

    /**
     * Gets the named destination using the classname.
     *
     * @param className String containing the classname.
     * @param isHouseholdOrNews Boolean indicating whether the data contains
     * household data.
     * @return String containing the named destination.
     */
    public static String fromClassName(String className, boolean isHouseholdOrNews) {
        if (className.equals(HOUSEHOLD_RISK_SPECIFICATION.className)) {
            if (isHouseholdOrNews) {
                return ContextHelp.HOUSEHOLD_RISK_SPECIFICATION.nameddest;
            } else {
                return ContextHelp.INDIVIDUAL_RISK_SPECIFICATION.nameddest;
            }
        } else if (className.equals(NEWS.className)) {
            if (isHouseholdOrNews) {
                return ContextHelp.NEWS.nameddest;
            } else {
                return ContextHelp.VIEW_REPORT.nameddest;
            }
        } else {
            return null;
        }
    }

}
