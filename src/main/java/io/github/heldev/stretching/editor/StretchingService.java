package io.github.heldev.stretching.editor;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import io.github.heldev.stretching.editor.inlays.IndentChangeInlayingDocumentListener;
import io.github.heldev.stretching.editor.inlays.InlayManipulator;

@Service
public final class StretchingService {

	private final Project project;

	public StretchingService(Project project) {
		this.project = project;
	}

	public void enableStretching(Editor editor) {
		getInlayManipulator().addIndentInlays(editor);
		editor.getDocument().addDocumentListener(IndentChangeInlayingDocumentListener.get(project));
	}

	public void disableStretching(Editor editor) {
		getInlayManipulator().removeIndentInlays(editor);
		editor.getDocument().removeDocumentListener(IndentChangeInlayingDocumentListener.get(project));
	}

	private InlayManipulator getInlayManipulator() {
		return project.getService(InlayManipulator.class);
	}
}
