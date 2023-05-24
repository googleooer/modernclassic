package gooer.modernclassic.data.tutorial;

import gooer.modernclassic.Modernclassic;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.lang.reflect.Array;

public class TutorialStep {


    private final String tutorialTitle;
    private final String tutorialText;
    private int duration;

    //Action is an int, but use GLFW codes.
    //-1 for no action
    //TODO: Properly implement actions.
    //private final int action;

    public TutorialStep(String tutorialTitle, String tutorialText, int duration/*, int action*/){
        this.tutorialTitle = tutorialTitle;
        this.tutorialText = tutorialText;
        this.duration = duration;
        //this.action = action;
    }

    public boolean tick() {
        this.decrementDuration();
        //Only print multiples of 10
        //if(this.duration % 10 == 0) Modernclassic.LOGGER.info(String.format("TutorialStep: Ticked step: %d", getDuration()));

        //TODO: Properly implement actions.
        //if(action != -1) return duration <=0 && new KeyBinding("", InputUtil.Type.KEYSYM, action, "").isPressed();

        return duration <= 0;
    }


    public String getTutorialTitle() {
        return tutorialTitle;
    }

    public String getTutorialText() {
        return tutorialText;
    }

    public int getDuration() {
        return duration;
    }

    public void decrementDuration(){
        this.duration = duration - 1;
    }

    /*

    public int getAction() {
        return action;
    }

     */

}
