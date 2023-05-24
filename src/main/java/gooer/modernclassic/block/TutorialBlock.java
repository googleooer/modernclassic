package gooer.modernclassic.block;

import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.block.entity.TutorialBlockEntity;
import gooer.modernclassic.data.tutorial.TutorialData;
import gooer.modernclassic.data.tutorial.TutorialGroup;
import gooer.modernclassic.data.tutorial.TutorialPacket;
import gooer.modernclassic.data.tutorial.TutorialStep;
import gooer.modernclassic.entity.player.CustomPlayerEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


public class TutorialBlock extends BlockWithEntity {

    //Get rid of this until we figure out custom resources
    //private final Identifier tutorialId;


    public TutorialBlock(Settings settings) {
        super(settings);
    }



    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TutorialBlockEntity(pos, state);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        TutorialBlockEntity blockEntity = (TutorialBlockEntity) world.getBlockEntity(pos);
        if (blockEntity == null) {
            //Modernclassic.LOGGER.error("A TutorialBlockEntity was null!");
            return;
        }

        if (blockEntity.getConsumable() && blockEntity.getConsumed()) return;



        if (!world.isClient && entity instanceof PlayerEntity) {

            if (hasMatchingTutorial(((CustomPlayerEntityAccess) entity).getQueuedTutorials(),blockEntity.getTutorialId())) return;

            //Modernclassic.LOGGER.warn("Player touched Tutorial Block!");
            //List<TutorialData> tutorials = ((CustomPlayerEntityAccess)entity).getQueuedTutorials();
            //tutorials.add(new TutorialData(this.tutorialTitle, this.tutorialText, duration, GLFW.GLFW_KEY_SPACE));
            //((CustomPlayerEntityAccess)entity).setQueuedTutorials(tutorials);

            //List<TutorialData> tutorials = ((CustomPlayerEntityAccess) entity).getQueuedTutorials();
            //This is just for debug. Replace later with code directly in .add()
            Identifier tutorialIdentifier = Modernclassic.parseIdentifier(blockEntity.getTutorialId());
            //TutorialData tutorialDataToAdd = Modernclassic.getTutorialFromDatapack(tutorialIdentifier);

            //tutorials.add(tutorialDataToAdd);
            //((CustomPlayerEntityAccess) entity).setQueuedTutorials(tutorials);


            //Update tutorial on server-side
            List<TutorialGroup> tutorials = ((CustomPlayerEntityAccess) entity).getQueuedTutorials();
            //Modernclassic.LOGGER.info(String.format("TutorialBlock: Getting tutorial from ID \"%s\"", Modernclassic.parseIdentifier(blockEntity.getTutorialId())));
            TutorialGroup tutorialToAdd = Modernclassic.tutorialDataLoader.getTutorial(Modernclassic.parseIdentifier(blockEntity.getTutorialId()));
            if(tutorialToAdd == null) {
                Modernclassic.LOGGER.error("Tried to add null tutorial!");
                return;
            }
            //Modernclassic.LOGGER.info("TutorialBlock: Adding new tutorial with info: ");
            //Modernclassic.LOGGER.info(String.format("TutorialBlock: ID: \"%s\"...", tutorialToAdd.getTutorialId()));
            //Modernclassic.LOGGER.info(String.format("TutorialBlock: First step title: \"%s\"...", tutorialToAdd.getSteps().get(0).getTutorialTitle()));
            //Modernclassic.LOGGER.info(String.format("TutorialBlock: First step text: \"%s\"...", tutorialToAdd.getSteps().get(0).getTutorialText()));
            //Modernclassic.LOGGER.info(String.format("TutorialBlock: First step duration: %d...", tutorialToAdd.getSteps().get(0).getDuration()));
            tutorials.add(tutorialToAdd);
            //Modernclassic.LOGGER.info(String.format("TutorialBlock: ToServerside -> Calling setQueuedTutorials, target new size: %d...", ((CustomPlayerEntityAccess) entity).getQueuedTutorials().size()));
            ((CustomPlayerEntityAccess) entity).setQueuedTutorials(tutorials);


            //Send tutorial to client, for clientside display
            //Modernclassic.LOGGER.info(String.format("TutorialBlock: ToClient -> Sending tutorial packet with id \"%s\" to Player client...", tutorialIdentifier.toString()));
            TutorialPacket.sendTutorialToClient((PlayerEntity) entity, blockEntity.getTutorialId(), -1);




            if(blockEntity.getConsumable()) blockEntity.setConsumed(true);


        }


    }

    private boolean hasMatchingTutorial(List<TutorialGroup> tutorials, String id){
        //Modernclassic.LOGGER.info(String.format("TutorialBlock: Checking %d tutorials for match...", tutorials.size()));
        //Identifier adjustedId = new Identifier("modernclassic", ("tutorials/"+id+".json"));
        for (TutorialGroup tutorial : tutorials) {
            //Modernclassic.LOGGER.info(String.format("Tutorial's id: %s", tutorial.getTutorialId()));
            //Modernclassic.LOGGER.info(String.format("Target id: %s", id));
            if (tutorial.getTutorialId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}