package je.raweeroj.a7minuteworkout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Update
    suspend fun update(historyEntity: HistoryEntity)

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)

    @Query("Select * from `history-table`")
    fun fetchAllHistory(): Flow<List<HistoryEntity>>

    @Query("Select * from `history-table` where id=:id")
    fun fetchHistoryById(id:Int): Flow<HistoryEntity>
}