package muargus.controller;

import java.util.ArrayList;
import muargus.model.MetadataMu;
import muargus.model.SyntheticData;
import muargus.model.VariableMu;
import muargus.view.SyntheticDataView;

/**
 *
 * @author pibd05
 */
public class SyntheticDataController extends ControllerBase<SyntheticData> {

    private final MetadataMu metadata;
    
    
    /**
     * Constructor for the RController.
     *
     * @param parentView the Frame of the mainFrame.
     * @param metadata the orginal metadata.
     */
    public SyntheticDataController(java.awt.Frame parentView, MetadataMu metadata) {
        super.setView(new SyntheticDataView(parentView, true, this));
        this.metadata = metadata;
        setModel(this.metadata.getCombinations().getSyntheticData());
        getView().setMetadata(this.metadata);
    }
    
    /**
     * Closes the view by setting its visibility to false.
     */
    public void close(){
        getView().setVisible(false);
    }
    
    public ArrayList<VariableMu> getNonSensitiveVariables(){
        return getModel().getNonSensitiveVariables();
    }
    
    public ArrayList<VariableMu> getSensitiveVariables(){
        return getModel().getSensitiveVariables();
    }
    
    public Object[][] getSensitiveData(){
        Object[][] data = new Object[getSensitiveVariables().size()][2];
        for(int i = 0; i<getSensitiveVariables().size(); i++){
            data[i][0] = getSensitiveVariables().get(i);
            data[i][1] = getSensitiveVariables().get(i).getAlpha();
        }
        return data;
    }
    
    public void runSyntheticData(){
        /* synthetic data: sensitive variables are numbered from x1 to xn, non-sensitive variables are numbered from s1 to sn*/
    }
    
}
