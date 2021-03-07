package io.github.heldev.shrinking;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import io.github.heldev.shrinking.editor.NewEditorListener;
import io.github.heldev.shrinking.editor.ShrinkingService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public final class ShrinkingFacade {
	private final Project project;

	public ShrinkingFacade(Project project) {
		this.project = project;
	}

	public void enableShrinking() {
		getEditorFactory().addEditorFactoryListener(NewEditorListener.get(project));

		getProjectEditors()
				.forEach(getService()::enableShrinking);
	}

	public void disableShrinking() {
		getEditorFactory()
				.removeEditorFactoryListener(NewEditorListener.get(project));

		getProjectEditors().forEach(getService()::disableShrinking);
	}

	private EditorFactory getEditorFactory() {
		return EditorFactory.getInstance();
	}

	private ShrinkingService getService() {
		return project.getService(ShrinkingService.class);
	}

	private List<Editor> getProjectEditors() {

		return Stream.of(EditorFactory.getInstance().getAllEditors())
				.filter(editor -> project.equals(editor.getProject()))
				.collect(Collectors.toList());
	}
}
