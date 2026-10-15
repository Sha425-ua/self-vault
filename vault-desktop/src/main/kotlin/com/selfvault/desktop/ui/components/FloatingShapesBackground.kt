package com.selfvault.desktop.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.selfvault.desktop.ui.theme.AppSpacing
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt
import com.selfvault.desktop.ui.theme.MaterialShapes
import com.selfvault.desktop.ui.theme.drawMaterialShape


class ParticleConfig(
    initialX: Float,
    initialY: Float,
    initialVx: Float,
    initialVy: Float,
    initialRadius: Float,
    initialShape: ShapeType,
    initialRotation: Float = Random.nextFloat() * 360f,
    initialVr: Float,
) {

    var x: Float by mutableFloatStateOf(initialX)
    var y: Float by mutableFloatStateOf(initialY)
    var vx: Float by mutableFloatStateOf(initialVx)
    var vy: Float by mutableFloatStateOf(initialVy)
    var vr: Float by mutableFloatStateOf(initialVr)
    var radius: Float by mutableFloatStateOf(initialRadius)
    var shape: ShapeType by mutableStateOf(initialShape)
    var rotation: Float by mutableFloatStateOf(initialRotation)

    fun update(width: Float, height: Float, dt: Float) {
        x += vx * dt
        y += vy * dt

        rotation += vr * dt

        if (rotation >= 360f) rotation -= 360f
        else if (rotation < 0f) rotation += 360f

        val size = radius * 2

        // Left Side
        if (x + size * 2 < 0) {
            x = width + size
            y = Random.nextFloat() * height
            onWrap()
        }

        // Right Side
        else if (x - size > width) {
            x = -size
            y = Random.nextFloat() * height
            onWrap()
        }

        // Top Side
        if (y + size * 2 < 0) {
            x = Random.nextFloat() * width
            y = height + size
            onWrap()
        }

        // Downside
        else if (y - size > height) {
            x = Random.nextFloat() * width
            y = -size
            onWrap()
        }
    }

    private fun onWrap() {
        shape = ShapeType.entries.random()
    }
}

enum class ShapeType {
    CIRCLE,
    SQUARE,
    OVAL,
    PENTAGON,
    VERY_SUNNY,
    ARROW,
    TRIANGLE,
    CLOVER_4LEAF,
    CLOVER_8LEAF,
    GEM,
    SOFT_BURST
}

data class ColorConfig(
    val color1: Color,
    val color2: Color,
    val color3: Color,
    val color4: Color
)

@Composable
fun defaultColorConfig(): ColorConfig = ColorConfig(
    color1 = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
    color2 = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
    color3 = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
    color4 = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f),
)

@Composable
fun FloatingShapesBackground(modifier: Modifier = Modifier) {
    val colors = defaultColorConfig()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        val particleList = remember {
            List(10) {
                val shape = ShapeType.entries.random()
                val radius = Random.nextInt(20, 60).toFloat()

                ParticleConfig(
                    initialX = Random.nextFloat() * widthPx,
                    initialY = Random.nextFloat() * heightPx,
                    initialRadius = radius,
                    initialShape = shape,
                    initialVx = Random.nextInt(-25, 25).toFloat(),
                    initialVy = Random.nextInt(-25, 25).toFloat(),
                    initialVr = Random.nextDouble(-25.0, 25.0).toFloat(),
                    initialRotation = Random.nextInt(-1, 1).toFloat(),
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            particleList.forEach { particle ->
                val shapeColor = remember {
                    when (Random.nextInt(1..3)) {
                        1 -> colors.color1
                        2 -> colors.color2
                        3 -> colors.color3
                        else -> colors.color4
                    }
                }
                SingleShape(particle, shapeColor)
            }
        }
    }
}

@Composable
fun SingleShape(
    particle: ParticleConfig,
    color: Color
) {

//    fun createPolygonPath(center: Offset, radius: Float, sides: Int): Path {
//        val path = Path()
//        val angleStep = (2.0 * Math.PI / sides)
//
//        for (i in 0 until sides) {
//            val angle = i * angleStep - (Math.PI / 2)
//            val x = center.x + radius * cos(angle).toFloat()
//            val y = center.y + radius * sin(angle).toFloat()
//
//            if (i == 0) {
//                path.moveTo(x, y)
//            } else {
//                path.lineTo(x, y)
//            }
//        }
//        path.close()
//        return path
//    }
//
//    fun createStarPath(center: Offset, outerRadius: Float, innerRadius: Float, points: Int): Path {
//        val path = Path()
//        val totalSteps = points * 2
//        val angleStep = (2.0 * Math.PI / totalSteps)
//
//        for (i in 0 until totalSteps) {
//            val angle = i * angleStep - (Math.PI / 2)
//
//            val currentRadius = if (i % 2 == 0) outerRadius else innerRadius
//
//            val x = center.x + currentRadius * cos(angle).toFloat()
//            val y = center.y + currentRadius * sin(angle).toFloat()
//
//            if (i == 0) {
//                path.moveTo(x, y)
//            } else {
//                path.lineTo(x, y)
//            }
//        }
//        path.close()
//        return path
//    }
//
//    val paint = remember(color) {
//        Paint().apply {
//            this.color = color
//            this.style = PaintingStyle.Fill
//            this.pathEffect = PathEffect.cornerPathEffect(8f)
//        }
//    }

    val rotationAngle = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        var lastTime = withInfiniteAnimationFrameMillis { it }

        launch {
            while (true) {
                rotationAngle.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(
                        easing = LinearEasing,
                        durationMillis = Random.nextInt(15000, 30000)
                    )
                )
                rotationAngle.snapTo(0f)
            }
        }

        while(true) {
           withInfiniteAnimationFrameMillis { frameTime ->
               val dt = (frameTime - lastTime) / 1000f
               lastTime = frameTime

               particle.update(380f, 420f, dt)
           }
        }
    }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)
        .clip(RoundedCornerShape(AppSpacing.DialogCard.cornerRadius - 12.dp))
    ) {

        val center = Offset(particle.x, particle.y)

        rotate(degrees = particle.rotation, pivot = center) {
            when (particle.shape) {
                ShapeType.CIRCLE -> drawMaterialShape(MaterialShapes.Circle, center, particle.radius, color)
                ShapeType.SQUARE -> drawMaterialShape(MaterialShapes.Square, center, particle.radius, color)
                ShapeType.GEM -> drawMaterialShape(MaterialShapes.Gem, center, particle.radius, color)
                ShapeType.OVAL -> drawMaterialShape(MaterialShapes.Oval, center, particle.radius, color)
                ShapeType.PENTAGON -> drawMaterialShape(MaterialShapes.Pentagon, center, particle.radius, color)
                ShapeType.VERY_SUNNY -> drawMaterialShape(MaterialShapes.VerySunny, center, particle.radius, color)
                ShapeType.TRIANGLE -> drawMaterialShape(MaterialShapes.Triangle, center, particle.radius, color)
                ShapeType.ARROW -> drawMaterialShape(MaterialShapes.Arrow, center, particle.radius, color)
                ShapeType.CLOVER_4LEAF -> drawMaterialShape(MaterialShapes.Clover4Leaf, center, particle.radius, color)
                ShapeType.CLOVER_8LEAF -> drawMaterialShape(MaterialShapes.Clover8Leaf, center, particle.radius, color)
                ShapeType.SOFT_BURST -> drawMaterialShape(MaterialShapes.SoftBurst, center, particle.radius, color)
            }
        }
    }
}