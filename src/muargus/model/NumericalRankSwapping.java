/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package muargus.model;

import java.util.ArrayList;

/**
 *
 * @author ambargus
 */
public class NumericalRankSwapping {
    private final ArrayList<VariableMu> variables;
    private final ArrayList<RankSwappingSpec> rankSwappings;
    
    public NumericalRankSwapping() {
        this.variables = new ArrayList<>();
        this.rankSwappings = new ArrayList<>();
    }

    public ArrayList<VariableMu> getVariables() {
        return this.variables;
    }

    public ArrayList<RankSwappingSpec> getRankSwappings() {
        return this.rankSwappings;
    }    
}
