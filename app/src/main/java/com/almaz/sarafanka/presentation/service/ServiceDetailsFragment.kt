package com.almaz.sarafanka.presentation.service

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.presentation.profile.ProfileViewModel
import com.almaz.sarafanka.utils.extensions.getDownloadablePhotoUrl
import com.almaz.sarafanka.utils.extensions.toGone
import com.almaz.sarafanka.utils.extensions.toInvisible
import com.almaz.sarafanka.utils.extensions.toVisible
import com.google.firebase.storage.FirebaseStorage
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
        arguments?.getParcelable<Service>("my_service")?.let { setUpServiceData(true, it) }
        arguments?.getParcelable<Service>("other_service")?.let { setUpServiceData(false, it) }
    }

    override fun onStart() {
        super.onStart()
        rootActivity.bottom_nav.toGone()
    }

    private fun setUpServiceData(isCurrentUserService: Boolean, service: Service) {
        btn_request_recommendation.setOnClickListener {
            if (isCurrentUserService) {
                requestRecommendation(service.owner_id)
            } else {
                giveFeedback()
            }
        }
        btn_share.setOnClickListener {
            shareService(service.owner_id)
        }
        if (isCurrentUserService) {
            btn_request_recommendation.text = resources.getText(R.string.request_recommendation)
        } else {
            btn_request_recommendation.text = resources.getText(R.string.give_feedback)

        }
        if (service.photo.isNullOrEmpty()) {
            card_no_service_photo.toVisible()
        } else {
            card_no_service_photo.toInvisible()
            imageListAdapter.submitList(service.photo.map {
                FirebaseStorage.getInstance().getDownloadablePhotoUrl(it)
            })
        }
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

    private fun requestRecommendation(ownerId: String) {
        val link = "http://sarafanka/user/${ownerId}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain";
            putExtra(
                Intent.EXTRA_TEXT,
                "Привет! Ты воспользовался(лась) моей услугой. Как тебе результат? Ты доволен(а)? " +
                        "Не мог бы ты оценить сервис и написать отзыв в приложении\n" +
                        "$link\n\n" +
                        "Чтобы открыть ссылку необходимо скачать приложение \"Sarafanka\"\n" +
                        "Переходи в Play Store: (будет ссылка)"
            )
        }
        try {
            startActivity(
                Intent.createChooser(
                    intent,
                    "Share"
                )
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Приложение \"Sarafanka\" не установлено",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun shareService(ownerId: String) {
        val link = "http://sarafanka/user/${ownerId}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain";
            putExtra(
                Intent.EXTRA_TEXT,
                "Я выложил новую услугу, она доступна по ссылке\n" +
                        "$link\n" +
                        "Посмотрии, возможно я смогу помочь тебе!\n\n" +
                        "Чтобы открыть ссылку необходимо скачать приложение \"Sarafanka\"\n" +
                        "Переходи в Play Store: (ссылка)"
            )
        }
        try {
            startActivity(
                Intent.createChooser(
                    intent,
                    "Share"
                )
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Приложение \"Sarafanka\" не установлено",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun giveFeedback() {
        rootActivity.navController.navigate(
            R.id.addReviewsFragment,
            bundleOf("service_id" to arguments?.getParcelable<Service>("other_service")?.id)
        )
    }
}
