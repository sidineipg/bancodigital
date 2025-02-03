package br.com.spg.bancodigital.services;

import br.com.spg.bancodigital.domain.user.User;
import br.com.spg.bancodigital.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    //url de notify do projeto desafio do pic pay
    //https://github.com/PicPay/picpay-desafio-backend
    public void sendNotification(User user, String message) throws Exception {
        String email = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequest, String.class);

        if (notificationResponse.getStatusCode() != HttpStatus.OK){
            System.out.println("Serviço de notificação falhou");
            throw new Exception("Serviço de notificação falhou");
        }

    }
}
