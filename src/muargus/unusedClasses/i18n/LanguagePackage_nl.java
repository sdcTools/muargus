package muargus.unusedClasses.i18n;

import java.util.ListResourceBundle;

public class LanguagePackage_nl extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            { Names.Microdata.getName()  , "\tMicrodata"   },
            { Names.Metadata.getName()    , "\tMetadata"    },
            { Names.Instructions.getName(), "\tVoor het veranderen/inspecteren van de metadata ga naar Specificeer --> Metadata\n"
                    + "\t\tVoor het specificeren van de combinaties ga naar Specificeer --> Combinaties\n" },
    };

}