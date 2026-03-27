package com.lusancode.HotelBooking.notification;

import com.lusancode.HotelBooking.dtos.NotificationDto;
import com.lusancode.HotelBooking.entities.Notification;
import com.lusancode.HotelBooking.enums.NotificationType;
import com.lusancode.HotelBooking.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;



    @Override
    @Async
    public void sendEmail(NotificationDto notificationDto) {

        log.info("Sending Email ...");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notificationDto.getRecipient());
        simpleMailMessage.setSubject(notificationDto.getSubject());
        simpleMailMessage.setText(notificationDto.getBody());

        javaMailSender.send(simpleMailMessage);
        // Save to database
        Notification notificationToSave = Notification.builder()
                .recipient(notificationDto.getRecipient())
                .subject(notificationDto.getSubject())
                .body(notificationDto.getBody())
                .bookingReference(notificationDto.getBookingReference())
                .type(NotificationType.EMAIL)
                .build();
        notificationRepository.save(notificationToSave);

    }

    @Override
    public void sendSMS() {

    }

    @Override
    public void sendWhatsapp() {

    }
}
