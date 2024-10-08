package com.main.comicapp.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.main.comicapp.models.ChatRoom;

public interface ChatRoomRepository {
    Query getAllChatRooms();
    Task<Void> createChatRoom(ChatRoom chatRoom);
}
