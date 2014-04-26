package net.sbarbaro.filemaster.ui;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import net.sbarbaro.filemaster.model.Rule;

/**
 *
 * @author steven
 */
public class RuleDescriptionUI extends RuleEditorSubpanel {

    // A textfield for entry of a rule description
    private final JTextField descField = new JTextField(60);
    
    // The Rule to describe
    private final Rule rule;

    public RuleDescriptionUI(Rule rule) {
        super();
        this.rule = rule;
    }

    @Override
    protected void add() {
    }

    @Override
    protected void delete(int index) {
    }

    @Override
    protected void layoutPanel() {

        descField.setEditable(true);
        descField.setText(rule.getDescription());

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(descField, c);

    }

    @Override
    protected void harvest() {
        rule.setDescription(descField.getText());
    }
}
