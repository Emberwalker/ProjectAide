package io.drakon.aide.lib

import cpw.mods.fml.common.registry.GameRegistry
import io.drakon.aide.tile.TileGenerator
import io.drakon.aide.block.GeneratorBlockBase

/**
 * Manager for registry additions.
 *
 * @author Arkan <arkan@emberwalker.cc>
 */
object MCManager {

  def registerBlocks() {
    GameRegistry.registerBlock(new GeneratorBlockBase("Mini", 2), "GeneratorBlockMini")
    GameRegistry.registerBlock(new GeneratorBlockBase("Medium", 4), "GeneratorBlockMedium")
    GameRegistry.registerBlock(new GeneratorBlockBase("Large", 6), "GeneratorBlockLarge")
  }

  def registerItems() {
    // NOOP
  }

  def registerTiles() {
    GameRegistry.registerTileEntity(classOf[TileGenerator], "TileGenerator")
  }

}
