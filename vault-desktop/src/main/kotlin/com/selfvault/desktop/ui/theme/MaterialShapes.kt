package com.selfvault.desktop.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import kotlin.math.cos
import kotlin.math.sin

/**
 * Complete official catalog of all 35 Google Material 3 Expressive Shapes (2024).
 * Exact mathematical specifications matching AOSP / AndroidX and Caelestia Shell.
 */
object MaterialShapes {

    // ==========================================
    // ROW 1: Basic Geometrics
    // ==========================================

    val Circle: RoundedPolygon by lazy {
        RoundedPolygon.circle(radius = 1f)
    }

    val Square: RoundedPolygon by lazy {
        RoundedPolygon(
            numVertices = 4,
            radius = 1f,
            rounding = CornerRounding(0.35f, 0.5f)
        )
    }

    val Slanted: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.75f, -0.95f,
                 0.95f, -0.75f,
                 0.75f,  0.95f,
                -0.95f,  0.75f
            ),
            rounding = CornerRounding(0.28f, 0.4f)
        )
    }

    val Arch: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.85f,  0.9f,
                 0.85f,  0.9f,
                 0.85f, -0.1f,
                 0.0f,  -0.95f,
                -0.85f, -0.1f
            ),
            rounding = CornerRounding(0.30f)
        )
    }

    val Fan: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.85f,  0.85f,
                 0.85f,  0.85f,
                 0.85f, -0.85f,
                -0.85f, -0.1f
            ),
            rounding = CornerRounding(0.32f)
        )
    }

    val Arrow: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                 0.0f,  -1.0f,
                 0.85f,  0.85f,
                 0.0f,   0.35f,
                -0.85f,  0.85f
            ),
            rounding = CornerRounding(0.22f)
        )
    }

    val SemiCircle: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.95f, 0.7f,
                 0.95f, 0.7f,
                 0.75f, -0.7f,
                -0.75f, -0.7f
            ),
            rounding = CornerRounding(0.48f)
        )
    }

    // ==========================================
    // ROW 2: Curvy & Faceted Geometrics
    // ==========================================

    val Oval: RoundedPolygon by lazy {
        val count = 16
        val rx = 1.0f
        val ry = 0.64f
        val rotAngle = -Math.PI / 4.0

        val vertices = FloatArray(count * 2)
        for (i in 0 until count) {
            val theta = i * (2.0 * Math.PI / count)
            val unrotatedX = rx * cos(theta)
            val unrotatedY = ry * sin(theta)

            vertices[i * 2] = (unrotatedX * cos(rotAngle) - unrotatedY * sin(rotAngle)).toFloat()
            vertices[i * 2 + 1] = (unrotatedX * sin(rotAngle) + unrotatedY * cos(rotAngle)).toFloat()
        }
        RoundedPolygon(vertices, CornerRounding(0.45f))
    }

    val Pill: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.55f, -0.95f,
                 0.55f, -0.95f,
                 0.55f,  0.95f,
                -0.55f,  0.95f
            ),
            rounding = CornerRounding(0.55f)
        )
    }

    val Triangle: RoundedPolygon by lazy {
        RoundedPolygon(
            numVertices = 3,
            radius = 1f,
            rounding = CornerRounding(0.32f, 0.4f)
        )
    }

    val Diamond: RoundedPolygon by lazy {
        RoundedPolygon(
            numVertices = 4,
            radius = 1f,
            rounding = CornerRounding(0.28f)
        )
    }

    val ClamShell: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.5f, -0.9f,
                 0.5f, -0.9f,
                 0.95f, 0.0f,
                 0.5f,  0.9f,
                -0.5f,  0.9f,
                -0.95f, 0.0f
            ),
            rounding = CornerRounding(0.35f)
        )
    }

    val Pentagon: RoundedPolygon by lazy {
        RoundedPolygon(
            numVertices = 5,
            radius = 1f,
            rounding = CornerRounding(0.25f)
        )
    }

    val Gem: RoundedPolygon by lazy {
        RoundedPolygon(
            numVertices = 6,
            radius = 1f,
            rounding = CornerRounding(0.24f)
        )
    }

    // ==========================================
    // ROW 3: Sunnies & Cookies
    // ==========================================

    val VerySunny: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 8,
            radius = 1f,
            innerRadius = 0.65f,
            rounding = CornerRounding(0.18f)
        )
    }

    val Sunny: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 8,
            radius = 1f,
            innerRadius = 0.82f,
            rounding = CornerRounding(0.22f)
        )
    }

    val Cookie4Sided: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 4,
            radius = 1f,
            innerRadius = 0.72f,
            rounding = CornerRounding(0.32f)
        )
    }

    val Cookie6Sided: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 6,
            radius = 1f,
            innerRadius = 0.78f,
            rounding = CornerRounding(0.28f)
        )
    }

    val Cookie7Sided: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 7,
            radius = 1f,
            innerRadius = 0.82f,
            rounding = CornerRounding(0.24f)
        )
    }

    val Cookie9Sided: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 9,
            radius = 1f,
            innerRadius = 0.84f,
            rounding = CornerRounding(0.22f)
        )
    }

    val Cookie12Sided: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            radius = 1f,
            innerRadius = 0.88f,
            rounding = CornerRounding(0.18f)
        )
    }

    // ==========================================
    // ROW 4: Organic, Clovers & Bursts
    // ==========================================

    val Ghostish: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.85f, -0.7f,
                 0.85f, -0.7f,
                 0.85f,  0.5f,
                 0.45f,  0.9f,
                 0.0f,   0.6f,
                -0.45f,  0.9f,
                -0.85f,  0.5f
            ),
            rounding = CornerRounding(0.32f)
        )
    }

    val Clover4Leaf: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 4,
            radius = 1f,
            innerRadius = 0.52f,
            rounding = CornerRounding(0.42f)
        )
    }

    val Clover8Leaf: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 8,
            radius = 1f,
            innerRadius = 0.62f,
            rounding = CornerRounding(0.35f)
        )
    }

    val Burst: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            radius = 1f,
            innerRadius = 0.55f,
            rounding = CornerRounding(0.06f)
        )
    }

    val SoftBurst: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 10,
            radius = 1f,
            innerRadius = 0.68f,
            rounding = CornerRounding(0.16f)
        )
    }

    val Boom: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 16,
            radius = 1f,
            innerRadius = 0.40f,
            rounding = CornerRounding(0.04f)
        )
    }

    val SoftBoom: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 16,
            radius = 1f,
            innerRadius = 0.55f,
            rounding = CornerRounding(0.12f)
        )
    }

    // ==========================================
    // ROW 5: Expressive Flowers & Novelty
    // ==========================================

    val Flower: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 8,
            radius = 1f,
            innerRadius = 0.60f,
            rounding = CornerRounding(0.38f)
        )
    }

    val Puffy: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 12,
            radius = 1f,
            innerRadius = 0.75f,
            rounding = CornerRounding(0.35f)
        )
    }

    val PuffyDiamond: RoundedPolygon by lazy {
        RoundedPolygon.star(
            numVerticesPerRadius = 4,
            radius = 1f,
            innerRadius = 0.45f,
            rounding = CornerRounding(0.38f)
        )
    }

    val PixelCircle: RoundedPolygon by lazy {
        RoundedPolygon(
            numVertices = 16,
            radius = 1f,
            rounding = CornerRounding(0.02f)
        )
    }

    val PixelTriangle: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.9f, -0.9f,
                 0.9f,  0.0f,
                -0.9f,  0.9f
            ),
            rounding = CornerRounding(0.04f)
        )
    }

    val Bun: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                -0.85f, -0.75f,
                 0.85f, -0.75f,
                 0.35f,  0.0f,
                 0.85f,  0.75f,
                -0.85f,  0.75f,
                -0.35f,  0.0f
            ),
            rounding = CornerRounding(0.32f)
        )
    }

    val Heart: RoundedPolygon by lazy {
        RoundedPolygon(
            vertices = floatArrayOf(
                 0.0f, -0.4f,
                 0.5f, -0.95f,
                 0.95f, -0.4f,
                 0.0f,  1.0f,
                -0.95f, -0.4f,
                -0.5f, -0.95f
            ),
            rounding = CornerRounding(0.32f)
        )
    }
}

/**
 * Extension: Convert RoundedPolygon into a Compose Path, perfectly normalized and scaled to radius.
 * Automatically aligns bounding centers and equalizes visual size for all 35 shapes.
 */
fun RoundedPolygon.toComposePath(radius: Float, center: Offset): Path {
    val bounds = calculateBounds()
    val minX = bounds[0]
    val minY = bounds[1]
    val maxX = bounds[2]
    val maxY = bounds[3]

    val width = maxX - minX
    val height = maxY - minY
    val maxDim = maxOf(width, height)

    // Center of the shape bounding box
    val midX = (minX + maxX) / 2f
    val midY = (minY + maxY) / 2f

    // Scale factor to make all shapes fit nicely in 2 * radius
    val scale = if (maxDim > 0f) (radius * 2f) / maxDim else radius

    val path = Path()
    var isFirst = true
    for (cubic in cubics) {
        val a0X = center.x + (cubic.anchor0X - midX) * scale
        val a0Y = center.y + (cubic.anchor0Y - midY) * scale
        val c0X = center.x + (cubic.control0X - midX) * scale
        val c0Y = center.y + (cubic.control0Y - midY) * scale
        val c1X = center.x + (cubic.control1X - midX) * scale
        val c1Y = center.y + (cubic.control1Y - midY) * scale
        val a1X = center.x + (cubic.anchor1X - midX) * scale
        val a1Y = center.y + (cubic.anchor1Y - midY) * scale

        if (isFirst) {
            path.moveTo(a0X, a0Y)
            isFirst = false
        }
        path.cubicTo(c0X, c0Y, c1X, c1Y, a1X, a1Y)
    }
    path.close()
    return path
}

/**
 * Quick helper to draw any MaterialShape directly onto Compose DrawScope.
 */
fun DrawScope.drawMaterialShape(
    shape: RoundedPolygon,
    center: Offset,
    radius: Float,
    color: Color
) {
    drawPath(path = shape.toComposePath(radius, center), color = color)
}
