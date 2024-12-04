package com.seiberl.protrackreader.persistance

import android.content.Context
import androidx.room.Room
import com.seiberl.protrackreader.persistance.repository.JumpDetailRepository
import com.seiberl.protrackreader.persistance.repository.JumpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private lateinit var database: AppDatabase

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "ProtrackDatabase"
        ).build()

        return database
    }

    @Provides
    fun provideJumpRepository(database: AppDatabase, @ApplicationContext context: Context) =
        JumpRepository(database.jumpDao, JumpFileManager(context))

    @Provides
    fun provideJumpDetailRepository(database: AppDatabase) = JumpDetailRepository(database.jumpDao)

}