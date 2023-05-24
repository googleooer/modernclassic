package gooer.modernclassic.entity.player;

import gooer.modernclassic.data.tutorial.TutorialGroup;

import java.util.List;

public interface CustomPlayerEntityAccess {

    void setQueuedTutorials(List<TutorialGroup> queuedTutorials);
    List<TutorialGroup> getQueuedTutorials();

}