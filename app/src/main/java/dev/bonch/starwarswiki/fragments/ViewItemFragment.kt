package dev.bonch.starwarswiki.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.bonch.starwarswiki.R
import dev.bonch.starwarswiki.models.Film
import kotlinx.android.synthetic.main.view_item_fragment.*
import kotlinx.coroutines.*


private lateinit var textView: TextView
private lateinit var charactersEL: ExpandableListView
private lateinit var description: String
private var names: Array<String?> = emptyArray()

class ViewItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments

        if (bundle !== null) {
            if (bundle.containsKey("film")) {
                val film = bundle.getParcelable<Film.Film>("film")!!
                CoroutineScope(Dispatchers.Main).launch {
                    val deferredNames = CoroutineScope(Dispatchers.IO).async {
                        Film.getCharactersNames(film.characters)
                    }
                    names = deferredNames.await()
                    Log.e("fefe", names.size.toString())
                }
                description = Film.factoryForView(film)
            }
        }
        val view = inflater.inflate(R.layout.view_item_fragment, container, false)

        textView = view.findViewById(R.id.text_view)
        textView.text = description

        return view
    }
}