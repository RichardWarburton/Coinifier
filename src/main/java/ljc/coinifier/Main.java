package ljc.coinifier;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Main {

	private static final List<Transformer> defaultTransformers = Lists.newArrayList();
	
	public static void main(String[] args) throws IOException {
		new Main(defaultTransformers, args).run();
	}
	
	private final String[] args;
	private final List<Transformer> transformers;

	public Main(List<Transformer> transformers, String ... args) {
		this.transformers = transformers;
		this.args = args;
	}

	private void run() throws IOException {
		for (String arg : args) {
			transformFile(arg);
		}
	}

	@VisibleForTesting
	String transformFile(String arg) throws IOException {
		File file = new File(arg);
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		Document document = new Document(Files.toString(file, Charset.defaultCharset()));
		parser.setSource(document.get().toCharArray());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		ASTRewrite rewriter = ASTRewrite.create(compilationUnit.getAST());
		
		for (Transformer transformer : transformers) {
			transformer.transformAST(compilationUnit, rewriter);
		}
		
		TextEdit edits = rewriter.rewriteAST(document, null);
		try {
			edits.apply(document);
		} catch (MalformedTreeException | BadLocationException e) {
			e.printStackTrace();
		}
		
		return document.get();
	}

}
