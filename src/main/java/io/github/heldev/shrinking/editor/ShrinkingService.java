package io.github.heldev.shrinking.editor;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import io.github.heldev.shrinking.editor.folding.FoldManipulator;
import io.github.heldev.shrinking.editor.folding.IndentChangeFoldingDocumentListener;

@Service
public final class ShrinkingService {

	private final Project project;

	public ShrinkingService(Project project) {
		this.project = project;
	}

	public void enableShrinking(Editor editor) {
		getFoldingManipulator().addFoldings(editor);
		editor.getDocument().addDocumentListener(IndentChangeFoldingDocumentListener.get(project));
	}

	public void disableShrinking(Editor editor) {
		editor.getDocument().removeDocumentListener(IndentChangeFoldingDocumentListener.get(project));
		getFoldingManipulator().removeFoldings(editor);
	}

	private FoldManipulator getFoldingManipulator() {
		return project.getService(FoldManipulator.class);
	}

}
