package pl.filip_politowski.mandaty.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.filip_politowski.mandaty.dao.FineSearchRequest;
import pl.filip_politowski.mandaty.dto.response.FineResponse;
import pl.filip_politowski.mandaty.service.EmployeeService;
import pl.filip_politowski.mandaty.service.FineService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fines")
public class FineTableController {
    private final FineService fineService;
    private final EmployeeService employeeService;

    @GetMapping
    public String finesTable(Model model) {
        List<FineResponse> fineResponses = fineService.findAllFinesWithEmployees();
        List<String> companies = employeeService.findAllCompanies();
        model.addAttribute("companies", companies);
        model.addAttribute("sortOrder", "asc");
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
    public String searchAndFilterFines(@ModelAttribute FineSearchRequest fineSearchRequest,@RequestParam String sortOrder, Model model) {

        List<FineResponse> fineResponses = fineService.findAllFinesFromPredicates(fineSearchRequest, sortOrder);
        List<String> companies = employeeService.findAllCompanies();

        model.addAttribute("companies", companies);
        model.addAttribute("phoneNumber", fineSearchRequest.getPhoneNumber());
        model.addAttribute("signature", fineSearchRequest.getSignature());
        model.addAttribute("firstName", fineSearchRequest.getFirstName());
        model.addAttribute("lastName", fineSearchRequest.getLastName());
        model.addAttribute("sortOrder", sortOrder);
        if (fineSearchRequest.getViolationReason() != null) {
            model.addAttribute("violationReason", fineSearchRequest.getViolationReason().toString());
        }
        model.addAttribute("violationDate", fineSearchRequest.getViolationDate());
        model.addAttribute("paymentDeadline", fineSearchRequest.getPaymentDeadline());
        model.addAttribute("companyName", fineSearchRequest.getCompanyName());
        if (fineSearchRequest.getFineStatus() != null) {
            model.addAttribute("fineStatus", fineSearchRequest.getFineStatus().toString());
        }

        if (fineSearchRequest.getCurrency() != null) {
            model.addAttribute("currency", fineSearchRequest.getCurrency().toString());
        }

        model.addAttribute("fines", fineResponses);

        return "fine_table";
    }


}