package io.drakon.aide.lib

import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.registry.GameRegistry

import io.drakon.aide.lib.Repo._
import io.drakon.aide.tile.TileGenerator

/**
 * Manager for registry additions.
 *
 * @author Arkan <arkan@emberwalker.cc>
 */
object MCManager {

  def registerBlocks() {
    GameRegistry.registerBlock(genMini, "GeneratorBlockMini")
    GameRegistry.registerBlock(genMedium, "GeneratorBlockMedium")
    GameRegistry.registerBlock(genLarge, "GeneratorBlockLarge")
  }

  def registerItems() {
    // NOOP
  }

  def registerTiles() {
    GameRegistry.registerTileEntity(classOf[TileGenerator], "TileGenerator")
  }

  def registerRecipes() {
    val dirt = new ItemStack(Blocks.dirt)
    val pearl = new ItemStack(Items.ender_pearl)
    val mini = new ItemStack(genMini)
    val medium = new ItemStack(genMedium)
    val large = new ItemStack(genLarge)

    GameRegistry.addRecipe(mini, "ddd", "dpd", "ddd", 'p':Character, pearl, 'd':Character, dirt)
    GameRegistry.addRecipe(medium, "ddd", "dpd", "ddd", 'p':Character, pearl, 'd':Character, mini)
    GameRegistry.addRecipe(large, "ddd", "dpd", "ddd", 'p':Character, pearl, 'd':Character, medium)
  }

}
