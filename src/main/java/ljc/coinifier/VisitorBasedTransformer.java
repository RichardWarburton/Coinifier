package ljc.coinifier;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public abstract class VisitorBasedTransformer extends ASTVisitor implements Transformer {

	protected ASTRewrite rewriter;
	
	@Override
	public final void transformAST(CompilationUnit compilationUnit, ASTRewrite rewriter) {
		this.rewriter = rewriter;
		compilationUnit.accept(this);
	}

}
