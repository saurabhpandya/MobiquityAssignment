package com.mobiquityassignment.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobiquityassignment.base.BaseFragment
import com.mobiquityassignment.base.ViewModelFactory
import com.mobiquityassignment.data.map.WeatherNetworkDataProvider
import com.mobiquityassignment.databinding.FragmentFavoriteBinding
import com.mobiquityassignment.networking.RetrofitClient
import com.mobiquityassignment.ui.favorite.viewmodel.FavoriteViewModel
import com.mobiquityassignment.utility.OnItemClickListner
import com.mobiquityassignment.utility.Status
import com.mobiquityassignment.utility.showToast

class FavoriteFragment : BaseFragment(), OnItemClickListner {
    private val TAG = this::class.java.canonicalName

    lateinit var binding: FragmentFavoriteBinding

    lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupObserver()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                WeatherNetworkDataProvider(RetrofitClient.WEATHER_SERVICE),
                this.requireActivity().application
            )
        ).get(FavoriteViewModel::class.java)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    private fun setupRecyclerView() {

        binding.rcyclrvwFavorite.layoutManager = LinearLayoutManager(activity)

        binding.rcyclrvwFavorite.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        viewModel.favoriteAdapter.onItemClickListner = this
        binding.rcyclrvwFavorite.adapter = viewModel.favoriteAdapter
        viewModel.favoriteAdapter.setChat(viewModel.getFavoroteCities())

    }

    private fun setupObserver() {
        viewModel.weatherDataResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.ERROR -> {
                    showProgress(false)
                    requireActivity().showToast(it.message!!)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    Log.d(TAG, "${it.data}")

                    val title = it.data?.name!!
                    val message =
                        "Temperature - ${it.data.main?.temp}\nFeels like - ${it.data.main?.feels_like}\nHumidity - ${it.data.main?.humidity}\nWeather - ${it.data.weather!![0].main}"
                    showAlert(title, message)

//                    binding.txtvwCurrentLocation.text = "Current Location - ${it.data?.name}"
//                    binding.txtvwCurTemp.text = "Temperature - ${it.data?.main?.temp}"
//                    binding.txtvwCurTempFeels.text = "Feels like - ${it.data?.main?.feels_like}"
//                    binding.txtvwCurHumidity.text = "Humidity - ${it.data?.main?.humidity}"
//                    binding.txtvwCurWeather.text = "Weather - ${it.data?.weather!![0].main}"

//                    val arylstWeather = ArrayList<WeatherModel>()
//                    arylstWeather.add(it.data!!)
//                    val mapper = ObjectMapper()
//                    val str = mapper.writeValueAsString(arylstWeather)
//                    Log.d(TAG, "ArrayList String : $str")
//                    val weatherModels = mapper.readValue(str, WeatherModels::class.java)
//                    Log.d(TAG, "ArrayList : $weatherModels")

                }
            }
        })
    }

    override fun onItemClickListner(position: Int) {
        val selectedCity = viewModel.getFavoroteCities()[position]
        Log.d(TAG, "You selected $selectedCity")
        viewModel.getWeatherDataByCity(selectedCity)
    }

    override fun onDeleteClickListner(position: Int) {
        viewModel.removeLocation(position)
    }

}