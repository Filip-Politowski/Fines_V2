package pl.filip_politowski.mandaty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.filip_politowski.mandaty.model.Currency;
import pl.filip_politowski.mandaty.model.Fine;
import pl.filip_politowski.mandaty.model.FineStatus;
import pl.filip_politowski.mandaty.model.ViolationReason;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    boolean existsBySignature(String signature);

    List<Fine> findAllByOrderByIdDesc();

    Optional<Fine> findByPdf(String pdfPath);

    Fine findFineBySignature(String signature);


}
