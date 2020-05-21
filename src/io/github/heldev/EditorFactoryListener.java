package io.github.heldev;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import org.jetbrains.annotations.NotNull;

class EditorFactoryListener implements com.intellij.openapi.editor.event.EditorFactoryListener {

	private final InlayManipulator inlayManipulator;
	private final InlayIndentDocumentListener inlayDocumentListener;

	public EditorFactoryListener(InlayManipulator inlayManipulator, InlayIndentDocumentListener inlayDocumentListener) {
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
