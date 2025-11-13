package com.minecart.central_heater;

import com.minecart.central_heater.block.BrickStoveBlock;
import com.minecart.central_heater.block.GoldenStoveBlock;
import com.minecart.central_heater.block.StoneStoveBlock;
import com.minecart.central_heater.block_entity.*;
import com.minecart.central_heater.item.BrickItem;
import com.minecart.central_heater.recipe.SeethingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

public class AllRegistry {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Central_heater.MODID);
    public static DeferredRegister.Items VITEMS = DeferredRegister.createItems("minecraft");
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Central_heater.MODID);
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Central_heater.MODID);
    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Central_heater.MODID);
    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Central_heater.MODID);


    public static DeferredBlock<Block> stone_stove = registerBlockWithItem("stone_stove", ()->new StoneStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

    public static DeferredBlock<Block> deepslate_stove = registerBlockWithItem("deepslate_stove", ()->new StoneStoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(8.0F, 8.0F).sound(SoundType.DEEPSLATE_BRICKS)));

    public static DeferredBlock<Block> red_nether_brick_stove = registerBlockWithItem("red_nether_brick_stove", ()->new GoldenStoveBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static DeferredBlock<Block> nether_brick_stove = registerBlockWithItem("nether_brick_stove", ()->new GoldenStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(6.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    public static DeferredBlock<Block> brick_stove = registerBlockWithItem("brick_stove", ()->new BrickStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F)));

    public static DeferredBlock<Block> mud_brick_stove = registerBlockWithItem("mud_brick_stove", ()->new BrickStoveBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 3.0F).sound(SoundType.MUD_BRICKS)));


    public static DeferredBlock<Block> stone_brick_tile = registerBlockWithItem("stone_brick_tile", ()->new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2F, 8F)));

    public static DeferredBlock<StairBlock> stone_brick_tile_stair = registerBlockWithItem("stone_brick_tile_stair", ()->new StairBlock(stone_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));
    public static DeferredBlock<SlabBlock> stone_brick_tile_slab = registerBlockWithItem("stone_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));
    public static DeferredBlock<WallBlock> stone_brick_tile_wall = registerBlockWithItem("stone_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(stone_brick_tile.get())));

    public static DeferredBlock<Block> deepslate_brick_tile = registerBlockWithItem("deepslate_brick_tile", ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4F, 8F).sound(SoundType.DEEPSLATE_BRICKS)));

    public static DeferredBlock<StairBlock> deepslate_brick_tile_stair = registerBlockWithItem("deepslate_brick_tile_stair", ()->new StairBlock(deepslate_brick_tile.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));
    public static DeferredBlock<SlabBlock> deepslate_brick_tile_slab = registerBlockWithItem("deepslate_brick_tile_slab", ()->new SlabBlock(BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));
    public static DeferredBlock<WallBlock> deepslate_brick_tile_wall = registerBlockWithItem("deepslate_brick_tile_wall", ()->new WallBlock(BlockBehaviour.Properties.ofFullCopy(deepslate_brick_tile.get())));

    public static DeferredBlock<Block> gold_bars = registerBlockWithItem("gold_bars", ()->new IronBarsBlock(
            BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(10.0F, 5.0F).sound(SoundType.METAL).noOcclusion()));


    public static DeferredItem<Item> stone_brick = ITEMS.register("stone_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> deepslate_brick = ITEMS.register("deepslate_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> mud_brick = ITEMS.register("mud_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> red_nether_brick = ITEMS.register("red_nether_brick", ()->new BrickItem(new Item.Properties()));

    public static DeferredItem<Item> cobble = ITEMS.registerSimpleItem("cobble");

    public static DeferredItem<Item> deepslate_cobble = ITEMS.registerSimpleItem("deepslate_cobble");

    public static DeferredItem<Item> diamond_shard = ITEMS.registerSimpleItem("diamond_shard");


    public static Supplier<BlockEntityType<StoneStoveBlockEntity>> stone_stove_be = BLOCK_ENTITIES.register("stone_stove",
            ()->BlockEntityType.Builder.of(StoneStoveBlockEntity::new, stone_stove.get(), deepslate_stove.get()).build(null));

    public static Supplier<BlockEntityType<GoldenStoveBlockEntity>> red_nether_brick_stove_be = BLOCK_ENTITIES.register("red_nether_brick_stove",
            ()->BlockEntityType.Builder.of(GoldenStoveBlockEntity::new, red_nether_brick_stove.get(), nether_brick_stove.get()).build(null));

    public static Supplier<BlockEntityType<BrickStoveBlockEntity>> brick_stove_be = BLOCK_ENTITIES.register("brick_stove",
            ()->BlockEntityType.Builder.of(BrickStoveBlockEntity::new, brick_stove.get(), mud_brick_stove.get()).build(null));

    public static Supplier<BlockEntityType<BurnableCampfireBlockEntity>> burnable_campfire = BLOCK_ENTITIES.register("burnable_campfire_be",
            ()->BlockEntityType.Builder.of(BurnableCampfireBlockEntity::new, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE).build(null));


    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SeethingRecipe>> Seething_ser = RECIPE_SERIALIZERS.register("seething", ()->new SimpleCookingSerializer<>(SeethingRecipe::new, 200));
    public static DeferredHolder<RecipeType<?>, RecipeType<SeethingRecipe>> Seething = RECIPE_TYPES.register("seething", ()->new RecipeType<SeethingRecipe>(){
        public String toString(){ return "seething"; }});

    public static void register(IEventBus modEventbus){
        ITEMS.register(modEventbus);
        BLOCKS.register(modEventbus);
        BLOCK_ENTITIES.register(modEventbus);
        RECIPE_TYPES.register(modEventbus);
        RECIPE_SERIALIZERS.register(modEventbus);
    }

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> supplier){
        return BLOCKS.register(name, supplier);
    }

    public static DeferredItem<Item> registerSimpleItem(String key){
        return ITEMS.registerSimpleItem(key);
    }

    public static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> supplier){
        DeferredBlock<T> ret = BLOCKS.register(name, supplier);
        ITEMS.registerSimpleBlockItem(ret);
        return ret;
    }
}
