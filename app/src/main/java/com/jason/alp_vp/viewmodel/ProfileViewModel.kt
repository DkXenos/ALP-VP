package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import com.jason.alp_vp.model.User
import com.jason.alp_vp.repository.MockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel(private val repository: MockRepository = MockRepository()) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _portfolioItems = MutableStateFlow<List<String>>(emptyList())
    val portfolioItems: StateFlow<List<String>> = _portfolioItems

    init {
        loadUserData()
        loadPortfolio()
    }

    private fun loadUserData() {
        // In a real app, this would get the current user from repository
        _user.value = repository.currentUser.value
    }

    private fun loadPortfolio() {
        _portfolioItems.value = repository.getPortfolioItems()
    }
}

