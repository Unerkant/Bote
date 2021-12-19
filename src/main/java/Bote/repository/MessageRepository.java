package Bote.repository;

import Bote.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Richter on Sat 24/07/2021
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findAll();
    List<Message> findByMessagetoken(String messagetoken);

}
