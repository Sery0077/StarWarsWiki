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
import dev.bonch.starwarswiki.models.People
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import dev.bonch.starwarswiki.network.retrofit.RetrofitService
import dev.bonch.starwarswiki.ui.adapters.Adapter
import kotlinx.coroutines.*
import java.io.IOException

private lateinit var recyclerView: RecyclerView
private lateinit var peoples: Array<People.People>
private lateinit var service: RetrofitService
private lateinit var errorTW: TextView
private lateinit var progressBar: ProgressBar
private lateinit var searchBtn: Button
private lateinit var searchEt: EditText

class PeopleFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.people_fragment, container, false)

        initView(view)
        service = RetrofitFactory.makeRetrofitService()

        CoroutineScope(Dispatchers.Main).launch{getPeopleList()}

        setClicker()

        return view
    }

    private fun updateRecyclerData(peoples: Array<People.People>) {
        val adapter = object: Adapter(peoples) {
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.nameItem?.text = peoples[position].name
                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("film", peoples[position])
                    (context as MainActivity).navigateToViewItemFragmentFromPeoples(bundle)
                }
                super.onBindViewHolder(holder, position)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.people_recycler_view)
        errorTW = view.findViewById(R.id.error_text_view)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView.layoutManager = LinearLayoutManager(activity@context)

        val activity = activity as MainActivity
        searchBtn = activity.findViewById(R.id.search)
        searchEt = activity.findViewById(R.id.text_search)
        searchEt.hint = "Enter a name of people"
    }

    private suspend fun getPeopleList() {
        progressBar.visibility = View.VISIBLE
        try {
            val response = service.getPeoplesList()
            if (response.isSuccessful) {
                peoples = response.body()!!.results
                updateRecyclerData(peoples)
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this.context, response.message(), Toast.LENGTH_SHORT).show()
            }
        } catch (err: IOException) {
            progressBar.visibility = View.GONE
            errorTW.text = getString(R.string.network_error)
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
            CoroutineScope(Dispatchers.Main).launch{getPeopleList()}
        }
    }

    private suspend fun searchFilm(name: String) {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        try {
            val response = service.searchPeople(name)
            if (response.isSuccessful) {
                if (response.body()!!.count == 0) {
                    progressBar.visibility = View.GONE
                    errorTW.text = getString(R.string.nothing_found_people)
                    errorTW.visibility = View.VISIBLE
                } else {
                    peoples = response.body()!!.results
                    progressBar.visibility = View.GONE
                    updateRecyclerData(peoples)
                }
            }
        } catch (err: IOException) {
            progressBar.visibility = View.GONE
            errorTW.text = getString(R.string.network_error)
            errorTW.visibility = View.VISIBLE
        }

    }
}