package io.github.heldev.stretching.editor.inlays;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import io.github.heldev.common.IndentChangeDetector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class IndentChangeInlayingDocumentListener implements DocumentListener {

	public static IndentChangeInlayingDocumentListener get(Project project) {
		return new IndentChangeInlayingDocumentListener(project);
	}

	private final Project project;

	private IndentChangeInlayingDocumentListener(Project project) {
		this.project = project;
	}

	@Override
	public void documentChanged(@NotNull DocumentEvent event) {
 		if (getIndentChangeDetector().isIndentChange(event)) {
			getStretchingService().refreshIndentInlays(event.getDocument());
		}
	}

	private IndentChangeDetector getIndentChangeDetector() {
		return project.getService(IndentChangeDetector.class);
	}

	private InlayManipulator getStretchingService() {
		return project.getService(InlayManipulator.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IndentChangeInlayingDocumentListener that = (IndentChangeInlayingDocumentListener) o;
		return Objects.equals(project, that.project);
	}

	@Override
	public int hashCode() {
		return Objects.hash(project);
	}
}
