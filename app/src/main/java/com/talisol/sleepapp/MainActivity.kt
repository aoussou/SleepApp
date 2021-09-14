package com.talisol.sleepapp

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.talisol.sleepapp.ui.theme.SleepAppTheme
import kotlinx.coroutines.*
import java.lang.Runnable
import kotlin.concurrent.timerTask
import android.media.AudioManager
import android.media.VolumeProvider
import kotlin.math.exp
import kotlin.math.log
import kotlin.math.sqrt


class MainActivity : ComponentActivity() {

    lateinit var runnable: Runnable

    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mp = MediaPlayer.create(this,R.raw.originofspecies_01_darwin_128kb)

        var vp = VolProv()

        var durationPodcast = mp.duration
        val originalVolume = 1.0f
        setContent {

            var cp by remember { mutableStateOf(0) }
            var volume by remember{ mutableStateOf(originalVolume)}
            mp.setVolume(volume,volume)

            SleepAppTheme {



                Column(modifier = Modifier.fillMaxSize()){


                    Button(onClick = {

                        if (mp.isPlaying){
                        mp.pause();
                            job?.cancel()
                    } else{

                        job = GlobalScope.launch(Dispatchers.Main){while(true){
                            cp = mp.currentPosition;
                            volume = newVolumePercentage(durationPodcast,cp)*originalVolume;
                            mp.setVolume(volume,volume);
                            delay(1000)}}


                        mp.start()


                    }


                    }) {
                        Text(text = "BUTTON")

                    }

                    Text(text = vp.currentVolume.toString())
                    Text(text = cp.toString())
                    Text(text =mp.duration.toString())

                    Text(text =(cp.toFloat()/mp.duration.toFloat()*100).toString())
                    Text(text =volume.toString())
                }
            }
        }
    }

    fun newVolumePercentage(totalDuration:Int,currentPosition:Int): Float{

        val a = (sqrt(5f) - 1) /2f
//        var percentageElapsed: Float = (totalDuration - (currentPosition).toFloat())/totalDuration
//        var percentageVolume = log(percentageElapsed.toDouble()+1f,2.0).toFloat()

        var percentageElapsed: Float = (currentPosition.toFloat()/totalDuration.toFloat())

//        var percentageVolume =  (exp(1f) - exp(percentageElapsed))/(exp(1f) - 1f  )

        var percentageVolume = 1f/(percentageElapsed + a) - 1f/(1f+a)

        return percentageVolume
    }
}

