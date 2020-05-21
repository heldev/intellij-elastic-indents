package io.github.heldev;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Integer.MAX_VALUE;

public class InlayManipulator {
	private final EditorFactory editorFactory;

	public InlayManipulator(EditorFactory editorFactory) {
		this.editorFactory = editorFactory;
	}


	public void refreshIndentInlays(Document document) {
		for(var editor: editorFactory.getEditors(document)) {
			removeIndentInlays(editor.getInlayModel());
			addIndentInlays(editor);
		}
	}

	public void addIndentInlays() {
		for(var editor: editorFactory.getAllEditors()) {
			removeIndentInlays(editor.getInlayModel());
			addIndentInlays(editor);
		}
	}

	public void addIndentInlays(Editor editor) {
		var document = editor.getDocument();
		var inlayModel = editor.getInlayModel();

		inlayModel.setConsiderCaretPositionOnDocumentUpdates(false);

		IntStream.range(0, document.getLineCount())
				.forEach(line -> addIndentInlays(inlayModel, document, line));
	}

	private void addIndentInlays(InlayModel inlayModel, Document document, int line) {
		var lineStart = document.getLineStartOffset(line);
		var indent = getIndent(document, lineStart);

		if (! indent.isEmpty()) {
			inlayModel.addInlineElement(lineStart, new IndentRenderer(indent));
		}
	}

	private String getIndent(Document document, int lineStart) {

		return document
				.getText()
				.substring(lineStart)
				.split("[^ ]", 2)[0];
	}

	public void removeIndentInlays() {
		for(var editor: editorFactory.getAllEditors()) {
			removeIndentInlays(editor.getInlayModel());
		}
	}

	private void removeIndentInlays(InlayModel inlayModel) {
		getExistingIndentInlays(inlayModel)
				.forEach(Inlay::dispose);
	}

	private List<Inlay<? extends IndentRenderer>> getExistingIndentInlays(InlayModel inlayModel) {
		return inlayModel.getInlineElementsInRange(0, MAX_VALUE, IndentRenderer.class);
	}
}
