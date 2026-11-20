package hu.szkristof.BFEditor;

import java.io.*;

public class Preferences implements Serializable {

	private int memoryCellCount = 1024;
	private MemoryCellOverflowBehaviour memoryCellOverflowBehaviour = MemoryCellOverflowBehaviour.WrapAround;
	private int defaultMemoryCellValue = 0;

	public int getMemoryCellCount() {
		return memoryCellCount;
	}

	public void setMemoryCellCount(int memoryCellCount) {
		this.memoryCellCount = memoryCellCount;
	}

	public MemoryCellOverflowBehaviour getMemoryCellOverflowBehaviour() {
		return memoryCellOverflowBehaviour;
	}

	public void setMemoryCellOverflowBehaviour(MemoryCellOverflowBehaviour memoryCellOverflowBehaviour) {
		this.memoryCellOverflowBehaviour = memoryCellOverflowBehaviour;
	}

	public int getDefaultMemoryCellValue() {
		return defaultMemoryCellValue;
	}

	public void setDefaultMemoryCellValue(int defaultMemoryCellValue) {
		this.defaultMemoryCellValue = defaultMemoryCellValue;
	}

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

	public enum MemoryCellOverflowBehaviour {

		None,
		WrapAround,
		Cap

	}

}
