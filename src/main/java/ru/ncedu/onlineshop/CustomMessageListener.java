package ru.ncedu.onlineshop;

/**
 * Created by Никита on 09.09.14.
 */

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ncedu.onlineshop.entity.users.AuditItem;
import ru.ncedu.onlineshop.service.dao.users.AuditItemDAO;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
* Class handles incoming messages
*
* @see //PointOfIssueMessageEvent
*/
public class CustomMessageListener implements MessageListener {
    @Autowired
    AuditItemDAO auditItemDAO;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomMessageListener.class);

//    private static List<Message> messages = new ArrayList<Message>();
//
//    public List<Message> getMessages() {
//        return messages;
//    }

    /**
     * Method implements JMS onMessage and acts as the entry
     * point for messages consumed by Springs DefaultMessageListenerContainer.
     * When DefaultMessageListenerContainer picks a message from the queue it
     * invokes this method with the message payload.
     */
    public void onMessage(Message message)
    {
        logger.info("Start. Receiving message: " + message);
        //logger.debug("Start. Incoming message: " + message + ". Messages list: " + messages);

        //messages.add(message);

        try {
            auditItemDAO.save((AuditItem)message.getObjectProperty("auditItem"));
        } catch (JMSException e) {
            logger.error("Error until receiving the message.", e);
        }

        logger.info("Finish");
        logger.debug("Finish. Message " + message + " had been received");
    }
}
