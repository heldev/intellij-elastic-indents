package io.github.heldev.stretching.editor.inlays;

import com.intellij.codeInsight.daemon.impl.HintRenderer;
import com.intellij.codeInsight.hints.HintWidthAdjustment;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import static java.util.Collections.nCopies;

class IndentInlayRenderer extends HintRenderer {
	private final Integer size;

	public IndentInlayRenderer(Integer size) {
		super(String.join("", nCopies(size, " ")));
		this.size = size;
	}

	@Override
	protected TextAttributes getTextAttributes(@NotNull Editor editor) {
		TextAttributes attributes = editor.getColorsScheme().getAttributes(DefaultLanguageHighlighterColors.STRING);
//		visualizeInlaysForDebug(attributes);

		return attributes;
	}

	private void visualizeInlaysForDebugging(TextAttributes attributes) {
		attributes.setBackgroundColor(JBColor.GREEN);
	}

	@Override
	public int calcWidthInPixels(@NotNull Inlay inlay) {
		String indentText = String.join("", nCopies(size, " "));

		return calcWidthInPixels(
				inlay.getEditor(),
				indentText,
				new HintWidthAdjustment(indentText, null, 0));
	}
}
