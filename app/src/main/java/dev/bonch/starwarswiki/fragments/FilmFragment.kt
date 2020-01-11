package dev.bonch.starwarswiki.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.bonch.starwarswiki.MainActivity
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
private lateinit var searchBtn: Button
private lateinit var searchEt: EditText

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

        setClicker()
        return view
    }


    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.film_recycler_view)
        errorTW = view.findViewById(R.id.error_text_view)
        progressBar = view.findViewById(R.id.progress_bar)

        val activity = activity as MainActivity
        searchBtn = activity.findViewById(R.id.search)
        searchEt = activity.findViewById(R.id.text_search)
        searchEt.hint = "Enter a title of film"
        recyclerView.layoutManager = LinearLayoutManager(activity@context)
    }

    private suspend fun getFilmList() {
        progressBar.visibility = View.VISIBLE
        try {
            val response = service.getFilmsList()
            if (response.isSuccessful) {
                films = response.body()!!.results
                progressBar.visibility = View.GONE
                updateRecyclerData(films)
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this.context, response.message(), Toast.LENGTH_SHORT).show()
            }
        } catch (err: IOException) {
            errorTW.text = getString(R.string.network_error)
            progressBar.visibility = View.GONE
            errorTW.visibility = View.VISIBLE
        }
    }

    private fun setClicker() {
        searchBtn.setOnClickListener {
            if (searchEt.text.isNullOrEmpty()) {
                Toast.makeText(this.context, "Enter a title for search!", Toast.LENGTH_SHORT).show()
            } else{
                CoroutineScope(Dispatchers.Main).launch {searchFilm(searchEt.text.toString())}
            }
        }

        errorTW.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch{getFilmList()}
        }
    }

    private suspend fun searchFilm(title: String) {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        try {
            val response = service.searchFilm(title)
            if (response.isSuccessful) {
                if (response.body()!!.count == 0) {
                    progressBar.visibility = View.GONE
                    errorTW.text = getString(R.string.nothing_found_film)
                    errorTW.visibility = View.VISIBLE
                } else {
                    films = response.body()!!.results
                    progressBar.visibility = View.GONE
                    updateRecyclerData(films)
                }
            }
        } catch (err: IOException) {
            progressBar.visibility = View.GONE
            errorTW.text = getString(R.string.network_error)
            errorTW.visibility = View.VISIBLE
        }

    }

    private fun updateRecyclerData(films: Array<Film.Film>) {
        val adapter = object: Adapter(films) {
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.nameItem?.text = films[position].title
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("film", films[position])
                    (context as MainActivity).navigateToViewItemFragment(bundle)
                }
                super.onBindViewHolder(holder, position)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
    }
}