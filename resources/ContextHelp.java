// hoe doen we het met open microdata

package muargus.resources;

/**
 *
 * @author pibd05
 */
public enum ContextHelp {

    MAIN_FRAME("muargus.view.MainFrameView", "MainFrame"),
    
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
    
    MAKE_PROTECTED_FILE("muargus.view.MakeProtectedFileView", "OutputMakeSuppressedFile"),
    VIEW_REPORT("muargus.view.ViewReportView", "OutputViewReport");
            

    public String nameddest;
    public String className;

    private ContextHelp(String className, String name) {
        this.nameddest = name;
        this.className = className;
    }

    public static String fromClassName(String className) {
        for (ContextHelp help : ContextHelp.values()) {
            if (help.className.equals(className)) {
                return help.nameddest;
            }
        }
        return null;
    }
    
    public static String fromClassName(String className, boolean isHousehold) {
        if(isHousehold){
            return ContextHelp.HOUSEHOLD_RISK_SPECIFICATION.nameddest;
        } else {
            return ContextHelp.INDIVIDUAL_RISK_SPECIFICATION.nameddest;
        }
    }

}
