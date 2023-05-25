package gooer.modernclassic.duck_accessors.entity.player;

public interface CameraPitchFixInterface
{
    //This restores camera pitch functionality from 1.12<=
    void setCameraPitch(float cameraPitch);
    void setPrevCameraPitch(float prevCameraPitch);
    float getCameraPitch();
    float getPrevCameraPitch();
}