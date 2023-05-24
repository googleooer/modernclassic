package gooer.modernclassic.data.tutorial;

import gooer.modernclassic.Modernclassic;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.List;

public class TutorialGroup {
    private final String tutorialId;
    private List<TutorialStep> steps;
    private TutorialStep currentStep;



    public TutorialGroup(List<TutorialStep> steps, String tutorialId) {
        this.steps = steps;
        this.currentStep = steps.get(0);
        this.tutorialId= tutorialId;
    }

    public List<TutorialStep> getSteps() {
        return steps;
    }

    public TutorialStep getCurrentStep() {
        return steps.get(0);
    }

    public void discardCurrentStep() {
        if(this.steps.isEmpty()) return;
        if(this.steps.get(0) != null) this.steps.remove(0);
    }

    public String getTutorialId() {
        return tutorialId;
    }

    public void setSteps(List<TutorialStep> steps) {
        this.steps = steps;
    }

    public boolean tick() {
        Modernclassic.LOGGER.info(String.format("Current step duration left: %d", getCurrentStep().getDuration()));
        getCurrentStep().decrementDuration();


        //if(getCurrentStep().getAction() != -1) return getCurrentStep().getDuration() <=0 && new KeyBinding("", InputUtil.Type.KEYSYM, getCurrentStep().getAction(), "").isPressed();

        return getCurrentStep().getDuration() <= 0;
    }
}

