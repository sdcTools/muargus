package muargus.model;

import java.util.ArrayList;

/**
 * Model class of the NumericalRankSwapping screen. Only a single instance of
 * this class will exist.
 *
 * @author Statistics Netherlands
 */
public class NumericalRankSwapping {

    private final ArrayList<VariableMu> variables;
    private final ArrayList<RankSwappingSpec> rankSwappings;

    /**
     * Constructor of the model class NumericalRankSwapping. Makes empty
     * arraylists for the variables and the rankSwappings.
     */
    public NumericalRankSwapping() {
        this.variables = new ArrayList<>();
        this.rankSwappings = new ArrayList<>();
    }

    /**
     * Gets the ArrayList containing all the numerical variables. If the
     * ArrayList is empty, use this method to add VariableMu's.
     *
     * @return ArrayList containing all the numerical variables.
     */
    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }

    /**
     * Gets the ArrayList containing the variable specifications for rank
     * swapping. The RankSwappingSpec contains all relevant information for
     * applying rank swapping on the relevant variable. If the ArrayList is
     * empty, use this method to add RankSwappingSpec's.
     *
     * @return ArrayList containing RankSwappingSpec's.
     */
    public ArrayList<RankSwappingSpec> getRankSwappings() {
        return this.rankSwappings;
    }
}
