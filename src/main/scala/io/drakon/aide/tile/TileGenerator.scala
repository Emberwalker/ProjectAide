package io.drakon.aide.tile

import scala.collection.mutable
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import java.util

import org.apache.logging.log4j.LogManager

import net.minecraft.block.{BlockSapling, IGrowable}
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

import io.drakon.aide.algorithm.IslandGenerator
import io.drakon.aide.lib.{BlockCoord, LogStore}

/**
 * Generator TE
 *
 * @author Arkan <arkan@emberwalker.cc>
 */
class TileGenerator(final val scaling:Int) extends TileEntity {
  import TileGenerator._
  logger.entry(scaling.asInstanceOf[Integer])

  private val islGen = new IslandGenerator()
  private val internalRand = new Random()
  private val javaRand = new util.Random()

  private var radius = scaling
  private var height = scaling
  private var warmedUp = false
  private var generatedSaplings = 0

  private var isRunning = false
  private var coordsTodo:mutable.Queue[Pair[BlockCoord, Int]] = null

  private var genFuture:Future[mutable.Queue[Pair[BlockCoord, Int]]] = null

  private def createNewFuture() {
    genFuture = future {
      logger.entry()
      val g = islGen.generate(height, radius)
      var out = new mutable.MutableList[Pair[BlockCoord, Int]]
      for (x <- Range(0, g.length - 1); z <- Range(0, g(0).length - 1); y <- Range(0, g(0)(0).length - 1)) {
        if (g(x)(z)(y) >= 1) {
          logger.debug(LogStore.MARKER_ISLAND_GEN, s"x=$x, z=$z, y=$y")
          val blT = if (y >= height - 2) 1 else 0
          out += new Pair(new BlockCoord(x + xCoord - getHalfR, y + yCoord + 1, z + zCoord - getHalfR), blT)
        }
      }
      out = internalRand.shuffle(out)
      logger.exit(out.toQueue)
    }
  }

  private def getHalfR:Int = radius / 2

  private def createBlockFromPair(pair:Pair[BlockCoord, Int]) {
    val c = pair._1
    val isGrass = pair._2 >= 1
    isGrass match {
      case true =>
        getWorldObj.setBlock(c.getX, c.getY, c.getZ, Blocks.grass)
        if (internalRand.nextInt(20) <= 1)
          Blocks.grass.asInstanceOf[IGrowable].func_149853_b(getWorldObj,javaRand,c.getX, c.getY, c.getZ)
        else if (generatedSaplings <= 2 && internalRand.nextInt(200) <= 1) {
          getWorldObj.setBlock(c.getX, c.getY + 1, c.getZ, Blocks.sapling, internalRand.nextInt(5), 3)
          Blocks.sapling.asInstanceOf[BlockSapling].func_149878_d(getWorldObj, c.getX, c.getY + 1, c.getZ, javaRand)
          generatedSaplings += 1
        }
      case _ => getWorldObj.setBlock(c.getX, c.getY, c.getZ, Blocks.dirt)
    }
  }

  private def recalculateHeightRadius() {
    logger.entry()
    val r1 = powerLevelOnSide(ForgeDirection.NORTH)
    val r2 = powerLevelOnSide(ForgeDirection.SOUTH)
    radius = Math.max(r1, r2)*scaling

    val h1 = powerLevelOnSide(ForgeDirection.EAST)
    val h2 = powerLevelOnSide(ForgeDirection.WEST)
    height = Math.max(h1, h2)*scaling

    if (radius <= 0) radius = scaling
    if (height <= 0) height = scaling

    logger.debug(s"r1: $r1, r2:$r2, h1: $h1, h2: $h2")
    logger.debug(s"r$radius, h$height")
    logger.exit()
  }

  override def updateEntity() {
    if (!warmedUp && powerLevelOnSide(ForgeDirection.DOWN) >= 14 && !getWorldObj.isRemote) {
      recalculateHeightRadius()
      createNewFuture()
      warmedUp = true
      return
    }

    if (getWorldObj.isRemote || genFuture == null || !genFuture.isCompleted) return
    if (isRunning) {
      for (_ <- Range(0, scaling)) {
        if (coordsTodo.isEmpty) {
          selfDestruct()
          return
        }
        createBlockFromPair(coordsTodo.dequeue())
      }
    } else {
      logger.debug("Starting to build...")
      val r = genFuture.value.get
      if (r.isFailure) {
        logger.error("Failed to get a valid list!")
        selfDestruct()
        return
      }
      coordsTodo = r.get
      isRunning = true
    }
  }

  private def selfDestruct() {
    logger.debug("Good night, Gracie.")
    getWorldObj.createExplosion(null, xCoord + 0.5, yCoord, zCoord + 0.5, 0, true)
    getWorldObj.setBlockToAir(xCoord, yCoord, zCoord)
  }

  override def canUpdate: Boolean = true

  // Based on https://github.com/OpenMods/OpenBlocks/blob/master/src/main/java/openblocks/common/tileentity/TileEntityDigitalFuse.java#L143
  private def powerLevelOnSide(side:ForgeDirection):Int = {
    val pX = xCoord + side.offsetX
    val pY = yCoord + side.offsetY
    val pZ = zCoord + side.offsetZ

		if (worldObj.isAirBlock(pX, pY, pZ)) { return 0; }

		val block = worldObj.getBlock(pX, pY, pZ)

		if (block == Blocks.redstone_wire) {
			return worldObj.getBlockMetadata(pX, pY, pZ)
		} else if (block.hasComparatorInputOverride) {
			return block.getComparatorInputOverride(worldObj, pX, pY, pZ, side.getOpposite.ordinal())
		} else if (block.canProvidePower) {
      return Math.max(
        block.isProvidingStrongPower(worldObj, pX, pY, pZ, side.getOpposite.ordinal()),
        block.isProvidingWeakPower(worldObj, pX, pY, pZ, side.getOpposite.ordinal())
      )
    }

		0
  }

  logger.exit(this)
}

object TileGenerator {

  private val logger = LogManager.getLogger

}
