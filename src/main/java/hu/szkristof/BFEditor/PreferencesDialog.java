package hu.szkristof.BFEditor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Swing dialog used to view and change application preferences. */
public class PreferencesDialog extends JDialog {

	private final Preferences preferences;

	/** Creates and lays out the preferences dialog. */
	public PreferencesDialog() {
		super(BFEditor.getInstance(), "Preferences", true);

		preferences = Preferences.deserialize();

		setLayout(new BorderLayout());

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(20, 20));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		JLabel titleLabel = new JLabel("Preferences");
		titleLabel.setFont(new Font(null, Font.BOLD, 20));
		contentPane.add(titleLabel, BorderLayout.NORTH);

		JPanel optionsPanel = new JPanel();
		GridLayout layout = new GridLayout(0, 2);
		layout.setHgap(10);
		layout.setVgap(10);
		optionsPanel.setLayout(layout);

		JLabel memoryCellCountLabel = new JLabel("Memory Cell Count");
		JSpinner memoryCellCountSpinner = new JSpinner(new SpinnerNumberModel(preferences.getMemoryCellCount(), 1, null, 1));

		optionsPanel.add(memoryCellCountLabel);
		optionsPanel.add(memoryCellCountSpinner);

		JLabel memoryCellOverflowLabel = new JLabel("Memory Cell Overflow Behaviour");
		JComboBox<Preferences.MemoryCellOverflowBehaviour> memoryCellOverflowBehaviourSelector = new JComboBox<>(Preferences.MemoryCellOverflowBehaviour.values());
		memoryCellOverflowBehaviourSelector.setSelectedItem(preferences.getMemoryCellOverflowBehaviour());

		optionsPanel.add(memoryCellOverflowLabel);
		optionsPanel.add(memoryCellOverflowBehaviourSelector);

		JLabel defaultMemoryCellValueLabel = new JLabel("Default Memory Cell Value");
		JSpinner defaultMemoryCellValueSpinner = new JSpinner(new SpinnerNumberModel(preferences.getDefaultMemoryCellValue(), 0, 255, 1));

		optionsPanel.add(defaultMemoryCellValueLabel);
		optionsPanel.add(defaultMemoryCellValueSpinner);

		contentPane.add(optionsPanel, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(_ -> {
			setVisible(false);
		});

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(_ -> {
			preferences.setMemoryCellCount((Integer)memoryCellCountSpinner.getValue());
			preferences.setMemoryCellOverflowBehaviour(memoryCellOverflowBehaviourSelector.getItemAt(memoryCellOverflowBehaviourSelector.getSelectedIndex()));
			preferences.setDefaultMemoryCellValue((Integer)defaultMemoryCellValueSpinner.getValue());
			preferences.serialize();
			setVisible(false);
		});

		buttonsPanel.add(cancelButton);
		buttonsPanel.add(saveButton);

		contentPane.add(buttonsPanel, BorderLayout.SOUTH);

		add(contentPane, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(BFEditor.getInstance());
	}

	/**
	 * Gets the preferences managed by the dialog.
	 *
	 * @return active preferences
	 */
	public Preferences getPreferences() {
		return preferences;
	}
}
