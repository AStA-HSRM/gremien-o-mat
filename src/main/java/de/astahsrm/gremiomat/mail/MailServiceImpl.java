package de.astahsrm.gremiomat.mail;

import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender emailSender;

    // read out mail username from application.properties
    @Value("${mail.username}")
    private String fromEmail;

    @Override
    public void sendWelcomeMailToCandidate(Candidate candidate, String password) {
        /*
        @Autowired
        public SimpleMailMessage template;

        String text = String.format(template.getText(), templateArgs);  
        sendSimpleMessage(to, subject, text);
        */
        
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("[email protected]");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
}
