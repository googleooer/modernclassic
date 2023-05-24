package gooer.modernclassic;

import gooer.modernclassic.block.FatigueBlock;
import gooer.modernclassic.block.PoweredCopperRailBlock;
import gooer.modernclassic.block.TutorialBlock;
import gooer.modernclassic.block.entity.FatigueBlockEntity;
import gooer.modernclassic.block.entity.TutorialBlockEntity;
import gooer.modernclassic.data.tutorial.TutorialDataLoader;
import gooer.modernclassic.data.tutorial.TutorialGroup;
import gooer.modernclassic.entity.player.CustomPlayerEntityAccess;
import gooer.modernclassic.entity.vehicle.CustomMinecartTypeEnum;
import gooer.modernclassic.entity.vehicle.dispenser.DispenserMinecartBehavior;
import gooer.modernclassic.item.CustomMinecartItem;
import gooer.modernclassic.networking.NetworkingMessages;
import gooer.modernclassic.screen.FletchingTableScreenHandler;
import gooer.modernclassic.screen.WaypointScreenHandler;
import gooer.modernclassic.entity.vehicle.DispenserMinecartEntity;
import gooer.modernclassic.block.WaypointBlock;
import gooer.modernclassic.block.entity.WaypointBlockEntity;
import gooer.modernclassic.data.tutorial.TutorialPacket;
import gooer.modernclassic.data.tutorial.TutorialStep;
import gooer.modernclassic.world.BetaRewindServerState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.ToIntFunction;

public class Modernclassic implements ModInitializer {




    public static final String MOD_ID = "modernclassic";
    public static final String MOD_NAME = "Beta Rewind";
    public static final String CURR_VERSION = "Alpha 20230227";

    public static final Logger LOGGER = LoggerFactory.getLogger("Beta Rewind");


    //Declare dummy block
    public static final WaypointBlock WAYPOINT_BLOCK = new WaypointBlock(FabricBlockSettings.of(Material.METAL).strength(8.0f).requiresTool().luminance(state -> 7));
    public static final BlockEntityType<WaypointBlockEntity> WAYPOINT_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(WaypointBlockEntity::new, WAYPOINT_BLOCK).build(null);
    public static final ScreenHandlerType<WaypointScreenHandler> WAYPOINT_SCREEN_HANDLER;

    public static final FatigueBlock FATIGUE_BLOCK = new FatigueBlock(FabricBlockSettings.of(Material.METAL).strength(0.1f).luminance(7));
    public static final BlockEntityType<FatigueBlockEntity> FATIGUE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(FatigueBlockEntity::new, FATIGUE_BLOCK).build(null);



    public static final Identifier WAYPOINT = new Identifier(MOD_ID, "waypoint");

    public static final Item SPAWNER_MINECART_ITEM = Registry.register(Registry.ITEM, new Identifier("modernclassic", "spawner_minecart"),
            new CustomMinecartItem(CustomMinecartTypeEnum.Type.SPAWNER, new Item.Settings().maxCount(1).group(ItemGroup.TRANSPORTATION).rarity(Rarity.EPIC)));

    public static final Item DISPENSER_MINECART_ITEM = Registry.register(Registry.ITEM, new Identifier("modernclassic", "dispenser_minecart"),
            new CustomMinecartItem(CustomMinecartTypeEnum.Type.DISPENSER, new Item.Settings().maxCount(1).group(ItemGroup.TRANSPORTATION)));

    public static final Item DROPPER_MINECART_ITEM = Registry.register(Registry.ITEM, new Identifier("modernclassic", "dropper_minecart"),
            new CustomMinecartItem(CustomMinecartTypeEnum.Type.DROPPER, new Item.Settings().maxCount(1).group(ItemGroup.TRANSPORTATION)));


    public static final Block FRAMED_GLASS_BLOCK = Registry.register(Registry.BLOCK, new Identifier("modernclassic", "framed_glass"),
            new GlassBlock(AbstractBlock.Settings.of(Material.GLASS).strength(0.6f).sounds(BlockSoundGroup.GLASS).nonOpaque().allowsSpawning(Modernclassic::never)
                    .solidBlock(Modernclassic::never).suffocates(Modernclassic::never).blockVision(Modernclassic::never)));

    public static final Item FRAMED_GLASS_ITEM = Registry.register(Registry.ITEM, new Identifier("modernclassic", "framed_glass"),
            new BlockItem(FRAMED_GLASS_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

    public static final Block POWERED_COPPER_RAIL = Registry.register(Registry.BLOCK, new Identifier("modernclassic", "powered_copper_rail"),
            new PoweredCopperRailBlock(AbstractBlock.Settings.of(Material.DECORATION).noCollision().strength(0.7f).sounds(BlockSoundGroup.METAL)));

    public static final Item POWERED_COPPER_RAIL_ITEM = Registry.register(Registry.ITEM, new Identifier("modernclassic", "powered_copper_rail"),
            new BlockItem(POWERED_COPPER_RAIL, new Item.Settings().group(ItemGroup.TRANSPORTATION)));


    public static final TutorialBlock TUTORIAL_BLOCK = Registry.register(Registry.BLOCK, new Identifier("modernclassic", "tutorial_block"),
            new TutorialBlock(AbstractBlock.Settings.of(Material.STONE).strength(-1.0f, 3600000.8f).nonOpaque().allowsSpawning(Modernclassic::never)
                    .solidBlock(Modernclassic::never).suffocates(Modernclassic::never).blockVision(Modernclassic::never).noCollision()));

    public static final BlockEntityType<TutorialBlockEntity> TUTORIAL_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(TutorialBlockEntity::new, TUTORIAL_BLOCK).build(null);



    public static final EntityType<DispenserMinecartEntity> DISPENSER_MINECART = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("modernclassic", "dispenser_minecart"),
            FabricEntityTypeBuilder.<DispenserMinecartEntity>create(SpawnGroup.MISC, DispenserMinecartEntity::new)
                    .dimensions(EntityDimensions.fixed(0.98f, 0.7f)).trackRangeChunks(8).build());


    public static final GameRules.Key<GameRules.BooleanRule> ALLOW_SPRINTING =
            GameRuleRegistry.register("allowSprinting", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));








    //public static final ReworkedCampfireBlock REWORKED_CAMPFIRE_BLOCK = new ReworkedCampfireBlock(true, 1, FabricBlockSettings.of(Material.WOOD, MapColor.SPRUCE_BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD).luminance(state->15).nonOpaque());
    //public static final BlockEntityType<ReworkedCampfireBlockEntity> REWORKED_CAMPFIRE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(ReworkedCampfireBlockEntity::new, REWORKED_CAMPFIRE_BLOCK).build(null);

    //public static final Identifier CAMPFIRE = new Identifier("minecraft", "campfire");





    public static final ScreenHandlerType<FletchingTableScreenHandler> FLETCHING_SCREEN_HANDLER;
    public static final Identifier FLETCHING = new Identifier(MOD_ID, "fletching");



    static {
        WAYPOINT_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(WAYPOINT, WaypointScreenHandler::new);
        FLETCHING_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(FLETCHING, FletchingTableScreenHandler::new);


    }


    private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) != false ? litLevel : 0;
    }

    private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        return false;
    }

    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    public static List<Block> shiftUsedBlocks = Arrays.asList(Blocks.NOTE_BLOCK);




    public static Identifier parseIdentifier(String identifierString) {
        String[] split = identifierString.split(":");
        if (split.length != 2) {
            LOGGER.error(String.format("Error parsing identifier \"%s\". Split length != 2.", identifierString));
            throw new IllegalArgumentException("Invalid identifier string: " + identifierString);
        }
        return new Identifier(split[0], split[1]);
    }

    public static final TutorialDataLoader tutorialDataLoader = new TutorialDataLoader();

    public static BetaRewindServerState serverState;






    @Override
    public void onInitialize() {

        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            serverState = BetaRewindServerState.getServerState(handler.player.world.getServer());

            PacketByteBuf data = PacketByteBufs.create();
            data.writeBoolean(serverState.ignoreExperimentalWarning);
            ServerPlayNetworking.send(handler.player, NetworkingMessages.IGNORE_EXPERIMENTAL_WARNING, data);
        }));





        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(tutorialDataLoader);

        //Register tutorial tick
        ServerTickEvents.END_SERVER_TICK.register(server -> {

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                List<TutorialGroup> tutorials = ((CustomPlayerEntityAccess) player).getQueuedTutorials();

                if (!tutorials.isEmpty() && tutorials.get(0) != null) {
                    TutorialGroup currentTutorial = tutorials.get(0);
                    if (currentTutorial.getSteps().isEmpty()) continue;
                    TutorialStep currentTutorialStep = currentTutorial.getCurrentStep();

                    if (currentTutorialStep.tick() /*Returns true if finished ticking*/) {
                        //LOGGER.info("ServerTick: Serverside -> Tutorial step has finished ticking.");


                        if(currentTutorial.getSteps().size() > 1) {

                            //LOGGER.info(String.format("ServerTick: Serverside -> Tutorial steps left: %d", currentTutorial.getSteps().size()-1));

                            //Remove tutorial step from server
                            tutorials.get(0).discardCurrentStep();
                            //LOGGER.info("ServerTick: Serverside -> Discarded current step from serverside player entity...");

                            //Remove tutorial step from client
                            TutorialPacket.sendTutorialToClient(player, "", -3);
                            //LOGGER.info(String.format("ServerTick: Serverside -> Sent tutorial packet to player %s with code -3 (discard tutorial step)...", player.getName()));

                        } else {

                            //LOGGER.info("ServerTick: Serverside -> No more tutorial steps left. Removing tutorial from server and client...");

                            //Remove tutorial from server
                            List<TutorialGroup> adjustedTutorials = ((CustomPlayerEntityAccess) player).getQueuedTutorials();
                            adjustedTutorials.remove(0);
                            ((CustomPlayerEntityAccess) player).setQueuedTutorials(adjustedTutorials);
                            //LOGGER.info("ServerTick: Serverside -> Discarded tutorial from serverside player entity...");

                            //Remove tutorial from client
                            TutorialPacket.sendTutorialToClient(player, "", -2);
                            //LOGGER.info(String.format("ServerTick: Serverside -> Sent tutorial packet to player %s with code -2 (discard tutorial)...", player.getName()));


                        }
                    }
                }
            }
        });

        TutorialPacket.registerClientPacket();



        /*
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("tutorial", "my_resources");
            }

            @Override
            public void reload(ResourceManager manager) {
                // Clear Caches Here

                for(Identifier id : manager.findResources("my_resource_folder", path -> path.endsWith(".json"))) {
                    try(InputStream stream = manager.getResource(id).getInputStream()) {
                        // Consume the stream however you want, medium, rare, or well done.
                    } catch(Exception e) {
                        TUTORIAL_LOG.error("Error occurred while loading resource json " + id.toString(), e);
                    }
                }
            }
        });


         */

        System.out.println("Initializing modernclassic a1.0.0...");

        //Register block
        Registry.register(Registry.BLOCK, new Identifier("modernclassic", "waypoint"), WAYPOINT_BLOCK);
        //Register item
        Registry.register(Registry.ITEM, new Identifier("modernclassic", "waypoint"), new BlockItem(WAYPOINT_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS)));


        //Register entity
        Registry.register(Registry.BLOCK_ENTITY_TYPE, WAYPOINT, WAYPOINT_BLOCK_ENTITY);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, "tutorial_block", TUTORIAL_BLOCK_ENTITY);

        Registry.register(Registry.BLOCK, new Identifier("modernclassic","fatigue_block"), FATIGUE_BLOCK);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("modernclassic","fatigue_block"), FATIGUE_BLOCK_ENTITY);
        Registry.register(Registry.ITEM, new Identifier("modernclassic", "fatigue_block"), new BlockItem(FATIGUE_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS)));

        DispenserMinecartBehavior.registerDefaults();





/*
        int campfireRawId = Registry.BLOCK.getRawId(Blocks.CAMPFIRE);
        Optional<RegistryKey<Block>> campfireRegKey = Registry.BLOCK.getKey(Blocks.CAMPFIRE);
        if(campfireRegKey.isPresent()) {
            Registry.BLOCK.set(campfireRawId, campfireRegKey.get(), REWORKED_CAMPFIRE_BLOCK, Lifecycle.stable());
        }
*/





    }
}
