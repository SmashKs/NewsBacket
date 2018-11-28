package com.no1.taiwan.newsbasket.components.recyclerview.helpers

interface AdapterItemTouchHelper {
    fun onItemSwiped(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}
