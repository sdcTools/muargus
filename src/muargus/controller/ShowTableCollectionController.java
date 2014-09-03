package muargus.controller;

import java.util.ArrayList;
import muargus.MuARGUS;
import muargus.model.MetadataMu;
import muargus.model.TableCollection;
import muargus.model.TableMu;
import muargus.view.ShowTableCollectionView;

/**
 * The Controller class of the ShowTableCollection screen.
 *
 * @author ambargus
 */
public class ShowTableCollectionController {

    ShowTableCollectionView view;
    TableCollection model;
    MetadataMu metadataMu;
    CalculationService calculationService;

    /**
     * Constructor for the ShowTableCollectionController. This constructor makes
     * a new view and sets the metadata for both the controller and the view.
     *
     * @param parentView the Frame of the mainFrame that is given to the
     * ShowTableCollectionView.
     * @param metadata the orginal metadata.
     */
    public ShowTableCollectionController(java.awt.Frame parentView, MetadataMu metadata) {
        this.view = new ShowTableCollectionView(parentView, true, this);
        this.metadataMu = metadata;
        this.view.setMetadataMu(this.metadataMu);
    }

    /**
     * Opens the view by setting its visibility to true.
     */
    public void showView() {
        this.view.setVisible(true);
    }

    /**
     * Closes the view by setting its visibility to false.
     */
    public void close() {
        this.view.setVisible(false);
    }

    /**
     * Fuction for setting the model. This function is used by the view after
     * setting the model itself
     *
     * @param model the model class of the ShowTableCollection screen
     */
    public void setModel(TableCollection model) {
        this.model = model;
    }

    /**
     * Makes an ArrayList of all tables and sets this in the model class. This
     * method first sets the dimensions and the columnNames. Then it will make a
     * new ArrayList. The ArrayList is filled with for each dimension an
     * ArrayList with its tables. So first all the tables with dimension one are
     * added, then those with dimensions two etc. Finally the ArrayList will
     * contain all tables and will be added to the model.
     */
    public final void setAllTables() {
        setDimensions();
        setColumnNames();

        ArrayList<TableMu> allTables = new ArrayList<>();
        this.calculationService = MuARGUS.getCalculationService();
        for (int i = 1; i <= this.model.getDimensions(); i++) {
            allTables.addAll(this.calculationService.getTableUnsafeCombinations(metadataMu, i));
        }
        this.model.setAllTables(allTables);
    }

    /**
     * Sets the maximum number of dimensions in the model. This method will loop
     * through all of the original tables and change the dimensions to the size
     * of the largest table. After looping, the dimensions are set in the model.
     */
    public void setDimensions() {
        int dimensions = 0;
        for (TableMu t : this.model.getOriginalTables()) {
            if (t.getVariables().size() > dimensions) {
                dimensions = t.getVariables().size();
            }
        }
        this.model.setDimensions(dimensions);
    }

    /**
     * Sets the column names in the model. This method will generate an Array of
     * column names for each dimension and sets this in the model.
     */
    public void setColumnNames() {
        String[] columnNames = new String[this.model.getDimensions() + 1];
        columnNames[0] = "# unsafe cells";
        for (int i = 1; i < columnNames.length; i++) {
            columnNames[i] = "Var " + i;
        }
        this.model.setColumnNames(columnNames);
    }

    /**
     * Generates an array with the data that will be displayed on the table.
     * This function makes a new two-dimensional array with the length equal to
     * the number of tables and the with equal to the number of columns. The
     * first column will be filled with the number of unsafe combinations and
     * the following columns will be filled with the names of the varibles of
     * each table
     *
     * @param tables The Array of tables that should be displayed
     * @return A double array containing the data that will be displayed on the
     * table
     */
    public String[][] getData(ArrayList<TableMu> tables) {
        String[][] data = new String[tables.size()][this.model.getColumnNames().length];
        for (int i = 0; i < tables.size(); i++) {
            TableMu t = tables.get(i);
            data[i][0] = Integer.toString(t.getNrOfUnsafeCombinations());
            for (int j = 0; j < t.getVariables().size(); j++) {
                data[i][j + 1] = t.getVariables().get(j).getName();
            }
        }
        return data;
    }

    /**
     * Generates an ArrayList of tables that need to be displayed and sets the
     * subsetData. If all tabels should be shown, the subsetData is filled with
     * the complete data from the model. Otherwise only the tables that meet the
     * requirements will be added to a new ArrayList. This ArrayList is given to
     * the method getData(), which generates a double array with the data. The
     * subsetData is then filled with this new data.
     *
     * @param showAllTables boolean value that determines if all tables should
     * be shown or only the tables that have unsafe combinations (!= 0)
     */
    public void setSubData(boolean showAllTables) {
        if (this.model.getSelectedVariable().getName().equals("all") && showAllTables) {
            this.model.setSubdata(this.model.getData());
        } else {
            ArrayList<TableMu> tables = new ArrayList<>();
            for (TableMu t : this.model.getAllTables()) {
                if (this.model.getSelectedVariable().getName().equals("all")) {
                    if (t.getNrOfUnsafeCombinations() > 0) {
                        tables.add(t);
                    }
                } else if (t.getVariables().contains(this.model.getSelectedVariable())) {
                    if (showAllTables) {
                        tables.add(t);
                    } else {
                        if (t.getNrOfUnsafeCombinations() > 0) {
                            tables.add(t);
                        }
                    }
                }
            }
            this.model.setSubdata(getData(tables));
        }
    }
}
