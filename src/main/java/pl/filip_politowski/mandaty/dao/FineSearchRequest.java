package pl.filip_politowski.mandaty.dao;

import lombok.Getter;
import lombok.Setter;
import pl.filip_politowski.mandaty.model.Currency;
import pl.filip_politowski.mandaty.model.FineStatus;
import pl.filip_politowski.mandaty.model.ViolationReason;

import java.time.LocalDate;

@Getter
@Setter
public class FineSearchRequest {
    String firstName;
    String lastName;
    FineStatus status;
    Currency currency;
    String signature;
    String phoneNumber;
    ViolationReason violationReason;
    LocalDate violationDate;
    LocalDate paymentDeadline;
    String companyName;
    FineStatus fineStatus;



}
