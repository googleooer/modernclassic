package gooer.modernclassic.client;

public interface CameraPitchFixInterface
{
    //This restores camera pitch functionality from 1.12<=
    void setCameraPitch(float cameraPitch);
    void setPrevCameraPitch(float prevCameraPitch);
    float getCameraPitch();
    float getPrevCameraPitch();
}