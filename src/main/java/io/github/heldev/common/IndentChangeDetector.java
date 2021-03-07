package io.github.heldev.common;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.project.Project;

@Service
public final class IndentChangeDetector {

	private IndentChangeDetector(Project project) {
	}

	public boolean isIndentChange(DocumentEvent event) {
 		return isNewLineChange(event) || isTrailingSpaceChange(event);
	}

	private boolean isNewLineChange(DocumentEvent event) {

		return (event.getNewFragment().toString() + event.getOldFragment())
				.contains("\n");
	}

	private boolean isTrailingSpaceChange(DocumentEvent event) {
		Document document = event.getDocument();
		int changeOffset = event.getOffset();
		int lineStartOffset = document.getLineStartOffset(document.getLineNumber(changeOffset));

		return document.getText().substring(lineStartOffset, changeOffset).matches("\\s*");
	}

}
