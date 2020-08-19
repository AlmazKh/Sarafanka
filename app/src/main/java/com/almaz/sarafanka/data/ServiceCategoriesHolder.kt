package com.almaz.sarafanka.data

import com.almaz.sarafanka.core.model.ServiceCategory

class ServiceCategoriesHolder {
    private val categories: MutableList<ServiceCategory> = mutableListOf()

    init {
        categories.add(ServiceCategory(1, "Здоровье"))
        categories.add(ServiceCategory(2, "Закон, сделки, финансы"))
        categories.add(ServiceCategory(3, "Быт"))
        categories.add(ServiceCategory(4, "Авто"))
        categories.add(ServiceCategory(5, "Бизнес"))
        categories.add(ServiceCategory(6, "Авто"))
    }

    fun getCategories(): List<ServiceCategory> = categories
}