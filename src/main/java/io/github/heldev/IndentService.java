package io.github.heldev;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
final public class IndentService implements Disposable {

    private static final EditorFactory editorFactory = EditorFactory.getInstance();

    private final Project project;
    private final InlayManipulator inlayManipulator;
    private final IndentChangeDocumentListener indentChangeListener;
    private final NewEditorListener newEditorListener;
    private Optional<Disposable> disposableHolder;

    public IndentService(Project project) {
        this.project = project;
        inlayManipulator = new InlayManipulator(editorFactory, project);
        indentChangeListener = new IndentChangeDocumentListener(inlayManipulator);
        newEditorListener = new NewEditorListener(inlayManipulator, indentChangeListener, project, () -> disposableHolder.get());
        disposableHolder = Optional.empty();
    }

    public void toggleElasticIndents() {
        boolean isElasticityEnabled = disposableHolder.isPresent();

        if (! isElasticityEnabled) {
            enableIndents();
        } else {
            disableIndents();
        }
    }

    private void enableIndents() {
        Disposable disposable = Disposer.newDisposable("elasticIndentEditorListenerDisposable");
        disposableHolder = Optional.of(disposable);

        Set<Editor> projectEditors = getProjectEditors();
        projectEditors.forEach(inlayManipulator::addIndentInlays);
        addDocumentListeners(disposable, projectEditors);

        editorFactory.addEditorFactoryListener(newEditorListener, disposable);
    }

    private void addDocumentListeners(Disposable disposable, Set<Editor> projectEditors) {
        projectEditors
                .stream()
                .map(Editor::getDocument)
                .distinct().forEach(document -> document.addDocumentListener(indentChangeListener, disposable));
    }

    @Override
    public void dispose() {
        disableIndents();
    }

    private void disableIndents() {
        disposableHolder.ifPresent(disposable -> {
            Disposer.dispose(disposable);
            getProjectEditors().forEach(inlayManipulator::removeIndentInlays);
        });

        disposableHolder = Optional.empty();
    }

    private Set<Editor> getProjectEditors() {

        return Arrays.stream(editorFactory.getAllEditors())
                .filter(editor -> project.equals(editor.getProject()))
                .collect(toSet());
    }

}
