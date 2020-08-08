package io.github.heldev;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import org.jetbrains.annotations.NotNull;

class NewEditorListener implements EditorFactoryListener {

	private final InlayManipulator inlayManipulator;
	private final IndentChangeDocumentListener inlayDocumentListener;

	public NewEditorListener(InlayManipulator inlayManipulator, IndentChangeDocumentListener inlayDocumentListener) {
		this.inlayManipulator = inlayManipulator;
		this.inlayDocumentListener = inlayDocumentListener;
	}

	@Override
	public void editorCreated(@NotNull EditorFactoryEvent event) {
		Editor editor = event.getEditor();

		inlayManipulator.addIndentInlays(editor);
		editor.getDocument().addDocumentListener(inlayDocumentListener);
	}
}
