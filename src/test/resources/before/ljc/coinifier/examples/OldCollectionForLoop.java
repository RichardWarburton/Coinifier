package ljc.coinifier.examples;

import java.util.List;

public class OldCollectionForLoop {

	public void arrayExample(String[] args) {
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
	}

	public void listExample(List<String> args) {
		for (int i = 0; i < args.size(); i++) {
			System.out.println(args.get(i));
		}
	}

}