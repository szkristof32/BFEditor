package hu.szkristof.BFEditor;

import java.io.*;

/**
 * Stores runtime memory and overflow preferences for the Brainfuck engine.
 * Preferences can be serialized to and deserialized from disk.
 */
public class Preferences implements Serializable {

	private int memoryCellCount = 1024;
	private MemoryCellOverflowBehaviour memoryCellOverflowBehaviour = MemoryCellOverflowBehaviour.WrapAround;
	private int defaultMemoryCellValue = 0;

	/**
	 * Gets the configured memory segment size.
	 *
	 * @return number of memory cells
	 */
	public int getMemoryCellCount() {
		return memoryCellCount;
	}

	/**
	 * Sets the configured memory segment size.
	 *
	 * @param memoryCellCount number of memory cells
	 */
	public void setMemoryCellCount(int memoryCellCount) {
		this.memoryCellCount = memoryCellCount;
	}

	/**
	 * Gets the memory-cell overflow behavior.
	 *
	 * @return overflow behavior
	 */
	public MemoryCellOverflowBehaviour getMemoryCellOverflowBehaviour() {
		return memoryCellOverflowBehaviour;
	}

	/**
	 * Sets the memory-cell overflow behavior.
	 *
	 * @param memoryCellOverflowBehaviour overflow behavior
	 */
	public void setMemoryCellOverflowBehaviour(MemoryCellOverflowBehaviour memoryCellOverflowBehaviour) {
		this.memoryCellOverflowBehaviour = memoryCellOverflowBehaviour;
	}

	/**
	 * Gets the value assigned to memory cells at startup.
	 *
	 * @return initial memory-cell value
	 */
	public int getDefaultMemoryCellValue() {
		return defaultMemoryCellValue;
	}

	/**
	 * Sets the value assigned to memory cells at startup.
	 *
	 * @param defaultMemoryCellValue initial memory-cell value
	 */
	public void setDefaultMemoryCellValue(int defaultMemoryCellValue) {
		this.defaultMemoryCellValue = defaultMemoryCellValue;
	}

	/** Serializes these preferences to {@code preferences.dat}. */
	public void serialize() {
		try (FileOutputStream fileOutputStream = new FileOutputStream("preferences.dat")) {
			try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
				objectOutputStream.writeObject(this);
				System.out.println("Serialization successful");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deserializes preferences from {@code preferences.dat}.
	 * If no saved file exists, returns default preferences.
	 *
	 * @return saved or default preferences
	 */
	public static Preferences deserialize() {
		try (FileInputStream fileInputStream = new FileInputStream("preferences.dat")) {
			try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
				Preferences prefs = (Preferences) objectInputStream.readObject();
				System.out.println("Serialization successful");
				return prefs;
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Preferences file not found, using default values.");
			return new Preferences();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** Defines how an unsigned 8-bit memory cell handles overflow. */
	public enum MemoryCellOverflowBehaviour {

		None,
		WrapAround,
		Cap

	}

}
