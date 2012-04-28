package ljc.coinifier.transformers;

import static org.eclipse.jdt.core.dom.ParameterizedType.TYPE_ARGUMENTS_PROPERTY;

import java.util.List;

import ljc.coinifier.VisitorBasedTransformer;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

/**
 * Convert constructors in simple cases to use the diamond operator, eg:
 * 
 * List<Foo> list = new ArrayList<Foo>(); -> List<Foo> list = new ArrayList<>();
 */
@SuppressWarnings("unchecked")
public class DiamondOperatorTransformer extends VisitorBasedTransformer {

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		Type lType = node.getType();
		if (areArgumentsInferrable(lType)) {
			ParameterizedType lParamType = (ParameterizedType) lType;
			List<Type> lTypearguments = lParamType.typeArguments();
			
			for (VariableDeclarationFragment fragment : (List<VariableDeclarationFragment>) node.fragments()) {
				Expression init = fragment.getInitializer();
				
				if (init != null && init instanceof ClassInstanceCreation) {
					ClassInstanceCreation constructor = (ClassInstanceCreation) init;
					Type rType = constructor.getType();
					
					if(canDiamondise(rType)) {
						diamondiseMethod(node, lTypearguments, rType);
					}
				}
			}
		}
		
		return super.visit(node);
	}

	private void diamondiseMethod(VariableDeclarationStatement node,
			List<Type> lTypearguments, Type rType) {
		ParameterizedType rParamType = (ParameterizedType) rType;
		List<Type> rTypeArguments = rParamType.typeArguments();
		// TODO: generalise this
		if(rTypeArguments.size() == lTypearguments.size()) {
			ListRewrite listRewriter = rewriter.getListRewrite(rParamType, TYPE_ARGUMENTS_PROPERTY);
			for (Type argument : rTypeArguments) {										
				listRewriter.remove(argument, null);
			}
		}
	}

	private boolean canDiamondise(Type rType) {
		return rType.isParameterizedType() && !rType.isArrayType();
	}

	private boolean areArgumentsInferrable(Type lType) {
		return lType.isParameterizedType() && !lType.isArrayType() && !lType.isWildcardType();
	}

}
