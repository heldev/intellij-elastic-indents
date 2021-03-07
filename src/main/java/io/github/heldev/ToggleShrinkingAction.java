package io.github.heldev;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;


public class ToggleShrinkingAction extends ToggleAction {

	@Override
	public boolean isSelected(@NotNull AnActionEvent event) {
		return getFacade(event).isShrinkingEnabled();
	}

	@Override
	public void setSelected(@NotNull AnActionEvent event, boolean isEnabled) {
		if (isEnabled) {
			getFacade(event).enableShrinking();
		} else {
			getFacade(event).disableShrinking();
		}
	}

	public ElasticIndentationFacade getFacade(AnActionEvent event) {

		return requireNonNull(event.getProject())
				.getService(ElasticIndentationFacade.class);
	}
}
