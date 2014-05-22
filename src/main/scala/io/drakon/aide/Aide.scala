package io.drakon.aide

import io.drakon.aide.lib.Repo._

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.Mod.EventHandler
import io.drakon.aide.lib.MCManager

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
  }

  @EventHandler
  def postInit(evt:FMLPostInitializationEvent) {
    logger.debug("Postinit.")
  }

}
