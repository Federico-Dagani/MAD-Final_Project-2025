//import android.content.ContentValues
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//
//class RoomBookingDao(private val db: SQLiteDatabase) {
//
//    // Room operations
//    fun insertRoom(room: Room): Long {
//        val values = ContentValues().apply {
//            put(DatabaseHelper.COLUMN_ROOM_ID, room.id)
//            put(DatabaseHelper.COLUMN_ROOM_TYPE, room.type.name)
//            put(DatabaseHelper.COLUMN_ROOM_TITLE, room.title)
//        }
//        return db.insert(DatabaseHelper.TABLE_ROOM, null, values)
//    }
//
//    fun getRoomById(id: String): Room? {
//        val cursor = db.query(
//            DatabaseHelper.TABLE_ROOM,
//            null,
//            "${DatabaseHelper.COLUMN_ROOM_ID} = ?",
//            arrayOf(id),
//            null, null, null
//        )
//        return cursor.use {
//            if (it.moveToFirst()) cursorToRoom(it) else null
//        }
//    }
//
//    private fun cursorToRoom(cursor: Cursor): Room {
//        return Room(
//            id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_ID)),
//            type = RoomType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_TYPE))),
//            title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_TITLE))
//        )
//    }
//
//    // Event operations
//    fun insertEvent(event: Event): Long {
//        val values = ContentValues().apply {
//            put(DatabaseHelper.COLUMN_EVENT_TITLE, event.title)
//            put(DatabaseHelper.COLUMN_EVENT_START, event.startTime)
//            put(DatabaseHelper.COLUMN_EVENT_END, event.endTime)
//            put(DatabaseHelper.COLUMN_EVENT_ROOM_ID, event.roomId)
//        }
//        return db.insert(DatabaseHelper.TABLE_EVENT, null, values)
//    }
//
//    // Add more CRUD operations as needed...
//}