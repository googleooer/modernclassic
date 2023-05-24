package gooer.modernclassic.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Items.class)
public class ItemsMixin {

    //FOOD


    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cooked_beef")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookedBeefRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_BEEF).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beef")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings BeefRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.BEEF).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chicken")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings ChickenRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.CHICKEN).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cooked_chicken")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookedChickenRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_CHICKEN).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=rabbit")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings RabbitRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.RABBIT).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cooked_rabbit")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookedRabbitRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_RABBIT).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=mutton")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings MuttonRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.MUTTON).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cooked_mutton")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookedMuttonRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_MUTTON).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=porkchop")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings PorkchopRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.PORKCHOP).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cooked_porkchop")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookedPorkchopRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_PORKCHOP).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cod")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CodRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COD).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cooked_cod")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookedCodRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_COD).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=salmon")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings SalmonRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.SALMON).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cooked_salmon")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookedSalmonRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKED_SALMON).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=apple")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings AppleRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.APPLE).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_apple")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings GoldenAppleRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.GOLDEN_APPLE).maxCount(1); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=enchanted_golden_apple")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/EnchantedGoldenAppleItem;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings EnchantedGoldenAppleRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.ENCHANTED_GOLDEN_APPLE).maxCount(1); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=golden_carrot")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings GoldenCarrotRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.GOLDEN_CARROT).maxCount(4); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=melon_slice")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings MelonSliceRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.MELON_SLICE).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=bread")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings BreadRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.BREAD).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=pumpkin_pie")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings PumpkinPieRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.PUMPKIN_PIE).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=tropical_fish")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings TropicalFishRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.TROPICAL_FISH).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=pufferfish")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings PufferfishRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.PUFFERFISH).maxCount(64); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=cookie")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CookieRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.COOKIE).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=carrot")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/AliasedBlockItem;<init>(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings CarrotRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.CARROT).maxCount(64); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potato")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/AliasedBlockItem;<init>(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings PotatoRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.POTATO).maxCount(64); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=baked_potato")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/AliasedBlockItem;<init>(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings BakedPotatoRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.BAKED_POTATO).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=chorus_fruit")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ChorusFruitItem;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings ChorusFruitRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.CHORUS_FRUIT).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=beetroot")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings BeetrootRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.BEETROOT).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=sweet_berries")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/AliasedBlockItem;<init>(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings SweetBerriesRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.SWEET_BERRIES).maxCount(8); }

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=glow_berries")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/AliasedBlockItem;<init>(Lnet/minecraft/block/Block;Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings GlowBerriesRegistry(Item.Settings settings){ return new Item.Settings().group(ItemGroup.FOOD).food(FoodComponents.GLOW_BERRIES).maxCount(8); }




    //TOOL ITEMS



    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=bundle")),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BundleItem;<init>(Lnet/minecraft/item/Item$Settings;)V", ordinal = 0))
    private static Item.Settings BundleRegistry(Item.Settings settings){ return new Item.Settings().maxCount(1).group(ItemGroup.TOOLS); }



}