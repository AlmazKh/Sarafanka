package com.almaz.sarafanka.data.repository

import com.almaz.sarafanka.core.interfaces.ServiceRepository
import com.almaz.sarafanka.core.interfaces.UserRepository
import com.almaz.sarafanka.core.model.Review
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.ServiceCategory
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.data.ServiceCategoriesHolder
import com.almaz.sarafanka.utils.Response
import com.almaz.sarafanka.utils.extensions.getDownloadablePhotoUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

// Service collection
private const val SERVICES = "services"
private const val SERVICE_ID = "service_id"
private const val SERVICE_CATEGORY_ID = "category_id"
private const val SERVICE_SUBCATEGORY = "subcategory"
private const val SERVICE_PHOTO = "photo"
private const val SERVICE_DESCRIPTION = "description"
private const val SERVICE_OWNER_ID = "service_owner_id"

// Review collection
private const val REVIEWS = "reviews"
private const val REVIEW_ID = "review_id"
private const val REVIEW_DESCRIPTION = "description"
private const val REVIEW_IS_RECOMMENDED = "is_recommended"
private const val REVIEW_PHOTO = "photo"
private const val REVIEW_SERVICE_PRICE = "service_price"
private const val REVIEW_SERVICE_ID = "service_id"
private const val REVIEW_WRITER_ID = "writer_id"

class ServiceRepositoryImpl(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val serviceCategoriesHolder: ServiceCategoriesHolder,
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ServiceRepository {
    override suspend fun getServicesByUserId(id: String): List<Service> {
        return try {
            val snapshot =
                db.collection(SERVICES)
                    .whereEqualTo(SERVICE_OWNER_ID, id)
                    .get()
                    .await()
            snapshot.map {
                mapDocumentToService(it)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getServices(): List<Service> {
        return try {
            val snapshot =
                db.collection(SERVICES)
                    .get()
                    .await()
            snapshot.map {
                mapDocumentToService(it)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getServiceCategories(): List<ServiceCategory> {
        return serviceCategoriesHolder.getCategories()
    }

    override suspend fun publishService(
        category: String,
        profession: String,
        description: String?
    ): Response<Boolean> {
        return try {
            val serviceMap = HashMap<String, Any?>()
            serviceMap[SERVICE_CATEGORY_ID] = serviceCategoriesHolder.getCategoryIdByName(category)
            serviceMap[SERVICE_SUBCATEGORY] = profession
            serviceMap[SERVICE_DESCRIPTION] = description
            serviceMap[SERVICE_OWNER_ID] = firebaseAuth.currentUser?.uid
            db.collection(SERVICES)
                .add(serviceMap)
                .await()
            Response.success(true)
        } catch (e: Exception) {
            Response.error(e)
        }
    }

    private suspend fun mapDocumentToService(documentSnapshot: QueryDocumentSnapshot): Service =
        Service(
            id = documentSnapshot.get(SERVICE_ID).toString(),
            category = serviceCategoriesHolder.getCategoryById(
                documentSnapshot.get(
                    SERVICE_CATEGORY_ID
                ).toString().toInt()
            )!!,
            subcategory = documentSnapshot.get(SERVICE_SUBCATEGORY).toString(),
            photo = (documentSnapshot.get(SERVICE_PHOTO) as List<*>?)?.map {
                storage.getDownloadablePhotoUrl(
                    it.toString()
                )
            },
            description = documentSnapshot.get(SERVICE_DESCRIPTION).toString(),
            reviews = getServiceReviews(documentSnapshot.get(SERVICE_ID).toString()),
            owner_id = documentSnapshot.get(SERVICE_OWNER_ID).toString()
        )

    private suspend fun getServiceReviews(serviceId: String): List<Review> {
        val snapshot = db.collection(REVIEWS)
            .whereEqualTo(REVIEW_SERVICE_ID, serviceId)
            .get()
            .await()
        return snapshot.map {
            val user = userRepository.getUserById(it.get(REVIEW_WRITER_ID).toString())
            mapDocumentToReview(it, user)
        }
    }

    private fun mapDocumentToReview(documentSnapshot: QueryDocumentSnapshot, user: User): Review =
        Review(
            id = documentSnapshot.get(REVIEW_ID).toString(),
            description = documentSnapshot.get(REVIEW_DESCRIPTION).toString(),
            isRecommended = documentSnapshot.get(REVIEW_IS_RECOMMENDED).toString().toBoolean(),
            inContacts = true, // TODO
            photo = (documentSnapshot.get(REVIEW_PHOTO) as List<*>?)?.map {
                storage.getDownloadablePhotoUrl(
                    it.toString()
                )
            },
            service_price = documentSnapshot.get(REVIEW_SERVICE_PRICE).toString().toInt(),
            serviceId = documentSnapshot.get(REVIEW_SERVICE_ID).toString(),
            writer = user
        )
}
