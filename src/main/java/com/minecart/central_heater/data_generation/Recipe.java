package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.recipe.SeethingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Recipe extends RecipeProvider implements IConditionBuilder {
    public Recipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        //remove recipe from stonecutter
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_stairs_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:nether_brick_slab_from_nether_bricks_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_stairs_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_slab_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_slab_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_bricks_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_bricks_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_wall_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_slab_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_stairs_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_bricks_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_wall_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_walls_from_stone_stonecutting");

        //add recipe from stone cutter
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, AllRegistry.stone_brick_tile_stair, AllRegistry.stone_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, AllRegistry.stone_brick_tile_slab, AllRegistry.stone_brick_tile, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, AllRegistry.stone_brick_tile_wall, AllRegistry.stone_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS, AllRegistry.stone_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, AllRegistry.deepslate_brick_tile_stair, AllRegistry.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, AllRegistry.deepslate_brick_tile_slab, AllRegistry.deepslate_brick_tile, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, AllRegistry.deepslate_brick_tile_wall, AllRegistry.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, AllRegistry.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, AllRegistry.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, AllRegistry.deepslate_brick_tile, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_WALL, AllRegistry.deepslate_brick_tile);

        //deal with tile bricks recipes in crafting
        tileBrickRecipe(recipeOutput, AllRegistry.stone_brick_tile.asItem(), AllRegistry.stone_brick.asItem());
        tileBrickRecipe(recipeOutput, AllRegistry.deepslate_brick_tile.asItem(), AllRegistry.deepslate_brick.asItem());
        tileBrickRecipe(recipeOutput, Items.NETHER_BRICKS, Items.NETHER_BRICK);
        tileBrickRecipe(recipeOutput, Items.RED_NETHER_BRICKS, AllRegistry.red_nether_brick.asItem());
        tileBrickRecipe(recipeOutput, Items.BRICKS, Items.BRICK);

        //deal with bricks recipe in crafting
        brickRecipe(recipeOutput, Items.STONE_BRICKS, AllRegistry.stone_brick.asItem());
        brickRecipe(recipeOutput, Items.DEEPSLATE_BRICKS, AllRegistry.deepslate_brick.asItem());
        brickRecipe(recipeOutput, Items.MUD_BRICKS, AllRegistry.mud_brick.asItem());

        //other special blocks
        stairSlabWallCraftingRecipe(recipeOutput, AllRegistry.stone_brick_tile.asItem(), AllRegistry.stone_brick_tile_stair.asItem(),
                AllRegistry.stone_brick_tile_slab.asItem(), AllRegistry.stone_brick_tile_wall.asItem());
        stairSlabWallCraftingRecipe(recipeOutput, AllRegistry.deepslate_brick_tile.asItem(), AllRegistry.deepslate_brick_tile_stair.asItem(),
                AllRegistry.deepslate_brick_tile_slab.asItem(), AllRegistry.deepslate_brick_tile_wall.asItem());

        //other things
        oreBlasting(recipeOutput, List.of(AllRegistry.mud_brick), RecipeCategory.MISC, Items.PACKED_MUD, 0.1f, 200, "brick");

        oreSeething(recipeOutput, List.of(Items.COAL_BLOCK), RecipeCategory.MISC, AllRegistry.diamond_shard, 1f, 1000, "misc");
        oreSeething(recipeOutput, List.of(Items.SAND), RecipeCategory.MISC, Items.SOUL_SAND, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.DIRT), RecipeCategory.MISC, Items.SOUL_SOIL, 1f, 300, "misc");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllRegistry.diamond_shard, 9).requires(Items.DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND).requires(AllRegistry.diamond_shard, 9)
                .unlockedBy(getHasName(AllRegistry.diamond_shard), has(AllRegistry.diamond_shard)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllRegistry.stone_brick)
                .requires(AllRegistry.cobble).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.IRON_NUGGET)
                .unlockedBy(getHasName(AllRegistry.cobble), has(AllRegistry.cobble)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllRegistry.deepslate_brick)
                .requires(AllRegistry.deepslate_cobble).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.GOLD_NUGGET)
                .unlockedBy(getHasName(AllRegistry.deepslate_cobble), has(AllRegistry.deepslate_cobble)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllRegistry.red_nether_brick).requires(Items.NETHER_BRICK).requires(Ingredient.of(Items.NETHER_WART, Items.RED_DYE))
                .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK)).group("misc").save(recipeOutput);

        stoveCraftingRecipeBuilder(recipeOutput, AllRegistry.brick_stove, Items.BRICK, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllRegistry.mud_brick_stove, AllRegistry.mud_brick, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllRegistry.stone_stove, AllRegistry.stone_brick, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllRegistry.deepslate_stove, AllRegistry.deepslate_brick, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllRegistry.nether_brick_stove, Items.NETHER_BRICK, Items.GOLD_INGOT, AllRegistry.gold_bars);
        stoveCraftingRecipeBuilder(recipeOutput, AllRegistry.red_nether_brick_stove, AllRegistry.red_nether_brick, Items.GOLD_INGOT, AllRegistry.gold_bars);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllRegistry.gold_bars).pattern("###").pattern("###").define('#', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllRegistry.cobble, 4).requires(Items.COBBLESTONE)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLESTONE).requires(AllRegistry.cobble, 4)
                .unlockedBy(getHasName(AllRegistry.cobble), has(AllRegistry.cobble)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllRegistry.deepslate_cobble, 4).requires(Items.COBBLED_DEEPSLATE)
                .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(Items.COBBLED_DEEPSLATE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLED_DEEPSLATE).requires(AllRegistry.deepslate_cobble, 4)
                .unlockedBy(getHasName(AllRegistry.deepslate_cobble), has(AllRegistry.deepslate_cobble)).group("misc").save(recipeOutput);
    }

    protected static void emptyRecipe(RecipeOutput output, String id){
        emptyRecipe(output, ResourceLocation.parse(id));
    }

    protected static void emptyRecipe(RecipeOutput output, ResourceLocation id){
//        dummy recipe
        SimpleCookingRecipeBuilder.generic(Ingredient.of(Items.BARRIER), RecipeCategory.MISC, Items.BARRIER,
                0f, 0, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new).group("ungroupable").unlockedBy(getHasName(Items.BARRIER), has(Items.BARRIER)).save(output, id);
    }

    protected static void oreSeething(
            RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group){
        oreCooking(
                recipeOutput,
                AllRegistry.Seething_ser.get(),
                SeethingRecipe::new,
                ingredients,
                category,
                result,
                experience,
                cookingTime,
                group,
                "_from_seething"
        );
    }

    protected static void tileBrickRecipe(RecipeOutput output, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2).pattern("###").pattern("###").pattern("###")
                .define('#', Ingredient.of(ingredient)).group("building").unlockedBy(getHasName(ingredient), has(ingredient))
                .showNotification(true).save(output);
    }

    protected static void brickRecipe(RecipeOutput output, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result).pattern("##").pattern("##")
                .define('#', Ingredient.of(ingredient)).group("building").unlockedBy(getHasName(ingredient), has(ingredient))
                .showNotification(true).save(output);
    }

    protected static void stairSlabWallCraftingRecipe(RecipeOutput output, Item ingredient, Item stair, Item slab, Item wall){
        stairBuilder(stair, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
        wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void stoveCraftingRecipeBuilder(RecipeOutput output, ItemLike stove, ItemLike baseBrick, ItemLike ingot, ItemLike bars){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stove).pattern("*&*").pattern("# #").pattern("###")
                .define('#', baseBrick).define('*', ingot).define('&', bars).unlockedBy(getHasName(stove), has(stove)).group("misc").save(output);
    }
}
