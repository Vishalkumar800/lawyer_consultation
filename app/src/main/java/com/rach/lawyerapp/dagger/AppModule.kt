package com.rach.lawyerapp.dagger

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.rach.lawyerapp.ui.login.data.repo.AuthRepository
import com.rach.lawyerapp.ui.login.data.repo.GoogleAuthClient
import com.rach.lawyerapp.ui.login.data.repoImp.AuthRepositoryImp
import com.rach.lawyerapp.ui.login.data.repoImp.GoogleAuthClientImp
import com.rach.lawyerapp.utils.K
import com.razorpay.Checkout
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Authentication

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDataBase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideRazorPayCheckOut(@ApplicationContext context: Context): Checkout {
        val checkout = Checkout()
        checkout.setKeyID(K.RAZORPAY_ID)
        return checkout
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImp(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideOneTapSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthClient(oneTapClient: SignInClient): GoogleAuthClient {
        return GoogleAuthClientImp(oneTapClient)
    }


    /*

    {
      "rules": {
        "wallets": {
          "$uid": {
            ".read": "$uid === auth.uid",
            ".write": "$uid === auth.uid"
          }
        }
      }
    }

     */


}