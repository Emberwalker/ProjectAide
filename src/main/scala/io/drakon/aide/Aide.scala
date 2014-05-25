package io.drakon.aide

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}

import io.drakon.aide.lib.MCManager
import io.drakon.aide.lib.Repo._

/**
 * The Floating Island Generator.
 *
 * Name inspired by Aide, Basque deity of air.
 *
 * @author Arkan <arkan@emberwalker.cc>
 */
@SuppressWarnings(Array("unused"))
@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, modLanguage = "scala")
object Aide {

  @EventHandler
  def preInit(evt:FMLPreInitializationEvent) {
    logger.debug("Preinit.")
  }

  @EventHandler
  def init(evt:FMLInitializationEvent) {
    logger.debug("Init.")
    MCManager.registerBlocks()
    MCManager.registerItems()
    MCManager.registerTiles()
    MCManager.registerRecipes()
  }

  @EventHandler
  def postInit(evt:FMLPostInitializationEvent) {
    logger.debug("Postinit.")
  }

}
