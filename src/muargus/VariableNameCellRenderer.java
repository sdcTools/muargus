package muargus;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import muargus.model.VariableMu;

/**
 * List renderer. Shows the variable name instead of the variable object name.
 *
 * @author Statistics Netherlands
 */
public class VariableNameCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String valueString = value instanceof VariableMu ? ((VariableMu) value).getName() : value.toString();
        return super.getListCellRendererComponent(list, valueString, index, isSelected, cellHasFocus);
    }
}
