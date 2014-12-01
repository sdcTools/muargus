//package muargus.model;
//
//import java.util.ArrayList;
//
///**
// * Model class of the Synthetic Data screen. Only a single instance of this
// * class will exist.
// *
// * @author Statistics Netherlands
// */
//public class SyntheticData {
//
//    private final ArrayList<VariableMu> variables;
//    private final ArrayList<VariableMu> sensitiveVariables;
//    private final ArrayList<VariableMu> nonSensitiveVariables;
//
//    /**
//     * Constructor of the model class SyntheticData. Initializes the ArrayLists
//     * for all variables, the sensitive variables and the non-sensitive
//     * variable.
//     */
//    public SyntheticData() {
//        this.sensitiveVariables = new ArrayList<>();
//        this.nonSensitiveVariables = new ArrayList<>();
//        this.variables = new ArrayList<>();
//    }
//
//    /**
//     * Gets an ArrayList containing the sensitive variables. If the ArrayList is
//     * empty, use this method to add variables.
//     *
//     * @return ArrayList of VariableMu's containing the sensitive variables.
//     */
//    public ArrayList<VariableMu> getSensitiveVariables() {
//        return this.sensitiveVariables;
//    }
//
//    /**
//     * Gets an ArrayList containing the non-sensitive variables. If the
//     * ArrayList is empty, use this method to add variables.
//     *
//     * @return ArrayList of VariableMu's containing the non-sensitive variables.
//     */
//    public ArrayList<VariableMu> getNonSensitiveVariables() {
//        return this.nonSensitiveVariables;
//    }
//
//    /**
//     * Gets an ArrayList containing all numeric variables. If the ArrayList is
//     * empty, use this method to add variables.
//     *
//     * @return ArrayList of VariableMu's containing the numeric variables.
//     */
//    public ArrayList<VariableMu> getVariables() {
//        return this.variables;
//    }
//
//    /**
//     * Gets an ArrayList containing the variables used as neither sensitive nor
//     * non-sensitive variables.
//     *
//     * @return ArrayList of VariableMu's containing the variables used as
//     * neither sensitive nor non-sensitive variables.
//     */
//    public ArrayList<VariableMu> getOtherVariables() {
//        ArrayList<VariableMu> other = new ArrayList<>();
//        for (VariableMu variable : this.variables) {
//            if (!this.sensitiveVariables.contains(variable) && (!this.nonSensitiveVariables.contains(variable))) {
//                other.add(variable);
//            }
//        }
//        return other;
//    }
//}
