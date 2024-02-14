package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.domain.Chat;
import gdsc.sc8.LIFTY.domain.Message;
import gdsc.sc8.LIFTY.enums.Sender;
import gdsc.sc8.LIFTY.infrastructure.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;


    public void saveMessage(Sender sender, String content, Chat chat){
        Message message = Message.builder()
                .chat(chat)
                .content(content)
                .createdAt(LocalDateTime.now())
                .sender(sender)
                .build();
        messageRepository.save(message);
    }
}
