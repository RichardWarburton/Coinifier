package ljc.coinifier.examples;

import java.util.List;

public class OldCollectionForLoop {

	public void arrayExample(String[] args) {
		for (String arg : args) {
			System.out.println(arg);
		}
	}

	public void listExample(List<String> args) {
		for (String arg : args) {
			System.out.println(arg);
		}
	}

}
