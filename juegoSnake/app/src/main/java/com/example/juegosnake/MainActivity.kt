package com.example.juegosnake

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    lateinit var gameView: GameView
    var direction = Direction.UP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = GameView(this)
        findViewById<FrameLayout>(R.id.gameView).addView(gameView)
        startGame()

        /**
         * Setup del listener de la pantalla entera
         */
        findViewById<ConstraintLayout>(R.id.rootLayout).setOnTouchListener { _, event ->
            handleTouch(event)
            true
        }
    }

    private fun handleTouch(event: MotionEvent) {
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels


        val x = event.x
        val y = event.y

        /**
         * If para decidir donde se dio click y se decide una accion
         */
        if (y < screenHeight / 3) {
            /**
             * Arriba
             */
            if (direction != Direction.DOWN) {
                direction = Direction.UP
                gameView.setDirection(Direction.UP)
            }
        } else if (y > 2 * screenHeight / 3) {
            /**
             * ABAJO
             */
            if (direction != Direction.UP) {
                direction = Direction.DOWN
                gameView.setDirection(Direction.DOWN)
            }
        } else if (x < screenWidth / 3) {
            /**
             * Izquierda
             */
            if (direction != Direction.RIGHT) {
                direction = Direction.LEFT
                gameView.setDirection(Direction.LEFT)
            }
        } else if (x > 2 * screenWidth / 3) {
            /**
             * Derecha
             */
            if (direction != Direction.LEFT) {
                direction = Direction.RIGHT
                gameView.setDirection(Direction.RIGHT)
            }
        }
    }

    private fun startGame() {
        gameView.startGame()
    }
}
