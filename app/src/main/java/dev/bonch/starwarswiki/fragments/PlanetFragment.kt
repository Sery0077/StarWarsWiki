package dev.bonch.starwarswiki.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.bonch.starwarswiki.MainActivity
import dev.bonch.starwarswiki.R
import dev.bonch.starwarswiki.models.Planet
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import dev.bonch.starwarswiki.network.retrofit.RetrofitService
import dev.bonch.starwarswiki.ui.adapters.Adapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

private lateinit var recyclerView: RecyclerView
private lateinit var planets: Array<Planet.Planet>
private lateinit var service: RetrofitService
private lateinit var errorTW: TextView
private lateinit var progressBar: ProgressBar
private lateinit var searchBtn: Button
private lateinit var searchEt: EditText

class PlanetFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.planet_fragment, container, false)

        initView(view)

        service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.Main).launch{getPlanetList()}

        setClicker()
        return view
    }


    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.planet_recycler_view)
        errorTW = view.findViewById(R.id.error_text_view)
        progressBar = view.findViewById(R.id.progress_bar)

        val activity = activity as MainActivity
        searchBtn = activity.findViewById(R.id.search)
        searchEt = activity.findViewById(R.id.text_search)
        searchEt.hint = "Enter a name of planet"
        recyclerView.layoutManager = LinearLayoutManager(activity@context)
    }

    private suspend fun getPlanetList() {
        progressBar.visibility = View.VISIBLE
        try {
            val response = service.getPlanetsList()
            if (response.isSuccessful) {
                planets = response.body()!!.results
                progressBar.visibility = View.GONE
                updateRecyclerData(planets)
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
                Toast.makeText(this.context, "Enter a name for search!", Toast.LENGTH_SHORT).show()
            } else{
                CoroutineScope(Dispatchers.Main).launch {searchPlanet(searchEt.text.toString())}
            }
        }

        errorTW.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch{getPlanetList()}
        }
    }

    private suspend fun searchPlanet(name: String) {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        try {
            val response = service.searchPlanet(name)
            if (response.isSuccessful) {
                if (response.body()!!.count == 0) {
                    progressBar.visibility = View.GONE
                    errorTW.text = getString(R.string.nothing_found_film)
                    errorTW.visibility = View.VISIBLE
                } else {
                    planets = response.body()!!.results
                    progressBar.visibility = View.GONE
                    updateRecyclerData(planets)
                }
            }
        } catch (err: IOException) {
            progressBar.visibility = View.GONE
            errorTW.text = getString(R.string.network_error)
            errorTW.visibility = View.VISIBLE
        }

    }

    private fun updateRecyclerData(films: Array<Planet.Planet>) {
        val adapter = object: Adapter(films) {
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.nameItem?.text = films[position].name
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("planet", planets[position])
                    (context as MainActivity).navigateToViewItemFragmentFromPlanets(bundle)
                }
                super.onBindViewHolder(holder, position)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
    }
}