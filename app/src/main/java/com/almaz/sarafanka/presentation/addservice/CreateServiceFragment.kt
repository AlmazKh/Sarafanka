package com.almaz.sarafanka.presentation.addservice

import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.ServiceCategory
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.InfoPanelManager
import com.almaz.sarafanka.utils.extensions.hideKeyboard
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.extensions.toGone
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_service.*

class CreateServiceFragment :
    BaseFragment<CreateServiceViewModel>(CreateServiceViewModel::class.java) {
    override val layoutId = R.layout.fragment_create_service

    override fun setupView() {
        btn_back.setOnClickListener { rootActivity.navController.navigateUp() }
        btn_gallery.setOnClickListener {

        }
        btn_publish.setOnClickListener {
            if (checkFields()) {
                viewModel.publishService(
                    category = atv_category.text.toString(),
                    profession = atv_profession.text.toString(),
                    description = et_description.text.toString()
                )
            } else {
                InfoPanelManager.errorMainState.postValue("Необходимо заполнить все поля")
            }
        }
        atv_category.showSoftInputOnFocus = false
        atv_profession.showSoftInputOnFocus = false
        et_description.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) v.hideKeyboard()
        }
    }

    override fun subscribe(viewModel: CreateServiceViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.categoriesLiveData, ::bindServiceCategories)
        observe(viewModel.servicePublishedLiveData, ::bindPublished)
    }

    override fun onStart() {
        super.onStart()
        rootActivity.bottom_nav.toGone()
    }

    private fun bindServiceCategories(categories: List<ServiceCategory>) {
        val adapterCategory = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            categories.map { it.name })
        atv_category?.setAdapter(adapterCategory)
        atv_category.setOnItemClickListener { parent, view, position, id ->
            til_category.isErrorEnabled = false
            atv_profession.text.clear()
            val adapterProfession = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                categories[position].professions
            )
            atv_profession?.setAdapter(adapterProfession)
        }
        atv_profession.setOnItemClickListener { _, _, _, _ ->
            til_profession.isErrorEnabled = false
        }
    }

    private fun bindPublished(status: Boolean) {
        if (status) {
            rootActivity.navController.navigateUp()
            viewModel.servicePublishedLiveData.postValue(false)
        }
    }

    private fun checkFields(): Boolean {
        if (atv_category.text.isBlank()) {
            til_category.error = "Поле не может быть пустым"
            return false
        }
        if (atv_profession.text.isBlank()) {
            til_profession.error = "Поле не может быть пустым"
            return false
        }
        return true
    }

}