package ljc.coinifier.transformers;

import java.util.Iterator;
import java.util.List;

import ljc.coinifier.VisitorBasedTransformer;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;

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
		System.out.println("NODE:"+node);
		List<CatchClause> clauses = node.catchClauses();
		Iterator<CatchClause> it = clauses.iterator();
		while (it.hasNext()) {
			CatchClause outer = it.next();
			while (it.hasNext()) {
				CatchClause inner = it.next();
				if (outer.getBody().subtreeMatch(matcher, inner.getBody())) {
					// merge
					SingleVariableDeclaration svd = inner.getException();
					
				} else {
					break;
				}
			}
		}
		
		return super.visit(node);
	}

}

