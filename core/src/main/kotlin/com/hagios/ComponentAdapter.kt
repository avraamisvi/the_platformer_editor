package com.hagios

import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable

class ComponentAdapter(array: ArrayList<Component>) : ArrayListAdapter<Component, VisTable>(array) {
    override fun createView(item: Component?): VisTable? {
        val label = VisLabel(item?.label)
        val table = VisTable()
        table.left()
        table.add<VisLabel?>(label)
        return table
    }
}
