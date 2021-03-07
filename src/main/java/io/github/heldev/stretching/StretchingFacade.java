package io.github.heldev.stretching;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import io.github.heldev.stretching.editor.NewEditorListener;
import io.github.heldev.stretching.editor.StretchingService;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
final public class StretchingFacade {

	private static final EditorFactory editorFactory = EditorFactory.getInstance();

	private final Project project;

	public StretchingFacade(Project project) {
		this.project = project;
	}

	public void enableStretching() {
		editorFactory.addEditorFactoryListener(NewEditorListener.get(project));

		getProjectEditors().forEach(getService()::enableStretching);
	}

	public void disableStretching() {
		editorFactory.removeEditorFactoryListener(NewEditorListener.get(project));
		getProjectEditors().forEach(getService()::disableStretching);
	}

	private List<Editor> getProjectEditors() {

		return Arrays.stream(editorFactory.getAllEditors())
				.filter(editor -> project.equals(editor.getProject()))
				.collect(toList());
	}

	private StretchingService getService() {
		return project.getService(StretchingService.class);
	}

}
