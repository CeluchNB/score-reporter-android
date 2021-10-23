package com.noah.scorereporter.pages.season

import androidx.lifecycle.ViewModel
import com.noah.scorereporter.pages.PageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeasonViewModel @Inject constructor(
    private val repository: PageRepository
): ViewModel() {


}