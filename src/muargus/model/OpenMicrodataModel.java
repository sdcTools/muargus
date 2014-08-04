package muargus.model;

/**
 *
 * @author ambargus
 */
public class OpenMicrodataModel {
    private static String microdataPath;
    private static String metadataPath;
    public static final String instructionText1 = 
            "<html>\nFor changing/inspecting the metadata go to Specify --> Metadata "
            + "<br>\nFor specifying the combinations go to Specify --> Combinations\n</html>";
    public static final String instructionText2 = 
            "<html>\nAs no metadata file has been specified"
            + "<br>\nspecify the metadata file too or"
            + "<br>\nspecify the metadata via SpecifyMetadata\n</html>";

    /**
     * Constructs an object of the model class containing the data from the OpenMicrodata dialog.
     * The paths from the microdatafile and the metadatafile will be set as an empty string
     */
    public OpenMicrodataModel() {
        OpenMicrodataModel.microdataPath = "";
        OpenMicrodataModel.metadataPath = "";
    }

    /**
     * This method returns the currently stored path of the microdatafile
     * @return a string containing the currently used path of the microdatafile
     */
    public static String getMicrodataPath() {
        return microdataPath;
    }

    /**
     * This method sets the path to the microdatafile that will be used for calculations
     * @param microdataPath a string of the path of the file that contains the microdata
     */
    public static void setMicrodataPath(String microdataPath) {
        OpenMicrodataModel.microdataPath = microdataPath;
    }

    /** 
     * This method returns the currently stored path of the metadatafile
     * @return a string containing the currently used path of the metadatafile
     */
    public static String getMetadataPath() {
        return metadataPath;
    }

    /**
     * This method sets the path to the metadatafile that will be used for calculations
     * @param metadataPath a string of the path of the file that contains the metadata 
     */
    public static void setMetadataPath(String metadataPath) {
        OpenMicrodataModel.metadataPath = metadataPath;
    }
    
    /**
     * This method is meant for testing and prints the metadataPath and the microdataPath
     */
    public static void printVariables(){
        System.out.printf("%s\n%s\n", OpenMicrodataModel.metadataPath, OpenMicrodataModel.microdataPath);
    }
    
}
