package com.af.springserver.service;

import com.af.springserver.model.Incident;
import com.af.springserver.model.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendPushNotification(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Wysłano powiadomienie: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Błąd przy wysyłaniu powiadomienia: " + e.getMessage());
        }
    }

    public void sendIncidentNotification(User caregiver, Incident incident) {
        if (caregiver.getTokenFCM() != null) {
            String title = "Nowy incydent";
            String body = "Użytkownik " + incident.getUser().getName() + " zgłosił incydent.";
            sendPushNotification(caregiver.getTokenFCM(), title, body);
        }
    }
}
