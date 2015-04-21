package ru.ncedu.onlineshop;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.ncedu.onlineshop.entity.users.AuditItem;

import javax.jms.JMSException;
import javax.jms.Queue;

public class CustomMessageSender {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomMessageSender.class);

    private JmsTemplate jmsTemplate;
    private Queue ordersQueue;

    /**
     * Sends message using JMS Template.
     *
     * @param auditItem is the message
     * @throws JMSException the jMS exception
     */
    @Transactional
    public void sendMessage(AuditItem auditItem) throws JMSException
    {
        logger.info("Start. Sending auditItem: " + auditItem + ". Queue[" + ordersQueue.toString() + "]");

        org.apache.activemq.Message message = new ActiveMQMapMessage();
        message.setObjectProperty("auditItem", auditItem);

        jmsTemplate.convertAndSend(ordersQueue, auditItem);
        logger.info("Finish.");
    }

    public void setJmsTemplate(JmsTemplate template)
    {
        this.jmsTemplate = template;
    }

    public void setOrdersQueue(Queue queue)
    {
        this.ordersQueue = queue;
    }
}