package com.minecart.central_heater;

import com.minecart.central_heater.block.BlastOverheaterBlock;
import com.minecart.central_heater.block.GoldenStoveBlock;
import com.minecart.central_heater.block.StoneStoveBlock;
import com.minecart.central_heater.block_entity.BlastOverheaterBlockEntity;
import com.minecart.central_heater.block_entity.GoldenStoveBlockEntity;
import com.minecart.central_heater.block_entity.StoneStoveBlockEntity;
import com.minecart.central_heater.recipe.SeethingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

public class AllRegistry {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Central_heater.MODID);
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Central_heater.MODID);
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Central_heater.MODID);
    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Central_heater.MODID);
    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Central_heater.MODID);

    public static DeferredBlock<Block> Blast_overheater = BLOCKS.register("blast_overheater", ()->new BlastOverheaterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLAST_FURNACE)));
    public static DeferredItem<BlockItem> Blast_overheater_item = ITEMS.registerSimpleBlockItem(Blast_overheater);
    public static Supplier<BlockEntityType<BlastOverheaterBlockEntity>> Blast_overheater_be = BLOCK_ENTITIES.register("blast_overheater",
            ()->BlockEntityType.Builder.of(BlastOverheaterBlockEntity::new, Blast_overheater.get()).build(null));

    public static DeferredBlock<Block> Stone_stove = BLOCKS.register("stone_stove", ()->new StoneStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static DeferredItem<BlockItem> Stone_stove_item = ITEMS.registerSimpleBlockItem(Stone_stove);
    public static Supplier<BlockEntityType<StoneStoveBlockEntity>> Stone_stove_be = BLOCK_ENTITIES.register("stone_stove",
            ()->BlockEntityType.Builder.of(StoneStoveBlockEntity::new, Stone_stove.get()).build(null));

    public static DeferredBlock<Block> Red_nether_brick_stove = BLOCKS.register("red_nether_brick_stove", ()->new GoldenStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_NETHER_BRICKS)));
    public static DeferredItem<BlockItem> Red_nether_brick_stove_item = ITEMS.registerSimpleBlockItem(Red_nether_brick_stove);
    public static Supplier<BlockEntityType<GoldenStoveBlockEntity>> Red_nether_brick_stove_be = BLOCK_ENTITIES.register("red_nether_brick_stove",
            ()->BlockEntityType.Builder.of(GoldenStoveBlockEntity::new, Red_nether_brick_stove.get()).build(null));

    public static DeferredBlock<Block> Stone_tile = BLOCKS.register("stone_tile", ()->new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static DeferredItem<BlockItem> Stone_tile_item = ITEMS.registerSimpleBlockItem(Stone_tile);

    public static DeferredItem<Item> Cobble = ITEMS.registerSimpleItem("cobble", new Item.Properties());
    public static DeferredItem<Item> Stone_brick = ITEMS.registerSimpleItem("stone_brick", new Item.Properties());

    public static DeferredHolder<RecipeType<?>, RecipeType<SeethingRecipe>> Seething = RECIPE_TYPES.register("seething", ()->new RecipeType<>(){
        public String toString(){ return "seething"; }});

    public static void register(IEventBus modEventbus){
        ITEMS.register(modEventbus);
        BLOCKS.register(modEventbus);
        BLOCK_ENTITIES.register(modEventbus);
        RECIPE_TYPES.register(modEventbus);
        RECIPE_SERIALIZERS.register(modEventbus);
    }
}
