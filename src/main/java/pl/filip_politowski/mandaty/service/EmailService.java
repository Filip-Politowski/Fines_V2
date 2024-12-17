package pl.filip_politowski.mandaty.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.filip_politowski.mandaty.dto.request.FineRequest;
import pl.filip_politowski.mandaty.model.Employee;
import pl.filip_politowski.mandaty.model.Fine;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service

public class EmailService {

    private final JavaMailSender javaMailSender;
    private final FineService fineService;
    private final EmployeeService employeeService;

    public EmailService(JavaMailSender javaMailSender, FineService fineService, EmployeeService employeeService) {
        this.javaMailSender = javaMailSender;
        this.fineService = fineService;
        this.employeeService = employeeService;
    }

    public void sendEmail(String to, String subject, String signature) {

        Fine fine = fineService.findFineBySignature(signature);
        String mandateLink = "http://localhost:8080/fine-view/" + fine.getId();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fine-notifications@police.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(
                "Dzień dobry,\n" +
                        "\nPragniemy poinformować, że właśnie został dodany do naszej aplikacji mandat o numerze " + fine.getSignature() + " + na kwotę " + fine.getAmount() + ", " + fine.getCurrency() + "." +
                        " \nProsimy o dokonanie wpłaty do dnia " + fine.getPaymentDeadline() + ".\n" +
                        "Szczegółowe informacje dotyczące mandatu znajdziesz pod linkiem: " + mandateLink +
                        "\n\nWiadomość wysłana automatycznie. Nie odpowiadaj na nią."

        );

        javaMailSender.send(message);
    }
}
