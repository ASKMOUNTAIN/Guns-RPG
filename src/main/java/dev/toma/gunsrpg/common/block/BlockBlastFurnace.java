package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.BlastFurnaceContainer;
import dev.toma.gunsrpg.common.init.GRPGBlocks;
import dev.toma.gunsrpg.common.tileentity.BlastFurnaceTileEntity;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBlastFurnace extends GRPGBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.blast_furnace");

    public BlockBlastFurnace(String name) {
        super(name, Properties.of(Material.STONE).strength(2.2F, 16.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.LIT, false));
    }

    public static void updateBurnState(BlockPos pos, World world, boolean lit) {
        BlockState currentState = world.getBlockState(pos);
        if (currentState.getBlock() != GRPGBlocks.BLAST_FURNACE) return;
        world.setBlock(pos, currentState.setValue(BlockStateProperties.LIT, lit), 3);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(BlockStateProperties.LIT)) {
            Direction direction = stateIn.getValue(BlockStateProperties.HORIZONTAL_FACING);
            worldIn.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
            double rng = (rand.nextDouble() - rand.nextDouble()) / 3;
            if (rand.nextDouble() <= 0.2) {
                worldIn.playSound(null, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            switch (direction) {
                case NORTH: {
                    worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5 + rng, pos.getY() + 0.25, pos.getZ() - 0.05, 0, 0, 0);
                    break;
                }
                case EAST: {
                    worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 1.05, pos.getY() + 0.25, pos.getZ() + 0.5 + rng, 0, 0, 0);
                    break;
                }
                case SOUTH: {
                    worldIn.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5 + rng, pos.getY() + 0.25, pos.getZ() + 1.05, 0, 0, 0);
                    break;
                }
                case WEST: {
                    worldIn.addParticle(ParticleTypes.FLAME, pos.getX() - 0.05, pos.getY() + 0.25, pos.getZ() + 0.5 + rng, 0, 0, 0);
                    break;
                }
            }
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(BlockStateProperties.LIT) ? 12 : 0;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.is(oldState.getBlock())) {
            ModUtils.dropInventoryItems(world, pos);
        }
        super.onRemove(state, world, pos, state, p_196243_5_);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
        return new BlastFurnaceTileEntity();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isClientSide)
            return ActionResultType.SUCCESS;
        else {
            NetworkHooks.openGui((ServerPlayerEntity) player, getMenuProvider(state, world, pos), pos);
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inventory, entity) -> new BlastFurnaceContainer(id, inventory, (BlastFurnaceTileEntity) world.getBlockEntity(pos)), TITLE);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING).add(BlockStateProperties.LIT);
    }
}
