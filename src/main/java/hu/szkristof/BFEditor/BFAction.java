package hu.szkristof.BFEditor;

/** Represents an action that can be invoked by a menu item or keyboard shortcut. */
@FunctionalInterface
public interface BFAction {

	/** Performs the action. */
	void actionPerformed();

}
