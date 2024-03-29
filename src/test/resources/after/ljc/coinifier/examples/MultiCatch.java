package ljc.coinifier.examples;

public class MultiCatch {

	public void example() {
		try {
			Class.forName("ljc.coinifier.MultiCatch").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void doNothing() {
		try {
			Class.forName("ljc.coinifier.MultiCatch").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void processTwo() {
		try {
			Class.forName("ljc.coinifier.MultiCatch").newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}
}
