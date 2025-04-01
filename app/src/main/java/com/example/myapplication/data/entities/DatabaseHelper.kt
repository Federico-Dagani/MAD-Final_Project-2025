import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "room_booking.db"
        private const val DATABASE_VERSION = 1

        // Room table
        const val TABLE_ROOM = "room"
        const val COLUMN_ROOM_ID = "id"
        const val COLUMN_ROOM_TYPE = "type"
        const val COLUMN_ROOM_TITLE = "title"

        // Event table
        const val TABLE_EVENT = "event"
        const val COLUMN_EVENT_ID = "id"
        const val COLUMN_EVENT_TITLE = "title"
        const val COLUMN_EVENT_START = "start_time"
        const val COLUMN_EVENT_END = "end_time"
        const val COLUMN_EVENT_ROOM_ID = "room_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Room table
        val createRoomTable = """
            CREATE TABLE $TABLE_ROOM (
                $COLUMN_ROOM_ID TEXT PRIMARY KEY,
                $COLUMN_ROOM_TYPE TEXT NOT NULL,
                $COLUMN_ROOM_TITLE TEXT
            )
        """.trimIndent()

        // Create Event table
        val createEventTable = """
            CREATE TABLE $TABLE_EVENT (
                $COLUMN_EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EVENT_TITLE TEXT NOT NULL,
                $COLUMN_EVENT_START INTEGER NOT NULL,
                $COLUMN_EVENT_END INTEGER NOT NULL,
                $COLUMN_EVENT_ROOM_ID TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_EVENT_ROOM_ID) REFERENCES $TABLE_ROOM($COLUMN_ROOM_ID),
                CHECK ($COLUMN_EVENT_END > $COLUMN_EVENT_START)
            )
        """.trimIndent()

        db.execSQL(createRoomTable)
        db.execSQL(createEventTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROOM")
        onCreate(db)
    }
}