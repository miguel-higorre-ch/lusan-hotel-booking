package com.lusancode.HotelBooking.notification;

import com.lusancode.HotelBooking.dtos.NotificationDto;

public interface NotificationService {

    void sendEmail(NotificationDto notificationDto);

    void sendSMS();

    void sendWhatsapp();
}
