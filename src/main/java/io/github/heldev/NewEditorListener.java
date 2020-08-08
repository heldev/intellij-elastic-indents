package io.github.heldev;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

class NewEditorListener implements EditorFactoryListener {

	private final InlayManipulator inlayManipulator;
	private final IndentChangeDocumentListener inlayDocumentListener;
	private final Project project;
	private final Supplier<Disposable> disposableSupplier;

	public NewEditorListener(
			InlayManipulator inlayManipulator,
			IndentChangeDocumentListener inlayDocumentListener,
			Project project,
			Supplier<Disposable> disposableSupplier) {

		this.inlayManipulator = inlayManipulator;
		this.inlayDocumentListener = inlayDocumentListener;
		this.project = project;
		this.disposableSupplier = disposableSupplier;
	}

	@Override
	public void editorCreated(@NotNull EditorFactoryEvent event) {
		Editor editor = event.getEditor();

		if (project.equals(event.getEditor().getProject())) {
			inlayManipulator.addIndentInlays(editor);
			editor.getDocument().addDocumentListener(inlayDocumentListener, disposableSupplier.get());
		}
	}
}
