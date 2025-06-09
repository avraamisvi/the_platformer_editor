package com.hagios.editor.actors

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Array
import com.hagios.editor.ProjectResourceLoader
import ktx.collections.isNotEmpty
import kotlin.math.max
import kotlin.math.min

/**
 * Modified version to draw the whole map at once in order to be able to shrink and increase it
 */
class EditorTileMap(private val noTileMapTexture: Texture? = null) : Actor(), ActorDataChangeListener {
    private var layers: Array<TiledMapTileLayer>? = null
    private val vertices = FloatArray(200)
    private val viewBounds: Rectangle

    private var maxRow = 0
    private var maxCol = 0

    companion object {
        const val MAP_PATH = "map_path"
    }

    /**
     *
     * @param col The x of the tile at the bottom left of this chunk
     * @param row The x of the tile at the bottom left of this chunk
     * @param cwidth The width (in tiles) of this chunk
     * @param cheight The height (in tiles) of this chunk
     */
    init {
        this.viewBounds = Rectangle()
        width = noTileMapTexture?.width?.toFloat() ?: 0f
        height = noTileMapTexture?.height?.toFloat() ?: 0f

        userObject = ActorData("TileMap",
            mapOf(MAP_PATH to ActorProperty.string(MAP_PATH)),
            listOf(this))
    }

    fun loadFullMap(map: TiledMap) {
        val tileWidth = map.getProperties().get("tilewidth", Int::class.java)
        val tileHeight = map.getProperties().get("tileheight", Int::class.java)
        val numTilesHorizontal = map.getProperties().get("width") as Int
        val numTilesVertical = map.getProperties().get("height") as Int
        val mapWidth = tileWidth * numTilesHorizontal
        val mapHeight = tileHeight * numTilesVertical

        maxCol = numTilesHorizontal
        maxRow = numTilesVertical

        layers = Array<TiledMapTileLayer>(map.getLayers().getCount())

        map.layers.forEach {
            require(it is TiledMapTileLayer)
            layers!!.add(it)
        }

        setSize(mapWidth.toFloat(), mapHeight.toFloat())
    }


    fun changeLayers(applier: (layer: TiledMapTileLayer)->Unit) {
        layers?.forEach { applier.invoke(it) }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {

        if(layers.isNotEmpty()) {

            val cam = getStage().getCamera() as OrthographicCamera
            val width = cam.viewportWidth * cam.zoom
            val height = cam.viewportHeight * cam.zoom
            viewBounds.set(cam.position.x - width / 2, cam.position.y - height / 2, width, height)

            //draw the layers
            for (layer in layers!!) {
                if (!layer.isVisible()) continue

                if (layer is TiledMapTileLayer) {
                    drawLayer(layer, batch, parentAlpha)
                }
            }
        } else {
            noTileMapTexture?.let { batch.draw(noTileMapTexture, x, y) }
        }
    }

    protected fun drawLayer(layer: TiledMapTileLayer, batch: Batch, parentAlpha: Float) {
        val batchColor = batch.getColor()
        val color =
            Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity() * parentAlpha)

        val layerWidth = layer.getWidth()
        val layerHeight = layer.getHeight()

        val layerTileWidth = layer.getTileWidth().toFloat() // * unitScale;
        val layerTileHeight = layer.getTileHeight().toFloat() // * unitScale;

        val col1 = 0
        val col2 = maxCol

        val row1 = 0
        val row2 = maxRow

        var y = getY() + (row1 * layerTileHeight)
        val xStart = getX() + (col1 * layerTileWidth)
        val vertices = this.vertices

        for (row in row1..<row2) {
            var x = xStart
            for (col in col1..<col2) {
                val cell = layer.getCell(col, row)
                if (cell == null) {
                    x += layerTileWidth
                    continue
                }
                val tile = cell.getTile()
                if (tile == null) continue

                val flipX = cell.getFlipHorizontally()
                val flipY = cell.getFlipVertically()
                val rotations = cell.getRotation()

                val region = tile.getTextureRegion()

                val x1 = x
                val y1 = y
                val x2 = x1 + region.getRegionWidth() // * unitScale;
                val y2 = y1 + region.getRegionHeight() // * unitScale;

                val u1 = region.getU()
                val v1 = region.getV2()
                val u2 = region.getU2()
                val v2 = region.getV()

                vertices[Batch.X1] = x1
                vertices[Batch.Y1] = y1
                vertices[Batch.C1] = color
                vertices[Batch.U1] = u1
                vertices[Batch.V1] = v1

                vertices[Batch.X2] = x1
                vertices[Batch.Y2] = y2
                vertices[Batch.C2] = color
                vertices[Batch.U2] = u1
                vertices[Batch.V2] = v2

                vertices[Batch.X3] = x2
                vertices[Batch.Y3] = y2
                vertices[Batch.C3] = color
                vertices[Batch.U3] = u2
                vertices[Batch.V3] = v2

                vertices[Batch.X4] = x2
                vertices[Batch.Y4] = y1
                vertices[Batch.C4] = color
                vertices[Batch.U4] = u2
                vertices[Batch.V4] = v1

                if (flipX) {
                    var temp = vertices[Batch.U1]
                    vertices[Batch.U1] = vertices[Batch.U3]
                    vertices[Batch.U3] = temp
                    temp = vertices[Batch.U2]
                    vertices[Batch.U2] = vertices[Batch.U4]
                    vertices[Batch.U4] = temp
                }
                if (flipY) {
                    var temp = vertices[Batch.V1]
                    vertices[Batch.V1] = vertices[Batch.V3]
                    vertices[Batch.V3] = temp
                    temp = vertices[Batch.V2]
                    vertices[Batch.V2] = vertices[Batch.V4]
                    vertices[Batch.V4] = temp
                }
                if (rotations != 0) {
                    when (rotations) {
                        TiledMapTileLayer.Cell.ROTATE_90 -> {
                            val tempV = vertices[Batch.V1]
                            vertices[Batch.V1] = vertices[Batch.V2]
                            vertices[Batch.V2] = vertices[Batch.V3]
                            vertices[Batch.V3] = vertices[Batch.V4]
                            vertices[Batch.V4] = tempV

                            val tempU = vertices[Batch.U1]
                            vertices[Batch.U1] = vertices[Batch.U2]
                            vertices[Batch.U2] = vertices[Batch.U3]
                            vertices[Batch.U3] = vertices[Batch.U4]
                            vertices[Batch.U4] = tempU
                        }

                        TiledMapTileLayer.Cell.ROTATE_180 -> {
                            var tempU = vertices[Batch.U1]
                            vertices[Batch.U1] = vertices[Batch.U3]
                            vertices[Batch.U3] = tempU
                            tempU = vertices[Batch.U2]
                            vertices[Batch.U2] = vertices[Batch.U4]
                            vertices[Batch.U4] = tempU
                            var tempV = vertices[Batch.V1]
                            vertices[Batch.V1] = vertices[Batch.V3]
                            vertices[Batch.V3] = tempV
                            tempV = vertices[Batch.V2]
                            vertices[Batch.V2] = vertices[Batch.V4]
                            vertices[Batch.V4] = tempV
                        }

                        TiledMapTileLayer.Cell.ROTATE_270 -> {
                            val tempV = vertices[Batch.V1]
                            vertices[Batch.V1] = vertices[Batch.V4]
                            vertices[Batch.V4] = vertices[Batch.V3]
                            vertices[Batch.V3] = vertices[Batch.V2]
                            vertices[Batch.V2] = tempV

                            val tempU = vertices[Batch.U1]
                            vertices[Batch.U1] = vertices[Batch.U4]
                            vertices[Batch.U4] = vertices[Batch.U3]
                            vertices[Batch.U3] = vertices[Batch.U2]
                            vertices[Batch.U2] = tempU
                        }
                    }
                }
                batch.draw(region.getTexture(), vertices, 0, 20)
                x += layerTileWidth
            }
            y += layerTileHeight
        }
    }

    override fun propertyChanged(property: ActorProperty) {
        if(property.name == MAP_PATH) {
            ProjectResourceLoader.loadTileMap(property.asString())?.let {
                loadFullMap(it)
            }
        }
    }
}
