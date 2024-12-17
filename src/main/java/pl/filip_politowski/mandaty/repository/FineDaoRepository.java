package pl.filip_politowski.mandaty.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.filip_politowski.mandaty.dao.FineSearchRequest;
import pl.filip_politowski.mandaty.dto.response.FineResponse;
import pl.filip_politowski.mandaty.model.Employee;
import pl.filip_politowski.mandaty.model.Fine;

import java.util.ArrayList;
import java.util.List;

@Repository

public class FineDaoRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public FineDaoRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public List<Fine> findAllByCriteriaQuery(FineSearchRequest fineSearchRequest) {
        CriteriaQuery<Fine> criteriaQuery = criteriaBuilder.createQuery(Fine.class);
        List<Predicate> predicates = new ArrayList<>();

        Root<Fine> root = criteriaQuery.from(Fine.class);
        Join<Fine, Employee> employeeJoin = root.join("employee");
        if (fineSearchRequest.getFirstName() != null || fineSearchRequest.getLastName() != null || fineSearchRequest.getPhoneNumber() != null || fineSearchRequest.getSignature() != null) {
            if (fineSearchRequest.getFirstName() != null) {
                Predicate firstNamePredicate = criteriaBuilder.like(
                        employeeJoin.get("firstName"),
                        "%" + fineSearchRequest.getFirstName() + "%"
                );
                predicates.add(firstNamePredicate);
            }
            if (fineSearchRequest.getLastName() != null) {
                Predicate lastNamePredicate = criteriaBuilder.like(employeeJoin.get("lastName"), "%" + fineSearchRequest.getLastName() + "%");
                predicates.add(lastNamePredicate);
            }
            if (fineSearchRequest.getPhoneNumber() != null) {
                Predicate phoneNumberPredicate = criteriaBuilder.like(employeeJoin.get("phoneNumber"), "%" + fineSearchRequest.getPhoneNumber() + "%");
                predicates.add(phoneNumberPredicate);
            }
            if (fineSearchRequest.getSignature() != null) {
                Predicate signaturePredicate = criteriaBuilder.like(root.get("signature"), "%" + fineSearchRequest.getSignature() + "%");

                predicates.add(signaturePredicate);
            }


            if (fineSearchRequest.getCurrency() != null) {
                Predicate currencyPredicate = criteriaBuilder.equal(root.get("currency"), fineSearchRequest.getCurrency());
                predicates.add(currencyPredicate);
            }

        }


        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Fine> typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }


}
