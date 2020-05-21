package io.github.heldev;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import org.jetbrains.annotations.NotNull;

import static com.intellij.util.DocumentUtil.getFirstNonSpaceCharOffset;

class InlayIndentDocumentListener implements DocumentListener {

	private final InlayManipulator inlayManipulator;

	public InlayIndentDocumentListener(InlayManipulator inlayManipulator) {
		this.inlayManipulator = inlayManipulator;
	}

	@Override
	public void documentChanged(@NotNull DocumentEvent event) {
		Document document = event.getDocument();

		var isNewLineChange = (event.getNewFragment() + event.getOldFragment().toString()).contains("\n");
		var isIndentChange = getFirstNonSpaceCharOffset(document, document.getLineNumber(event.getOffset())) <= event.getOffset();

		if (isNewLineChange || isIndentChange) {
			inlayManipulator.refreshIndentInlays(document);
		}
	}
}
