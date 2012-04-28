package ljc.coinifier.transformers;

import static org.eclipse.jdt.core.dom.TryStatement.CATCH_CLAUSES_PROPERTY;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ljc.coinifier.VisitorBasedTransformer;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * Converts single catch blocks to multiple ones, eg:
 *
 * try { foo(); }
 * catch (ExceptionA e) { print(e); }
 * catch (ExceptionB e) { print(e); } 
 *
 * -> 
 *
 * try { foo(); }
 * catch (ExceptionA | ExceptionB e) { print(e); }
 * 
 */
@SuppressWarnings("unchecked")
public class MultiCatchTransformer extends VisitorBasedTransformer {

	private final ASTMatcher matcher = new ASTMatcher();
	
	@Override
	public boolean visit(TryStatement node) {
		List<CatchClause> clauses = node.catchClauses();
		Iterator<CatchClause> it = clauses.iterator();
		List<CatchClause> toRemove = new ArrayList<>();
		while (it.hasNext()) {
			CatchClause outer = it.next();
			SingleVariableDeclaration outerException = outer.getException();
			UnionType unionType = null;
			while (it.hasNext()) {
				CatchClause inner = it.next();
				if (outer.getBody().subtreeMatch(matcher, inner.getBody())) {
					SingleVariableDeclaration exception = inner.getException();
					// TODO: support merging multicatch
					if (!exception.getType().isUnionType()) {
						SimpleType type = (SimpleType) exception.getType();
						toRemove.add(inner);
						AST ast = node.getAST();
						
						if (unionType == null) {
							unionType = ast.newUnionType();
							// TODO: error case
							SimpleType oldType = clone((SimpleType) outerException.getType());
							unionType.types().add(oldType);
						}

						unionType.types().add(clone(type));
					}
				} else {
					break;
				}
			}
			
			// record catch removal & type replacement
			if (!toRemove.isEmpty()) {
				ListRewrite catchRewriter = rewriter.getListRewrite(node, CATCH_CLAUSES_PROPERTY);
				for (CatchClause catchClause : toRemove) {
					catchRewriter.remove(catchClause, null);
				}
				rewriter.replace(outerException.getType(), unionType, null);
			}
		}
		
		return super.visit(node);
	}
	
	private SimpleType clone(SimpleType type) {
		String name = type.getName().toString();
		AST ast = type.getAST();
		return ast.newSimpleType(ast.newSimpleName(name));
	}

}

