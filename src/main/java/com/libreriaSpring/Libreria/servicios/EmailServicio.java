package com.libreriaSpring.Libreria.servicios;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender sender;

    private static final String SUBJECT = "Bienvenido a Liberia Letras!";

    public void enviarThread(String to) {
        new Thread(() -> {
            try {
                MimeMessage message = sender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(to);
                helper.setSubject(SUBJECT);
                helper.setText("<html><body> <h1>Bienvenido a Libreria Letras!</h1> <h3>El registro fue exitoso. Esperamos que disfrutes mucho nuestra aplicación!</h3> <br> <img src='cid:identifier1234'> </body></html>", true);
                FileSystemResource res = new FileSystemResource(new File("D:\\card.png"));
                helper.addInline("identifier1234", res);
                sender.send(message);
            } catch (MessagingException ex) {
                Logger.getLogger(EmailServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}