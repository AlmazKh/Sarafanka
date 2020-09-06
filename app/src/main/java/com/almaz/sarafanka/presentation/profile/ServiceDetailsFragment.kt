package com.almaz.sarafanka.presentation.profile

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.extensions.toGone
import com.almaz.sarafanka.utils.extensions.toVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_service_details.*

class ServiceDetailsFragment : BaseFragment<ProfileViewModel>(ProfileViewModel::class.java) {

    private val imageListAdapter = ImageListAdapter(listOf())
    private val serviceReviewsAdapter = ServiceReviewsAdapter()

    override val layoutId: Int = R.layout.fragment_service_details

    override fun setupView() {
        btn_back.setOnClickListener {
            rootActivity.navController.navigateUp()
        }
        with(rv_service_image_list) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = imageListAdapter
        }
        with(rv_service_reviews) {
            layoutManager = LinearLayoutManager(context)
            adapter = serviceReviewsAdapter
        }
        arguments?.getParcelable<Service>("service")?.let { setUpServiceData(it) }
    }

    override fun onStart() {
        super.onStart()
        rootActivity.bottom_nav.toGone()
    }

    override fun onStop() {
        super.onStop()
        rootActivity.bottom_nav.toVisible()
    }

    private fun setUpServiceData(service: Service) {
        service.photo?.let { imageListAdapter.submitList(it) }
        if (service.reviews.isNullOrEmpty()) {
            tv_reviews_title.toGone()
            showSnackbar("У данной услуги пока нет примеров")
        } else {
            serviceReviewsAdapter.submitList(service.reviews)
            tv_reviews_title.toVisible()
        }
        tv_service_details_title.text = service.category.name
        tv_service_description.text = service.description
    }
}
