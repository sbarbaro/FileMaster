package net.sbarbaro.filemaster.ui;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import net.sbarbaro.filemaster.model.Rule;

/**
 * RuleDescriptionUI
 * <p>
 * RuleEditorSubpanel implementation that allows user to enter descriptive text
 * for a Rule
 * <p>
 * @author Steven A. Barbaro (steven@abarbaro.net)
 */
public class RuleDescriptionUI extends RuleEditorSubpanel {
    
    private static final long serialVersionUID = 5285124252219412961L;

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
        // Do nothing since only one description per rule
    }

    @Override
    protected void delete(int index) {
        // Do nothing since can't delete the single description for a rule
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
