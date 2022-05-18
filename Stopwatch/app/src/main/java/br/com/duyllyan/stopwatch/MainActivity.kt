package br.com.duyllyan.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast

const val OFFSET_KEY = "offset"
const val RUNNING_KEY = "running"
const val BASE_KEY = "base"
const val LOG_TAG = "lifecycle"

class MainActivity : AppCompatActivity() {

    lateinit var stopwatch: Chronometer
    var running = false
    var offset: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showLifecycleStatus("On Create")

        stopwatch = findViewById(R.id.stopwatch)

        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

        val startButton = findViewById<Button>(R.id.btn_start)
        startButton.setOnClickListener {
            startStopwatch()
        }

        val pauseButton = findViewById<Button>(R.id.btn_pause)
        pauseButton.setOnClickListener {
            pauseStopwatch()
        }

        val resetButton = findViewById<Button>(R.id.btn_reset)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    private fun startStopwatch() {
        if (!running) {
            setBaseTime()
            stopwatch.start()
            running = true
        }
    }

    private fun pauseStopwatch() {
        if (running) {
            saveOffset()
            stopwatch.stop()
            running = false
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    override fun onStart() {
        showLifecycleStatus("On Start")
        super.onStart()
    }

    override fun onPause() {
        showLifecycleStatus("On Pause")
        pauseStopwatch()
        super.onPause()
    }

    override fun onResume() {
        showLifecycleStatus("On Resume")
        startStopwatch()
        super.onResume()
    }

    override fun onStop() {
        showLifecycleStatus("On Stop")
        super.onStop()
    }

    override fun onRestart() {
        showLifecycleStatus("On Restart")
        super.onRestart()
    }

    override fun onDestroy() {
        showLifecycleStatus("On Destroy")
        super.onDestroy()
    }

    private fun showLifecycleStatus (status: String) {
        Toast.makeText(applicationContext, status, Toast.LENGTH_SHORT).show()
        Log.i(LOG_TAG, status)
    }
}