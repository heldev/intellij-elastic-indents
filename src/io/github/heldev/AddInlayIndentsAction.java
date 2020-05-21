package io.github.heldev;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;


public class AddInlayIndentsAction extends AnAction {

	private static final EditorFactory editorFactory = EditorFactory.getInstance();

	private static final InlayManipulator inlayManipulator = new InlayManipulator(editorFactory);
	private static final InlayIndentDocumentListener documentListener = new InlayIndentDocumentListener(inlayManipulator);
	private final EditorFactoryListener editorFactoryListener = new EditorFactoryListener(inlayManipulator, documentListener);

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

		editorFactory.addEditorFactoryListener(editorFactoryListener, disposable);
		inlayManipulator.addIndentInlays();
	}

	private void disableIndents() {
		Disposer.dispose(disposable);
		disposable = null;

		inlayManipulator.removeIndentInlays();
	}

}
