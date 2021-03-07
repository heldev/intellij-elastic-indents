package io.github.heldev;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import io.github.heldev.shrinking.ShrinkingFacade;
import io.github.heldev.stretching.StretchingFacade;

@Service
public final class ElasticIndentationFacade{

	private final Project project;
	private State state;

	public ElasticIndentationFacade(Project project) {
		this.project = project;
		state = State.OFF;
	}

	public void enableShrinking() {
		switch (state) {
			case OFF:
				break;

			case STRETCHING:
				getStretchingFacade().disableStretching();
				break;

			default:
				throw new IllegalStateException("Can't enable shrinking because it's already on");
		}

		getShrinkingFacade().enableShrinking();
		state = State.SHRINKING;
	}

	public void disableShrinking() {
		if (isShrinkingEnabled()) {
			getShrinkingFacade().disableShrinking();
			state = State.OFF;
		} else {
			throw new IllegalStateException("Can't disable shrinking because it's already off, state=" + state);
		}
	}

	public boolean isShrinkingEnabled() {
		return state.equals(State.SHRINKING);
	}

	public void enableStretching() {
		switch (state) {
			case OFF:
				break;

			case SHRINKING:
				getShrinkingFacade().disableShrinking();
				break;

			default:
				throw new IllegalStateException("Can't enable stretching because it's already on");
		}

		getStretchingFacade().enableStretching();
		state = State.STRETCHING;
	}

	public void disableStretching() {
		if (isStretchingEnabled()) {
			getStretchingFacade().disableStretching();
			state = State.OFF;
		} else {
			throw new IllegalStateException("Can't disable stretching because it's already off, state=" + state);
		}
	}

	public boolean isStretchingEnabled() {
		return state.equals(State.STRETCHING);
	}

	private ShrinkingFacade getShrinkingFacade() {
		return project.getService(ShrinkingFacade.class);
	}

	private StretchingFacade getStretchingFacade() {
		return project.getService(StretchingFacade.class);
	}
}

enum State {
	OFF, SHRINKING, STRETCHING
}
