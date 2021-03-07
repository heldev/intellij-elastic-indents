package io.github.heldev.shrinking.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class NewEditorListener implements EditorFactoryListener {

	public static NewEditorListener get(Project project) {
		return new NewEditorListener(project);
	}

	private final Project project;

	private NewEditorListener(Project project) {
		this.project = project;
	}

	@Override
	public void editorCreated(@NotNull EditorFactoryEvent event) {
		Editor editor = event.getEditor();
		if (project.equals(editor.getProject())) {
			getService().enableShrinking(editor);
		}
	}

	private ShrinkingService getService() {
		return project.getService(ShrinkingService.class);
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
