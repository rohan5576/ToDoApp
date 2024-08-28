package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.core.ui.theme.Constants.DATABASE_NAME
import com.example.core.ui.theme.ResourceProvider
import com.example.core.ui.theme.ResourceProviderImpl
import com.example.data.local.ToDoDatabase
import com.example.data.repository.ToDoRepository
import com.example.domain.usecases.AddToDoUseCase
import com.example.domain.usecases.GetToDoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ToDoDatabase {
        return Room.databaseBuilder(
            appContext, ToDoDatabase::class.java, DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(database: ToDoDatabase): ToDoRepository {
        return ToDoRepository(database.todoDao())
    }

    @Provides
    @Singleton
    fun provideAddTodoUseCase(repository: ToDoRepository): AddToDoUseCase {
        return AddToDoUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTodosUseCase(repository: ToDoRepository): GetToDoUseCase {
        return GetToDoUseCase(repository)
    }

    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }
}
