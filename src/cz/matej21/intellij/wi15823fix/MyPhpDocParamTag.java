package cz.matej21.intellij.wi15823fix;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NullableLazyValue;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocParamTagImpl;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocParamTag;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MyPhpDocParamTag extends PhpDocParamTagImpl {

	public MyPhpDocParamTag(ASTNode node) {
		super(node);
	}

	@Nullable
	@Override
	public String getVarName() {
		String varName = super.getVarName();
		if (varName != null && !varName.isEmpty()) {
			return varName;
		}
		Parameter parameter = getCorrespondingParameter();
		if (parameter != null) {
			return parameter.getName();
		}
		return "";
	}

	@NotNull
	@Override
	public PhpType getType() {
		PhpType type = super.getType();
		Parameter parameter = getCorrespondingParameter();
		if (parameter != null) {
			type.add(parameter.getDeclaredType());
		}

		return type;
	}

	protected Parameter getCorrespondingParameter() {
		return new NullableLazyValue<Parameter>() {
			@Nullable
			@Override
			protected Parameter compute() {
				PhpPsiElement prevDocTag = getPrevPsiSibling();
				Short nthParameter = 0;
				while (prevDocTag != null && prevDocTag instanceof PhpDocTag) {
					if (prevDocTag instanceof PhpDocParamTag) {
						nthParameter++;
					}
					prevDocTag = prevDocTag.getPrevPsiSibling();
				}
				PhpDocComment comment = (PhpDocComment) getParent();
				PsiElement nextSibling = comment.getNextPsiSibling();
				Parameter parameter = null;
				if (nextSibling instanceof Method) {
					Method method = (Method) nextSibling;
					if (method.getParameters().length > nthParameter) {
						parameter = method.getParameters()[nthParameter];
					}
				}
				return parameter;
			}
		}.compute();
	}
}
