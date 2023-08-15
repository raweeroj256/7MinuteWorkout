package je.raweeroj.a7minuteworkout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//creating a Data Model Class
@Entity(tableName = "history-table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    var date : String,
    @ColumnInfo(name = "time-finish")
    var timeFinish: String=""


    )
