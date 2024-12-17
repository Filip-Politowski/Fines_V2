package pl.filip_politowski.mandaty.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.filip_politowski.mandaty.dao.FineSearchRequest;
import pl.filip_politowski.mandaty.dto.response.FineResponse;
import pl.filip_politowski.mandaty.model.*;
import pl.filip_politowski.mandaty.service.EmployeeService;
import pl.filip_politowski.mandaty.service.FineService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fines")
public class FineTableController {
    private final FineService fineService;
    private final EmployeeService employeeService;

    @GetMapping
    public String finesTable(Model model, HttpSession session) {
        List<FineResponse> fineResponses = fineService.findAllFinesWithEmployees();
        List<String> companies = employeeService.findAllCompanies();
        model.addAttribute("companies", companies);
        session.isNew();
        model.addAttribute("fines", fineResponses);
        return "fine_table";
    }

    @PostMapping("/view-pdf")
    public ResponseEntity<byte[]> viewPdf(@RequestParam String pdfPath) throws IOException {
        Path path = Paths.get(pdfPath);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        byte[] pdfBytes = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename(path.getFileName().toString()).build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/delete-pdf")
    public String deletePdfByPath(@RequestParam("pdfPath") String pdfPath) {

        fineService.deletePdfByPath(pdfPath);

        return "redirect:/fines";
    }

    @GetMapping("/search")
    public String searchAndFilterFines(@RequestParam(value = "firstName", required = false) String firstName,
                                       @RequestParam(value = "lastName", required = false) String lastName,
                                       @RequestParam(value = "currency", required = false) Currency currency,
                                       @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                       @RequestParam(value = "signature", required = false) String signature,
                                       @RequestParam(value = "violationReason", required = false) ViolationReason violationReason,
                                       @RequestParam(value = "violationDate", required = false) LocalDate violationDate,
                                       @RequestParam(value = "paymentDeadline", required = false) LocalDate paymentDeadline,
                                       @RequestParam(value = "companyName", required = false) String companyName,
                                       @RequestParam(value = "fineStatus", required = false) FineStatus fineStatus,
                                       Model model) {
        FineSearchRequest fineSearchRequest = new FineSearchRequest();
        fineSearchRequest.setFirstName(firstName);
        fineSearchRequest.setLastName(lastName);
        fineSearchRequest.setCurrency(currency);
        fineSearchRequest.setPhoneNumber(phoneNumber);
        fineSearchRequest.setSignature(signature);
        fineSearchRequest.setViolationReason(violationReason);
        fineSearchRequest.setViolationDate(violationDate);
        fineSearchRequest.setPaymentDeadline(paymentDeadline);
        fineSearchRequest.setCompanyName(companyName);
        fineSearchRequest.setFineStatus(fineStatus);

        List<FineResponse> fineResponses = fineService.findAllFinesFromPredicates(fineSearchRequest);
        List<String> companies = employeeService.findAllCompanies();

        model.addAttribute("companies", companies);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("signature", signature);
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        if (violationReason != null) {
            model.addAttribute("violationReason", violationReason.toString());
        }
        model.addAttribute("violationDate", violationDate);
        model.addAttribute("paymentDeadline", paymentDeadline);
        model.addAttribute("companyName", companyName);
        if (fineStatus != null) {
            model.addAttribute("fineStatus", fineStatus.toString());
        }

        if (currency != null) {
            model.addAttribute("currency", currency.toString());
        }

        model.addAttribute("fines", fineResponses);

        return "fine_table";
    }


}
