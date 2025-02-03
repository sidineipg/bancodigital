package br.com.spg.bancodigital.services;

import br.com.spg.bancodigital.domain.transaction.Transaction;
import br.com.spg.bancodigital.domain.user.User;
import br.com.spg.bancodigital.dtos.NotificationDTO;
import br.com.spg.bancodigital.dtos.TransactionDTO;
import br.com.spg.bancodigital.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        //Validador retirado do desafio do pic pay
        //https://github.com/PicPay/picpay-desafio-backend
        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if (!isAuthorized){
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        //Serviço fora do ar em 02/02/2025
        try{
            this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
            this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");
        }catch (Exception ex) {
            System.out.println("Erro ao enviar notificação");
        }


        return newTransaction;

    }

    public boolean authorizeTransaction(User user, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK){
            String message = authorizationResponse.getBody().get("status").toString();
            return (message.equalsIgnoreCase("success"));
        }else{
            return false;
        }

    }


}
