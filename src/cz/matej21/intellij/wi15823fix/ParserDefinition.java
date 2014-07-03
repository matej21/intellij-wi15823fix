package cz.matej21.intellij.wi15823fix;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.jetbrains.php.lang.documentation.phpdoc.parser.PhpDocParser;
import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocTagParserRegistry;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocPsiCreator;
import com.jetbrains.php.lang.parser.PhpParserDefinition;
import org.jetbrains.annotations.NotNull;


public class ParserDefinition extends PhpParserDefinition {

	@NotNull
	@Override
	public PsiElement createElement(ASTNode node) {

		IElementType type = node.getElementType();
		if (type == PhpDocPsiCreator.phpDocParam) {
			return new MyPhpDocParamTag(node);
		}
		return super.createElement(node);
	}

	public ParserDefinition() {
		super();
		new PhpDocParser(); //initialize phpdoc parser registry
		PhpDocTagParserRegistry.register("@param", new MyPhpDocParamTagParser());
	}

}
