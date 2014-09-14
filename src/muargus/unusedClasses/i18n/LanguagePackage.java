package muargus.unusedClasses.i18n;

import java.util.ListResourceBundle;


public class LanguagePackage extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            { Names.Microdata.getName()   , "\tMicrodata"   },
            { Names.Metadata.getName()    , "\tMetadata"    },
            { Names.Instructions.getName(), "\tFor changing/inspecting the metadata go to Specify --> Metadata\n"
                    + "\t\tFor specifying the combinations go to Specify --> Combinations\n" },
    };
}