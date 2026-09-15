package com.selfvault.desktop.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.selfvault.desktop.ui.theme.AppSpacing
import kotlin.Int
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.random.nextInt


data class ParticleConfig(
    var startXRatio: Float,
    var startYRatio: Float,
    var radius: Float,
    var shape: ShapeType,
    var durationMillis: Int,
    var delayMillis: Int = 0,
    var driftX: Float,
    var driftY: Float,
    var rotationAngle: Float,
    var innerRadius: Float? = null
) {
//    fun respawn() {
//        startXRatio = Random.nextFloat()
//        startYRatio = Random.nextFloat()
//        radius = radius
//        shape = shape
//        durationMillis = Random.nextInt(30000, 62000)
//        driftX = Random.nextDouble(-1.0, 1.0).toFloat()
//        driftY = Random.nextDouble(-1.0, 1.0).toFloat()
//        delayMillis = Random.nextInt(0, 1)
//        rotationAngle = Random.nextInt(-1, 1).toFloat()
//        innerRadius = innerRadius
//    }
}

//TODO: добавить появление фигур вне видимого пользователю слоя и создать респавн фигур, завершивших анимацию

enum class ShapeType {
    CIRCLE,
    SQUARE,
    OVAL,
    PENTAGON,
    OCTAGON
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
fun FloatingShapesBackground() {
    val colors = defaultColorConfig()

    val particleList = remember {
        List(10) {
            val shape = ShapeType.entries.random()
            val radius = Random.nextInt(30, 55).toFloat()

            val innerRadius = if (shape == ShapeType.OCTAGON) {
                radius * Random.nextDouble(0.80, 0.86).toFloat()
            } else {
                0f
            }

            ParticleConfig(
                startXRatio = Random.nextFloat(),
                startYRatio = Random.nextFloat(),
                radius = radius,
                shape = shape,
                durationMillis = Random.nextInt(30000, 62000),
                driftX = Random.nextDouble(-1.0, 1.0).toFloat(),
                driftY = Random.nextDouble(-1.0, 1.0).toFloat(),
                delayMillis = Random.nextInt(0, 1),
                rotationAngle = Random.nextInt(-1, 1).toFloat(),
                innerRadius = innerRadius

            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        particleList.forEach { particle ->
            val shapeColor = remember {
                when(Random.nextInt(1..3)) {
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

@Composable
fun SingleShape(
    particle: ParticleConfig,
    color: Color
) {
    val transition = rememberInfiniteTransition(label = "engine")

    val animatedOffset by transition.animateFloat(
        initialValue = -500f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = particle.durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(particle.delayMillis)
        ),
        label = "blobOffSet"
    )
    val rotationAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = particle.durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation"
    )

    fun createPolygonPath(center: Offset, radius: Float, sides: Int): Path {
        val path = Path()
        val angleStep = (2.0 * Math.PI / sides)

        for (i in 0 until sides) {
            val angle = i * angleStep - (Math.PI / 2)
            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        return path
    }

    fun createStarPath(center: Offset, outerRadius: Float, innerRadius: Float, points: Int): Path {
        val path = Path()
        val totalSteps = points * 2
        val angleStep = (2.0 * Math.PI / totalSteps)

        for (i in 0 until totalSteps) {
            val angle = i * angleStep - (Math.PI / 2)

            val currentRadius = if (i % 2 == 0) outerRadius else innerRadius

            val x = center.x + currentRadius * cos(angle).toFloat()
            val y = center.y + currentRadius * sin(angle).toFloat()

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        return path
    }

    val paint = remember(color) {
        Paint().apply {
            this.color = color
            this.style = PaintingStyle.Fill
            this.pathEffect = PathEffect.cornerPathEffect(8f)
        }
    }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)
        .clip(RoundedCornerShape(AppSpacing.cardCornerRadius - 12.dp))
    ) {

        val currentX = (particle.startXRatio * size.width)
        val currentY = (particle.startYRatio * size.height)

        val animatedCenter = Offset(
            x = currentX + (animatedOffset * particle.driftX),
            y = currentY + (animatedOffset * particle.driftY)
        )

        rotate(degrees = rotationAngle, pivot = animatedCenter) {
            when (particle.shape) {
                ShapeType.CIRCLE -> drawCircle(
                    color = color,
                    radius = particle.radius,
                    center = animatedCenter
                )

                ShapeType.SQUARE -> drawRoundRect(
                    color = color,
                    topLeft = animatedCenter,
                    size = Size(
                        width = particle.radius * 2,
                        height = particle.radius * 2,
                    ),
                    cornerRadius = CornerRadius(x = 12f, y = 12f)
                )
                ShapeType.OVAL -> drawOval(
                    color = color,
                    topLeft = animatedCenter,
                    size = Size(
                        width = particle.radius * 1.3f,
                        height = particle.radius * 2,
                    )
                )
                ShapeType.PENTAGON -> {
                    val polygonPath = createPolygonPath(
                        center = animatedCenter,
                        radius = particle.radius,
                        sides = 5
                    )
                    drawIntoCanvas { canvas ->
                        canvas.drawPath(polygonPath, paint)
                    }
                }
                ShapeType.OCTAGON -> {
                    val polygonPath = createStarPath(
                        center = animatedCenter,
                        innerRadius = particle.innerRadius ?: (particle.radius * 0.82f),
                        outerRadius = particle.radius,
                        points = 8
                    )
                    drawIntoCanvas { canvas ->
                        canvas.drawPath(polygonPath, paint)
                    }
                }
            }
        }
    }
}