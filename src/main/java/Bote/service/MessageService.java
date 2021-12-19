package Bote.service;

import Bote.model.Message;
import Bote.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void saveNewMessage(Message message) {

        messageRepository.save(message);
    }

    /* Alle Message von einem Freund holen(nach token ausgeben) fun. 100% */
    public List<Message> gemeisameMessage(String token){

        return messageRepository.findByMessagetoken(token);
    }

    /* Übeflüssig */
    public List<Message> getAllMessages() {

        return messageRepository.findAll();
    }
}
