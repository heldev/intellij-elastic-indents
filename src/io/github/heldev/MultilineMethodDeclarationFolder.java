package io.github.heldev;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.heldev.Constants.foldingGroup;

public class MultilineMethodDeclarationFolder extends FoldingBuilderEx {
	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement psiElement, @NotNull Document document, boolean b) {

		return PsiTreeUtil.findChildrenOfType(psiElement, PsiParameterList.class)
				.stream()
				.map(PsiElement::getLastChild)
				.filter(this::isUnderindentedMethodClosingParen)
				.map( closingParen -> new FoldingDescriptor(closingParen.getNode(), closingParen.getTextRange(), foldingGroup))
				.toArray(FoldingDescriptor[]::new);
	}

	private boolean isUnderindentedMethodClosingParen(PsiElement closingParen) {

		return closingParen.getPrevSibling() instanceof PsiWhiteSpace
				&& closingParen.getPrevSibling().textContains('\n');
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode closingParen) {
		return "    " + closingParen.getText();
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
		return true;
	}
}
