// AppDatabase.kt
package com.example.madcamp1stweek

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(entities = [User::class], version = 2) // 버전 증가
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 마이그레이션 정의
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 새로운 컬럼 추가
                database.execSQL("ALTER TABLE users ADD COLUMN loginCount INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "madcamp_database"
                )
                    .addMigrations(MIGRATION_1_2) // 마이그레이션 추가
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
