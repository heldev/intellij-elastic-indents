package io.github.heldev;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;


public class ToggleElasticIndentsAction extends DumbAwareAction {

	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {

		requireNonNull(event.getProject())
				.getService(IndentService.class)
				.toggleElasticIndents();
	}

}
