package io.github.heldev;

import com.intellij.codeInsight.daemon.impl.HintRenderer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.util.DocumentUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

import static com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR;
import static com.intellij.util.DocumentUtil.getFirstNonSpaceCharOffset;
import static com.intellij.util.DocumentUtil.getLineTextRange;
import static java.lang.Integer.MAX_VALUE;

public class AddInlayIndentsAction extends AnAction {

	private static class InlayIndentDocumentListener implements DocumentListener {

		public static final EditorFactory editorFactory = EditorFactory.getInstance();

		public void addIndentInlays() {
			for(var editor: editorFactory.getAllEditors()) {
				addIndentInlays(editor.getDocument(), editor.getInlayModel());
			}
		}

		@Override
		public void documentChanged(@NotNull DocumentEvent event) {
			Document document = event.getDocument();

			for(var editor: editorFactory.getEditors(document)) {
				InlayModel inlayModel = editor.getInlayModel();

				var isNewLineChange = (event.getNewFragment() + event.getOldFragment().toString()).contains("\n");
				var isIndentChange = getFirstNonSpaceCharOffset(document, document.getLineNumber(event.getOffset())) <= event.getOffset();

				if (isNewLineChange || isIndentChange) {
					removeIndentInlays(inlayModel);
					addIndentInlays(document, inlayModel);
				}
			}
		}


		private void addIndentInlays(Document document, InlayModel inlayModel) {
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
			var lineText = document.getText(getLineTextRange(document, line)).replace("\n", "");

			if (! lineText.isEmpty()) {
				var indent = DocumentUtil.getIndent(document, lineStart).toString();
				inlayModel.addInlineElement(lineStart, new IndentRenderer(indent + indent.length()));
			}
		}
	}
	private final InlayIndentDocumentListener listener = new InlayIndentDocumentListener();

	private boolean isActivated = false;

	@Override
	public void update(@NotNull AnActionEvent e) {
		setEnabledInModalContext(true);
		// Using the event, evaluate the context, and enable or disable the action.
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		var editor = event.getData(EDITOR);

		if (editor != null) {
			var document = editor.getDocument();

			if (! isActivated) {
				listener.addIndentInlays();
				document.addDocumentListener(listener);
			} else {
				document.removeDocumentListener(listener);
				listener.removeIndentInlays();
			}

			isActivated = ! isActivated;
		}
	}

	private static class IndentRenderer extends HintRenderer {
		public IndentRenderer(@Nullable String text) {
			super(text);
		}
	}

}
