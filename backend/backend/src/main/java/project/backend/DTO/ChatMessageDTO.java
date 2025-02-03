package project.backend.DTO;


import lombok.*;

import java.util.List;

@Getter
@Setter
public class ChatMessageDTO<T>
{
    // 메시지  타입 : 입장, 채팅, 퇴장
    public enum MessageType{
        JOIN, TALK, LEAVE
    }

    private MessageType messageType; // 메시지 타입
    private Long chatRoomId; // 방번호
    private String message; // 메시지
    private T object;//여러가지 타입의 데이터를 모두 넘겨받음

}
