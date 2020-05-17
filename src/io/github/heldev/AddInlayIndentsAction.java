package io.github.heldev;

import com.intellij.codeInsight.daemon.impl.HintRenderer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

import static com.intellij.util.DocumentUtil.getFirstNonSpaceCharOffset;
import static java.lang.Integer.MAX_VALUE;

public class AddInlayIndentsAction extends AnAction {

	private static final EditorFactory editorFactory = EditorFactory.getInstance();

	private static class InlayIndentDocumentListener implements DocumentListener {

		private final Disposable disposable;

		public InlayIndentDocumentListener(Disposable disposable) {
			this.disposable = disposable;
		}

		public void addIndentInlays() {
			for(var editor: editorFactory.getAllEditors()) {
				addIndentInlays(editor);
				editor.getDocument().addDocumentListener(this, disposable);
			}
		}

		@Override
		public void documentChanged(@NotNull DocumentEvent event) {
			Document document = event.getDocument();

			for(var editor: editorFactory.getEditors(document)) {
				var isNewLineChange = (event.getNewFragment() + event.getOldFragment().toString()).contains("\n");
				var isIndentChange = getFirstNonSpaceCharOffset(document, document.getLineNumber(event.getOffset())) <= event.getOffset();

				if (isNewLineChange || isIndentChange) {
					removeIndentInlays(editor.getInlayModel());
					addIndentInlays(editor);
				}
			}
		}

		private void addIndentInlays(Editor editor) {
			var document = editor.getDocument();
			var inlayModel = editor.getInlayModel();

			inlayModel.setConsiderCaretPositionOnDocumentUpdates(false);

			IntStream.range(0, document.getLineCount())
					.forEach(line -> addIndentInlays(inlayModel, document, line));
		}

		public void removeIndentInlays() {
			for(var editor: editorFactory.getAllEditors()) {
				removeIndentInlays(editor.getInlayModel());
			}
		}

		private void removeIndentInlays(InlayModel inlayModel) {
			getExistingIndentInlays(inlayModel)
					.forEach(Inlay::dispose);
		}

		private List<Inlay<? extends IndentRenderer>> getExistingIndentInlays(InlayModel inlayModel) {
			return inlayModel.getInlineElementsInRange(0, MAX_VALUE, IndentRenderer.class);
		}


		private void addIndentInlays(InlayModel inlayModel, Document document, int line) {
			var lineStart = document.getLineStartOffset(line);
			var indent = getIndent(document, lineStart);

			if (! indent.isEmpty()) {
				inlayModel.addInlineElement(lineStart, new IndentRenderer(indent));
			}
		}

		private String getIndent(Document document, int lineStart) {

			return document
					.getText()
					.substring(lineStart)
					.split("[^ ]", 2)[0];
		}
	}

	private InlayIndentDocumentListener editorListener;

	private final EditorFactoryListener editorFactoryListener = new EditorFactoryListener() {
		@Override
		public void editorCreated(@NotNull EditorFactoryEvent event) {
			event.getEditor();
			editorListener.addIndentInlays();
		}
	};

	private Disposable disposable;


	@Override
	public void update(@NotNull AnActionEvent e) {
		setEnabledInModalContext(true);
		// Using the event, evaluate the context, and enable or disable the action.
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {

		if (disposable == null) {
			disposable = Disposer.newDisposable("elasticIndentEditorListenerDisposable");

			editorListener = new InlayIndentDocumentListener(disposable);
			editorFactory.addEditorFactoryListener(editorFactoryListener, disposable);
			editorListener.addIndentInlays();
		} else {
			Disposer.dispose(disposable);
			disposable = null;

			editorListener.removeIndentInlays();
		}
	}

	private static class IndentRenderer extends HintRenderer {
		public IndentRenderer(@Nullable String text) {
			super(text);
		}
	}

}
