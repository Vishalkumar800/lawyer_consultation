package com.rach.lawyerapp.ui.home.data.repoImp

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rach.lawyerapp.ui.home.data.model.LawyerDataBaseModel
import com.rach.lawyerapp.ui.home.data.model.LawyerModel
import com.rach.lawyerapp.ui.home.data.repo.LawyerRepository
import com.rach.lawyerapp.utils.DataBaseConstants
import com.rach.lawyerapp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LawyerRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore
) : LawyerRepository {

    private suspend fun fetchLawyers(query: Query): Response<List<LawyerModel>> {
        return try {
            val snapshot = query.get().await()
            val lawyers = snapshot.toObjects(LawyerDataBaseModel::class.java)
                .map { it.toDomain() }
            Response.Success(lawyers)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

    override fun getLawyers(): Flow<Response<List<LawyerModel>>> = flow {
        try {
            emit(Response.Loading())
            val query = firestore.collection(DataBaseConstants.LAWYERS_COLLECTION)
            emit(fetchLawyers(query))
        } catch (e: Exception) {
            emit(Response.Error(e))
        }
    }

    override fun searchLawyers(searchText: String): Flow<Response<List<LawyerModel>>> = flow {
        try {
            emit(Response.Loading())
            val query = firestore.collection(DataBaseConstants.LAWYERS_COLLECTION)
                .whereGreaterThanOrEqualTo(
                    DataBaseConstants.SEARCH_NAME,
                    searchText
                ) //  Agar query = "A" hai, to "A" se start hone wale aur uske baad aane wale saare words match honge.  //  "Aarav", "Bobby", "Charlie" match honge.
                .whereLessThanOrEqualTo(
                    DataBaseConstants.SEARCH_NAME,
                    searchText + "\uf8ff"
                )     //Yeh "A" se start hone wale saare words ko match karega: ✅ "Aarav", "Aditi", "Aakash" ❌ "Bobby", "Charlie" match nahi honge

            emit(fetchLawyers(query))

            /*
            Firestore me strings ka lexicographical (dictionary order) comparison hota hai.
            "\uf8ff" ek super high Unicode value hai, jo kisi bhi normal character se bada hota hai.
            Jab "A\uf8ff" likhte ho, to Firestore "A" se start hone wale sabhi values ko le lega lekin "B" ya uske baad wale ko nahi lega.
            */

        } catch (e: Exception) {
            emit(Response.Error(e))
        }

    }

    override fun filterLawyers(category: List<String>): Flow<Response<List<LawyerModel>>> = flow {
        try {

            emit(Response.Loading())
            val query = firestore.collection(DataBaseConstants.LAWYERS_COLLECTION)
                .whereArrayContainsAny(DataBaseConstants.CATEGORY,category)

            emit(fetchLawyers(query))

        }catch (e:Exception){
            emit(Response.Error(e))
        }
    }


}