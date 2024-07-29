package com.android.benedictcumberbatchapp.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.benedictcumberbatchapp.R
import com.android.benedictcumberbatchapp.databinding.FragmentMovieListBinding
import com.android.benedictcumberbatchapp.ui.MovieViewModel
import com.android.benedictcumberbatchapp.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val movieViewModel: MovieViewModel by activityViewModels()

    private val movieAdapter = MovieAdapter()

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        _binding = FragmentMovieListBinding.inflate(getLayoutInflater())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.isVisible = true
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieViewModel.moviePagingList.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progress.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.progress.isVisible = false
                            movieAdapter.submitData(lifecycle, state.data)
                        }

                        else -> {
                            binding.progress.isVisible = false
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapter.loadStateFlow.collect { loadStates ->

                binding.movieRecyclerView.isVisible = loadStates.source.refresh is LoadState.NotLoading

                binding.progress.isVisible = loadStates.source.refresh is LoadState.Loading

                val errorState = loadStates.source.append as? LoadState.Error
                    ?: loadStates.source.prepend as? LoadState.Error
                    ?: loadStates.source.refresh as? LoadState.Error

                errorState?.let {
                    Toast.makeText(requireContext(), it.error.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }
        }

        setClickListener()
        setRecyclerView()


    }

    private fun setRecyclerView() {
        binding.movieRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setClickListener() {
        movieAdapter.setMovieDetailListener {
            movieViewModel.setMovieDetails(it)
            val action = MovieListFragmentDirections.actionMovieListFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}