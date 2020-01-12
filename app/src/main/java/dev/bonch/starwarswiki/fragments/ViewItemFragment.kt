package dev.bonch.starwarswiki.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import dev.bonch.starwarswiki.MainActivity
import dev.bonch.starwarswiki.R
import dev.bonch.starwarswiki.models.*
import kotlinx.coroutines.*

private lateinit var descriptionTW: TextView
private lateinit var expandableList: ExpandableListView
private lateinit var description: String
private lateinit var adapter: ExpandableListAdapter
private lateinit var progressBar: ProgressBar
private lateinit var nameItemTW: TextView
private lateinit var job: Job
private val data: Array<Array<String?>?> = arrayOfNulls(5)

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
                    val film = bundle.getParcelable<Film.Film>("film")!!
                    nameItemTW.text = film.title
                    formViewForFilm(film)
                }
                bundle.containsKey("people") -> CoroutineScope(Dispatchers.Main).launch {
                    val people = bundle.getParcelable<People.People>("people")!!
                    formViewForPeople(people)
                    nameItemTW.text = people.name
                }
                bundle.containsKey("planet") -> CoroutineScope(Dispatchers.Main).launch {
                    val planet = bundle.getParcelable<Planet.Planet>("planet")!!
                    formViewForPlanet(planet)
                    nameItemTW.text = planet.name
                }
                bundle.containsKey("vehicle") -> CoroutineScope(Dispatchers.Main).launch {
                    val vehicle = bundle.getParcelable<Vehicle.Vehicle>("vehicle")!!
                    formViewForVehicle(vehicle)
                    nameItemTW.text = vehicle.name
                }
                bundle.containsKey("starship") -> CoroutineScope(Dispatchers.Main).launch {
                    val starship = bundle.getParcelable<Starship.Starship>("starship")!!
                    formViewForStarship(starship)
                    nameItemTW.text = starship.name
                }
                bundle.containsKey("specie") -> CoroutineScope(Dispatchers.Main).launch {
                    val specie = bundle.getParcelable<Specie.Specie>("specie")!!
                    formViewForSpecie(specie)
                    nameItemTW.text = specie.name
                }
            }
        }
        return view
    }

    private fun initView(view: View) {
        val activity = activity as MainActivity
        val searchBtn = activity.findViewById<Button>(R.id.search)
        val searchEt = activity.findViewById<EditText>(R.id.text_search)
        nameItemTW = activity.findViewById(R.id.name_item)
        nameItemTW.visibility = View.VISIBLE
        searchBtn.visibility = View.GONE
        searchEt.visibility = View.GONE

        expandableList = view.findViewById(R.id.expandable_list_view)
        descriptionTW = view.findViewById(R.id.description)
        descriptionTW.visibility = View.GONE
        progressBar = view.findViewById(R.id.progress_bar)
    }

    private fun setClickers() {
        expandableList.setOnGroupClickListener { parent, v, groupPosition, id ->
            setExpandableListViewHeight(parent!!, groupPosition)
            false
        }
    }

    private fun setExpandableListViewHeight(listView: ExpandableListView, group: Int) {
        val listAdapter = listView.expandableListAdapter as ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if ((((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group)))) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params = listView.layoutParams
        var height = (totalHeight + (listView.dividerHeight * (listAdapter.groupCount - 1)))
        if (height < listView.measuredHeight)
            height = listView.measuredHeight
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

    private suspend fun formViewForFilm(film: Film.Film) {
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val names = People.getPeoplesNames(film.characters)
                data[0] = names
            }
            withContext(Dispatchers.Default) {
                val planets = Planet.getPlanetsNames(film.planets)
                data[1] = planets
            }
            withContext(Dispatchers.Default) {
                val starships = Starship.getStarshipsNames(film.starships)
                data[2] = starships
            }
            withContext(Dispatchers.Default) {
                val species = Specie.getSpeciesNames(film.species)
                data[3] = species
            }
            withContext(Dispatchers.Default) {
                val vehicles = Vehicle.getVehiclesNames(film.vehicles)
                data[4] = vehicles
            }
        }
        description = Film.factoryDescription(film)
        val groups = listOf("Characters", "Planets", "Starships", "Species", "Vehicles")
        val groupDataList: MutableList<Map<String, String>> = arrayListOf()
        val groupFrom = arrayOf("groupName")
        val groupTo = intArrayOf(android.R.id.text1)
        val childTo = intArrayOf(android.R.id.text1)
        val childFrom = arrayOf("value")
        descriptionTW.text = description
        job.invokeOnCompletion {
            if (!job.isCancelled) {
                val dataAll: MutableList<MutableList<Map<String, String?>>> = arrayListOf()
                groups.forEach {
                    groupDataList += mapOf(Pair("groupName", it))
                }

                data.forEach {
                    val childDataList: MutableList<Map<String, String?>> = arrayListOf()
                    it?.forEach { value ->
                        childDataList += mapOf(Pair("value", value))
                    }
                    dataAll += childDataList
                }

                adapter = object : SimpleExpandableListAdapter(
                    this.context, groupDataList,
                    android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                    dataAll, android.R.layout.simple_list_item_1, childFrom,
                    childTo
                ) {}
                expandableList.setAdapter(adapter)

                setClickers()
                progressBar.visibility = View.GONE
                descriptionTW.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun formViewForPeople(people: People.People) {
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val films = Film.getFilmsNames(people.films)
                data[0] = films
            }
            withContext(Dispatchers.Default) {
                val starships = Starship.getStarshipsNames(people.starships)
                data[1] = starships
            }
            withContext(Dispatchers.Default) {
                val species = Specie.getSpeciesNames(people.species)
                data[2] = species
            }
            withContext(Dispatchers.Default) {
                val vehicles = Vehicle.getVehiclesNames(people.vehicles)
                data[3] = vehicles
            }
        }
        description = People.factoryDescription(people)
        val groups = listOf("Films", "Starships", "Species", "Vehicles")
        val groupDataList: MutableList<Map<String, String>> = arrayListOf()
        val groupFrom = arrayOf("groupName")
        val groupTo = intArrayOf(android.R.id.text1)
        val childTo = intArrayOf(android.R.id.text1)
        val childFrom = arrayOf("value")
        descriptionTW.text = description
        job.invokeOnCompletion {
            if (!job.isCancelled) {
                val dataAll: MutableList<MutableList<Map<String, String?>>> = arrayListOf()
                groups.forEach {
                    groupDataList += mapOf(Pair("groupName", it))
                }

                data.forEach {
                    val childDataList: MutableList<Map<String, String?>> = arrayListOf()
                    it?.forEach { value ->
                        childDataList += mapOf(Pair("value", value))
                    }
                    dataAll += childDataList
                }

                adapter = object : SimpleExpandableListAdapter(
                    this.context, groupDataList,
                    android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                    dataAll, android.R.layout.simple_list_item_1, childFrom,
                    childTo
                ) {}
                expandableList.setAdapter(adapter)

                setClickers()
                progressBar.visibility = View.GONE
                descriptionTW.visibility = View.VISIBLE
                Log.e("fefe", "click")
            }
        }
    }

    private suspend fun formViewForPlanet(planet: Planet.Planet) {
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val films = Film.getFilmsNames(planet.films)
                data[0] = films
            }
            withContext(Dispatchers.Default) {
                val residents = People.getPeoplesNames(planet.residents)
                data[1] = residents
            }
        }
        description = Planet.factoryDescription(planet)
        val groups = listOf("Films", "Residents")
        val groupDataList: MutableList<Map<String, String>> = arrayListOf()
        val groupFrom = arrayOf("groupName")
        val groupTo = intArrayOf(android.R.id.text1)
        val childTo = intArrayOf(android.R.id.text1)
        val childFrom = arrayOf("value")
        descriptionTW.text = description
        job.invokeOnCompletion {
            if (!job.isCancelled) {
                val dataAll: MutableList<MutableList<Map<String, String?>>> = arrayListOf()
                groups.forEach {
                    groupDataList += mapOf(Pair("groupName", it))
                }

                data.forEach {
                    val childDataList: MutableList<Map<String, String?>> = arrayListOf()
                    it?.forEach { value ->
                        childDataList += mapOf(Pair("value", value))
                    }
                    dataAll += childDataList
                }

                adapter = object : SimpleExpandableListAdapter(
                    this.context, groupDataList,
                    android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                    dataAll, android.R.layout.simple_list_item_1, childFrom,
                    childTo
                ) {}
                expandableList.setAdapter(adapter)

                setClickers()
                progressBar.visibility = View.GONE
                descriptionTW.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun formViewForVehicle(vehicle: Vehicle.Vehicle) {
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val films = Film.getFilmsNames(vehicle.films)
                data[0] = films
            }
            withContext(Dispatchers.Default) {
                val pilots = People.getPeoplesNames(vehicle.pilots)
                data[1] = pilots
            }
        }
        description = Vehicle.factoryDescription(vehicle)
        val groups = listOf("Films", "Pilots")
        val groupDataList: MutableList<Map<String, String>> = arrayListOf()
        val groupFrom = arrayOf("groupName")
        val groupTo = intArrayOf(android.R.id.text1)
        val childTo = intArrayOf(android.R.id.text1)
        val childFrom = arrayOf("value")
        descriptionTW.text = description
        job.invokeOnCompletion {
            if (job.isCancelled) {
                val dataAll: MutableList<MutableList<Map<String, String?>>> = arrayListOf()
                groups.forEach {
                    groupDataList += mapOf(Pair("groupName", it))
                }

                data.forEach {
                    val childDataList: MutableList<Map<String, String?>> = arrayListOf()
                    it?.forEach { value ->
                        childDataList += mapOf(Pair("value", value))
                    }
                    dataAll += childDataList
                }

                adapter = object : SimpleExpandableListAdapter(
                    this.context, groupDataList,
                    android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                    dataAll, android.R.layout.simple_list_item_1, childFrom,
                    childTo
                ) {}
                expandableList.setAdapter(adapter)

                setClickers()
                progressBar.visibility = View.GONE
                descriptionTW.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun formViewForStarship(starship: Starship.Starship) {
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val films = Film.getFilmsNames(starship.films)
                data[0] = films
            }
            withContext(Dispatchers.Default) {
                val pilots = People.getPeoplesNames(starship.pilots)
                data[1] = pilots
            }
        }
        description = Starship.factoryDescription(starship)
        val groups = listOf("Films", "Pilots")
        val groupDataList: MutableList<Map<String, String>> = arrayListOf()
        val groupFrom = arrayOf("groupName")
        val groupTo = intArrayOf(android.R.id.text1)
        val childTo = intArrayOf(android.R.id.text1)
        val childFrom = arrayOf("value")
        descriptionTW.text = description
        job.invokeOnCompletion {
            if (!job.isCancelled) {
                val dataAll: MutableList<MutableList<Map<String, String?>>> = arrayListOf()
                groups.forEach {
                    groupDataList += mapOf(Pair("groupName", it))
                }

                data.forEach {
                    val childDataList: MutableList<Map<String, String?>> = arrayListOf()
                    it?.forEach { value ->
                        childDataList += mapOf(Pair("value", value))
                    }
                    dataAll += childDataList
                }

                adapter = object : SimpleExpandableListAdapter(
                    this.context, groupDataList,
                    android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                    dataAll, android.R.layout.simple_list_item_1, childFrom,
                    childTo
                ) {}
                expandableList.setAdapter(adapter)

                setClickers()
                progressBar.visibility = View.GONE
                descriptionTW.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun formViewForSpecie(specie: Specie.Specie) {
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Default) {
                val films = Film.getFilmsNames(specie.films)
                data[0] = films
            }
            withContext(Dispatchers.Default) {
                val peoples = People.getPeoplesNames(specie.people)
                data[1] = peoples
            }
        }
        description = Specie.factoryDescription(specie)
        val groups = listOf("Films", "Peoples")
        val groupDataList: MutableList<Map<String, String>> = arrayListOf()
        val groupFrom = arrayOf("groupName")
        val groupTo = intArrayOf(android.R.id.text1)
        val childTo = intArrayOf(android.R.id.text1)
        val childFrom = arrayOf("value")
        descriptionTW.text = description
        job.invokeOnCompletion {
            if (!job.isCancelled) {
                val dataAll: MutableList<MutableList<Map<String, String?>>> = arrayListOf()
                groups.forEach {
                    groupDataList += mapOf(Pair("groupName", it))
                }

                data.forEach {
                    val childDataList: MutableList<Map<String, String?>> = arrayListOf()
                    it?.forEach { value ->
                        childDataList += mapOf(Pair("value", value))
                    }
                    dataAll += childDataList
                }

                adapter = object : SimpleExpandableListAdapter(
                    this.context, groupDataList,
                    android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo,
                    dataAll, android.R.layout.simple_list_item_1, childFrom,
                    childTo
                ) {}
                expandableList.setAdapter(adapter)

                setClickers()
                progressBar.visibility = View.GONE
                descriptionTW.visibility = View.VISIBLE
            }
        }
    }

    override fun onDetach() {
        job.cancelChildren()
        job.cancel()
        nameItemTW.visibility = View.GONE
        super.onDetach()
    }
}