package ljc.coinifier;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;


public interface Transformer {

	void transformAST(CompilationUnit compilationUnit, ASTRewrite rewriter);
	
}
