package io.drakon.aide.lib

import org.apache.logging.log4j.LogManager
import net.minecraft.block.Block
import io.drakon.aide.block.GeneratorBlockBase

/**
 * Object repository
 *
 * @author Arkan <arkan@emberwalker.cc>
 */
object Repo {

  final val MOD_ID = "aide"
  final val MOD_NAME = "Project Aide"
  final val MOD_VERSION = "${version}"

  val logger = LogManager.getLogger(MOD_NAME)

  var genMini = new GeneratorBlockBase("Mini", 2)
  val genMedium = new GeneratorBlockBase("Medium", 4)
  val genLarge = new GeneratorBlockBase("Large", 6)

}
