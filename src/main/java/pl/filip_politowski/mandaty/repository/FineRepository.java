package pl.filip_politowski.mandaty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.filip_politowski.mandaty.model.Fine;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    boolean existsBySignature(String signature);

    List<Fine> findAllByOrderByIdDesc();

    Optional<Fine> findByPdf(String pdfPath);

    Fine findFineBySignature(String signature);


}
