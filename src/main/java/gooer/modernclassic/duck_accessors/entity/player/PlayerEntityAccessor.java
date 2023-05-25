package gooer.modernclassic.duck_accessors.entity.player;

import gooer.modernclassic.data.tutorial.TutorialGroup;

import java.util.List;

public interface PlayerEntityAccessor {

    void setQueuedTutorials(List<TutorialGroup> queuedTutorials);
    List<TutorialGroup> getQueuedTutorials();

}