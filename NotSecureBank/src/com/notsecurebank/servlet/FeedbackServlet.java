package com.notsecurebank.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.notsecurebank.util.OperationsUtil;
import com.notsecurebank.util.ServletUtil;

public class FeedbackServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LogManager.getLogger(FeedbackServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doPost");

        // the feedback is not actually submitted
        if (request.getParameter("comments") == null) {
            LOG.error("Null comments.");
            response.sendRedirect("index.jsp");
            return;
        }

        String name = ServletUtil.sanitizeWeb(request.getParameter("name"));
        if (name != null) {
            request.setAttribute("message_feedback", name);
            String email = ServletUtil.sanitizeWeb(request.getParameter("email_addr"));
            String subject = ServletUtil.sanitizeWeb(request.getParameter("subject"));
            String comments = ServletUtil.sanitizeWeb(request.getParameter("comments"));
            // store feedback in the DB - display their feedback once submitted

            String feedbackId = OperationsUtil.sendFeedback(name, email, subject, comments);
            if (feedbackId != null) {
                LOG.info("feedbackId = '" + feedbackId + "'.");
                request.setAttribute("feedback_id", feedbackId);
            }

        } else {
            LOG.error("Null name.");
            request.removeAttribute("name");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/feedbacksuccess.jsp");
        dispatcher.forward(request, response);
    }
}
