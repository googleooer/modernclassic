package gooer.modernclassic.input;

import gooer.modernclassic.client.ModernclassicClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BetaRewindKeyBinds {

    private static KeyBinding repeatLastCommandKey;

    private String getLastSentCommand(){
        int messageHistorySize = -1;
        List<String> messageHistory = new ArrayList<String>();
        messageHistory = new ArrayList<String>(MinecraftClient.getInstance().inGameHud.getChatHud().getMessageHistory());
        Collections.reverse(messageHistory);

        for(String message : messageHistory){
            if(message.startsWith("/")) {
                return message;
            }
        }

        return null;
    }

    public void init(){
        repeatLastCommandKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.modernclassic.repeatlastcommand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KeyBinding.MISC_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (repeatLastCommandKey.wasPressed()){
                if(getLastSentCommand() == null) {
                    client.inGameHud.getChatHud().addMessage(Text.translatable("key.modernclassic.repeatlastcommand.nonefound"));
                    return;
                }

                client.player.sendCommand(getLastSentCommand().replaceFirst("/",""));

            }
        });




    }
}