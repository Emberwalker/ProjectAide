package io.drakon.aide.block

import io.drakon.aide.lib.Repo._

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.world.{World, IBlockAccess}
import net.minecraft.tileentity.TileEntity
import io.drakon.aide.tile.TileGenerator
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import net.minecraftforge.common.util.ForgeDirection

/**
 * Island generator base block.
 *
 * @author Arkan <arkan@emberwalker.cc>
 */
class GeneratorBlockBase(val size:String, val scaling:Int) extends Block(Material.anvil) {

  private var textureTop, textureBottom, textureHorizon, textureVertical:IIcon = null

  setBlockName(s"GeneratorBlock$size")
  setHardness(1.0f)
  setCreativeTab(CreativeTabs.tabRedstone)

  override def registerBlockIcons(registry : IIconRegister) {
    textureTop = registry.registerIcon("aide:top")
    textureBottom = registry.registerIcon("aide:bottom")
    textureHorizon = registry.registerIcon("aide:horizontal")
    textureVertical = registry.registerIcon("aide:vertical")
  }

  override def getIcon(side : Int, meta : Int): IIcon = {
    import net.minecraftforge.common.util.ForgeDirection._

    ForgeDirection.getOrientation(side) match {
      case UP => textureTop
      case DOWN => textureBottom
      case NORTH => textureHorizon
      case SOUTH => textureHorizon
      case _ => textureVertical
    }
  }

  override def getWeakChanges(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = true

  override def shouldCheckWeakPower(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean = true

  override def hasTileEntity(metadata: Int): Boolean = true

  override def canConnectRedstone(world: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean = true

  override def createTileEntity(world: World, meta: Int): TileEntity = new TileGenerator(scaling)

}
