package com.mobiquityassignment.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mobiquityassignment.R
import com.mobiquityassignment.base.BaseFragment
import com.mobiquityassignment.base.ViewModelFactory
import com.mobiquityassignment.data.map.WeatherNetworkDataProvider
import com.mobiquityassignment.databinding.FragmentMapBinding
import com.mobiquityassignment.networking.RetrofitClient
import com.mobiquityassignment.ui.map.viewmodel.MapViewModel
import com.mobiquityassignment.utility.Status
import com.mobiquityassignment.utility.showToast


class MapFragment : BaseFragment(), OnMapReadyCallback, LocationListener, View.OnClickListener {
    private val TAG = this::class.java.canonicalName

    lateinit var binding: FragmentMapBinding

    lateinit var viewModel: MapViewModel

    private lateinit var mMap: GoogleMap

    private var locationManager: LocationManager? = null

    private lateinit var centerLatLng: LatLng
    private lateinit var centerMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initMap()
        setupViewModel()
        setListener()
        setupObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (locationManager != null)
            locationManager!!.removeUpdates(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnCameraMoveStartedListener {
            centerLatLng = mMap.projection.visibleRegion.latLngBounds.center
            binding.txtvwCurrentLocation.text =
                "Current Location: ${centerLatLng.latitude}, ${centerLatLng.longitude}"
        }

        mMap.setOnCameraIdleListener {
            centerLatLng = mMap.projection.visibleRegion.latLngBounds.center
            binding.txtvwCurrentLocation.text =
                "Current Location: ${centerLatLng.latitude}, ${centerLatLng.longitude}"
            viewModel.getWeatherDataByCoOrdinates(centerLatLng.latitude, centerLatLng.longitude)
        }

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
    }


    override fun onLocationChanged(location: Location) {
        val latlng = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10.0f))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map_favorite -> {
                view?.findNavController()
                    ?.navigate(R.id.action_mapFragment_to_favoriteFragment, null)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkForPermission()
    }

    private fun checkForPermission() {
        Dexter.withContext(requireActivity())
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        initLocationListener()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            }).check()
    }

    private fun initLocationListener() {
        // setuping locatiomanager to perfrom location related operations
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Requesting locationmanager for location updates
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        Log.d(TAG, "initLocationListener")
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 1, 1f, this
        )
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.requireActivity(),
            ViewModelFactory(
                WeatherNetworkDataProvider(RetrofitClient.WEATHER_SERVICE),
                this.requireActivity().application
            )
        ).get(MapViewModel::class.java)
        binding.vm = viewModel
        binding.executePendingBindings()
    }

    private fun setListener() {
        binding.btnSave.setOnClickListener(this)
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
                    binding.txtvwCurrentLocation.text = "Current Location - ${it.data?.name}"
                    binding.txtvwCurTemp.text = "Temperature - ${it.data?.main?.temp}"
                    binding.txtvwCurTempFeels.text = "Feels like - ${it.data?.main?.feels_like}"
                    binding.txtvwCurHumidity.text = "Humidity - ${it.data?.main?.humidity}"
                    binding.txtvwCurWeather.text = "Weather - ${it.data?.weather!![0].main}"

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                viewModel.saveLocation()
            }
        }
    }
}
