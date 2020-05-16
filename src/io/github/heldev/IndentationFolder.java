package io.github.heldev;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.github.heldev.Constants.foldingGroup;

public class IndentationFolder extends FoldingBuilderEx {
	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement psiElement, @NotNull Document document, boolean b) {

		return PsiTreeUtil.findChildrenOfType(psiElement, PsiWhiteSpace.class)
				.stream()
				.filter(whitespaceElement -> (whitespaceElement.getText().endsWith(" ") && (whitespaceElement.getText().contains("\n") || whitespaceElement.getPrevSibling() == null)))
				.map(this::getIndentationFoldingDescriptor).toArray(FoldingDescriptor[]::new);
	}

	@NotNull
	private FoldingDescriptor getIndentationFoldingDescriptor(PsiWhiteSpace indentation) {
		TextRange range = indentation.getTextRange();
		int start = range.getStartOffset() + indentation.getText().lastIndexOf('\n') + 1;

		return new FoldingDescriptor(indentation.getNode(), new TextRange(start, range.getEndOffset()), foldingGroup);
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode astNode) {
		String text = astNode.getText();
		return text.substring(text.lastIndexOf('\n') + 1).replace("  ", "    ");
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
		return true;
	}
}
