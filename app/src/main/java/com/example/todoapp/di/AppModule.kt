package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.local.ToDoDatabase
import com.example.todoapp.data.repository.ToDoRepository
import com.example.todoapp.domain.usecases.AddToDoUseCase
import com.example.todoapp.domain.usecases.GetToDoUseCase
import com.example.todoapp.utils.Constants.DATABASE_NAME
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
            appContext,
            ToDoDatabase::class.java,
            DATABASE_NAME
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