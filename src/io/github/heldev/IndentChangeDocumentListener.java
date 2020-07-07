package io.github.heldev;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import org.jetbrains.annotations.NotNull;

import static com.intellij.util.DocumentUtil.getFirstNonSpaceCharOffset;

class IndentChangeDocumentListener implements DocumentListener {

	private final InlayManipulator inlayManipulator;

	public IndentChangeDocumentListener(InlayManipulator inlayManipulator) {
		this.inlayManipulator = inlayManipulator;
	}

	@Override
	public void documentChanged(@NotNull DocumentEvent event) {
 		if (isNewLineChange(event) || isIndentChange(event)) {
			inlayManipulator.refreshIndentInlays(event.getDocument());
		}
	}

	private boolean isNewLineChange(DocumentEvent event) {

		return (event.getNewFragment().toString() + event.getOldFragment())
				.contains("\n");
	}

	private boolean isIndentChange(DocumentEvent event) {
		Document document = event.getDocument();
		int changeOffset = event.getOffset();
		int lineStartOffset = document.getLineStartOffset(document.getLineNumber(changeOffset));

		return document.getText().substring(lineStartOffset, changeOffset).matches("\\s*");
	}
}
