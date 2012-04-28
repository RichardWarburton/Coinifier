package ljc.coinifier;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.nio.charset.Charset;

import ljc.coinifier.transformers.DiamondOperatorTransformer;
import ljc.coinifier.transformers.ForeachLoopTransformer;
import ljc.coinifier.transformers.MultiCatchTransformer;

import org.junit.Test;

import com.google.common.io.Files;

public class TestSuite {

	private static final String RESOURCES = "src/test/resources/";
	private static final String PACKAGE = "ljc/coinifier/examples/";

	@Test
	public void forLoops() throws Exception {
		assertExamplesEqual("OldCollectionForLoop", new ForeachLoopTransformer());
	}
	
	@Test
	public void diamondOperator() throws Exception {
		System.out.println(new File(".").getAbsolutePath());
		assertExamplesEqual("DiamondOperator", new DiamondOperatorTransformer());
	}
	
	@Test
	public void multiCatch() throws Exception {
		assertExamplesEqual("MultiCatch", new MultiCatchTransformer());
	}
	
	void assertExamplesEqual(String name, Transformer ... transformers) throws Exception {
		String beforeFile = RESOURCES + "before/" + PACKAGE + name + ".java";
		String afterFile = RESOURCES + "after/" + PACKAGE + name + ".java";
		Main main = new Main(asList(transformers), beforeFile);
		String result = main.transformFile(beforeFile);
		System.out.println("Generated:");
		System.out.println(result);
		
		File after = new File(afterFile);
		String expected = Files.toString(after, Charset.defaultCharset());
		System.out.println("Expected:");
		System.out.println(expected);
		
		assertEquals(expected, result);
	}
	
}