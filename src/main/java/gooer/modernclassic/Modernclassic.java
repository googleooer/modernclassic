package gooer.modernclassic;

import gooer.modernclassic.screen.FletchingTableScreenHandler;
import gooer.modernclassic.screen.WaypointScreenHandler;
import gooer.modernclassic.world.level.block.WaypointBlock;
import gooer.modernclassic.world.level.block.entity.WaypointBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Modernclassic implements ModInitializer {

    public static final String MOD_ID = "modernclassic";
    public static final String MOD_NAME = "Modern Classic";


    //Declare dummy block
    public static final WaypointBlock WAYPOINT_BLOCK = new WaypointBlock(FabricBlockSettings.of(Material.METAL).strength(8.0f).requiresTool().luminance(state -> 7));
    public static final BlockEntityType<WaypointBlockEntity> WAYPOINT_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(WaypointBlockEntity::new, WAYPOINT_BLOCK).build(null);
    public static final ScreenHandlerType<WaypointScreenHandler> WAYPOINT_SCREEN_HANDLER;

    public static final Identifier WAYPOINT = new Identifier(MOD_ID, "waypoint");


    public static final ScreenHandlerType<FletchingTableScreenHandler> FLETCHING_SCREEN_HANDLER;
    public static final Identifier FLETCHING = new Identifier(MOD_ID, "fletching");

    static {
        WAYPOINT_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(WAYPOINT, WaypointScreenHandler::new);
        FLETCHING_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(FLETCHING, FletchingTableScreenHandler::new);


    }


    @Override
    public void onInitialize() {

        //Register block
        Registry.register(Registry.BLOCK, new Identifier("modernclassic", "waypoint"), WAYPOINT_BLOCK);
        //Register item
        Registry.register(Registry.ITEM, new Identifier("modernclassic", "waypoint"), new BlockItem(WAYPOINT_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS)));
        //Register entity
        Registry.register(Registry.BLOCK_ENTITY_TYPE, WAYPOINT, WAYPOINT_BLOCK_ENTITY);

    }
}
