package com.hagios

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor


/**
 * Loads a Tiled map file (*.tmx) with a given filename
 * and adds it to a stage (so that the tilemap automatically renders).
 */
@Deprecated("moved to actors")
class TiledMapActor(filename: String?) : Actor() {
    private val tiledMap: TiledMap
    private val tiledCamera: OrthographicCamera
    private val tiledMapRenderer: OrthoCachedTiledMapRenderer
    private val shapeRenderer: ShapeRenderer = ShapeRenderer()

    /**
     * Loads a Tiled map file (*.tmx) with a given filename
     * and adds it to a stage (so that the tilemap automatically renders).
     */
    init {
        // set up tile map, renderer, and camera
        tiledMap = TmxMapLoader().load(filename)

        val tileWidth = tiledMap.getProperties().get("tilewidth") as Int
        val tileHeight = tiledMap.getProperties().get("tileheight") as Int
        val numTilesHorizontal = tiledMap.getProperties().get("width") as Int
        val numTilesVertical = tiledMap.getProperties().get("height") as Int
        val mapWidth = tileWidth * numTilesHorizontal
        val mapHeight = tileHeight * numTilesVertical

        tiledMapRenderer = OrthoCachedTiledMapRenderer(tiledMap)
        tiledMapRenderer.setBlending(true)

        tiledCamera = OrthographicCamera()
        tiledCamera.setToOrtho(false, windowWidth.toFloat(), windowHeight.toFloat())
        tiledCamera.update()

        this.width = mapWidth.toFloat()
        this.height = mapHeight.toFloat()
    }

    /**
     * Search the map layers for Rectangle Objects that contain a property (key) called "name" with associated value propertyName;
     * typically used to store non-actor information such as spawn point locations or dimensions of Solid objects.
     * Retrieve data as object, then cast to desired type: for example, float w = (float)obj.getProperties().get("width").
     */
    fun getRectangleList(propertyName: String?): ArrayList<MapObject?> {
        val list = ArrayList<MapObject?>()

        for (layer in tiledMap.getLayers()) {
            for (obj in layer.getObjects()) {
                if (obj !is RectangleMapObject) continue

                val props = obj.getProperties()

                if (props.containsKey("name") && props.get("name") == propertyName) list.add(obj)
            }
        }
        return list
    }

    /**
     * Search the map layers for Tile Objects (tile-like elements of object layers)
     * that contain a property (key) called "name" with associated value propertyName;
     * typically used to store actor information and will be used to create instances.
     */
    fun getTileList(propertyName: String?): ArrayList<MapObject?> {
        val list = ArrayList<MapObject?>()

        for (layer in tiledMap.getLayers()) {
            for (obj in layer.getObjects()) {
                if (obj !is TiledMapTileMapObject) continue

                val props = obj.getProperties()

                // Default MapProperties are stored within associated Tile object
                // Instance-specific overrides are stored in MapObject
                val tmtmo = obj
                val t = tmtmo.getTile()
                val defaultProps = t.getProperties()

                if (defaultProps.containsKey("name") && defaultProps.get("name") == propertyName) list.add(obj)

                // get list of default property keys
                val propertyKeys = defaultProps.getKeys()

                // iterate over keys; copy default values into props if needed
                while (propertyKeys.hasNext()) {
                    val key = propertyKeys.next()

                    // check if value already exists; if not, create property with default value
                    if (props.containsKey(key)) continue
                    else {
                        val value = defaultProps.get(key)
                        props.put(key, value)
                    }
                }
            }
        }
        return list
    }

    /**
     * Automatically called by Stage.
     */
    override fun act(dt: Float) {
        super.act(dt)
    }

//    /**
//     * Automatically called by Stage; keeps the map camera position in
//     * sync with the stage camera, and renders the map to the screen.
//     */
//    override fun draw(batch: Batch, parentAlpha: Float) {
//        // adjust tilemap camera to stay in sync with main camera
//        val mainCamera = getStage().getCamera()
//
////        tiledCamera.position.x = mainCamera.position.x + this.x
////        tiledCamera.position.y = mainCamera.position.y + this.y
////        tiledCamera.zoom = this.scaleY
//
////        tiledCamera.update()
//        val projection = batch.projectionMatrix.cpy()
//        val transform = batch.transformMatrix.cpy().trn(x, y, 0f)
//        val matrix = projection.mul(transform)
//
//        println("$width, $height")
//
//        tiledMapRenderer.setView(matrix, 0f, 0f, mainCamera.viewportWidth, mainCamera.viewportHeight)
//
//        // need the following code to force batch order,
//        //  otherwise it is batched and rendered last
//        batch.end()
//        tiledMapRenderer.render(arrayOf(1).toIntArray())
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
//        shapeRenderer.rect(x, y, width * transform.scaleX, height * transform.scaleY)
//        shapeRenderer.end()
//        batch.begin()
//    }


    override fun draw(batch: Batch, parentAlpha: Float) {
        // adjust tilemap camera to stay in sync with main camera
        val mainCamera = getStage().getCamera()
        val vec = parent.parentToLocalCoordinates(Vector2(x, y))
        tiledCamera.position.x = mainCamera.position.x + vec.x
        tiledCamera.position.y = mainCamera.position.y + vec.y
        tiledCamera.zoom = batch.transformMatrix.scaleX
        tiledCamera.update()
        tiledMapRenderer.setView(tiledCamera)


        println("$x, $y, $vec")

        // need the following code to force batch order,
        //  otherwise it is batched and rendered last
        batch.end()
        tiledMapRenderer.render()
        batch.begin()
    }
    companion object {
        // window dimensions
        var windowWidth: Int = 800
        var windowHeight: Int = 600
    }
}
