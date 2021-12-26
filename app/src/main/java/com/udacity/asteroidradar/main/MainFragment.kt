package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.adapter.AsteroidRowAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val asteroidListAdapter = AsteroidRowAdapter(AsteroidRowAdapter.OnClickListener {
        viewModel.displayAsteroidDetails((it))
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter = asteroidListAdapter

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            asteroidListAdapter.submitList(it)
            binding.executePendingBindings()
        })

        viewModel.navigateToSelectedAsteroid.observe (viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_all_menu -> {
                viewModel.getAllAsteroids()
                asteroidListAdapter.submitList(viewModel.asteroidList.value)
                return true
            }
            R.id.show_today_menu -> {
                viewModel.getAsteroidsForToday()
                asteroidListAdapter.submitList(viewModel.asteroidList.value)
                return true
            }
            R.id.show_saved_menu -> {
                viewModel.getAllAsteroids()
                asteroidListAdapter.submitList(viewModel.asteroidList.value)
                return true
            }
        }
        return false
    }
}
