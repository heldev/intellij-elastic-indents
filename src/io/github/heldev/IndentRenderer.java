package io.github.heldev;

import com.intellij.codeInsight.daemon.impl.HintRenderer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;

import static java.util.Collections.nCopies;

class IndentRenderer extends HintRenderer {
	private final Integer size;

	public IndentRenderer(Integer size) {
		super(String.join("", nCopies(size, " ")));
		this.size = size;
	}

	@Override
	protected TextAttributes getTextAttributes(@NotNull Editor editor) {
		TextAttributes attributes = editor.getColorsScheme().getAttributes(DefaultLanguageHighlighterColors.STRING);
//		attributes.setBackgroundColor(JBColor.GREEN);

		return attributes;
	}

	@Override
	public int calcWidthInPixels(@NotNull Inlay inlay) {

		int charWidth = getFontMetrics(inlay.getEditor())
				.getMetrics()
				.charWidth('E');

		return charWidth * size;
	}
}
