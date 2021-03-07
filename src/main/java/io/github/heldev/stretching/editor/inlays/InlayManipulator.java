package io.github.heldev.stretching.editor.inlays;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.project.Project;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Integer.MAX_VALUE;

@Service
public final class InlayManipulator {
	private final Project project;

	public InlayManipulator(Project project) {
		this.project = project;
	}

	public void refreshIndentInlays(Document document) {
		for(Editor editor: EditorFactory.getInstance().getEditors(document, project)) {
			removeIndentInlays(editor);
			addIndentInlays(editor);
		}
	}

	public void addIndentInlays(Editor editor) {
		Document document = editor.getDocument();
		InlayModel inlayModel = editor.getInlayModel();

		inlayModel.setConsiderCaretPositionOnDocumentUpdates(false);

		IntStream.range(0, document.getLineCount())
				.forEach(line -> addIndentInlays(inlayModel, document, line));
	}

	private void addIndentInlays(InlayModel inlayModel, Document document, int line) {
		int lineStart = document.getLineStartOffset(line);
		Integer indentSpaceCount = countIndentSpaces(document, lineStart);

		if (0 < indentSpaceCount) {
			inlayModel.addInlineElement(lineStart, new IndentInlayRenderer(indentSpaceCount));
		}
	}

	private Integer countIndentSpaces(Document document, int lineStart) {

		return document
				.getText()
				.substring(lineStart)
				.split("[^ ]", 2)[0]
				.length();
	}

	public void removeIndentInlays(Editor editor) {
		getExistingIndentInlays(editor.getInlayModel()).forEach(Inlay::dispose);
	}

	private List<Inlay<? extends IndentInlayRenderer>> getExistingIndentInlays(InlayModel inlayModel) {
		return inlayModel.getInlineElementsInRange(0, MAX_VALUE, IndentInlayRenderer.class);
	}
}
