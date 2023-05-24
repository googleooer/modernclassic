package gooer.modernclassic.data.tutorial;

import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.entity.player.CustomPlayerEntityAccess;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

public class TutorialPacket {
    public static void sendTutorialToClient(PlayerEntity player, String tutorialId, int code) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeUuid(player.getUuid());
        buf.writeString(tutorialId);
        buf.writeInt(code);
        ServerPlayNetworking.send((ServerPlayerEntity) player, new Identifier("modernclassic", "tutorial"), buf);
        //Modernclassic.LOGGER.info("TutorialPacket: Serverside -> Tutorial packet sent!");
    }

    public static void registerClientPacket() {
        ClientPlayNetworking.registerGlobalReceiver(new Identifier("modernclassic", "tutorial"), (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            String id = buf.readString();
            //-1 or anything else = Add tutorial.
            //-2 = Remove current tutorial.
            //-3 = Remove current tutorial step.
            int code = buf.readInt();
            client.execute(() -> {
                //Modernclassic.LOGGER.info(String.format("Received tutorial packet with code %d", code));
                PlayerEntity playerEntity = client.player;
                if (playerEntity != null && playerEntity.getUuid().equals(uuid)) {
                    List<TutorialGroup> tutorials = ((CustomPlayerEntityAccess) playerEntity).getQueuedTutorials();

                    switch (code) {
                        case -3 -> {
                           if(tutorials.get(0) != null) {
                                TutorialGroup currentTutorial = tutorials.get(0);
                                currentTutorial.discardCurrentStep();
                                //Modernclassic.LOGGER.info("TutorialPacket: Client-> Discarded current tutorial step...");
                            }
                        }
                        case -2 -> {
                            if(tutorials.isEmpty()) return;
                            tutorials.remove(0);
                            //Modernclassic.LOGGER.info("TutorialPacket: Client -> Removed current tutorial to local array...");
                        }
                        default -> {
                            TutorialGroup tutorialToAdd = Modernclassic.tutorialDataLoader.getTutorial(Modernclassic.parseIdentifier(id));
                            tutorials.add(tutorialToAdd);
                            //Modernclassic.LOGGER.info(String.format("TutorialPacket: Client -> Added new tutorial %s...", Modernclassic.parseIdentifier(id).toString()));
                        }
                    }
                    /*

                    if (code == -2) {
                        tutorials.remove(0);
                    } else {
                        TutorialData tutorialToAdd = Modernclassic.getTutorial(Modernclassic.parseIdentifier(id));
                        tutorials.add(tutorialToAdd);
                    }

                     */
                    //Modernclassic.LOGGER.info(String.format("TutorialPacket: Client -> Calling setQueuedTutorials. Target new size: %d...", tutorials.size()));
                    ((CustomPlayerEntityAccess) playerEntity).setQueuedTutorials(tutorials);

                }
            });
        });

        /*
        ClientPlayNetworking.registerGlobalReceiver(new Identifier("modernclassic", "end_tutorial"), (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            String title = buf.readString();
            String text = buf.readString();
            int duration = buf.readInt();
            int action = buf.readInt();
            client.execute(() -> {
                PlayerEntity playerEntity = client.player;
                if (playerEntity != null && playerEntity.getUuid().equals(uuid)) {
                    List<TutorialData> tutorials = ((CustomPlayerEntityAccess) playerEntity).getQueuedTutorials();
                    if (action == -2) {
                        tutorials.remove(0);
                    } else {
                        tutorials.add(new TutorialData(title, text, duration, action));
                    }
                    ((CustomPlayerEntityAccess) playerEntity).setQueuedTutorials(tutorials);
                }
            });
        });

        */

    }
}