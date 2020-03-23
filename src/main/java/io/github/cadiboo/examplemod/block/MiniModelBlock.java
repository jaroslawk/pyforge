package io.github.cadiboo.examplemod.block;

import io.github.cadiboo.examplemod.client.gui.MiniModelScreen;
import io.github.cadiboo.examplemod.init.ModTileEntityTypes;
import io.github.cadiboo.examplemod.tileentity.MiniModelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Cadiboo
 */
public class MiniModelBlock extends Block {

	public MiniModelBlock(final Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(final BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		// Always use TileEntityType#create to allow registry overrides to work.
		return ModTileEntityTypes.MINI_MODEL.create();
	}

	/**
	 * @deprecated Call via {@link BlockState#isSolid()} whenever possible.
	 * Implementing/overriding is fine.
	 */
	@Override
	public boolean isSolid(final BlockState state) {
		// This prevents xraying through the world and allows light to go through this block.
		return false;
	}

	/**
	 * @deprecated Call via {@link BlockState#getCollisionShape(IBlockReader, BlockPos, ISelectionContext)} whenever possible.
	 * Implementing/overriding is fine.
	 */
	@Override
	@Nonnull
	public VoxelShape getCollisionShape(final BlockState state, final IBlockReader reader, final BlockPos pos, final ISelectionContext context) {
		// Allow entities to walk through the model
		return VoxelShapes.empty();
	}

	/**
	 * @deprecated Call via {@link BlockState#onBlockActivated(World, PlayerEntity, Hand, BlockRayTraceResult)} whenever possible.
	 * Implementing/overriding is fine.
	 */
	@Override
	public boolean onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
		// Only open the gui on the physical client
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> openGui(worldIn, pos));
		return true;
	}

	// @OnlyIn(Dist.CLIENT) Makes it so this method will be removed from the class on the PHYSICAL SERVER
	// This is because we only want to handle opening the GUI on the physical client.
	@OnlyIn(Dist.CLIENT)
	private void openGui(final World worldIn, final BlockPos pos) {
		// Only handle opening the Gui screen on the logical client
		if (worldIn.isRemote) {
			final TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof MiniModelTileEntity) {
				Minecraft.getInstance().displayGuiScreen(new MiniModelScreen(((MiniModelTileEntity) tileEntity)));
			}
		}
	}

}
