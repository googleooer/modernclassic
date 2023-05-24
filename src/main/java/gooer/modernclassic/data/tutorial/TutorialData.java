package gooer.modernclassic.data.tutorial;

import gooer.modernclassic.data.tutorial.TutorialStep;

import java.util.List;

public class TutorialData {
    private final String tutorialId;
    private final List<TutorialStep> steps;
    private final TutorialStep currentStep;


    public TutorialData(List<TutorialStep> steps, String tutorialId) {
        this.steps = steps;
        this.currentStep = steps.get(0);
        this.tutorialId = tutorialId;
    }

    public List<TutorialStep> getSteps() {
        return steps;
    }

    public String getTutorialId() {
        return tutorialId;
    }



}