package dev.bonch.starwarswiki.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.SimpleExpandableListAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.bonch.starwarswiki.R
import dev.bonch.starwarswiki.models.Film
import kotlinx.coroutines.*

private lateinit var descriptionTW: TextView
private lateinit var expandableList: ExpandableListView
private lateinit var description: String
private lateinit var adapter: ExpandableListAdapter
private var names: Array<String?> = emptyArray()
private var vehicles: Array<String?> = emptyArray()
private var starships: Array<String?> = emptyArray()
private var planets: Array<String?> = emptyArray()
private var species: Array<String?> = emptyArray()
private lateinit var job: Job

class ViewItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.view_item_fragment, container, false)
        initView(view)
        val bundle = arguments
        if (bundle !== null) {
            when {
                bundle.containsKey("film") -> CoroutineScope(Dispatchers.Main).launch {
                    formViewForFilm(bundle.getParcelable("film")!!)
                }
            }
        }

        return view
    }

    private fun initView(view: View) {
        expandableList = view.findViewById(R.id.expandable_list_view)
        descriptionTW = view.findViewById(R.id.description)
        descriptionTW.movementMethod = ScrollingMovementMethod()
    }

    suspend fun formViewForFilm(film: Film.Film) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val deferredCharactersNames = CoroutineScope(Dispatchers.IO).async {
                Film.getCharactersNames(film.characters)
            }
            names = deferredCharactersNames.await()

            val deferredPlanetsNames = CoroutineScope(Dispatchers.IO).async {
                Film.getPlanetsNames(film.planets)
            }
            planets = deferredPlanetsNames.await()

            val deferredStarshipsNames = CoroutineScope(Dispatchers.IO).async {
                Film.getStarshipsNames(film.starships)
            }
            starships = deferredStarshipsNames.await()

            val deferredSpeciesNames = CoroutineScope(Dispatchers.IO).async {
                Film.getSpeciesNames(film.species)
            }
            species = deferredSpeciesNames.await()

            val deferredVehiclesNames = CoroutineScope(Dispatchers.IO).async {
                Film.getVehiclesNames(film.vehicles)
            }
            vehicles = deferredVehiclesNames.await()
        }
        job.invokeOnCompletion {
            val groups = listOf("Characters", "Planets", "Starships", "Species", "Vehicles")
            val groupFrom = arrayOf("groupName")
            val groupTo = intArrayOf(android.R.id.text1)
            val childTo = intArrayOf(android.R.id.text1)
            val childFrom = arrayOf("value")
            val data = listOf(names, planets, starships, species, vehicles)
            val groupDataList: MutableList<Map<String, String>> = arrayListOf()
            val dataAll: MutableList<MutableList<Map<String, String?>>> = arrayListOf()
            groups.forEach {
                groupDataList += mapOf(Pair("groupName", it))
            }
            data.forEach{
                val childDataList: MutableList<Map<String, String?>> = arrayListOf()
                it.forEach { value ->
                    childDataList += mapOf(Pair("value", value))
                }
                dataAll += childDataList
            }
            adapter = object: SimpleExpandableListAdapter(
                this.context, groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                dataAll, android.R.layout.simple_list_item_1, childFrom,
                childTo) {}
            expandableList.setAdapter(adapter)
        }
        description = Film.factoryForViewFilm(film)
        descriptionTW.text = description
    }

}