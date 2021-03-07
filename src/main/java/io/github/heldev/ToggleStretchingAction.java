package io.github.heldev;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;


public class ToggleStretchingAction extends ToggleAction {

	@Override
	public boolean isSelected(@NotNull AnActionEvent event) {
		return getFacade(event).isStretchingEnabled();
	}

	@Override
	public void setSelected(@NotNull AnActionEvent event, boolean isEnabled) {
		if (isEnabled) {
			getFacade(event).enableStretching();
		} else {
			getFacade(event).disableStretching();
		}
	}

	public ElasticIndentationFacade getFacade(AnActionEvent event) {

		return requireNonNull(event.getProject())
				.getService(ElasticIndentationFacade.class);
	}
}
