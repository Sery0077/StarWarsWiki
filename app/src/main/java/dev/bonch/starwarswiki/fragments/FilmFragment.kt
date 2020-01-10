package dev.bonch.starwarswiki.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.bonch.starwarswiki.R
import dev.bonch.starwarswiki.models.Film
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import dev.bonch.starwarswiki.network.retrofit.RetrofitService
import dev.bonch.starwarswiki.ui.adapters.Adapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

private lateinit var recyclerView: RecyclerView
private lateinit var films: Array<Film.Film>
private lateinit var service: RetrofitService
private lateinit var errorTW: TextView
private lateinit var progressBar: ProgressBar

class FilmFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.film_fragment, container, false)

        initView(view)
        service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.Main).launch{getFilmList()}

        return view
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(activity@context)
        val adapter = object: Adapter(films) {
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder?.nameItem?.text = films[position].title
                super.onBindViewHolder(holder, position)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.film_recycler_view)
        errorTW = view.findViewById(R.id.error_text_view)
        progressBar = view.findViewById(R.id.progress_bar)
    }

    private suspend fun getFilmList() {
        progressBar.visibility = View.VISIBLE
        try {
            val response = service.getFilmsList()
            if (response.isSuccessful) {
                films = response.body()!!.results
                progressBar.visibility = View.GONE
                initRecycler()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(PeopleFragment@context, response.message(), Toast.LENGTH_SHORT).show()
            }
        } catch (err: IOException) {
            progressBar.visibility = View.GONE
            errorTW.visibility = View.VISIBLE
            errorTW.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch{getFilmList()}
            }
        }
    }
}