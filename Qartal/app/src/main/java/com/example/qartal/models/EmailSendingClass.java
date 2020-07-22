package com.example.qartal.models;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSendingClass {
    public  void sendMail(String receipt , String Name) throws MessagingException {
        System.out.println("Preparing to send Email");



        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        final String MyEmil = "shtiatqabas@gmail.com";
        final String Password = "qabas13021996q";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MyEmil,Password);
            }
        });

        String StrName = Name;

        Message message = MyMessage_2(session,MyEmil,receipt , StrName);


        Transport.send(message);


        System.out.println("Email is Sent successful");



    }

    public  void sendMailToMe(String receipt , String Name , String Phone , String C_Email,String Pass) throws MessagingException {
        System.out.println("Preparing to send Email");



        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        final String MyEmil = "shtiatqabas@gmail.com";
        final String Password = "qabas13021996q";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MyEmil,Password);
            }
        });

        String StrName = Name;

        Message message = MyMessage_1(session,MyEmil,receipt , StrName,Phone,C_Email,Pass);


        Transport.send(message);


        System.out.println("Email is Sent successful");



    }

    private static Message MyMessage_1(Session session, String myEmail, String recepint , String Name , String Phone , String C_Emial , String Password)  {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(recepint));
            message.setSubject("Email Verification ");
            message.setText("Hi Admin. \n ,Your Client Information Down Below : \n" +
                    "Email : " + C_Emial  + " .\n" +
                    "Name : " + Name  + " .\n" +
                    "Password : " + Password  + " .\n" +
                    "Phone : " + Phone + ".\n" +
                    "Thank You " +
                    "Watch Family." );

            return message;

        }catch (Exception ex){
            Logger.getLogger(EmailSendingClass.class.getName()).log(Level.SEVERE,null,ex);
        }
        return null;
    }

    private static Message MyMessage_2(Session session, String myEmil, String recepint , String Name)  {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmil));
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(recepint));
            message.setSubject("Email Verification ");
            message.setText("Hi "+Name+". \nWe are Studding You Request  , \n As soon as possible we will connect with you ." +
                    "\n Thank you for using Watch app. \n " +
                    "Watch family." );

            return message;

        }catch (Exception ex){
            Logger.getLogger(EmailSendingClass.class.getName()).log(Level.SEVERE,null,ex);
        }
        return null;
    }


}

