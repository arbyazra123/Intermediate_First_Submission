package com.kmm.intermediatefirstsubmission

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kmm.intermediatefirstsubmission.data.auth.repository.AuthRepository
import com.kmm.intermediatefirstsubmission.data.auth.viewmodel.AuthViewModel
import com.kmm.intermediatefirstsubmission.data.auth.viewmodel.AuthViewModelImpl
import com.kmm.intermediatefirstsubmission.data.auth.viewmodel.SessionViewModel
import com.kmm.intermediatefirstsubmission.data.core.local_data.SessionPreference
import com.kmm.intermediatefirstsubmission.data.network.ApiConfig
import com.kmm.intermediatefirstsubmission.data.story.repository.StoryRemoteRepository
import com.kmm.intermediatefirstsubmission.data.story.view_model.StoryViewModel
import com.kmm.intermediatefirstsubmission.data.story.view_model.StoryViewModelImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class App : Application() {
    private val viewModelModule = module {
        viewModel<AuthViewModel> {
            AuthViewModelImpl(get())
        }
        viewModel<StoryViewModel> {
            StoryViewModelImpl(get())
        }
        viewModel {
            SessionViewModel(get())
        }
    }
    private val repositoryModule = module {
        single { AuthRepository(get()) }
        single { StoryRemoteRepository(get()) }
    }
    private val apiModule = module {
        single { ApiConfig.getService(get()) }
    }
    private val preferenceModule = module {

        single { SessionPreference.getInstance(dataStore) }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
//            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(apiModule, repositoryModule, viewModelModule, preferenceModule))
        }
    }
}