package com.jason.alp_vp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jason.alp_vp.model.ForumPost
import com.jason.alp_vp.repository.MockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForumViewModel(private val repository: MockRepository = MockRepository()) : ViewModel() {

    private val _posts = MutableStateFlow<List<ForumPost>>(emptyList())
    val posts: StateFlow<List<ForumPost>> = _posts

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            repository.forumPosts.collect { postList ->
                _posts.value = postList
            }
        }
    }
}

