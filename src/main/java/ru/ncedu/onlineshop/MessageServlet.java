package ru.ncedu.onlineshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Никита on 08.09.14.
 */

//@WebServlet(
//            name="MessageServlet",
//            displayName="Message Servlet",
//            urlPatterns = {"/messages"},
//            loadOnStartup = 1)
public class MessageServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MessageServlet.class);

    ApplicationContext appContext;
    CustomMessageSender oms;

    /**
     * A convenience method which can be overridden so that there's no need
     * to call <code>super.init(config)</code>.
     * <p/>
     * <p>Instead of overriding {@link #init(javax.servlet.ServletConfig)}, simply override
     * this method and it will be called by
     * <code>GenericServlet.init(ServletConfig config)</code>.
     * The <code>ServletConfig</code> object can still be retrieved via {@link
     * #getServletConfig}.
     *
     * @throws javax.servlet.ServletException if an exception occurs that
     *                                        interrupts the servlet's
     *                                        normal operation
     */
    @Override
    public void init() throws ServletException {
        appContext = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/config/spring-config.xml");
        oms = (CustomMessageSender)appContext.getBean("messageSender");
    }

    /**
     * Called by the server (via the <code>service</code> method) to
     * allow a servlet to handle a GET request.
     * <p/>
     * <p>Overriding this method to support a GET request also
     * automatically supports an HTTP HEAD request. A HEAD
     * request is a GET request that returns no body in the
     * response, only the request header fields.
     * <p/>
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or
     * output stream object, and finally, write the response data.
     * It's best to include content type and encoding. When using
     * a <code>PrintWriter</code> object to return the response,
     * set the content type before accessing the
     * <code>PrintWriter</code> object.
     * <p/>
     * <p>The servlet container must write the headers before
     * committing the response, because in HTTP the headers must be sent
     * before the response body.
     * <p/>
     * <p>Where possible, set the Content-Length header (with the
     * {@link javax.servlet.ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * <p/>
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * <p/>
     * <p>The GET method should be safe, that is, without
     * any side effects for which users are held responsible.
     * For example, most form queries have no side effects.
     * If a client request is intended to change stored data,
     * the request should use some other HTTP method.
     * <p/>
     * <p>The GET method should also be idempotent, meaning
     * that it can be safely repeated. Sometimes making a
     * method safe also makes it idempotent. For example,
     * repeating queries is both safe and idempotent, but
     * buying a product online or modifying data is neither
     * safe nor idempotent.
     * <p/>
     * <p>If the request is incorrectly formatted, <code>doGet</code>
     * returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws java.io.IOException            if an input or output error is
     *                                        detected when the servlet handles
     *                                        the GET request
     * @throws javax.servlet.ServletException if the request for the GET
     *                                        could not be handled
     * @see javax.servlet.ServletResponse#setContentType
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Start.");
        logger.debug("Response: " + resp.toString());

        showHtml(resp);

        logger.info("Finish.");
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a POST request.
     * <p/>
     * The HTTP POST method allows the client to send
     * data of unlimited length to the Web server a single time
     * and is useful when posting information such as
     * credit card numbers.
     * <p/>
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or output
     * stream object, and finally, write the response data. It's best
     * to include content type and encoding. When using a
     * <code>PrintWriter</code> object to return the response, set the
     * content type before accessing the <code>PrintWriter</code> object.
     * <p/>
     * <p>The servlet container must write the headers before committing the
     * response, because in HTTP the headers must be sent before the
     * response body.
     * <p/>
     * <p>Where possible, set the Content-Length header (with the
     * {@link javax.servlet.ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * <p/>
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * <p/>
     * <p>This method does not need to be either safe or idempotent.
     * Operations requested through POST can have side effects for
     * which the user can be held accountable, for example,
     * updating stored data or buying items online.
     * <p/>
     * <p>If the HTTP POST request is incorrectly formatted,
     * <code>doPost</code> returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws java.io.IOException            if an input or output error is
     *                                        detected when the servlet handles
     *                                        the request
     * @throws javax.servlet.ServletException if the request for the POST
     *                                        could not be handled
     * @see javax.servlet.ServletOutputStream
     * @see javax.servlet.ServletResponse#setContentType
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Start.");
        logger.debug("Response: " + resp.toString());

        showHtml(resp);

        String msg = req.getParameter("editMsg");
//        try {
//            oms.sendMessage(msg);
//        } catch (JMSException e) {
//            logger.error("Error sending message: " + msg);
//        }

        logger.info("Finish.");
    }

    private void showHtml(HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Start.");
        logger.debug("Response: " + resp.toString());

        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");

        writer.println(
                "<html>\n" +
                "    <head>\n" +
                "        <meta charset=\"utf-8\">\n" +
                "        <title>Read msg</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <form method=\"POST\" action=\"../messages\">" +
                "            <input type=\"edit\" name=\"editMsg\" value=\"\">" +
                "            <input type=\"submit\" value=\"Send\">" +
                "        </form>" +
                "        <form method=\"GET\" action=\"../messages\">" +
                "            <input type=\"submit\" value=\"Refresh\">" +
                "        </form>"
        );

        writeMessagesToHtml(writer);

        writer.println(
                "    </body>\n" +
                "</html>"
        );
        logger.info("Finish.");
    }

    private void writeMessagesToHtml(PrintWriter writer) {
        logger.info("Start.");
        CustomMessageListener oml = (CustomMessageListener)appContext.getBean("ordersMessageListener");

        writer.println(
                "        <table border=\"1\">\n" +
                "            <caption>Messages</caption>\n" +
                "            <tr>\n" +
                "                <th>N</th>\n" +
                "                <th>Message</th>\n" +
                "            </tr>\n"
        );

//        logger.info("Showing messages from the listener. Count: " + oml.getMessages().size());
//        logger.debug("Showing messages from the listener: " + oml.getMessages());

        int counter = 0;
//        for (Message msg: oml.getMessages()) {
//
//            ++counter;
//            TextMessage msgTxt =(TextMessage)msg;
//            try {
//                writer.println(
//                "            <tr>\n" +
//                "                <td>" + counter + "</td>\n" +
//                "                <td>" + msgTxt.getText() +"</td>\n" +
//                "            </tr>\n"
//                );
//            } catch (JMSException e) {
//                logger.error("Can't take text from the message " + counter + ": " + msg, e);
//            }
//            logger.info("Message " +  counter + " has been written.");
//        }
        writer.println(
                "        </table>"
        );
        logger.info("Finish.");
    }
}
