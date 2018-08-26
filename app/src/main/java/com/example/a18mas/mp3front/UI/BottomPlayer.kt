package com.example.a18mas.mp3front.UI

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a18mas.mp3front.R
//

import android.os.*

import android.util.Log

import android.widget.SeekBar
import android.widget.ToggleButton
import com.example.a18mas.mp3front.MyExoPlayer
import com.example.a18mas.mp3front.helper.PlayerEventListener
//  import com.example.a18mas.mp3front.helper.currentMeta
import com.example.a18mas.mp3front.helper.playerImpl
import kotlinx.android.synthetic.main.fragment_bottom_player.*


/*

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_player, container, false)
    }

 */

/////////////////////


class BottomPlayer : Fragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener, PlayerEventListener {


    override fun OnCompletion(function: () -> Unit) {
        //  Log.i(TAG, "EVENT on completion!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        progress?.progress = 0
        playerImpl?.status = 2
        if (btn_toggle_play_pause?.isChecked == false) {
            btn_toggle_play_pause.toggle()
            playerImpl?.seekTo(0)
            playerImpl?.pause()
        }
    }

    override fun OnSetNewDataSource(function: () -> Unit) {
        Log.i(TAG, "OnSetNewDataSource max-progress = ${playerImpl?.getDuration() as Int}")

        /// Log.i(TAG, "set max = ${currentMeta?.duration_seconds as Int * 1000}")

        progress?.max = playerImpl?.getDuration() as Int //  currentMeta?.duration_seconds as Int * 1000
    }

    private var handler: Handler? = null
    private var userIsSeeking: Boolean = false
    private var btn: ToggleButton? = null
    private var progress: SeekBar? = null
    private var goto: Long = 0
    private val TAG: String = "PF_I"
    private val TAG_Seek: String = "PF_I"


    override fun onClick(v: View?) {
        toggle()
    }


    fun toggle() {
        playerImpl?.toggle()
    }

    fun seekTo(positionMS: Long) {
        Log.i("sekkTo", "positionMS: ${positionMS}")
        playerImpl?.seekTo(positionMS)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        Log.i(TAG_Seek, "[1]onStartTrackingTouch")
        userIsSeeking = true

    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (userIsSeeking) {
            goto = progress.toLong()
            Log.i(TAG_Seek, "[2]user is seeking to = $goto")

        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //  SystemClock.sleep(100);

        Log.i(TAG_Seek, "[3]onStopTrackingTouch @${goto}")
        userIsSeeking = false
        seekTo(goto)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {


        Log.i(TAG, "BottomPlayer: on create view")
        var view = inflater.inflate(R.layout.fragment_bottom_player, container, false)
        btn = view.findViewById(R.id.btn_toggle_play_pause) as ToggleButton
        btn?.setOnClickListener(this)
        progress = view.findViewById(R.id.progressBar) as SeekBar
        progress?.setOnSeekBarChangeListener(this)
        if (playerImpl == null) {
            MyExoPlayer()
        }
        playerImpl?.setOnSetNewDataSourceEventListener(this)

        if (handler == null) {
            handler = Handler()
            handler?.post(syncProgressBar)
        }

        return view

    }


    val syncProgressBar = object : Runnable {
        override fun run() {
            myActivity?.runOnUiThread {
                //  Log.i("SYNC", "stat ${playerImpl?.status} ,  ${playerImpl?.getCurrentPos()}/${playerImpl?.getDuration()} ||  ${progress?.progress}/${progress?.max}")
                //  progress?.progress = playerImpl?.getCurrentPos() as Int
                progress?.max = playerImpl?.getDuration() as Int

                if (playerImpl?.status == 1)
                    progress?.progress = playerImpl?.getCurrentPos() as Int
            }
            handler?.postDelayed(this, 100)
        }
    }


}


