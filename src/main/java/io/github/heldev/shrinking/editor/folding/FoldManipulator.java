package io.github.heldev.shrinking.editor.folding;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.FoldingModel;
import com.intellij.openapi.editor.ex.FoldingModelEx;
import com.intellij.openapi.project.Project;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.Collections.nCopies;

@Service
public final class FoldManipulator {

	public FoldManipulator(Project project) {
	}

	public void refreshFoldings(Editor editor) {
		removeFoldings(editor);
		addFoldings(editor);
	}

	public void removeFoldings(Editor editor) {
		updateFoldings(editor, this::removeFoldings);
	}

	private void removeFoldings(FoldingModelEx foldingModel) {
		for (FoldRegion region : foldingModel.getAllFoldRegions()) {
			if (isIndentFold(region)) {
				foldingModel.removeFoldRegion(region);
			}
		}
	}

	private boolean isIndentFold(FoldRegion region) {
		return region.shouldNeverExpand() && region.getPlaceholderText().matches(" +");
	}

	public void addFoldings(Editor editor) {
		updateFoldings(editor, foldingModel -> addFoldings(editor, foldingModel));
	}

	private void updateFoldings(Editor editor, Consumer<FoldingModelEx> action) {
		FoldingModel foldingModel = editor.getFoldingModel();
		if (foldingModel instanceof FoldingModelEx) {
			foldingModel.runBatchFoldingOperation(() -> action.accept((FoldingModelEx) foldingModel));
		}
	}

	private void addFoldings(Editor editor, FoldingModelEx foldingModel) {
		Document document = editor.getDocument();

		IntStream.range(0, document.getLineCount())
				.forEach(line -> addFoldings(foldingModel, document, line));
	}

	private void addFoldings(FoldingModelEx foldingModel, Document document, int line) {
		int lineStart = document.getLineStartOffset(line);
		int indentSpacePairCount = countIndentSpacePairs(document, lineStart);

		if (0 < indentSpacePairCount) {

			FoldRegion foldRegion = foldingModel.createFoldRegion(
					lineStart,
					lineStart + indentSpacePairCount * 2,
					String.join("", nCopies(indentSpacePairCount, " ")),
					null,
					true);

			if (foldRegion != null) {
				foldRegion.setExpanded(false);
			}
		}
	}

	private int countIndentSpacePairs(Document document, int lineStart) {

		return document
				.getText()
				.substring(lineStart)
				.split("[^ ]", 2)[0]
				.length() / 2;
	}
}
