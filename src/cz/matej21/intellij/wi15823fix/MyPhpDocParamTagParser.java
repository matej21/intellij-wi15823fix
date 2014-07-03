package cz.matej21.intellij.wi15823fix;

import com.intellij.lang.PsiBuilder;
import com.jetbrains.php.lang.documentation.phpdoc.parser.tags.PhpDocParamTagParser;
import com.jetbrains.php.lang.parser.PhpPsiBuilder;


public class MyPhpDocParamTagParser extends PhpDocParamTagParser {

	@Override
	public boolean parseContents(PhpPsiBuilder builder) {
		if (parseVar(builder)) {
			return true;
		}
		PsiBuilder.Marker start = builder.mark();
		if ((parseTypes(builder)) && (parseVar(builder))) {
			start.drop();
			return true;
		}
		start.rollbackTo();
		start = builder.mark();
		if ((parseTypes(builder))) {
			start.drop();
			return true;
		}
		start.rollbackTo();
		return false;
	}
}
