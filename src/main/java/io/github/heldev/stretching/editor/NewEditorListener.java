package io.github.heldev.stretching.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import io.github.heldev.stretching.editor.inlays.IndentChangeInlayingDocumentListener;
import io.github.heldev.stretching.editor.inlays.InlayManipulator;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class NewEditorListener implements EditorFactoryListener {

	private final Project project;

	public static NewEditorListener get(Project project) {
		return new NewEditorListener(project);
	}

	private NewEditorListener(Project project) {
		this.project = project;
	}

	@Override
	public void editorCreated(@NotNull EditorFactoryEvent event) {
		Editor editor = event.getEditor();

		if (project.equals(event.getEditor().getProject())) {
			getInlayManipulator().addIndentInlays(editor);
			editor.getDocument().addDocumentListener(IndentChangeInlayingDocumentListener.get(project));
		}
	}

	private InlayManipulator getInlayManipulator() {
		return project.getService(InlayManipulator.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		NewEditorListener that = (NewEditorListener) o;
		return Objects.equals(project, that.project);
	}

	@Override
	public int hashCode() {
		return Objects.hash(project);
	}
}
