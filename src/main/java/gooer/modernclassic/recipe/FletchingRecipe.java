package gooer.modernclassic.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FletchingRecipe
        implements Recipe<CraftingInventory> {

    private final Ingredient inputA;
    private final Ingredient inputB;
    private final ItemStack result;
    private final Identifier id;

    public FletchingRecipe(Identifier id, ItemStack result, Ingredient inputA, Ingredient inputB) {
        this.id = id;
        this.inputA = inputA;
        this.inputB = inputB;
        this.result = result;
    }

    public Ingredient getInputA() {
        return this.inputA;
    }

    public Ingredient getInputB() {
        return this.inputB;
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        if(inventory.size() < 2) return false;
        return inputA.test(inventory.getStack(0)) && inputB.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        return this.getOutput().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.result;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    public static class Type implements RecipeType<FletchingRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "fletching_recipe";
    }
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }


}