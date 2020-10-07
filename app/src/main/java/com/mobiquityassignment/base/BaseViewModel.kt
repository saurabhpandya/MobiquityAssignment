package com.mobiquityassignment.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = this::class.java.canonicalName
}