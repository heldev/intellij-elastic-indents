package io.github.heldev;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;


public class ToggleElasticIndents extends AnAction {

	private static final EditorFactory editorFactory = EditorFactory.getInstance();

	private static final InlayManipulator inlayManipulator = new InlayManipulator(editorFactory);

	private static final IndentChangeDocumentListener indentChangeListener =
			new IndentChangeDocumentListener(inlayManipulator);

	private final NewEditorListener newEditorListener = new NewEditorListener(inlayManipulator, indentChangeListener);

	private Disposable disposable;

	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		if (disposable == null) {
			enableIndents();
		} else {
			disableIndents();
		}
	}

	private void enableIndents() {
		disposable = Disposer.newDisposable("elasticIndentEditorListenerDisposable");
		inlayManipulator.addIndentInlays();

		for(Editor editor: editorFactory.getAllEditors()) {
			editor.getDocument().addDocumentListener(indentChangeListener);
		}

		editorFactory.addEditorFactoryListener(newEditorListener, disposable);
	}

	private void disableIndents() {
		Disposer.dispose(disposable);

		inlayManipulator.removeIndentInlays();
	}

}
