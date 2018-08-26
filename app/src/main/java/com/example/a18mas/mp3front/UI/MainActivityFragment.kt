package com.example.a18mas.mp3front.UI

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a18mas.mp3front.R.layout.fragment_main
 import com.example.a18mas.mp3front.helper.MyEventListener
import com.example.a18mas.mp3front.helper.currentMeta
import com.example.a18mas.mp3front.helper.getMyContext
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main.*



/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(),
        View.OnClickListener,
        MyEventListener {
    override fun OnHaveToHide(hide: Boolean, function: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun OnCompletion(function: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun OnSetNewDataSource(function: () -> Unit) {
        Log.i("MAIN_FRAGMENT","OnSetNewDataSource ")


        //Picasso.with(myContext).load(currentMeta?.thumbnail?.get("medium")).fit().into(thumbnail)
        //title.text = currentMeta?.title
        //  Log.i("MAIN_FRAGMENT","OnSetNewDataSource title=${currentMeta?.title}")




    }

    override fun onClick(p0: View?) {
        Log.i("FRAGMENT","CLICK.......................")

    }

    override fun onResume() {
        Log.i("FRAGMENT","onResume")
        Picasso.with(myContext).load(currentMeta?.thumbnail?.get("high")).fit().into(thumbnail)
        title.text = currentMeta?.title
        Log.i("MAIN_FRAGMENT","OnSetNewDataSource title=${currentMeta?.title}")
        super.onResume()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        Log.i("FRAGMENT","onCreateView")

        var view = inflater.inflate(fragment_main, container, false)
        //var btn = view.findViewById(R.id.btn_search) as Button
        //btn.setOnClickListener(this)



        return view

    }

}
