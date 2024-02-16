package gdsc.sc8.LIFTY.service;

import gdsc.sc8.LIFTY.domain.Chat;
import gdsc.sc8.LIFTY.domain.Message;
import gdsc.sc8.LIFTY.enums.Sender;
import gdsc.sc8.LIFTY.infrastructure.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Message> makeContents(Chat chat){
        LocalDateTime now = LocalDateTime.now();
        List<Message> messages = new ArrayList<>();
        List<Message> message = new ArrayList<>();
        Sender prevSender = Sender.MODEL;

        List<Message> candidates = messageRepository.findByChatAndCreatedAtBetween(chat,now.minusMinutes(15),now);

        // ensure that multiturn requests
        for(Message candidate:candidates){
            if (candidate.getSender()==Sender.USER){
                message.add(candidate);
                prevSender=Sender.USER;
            } else if (prevSender==Sender.USER && candidate.getSender()==Sender.MODEL) {
                prevSender = Sender.MODEL;
                message.add(candidate);
                messages.addAll(message);
                message.clear();
            } else break;
        }
        messages.add(candidates.get(candidates.size()-1));


        return messages;
    }
}
