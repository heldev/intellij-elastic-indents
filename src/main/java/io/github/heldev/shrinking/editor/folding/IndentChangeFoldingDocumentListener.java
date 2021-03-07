package io.github.heldev.shrinking.editor.folding;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import io.github.heldev.common.IndentChangeDetector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class IndentChangeFoldingDocumentListener implements DocumentListener {

	public static IndentChangeFoldingDocumentListener get(Project project) {
		return new IndentChangeFoldingDocumentListener(project);
	}

	private final Project project;

	private IndentChangeFoldingDocumentListener(Project project) {
		this.project = project;
	}

	@Override
	public void documentChanged(@NotNull DocumentEvent event) {
		if (getIndentChangeDetector().isIndentChange(event)) {
			for (Editor editor : getEditors(event)) {
				getFoldManipulator().refreshFoldings(editor);
			}
		}
	}

	private Editor[] getEditors(@NotNull DocumentEvent event) {

		return EditorFactory.getInstance()
				.getEditors(event.getDocument(), project);
	}

	private IndentChangeDetector getIndentChangeDetector() {
		return project.getService(IndentChangeDetector.class);
	}

	private FoldManipulator getFoldManipulator() {
		return project.getService(FoldManipulator.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IndentChangeFoldingDocumentListener that = (IndentChangeFoldingDocumentListener) o;
		return Objects.equals(project, that.project);
	}

	@Override
	public int hashCode() {
		return Objects.hash(project);
	}
}
