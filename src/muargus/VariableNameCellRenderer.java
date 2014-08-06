package muargus;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import muargus.model.VariableMu;

public class VariableNameCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String valueString = ((VariableMu)value).getName();
        return super.getListCellRendererComponent(list, valueString, index, isSelected, cellHasFocus);
    }
}
