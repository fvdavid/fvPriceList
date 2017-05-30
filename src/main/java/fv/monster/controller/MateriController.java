package fv.monster.controller;

import fv.monster.model.Account;
import fv.monster.model.Materi;
import fv.monster.repository.AccountRepository;
import fv.monster.repository.MateriRepository;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.collections.IteratorUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author fvsaddam - saddamtbg@gmail.com
 */
@Controller
@RequestMapping("/materi")
public class MateriController {

    @Value("classpath:/report/product.jrxml")
    private Resource materiJrxml;
    private JasperReport materiJasper;

    @Autowired
    private MateriRepository materiRepository;

    @Autowired
    private AccountRepository accountRepository;

    private JasperReport getMateriJasper() throws IOException, JRException {
        if (materiJasper == null) {
            materiJasper = JasperCompileManager.compileReport(materiJrxml.getInputStream());
        }
        return materiJasper;
    }

    private JasperPrint generateMateriJasperReport() throws JRException, IOException {
        Map<String, Object> reportParameters = new HashMap<>();
        reportParameters.put("terakhirUpdate", new Date());
        JRBeanCollectionDataSource reportData = new JRBeanCollectionDataSource(IteratorUtils.toList(materiRepository.findAll().iterator()));
        JasperPrint materiReport = JasperFillManager.fillReport(getMateriJasper(), reportParameters,
                reportData);
        return materiReport;
    }

    @GetMapping("/pdf")
    @ResponseBody
    public void generatePdf(HttpServletResponse response) throws JRException, IOException {
        JasperPrint materiReport = generateMateriJasperReport();
        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "attachment; filename=daftar-barang.pdf");
        JasperExportManager.exportReportToPdfStream(materiReport, response.getOutputStream());
    }

    @GetMapping("/xls")
    @ResponseBody
    public void generateXls(HttpServletResponse response) throws JRException, IOException {
        JasperPrint materiReport = generateMateriJasperReport();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=daftar-barang.xlsx");
        JRXlsxExporter exporter = new JRXlsxExporter();

        exporter.setExporterInput(new SimpleExporterInput(materiReport));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
    }

    @GetMapping("excel/uploadStatus")
    public String uploadExcelStatus() {
        return "materi/excel/uploadStatus";
    }

    @GetMapping("excel/upload")
    public String uploadExcel() {
        return "materi/excel/upload";
    }

    @PostMapping("excel/upload")
    public String singleFileUploadExcel(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "silahkan select file upload");
            return "redirect:/materi/excel/uploadStatus";
        }

        try {
            int i = 1;
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet worksheet = workbook.getSheetAt(0);

            while (i <= worksheet.getLastRowNum()) {
                Materi materi = new Materi();

                HSSFRow row = worksheet.getRow(i++);

                materi.setNomor(row.getCell(0).getNumericCellValue());
                materi.setNama(row.getCell(1).getStringCellValue());
                materi.setHarga(row.getCell(2).getNumericCellValue());
                materi.setDeskripsi(row.getCell(3).getStringCellValue());
                materi.setGambar(row.getCell(4).getStringCellValue());
                materi.setStatus(row.getCell(5).getStringCellValue());

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                materi.setAccountId(accountRepository.findOneByUserName(username));
                materi.setShareto("FV-ADMIN");

                materiRepository.save(materi);

                // row.getCell(0).getNumericCellValue()
                System.out.println("no: " + row.getCell(0).getNumericCellValue());
                System.out.println("nama: " + row.getCell(1).getStringCellValue());
                System.out.println("harga: " + row.getCell(2).getNumericCellValue());
                System.out.println("deskripsi: " + row.getCell(3).getStringCellValue());
                System.out.println("gambar: " + row.getCell(4).getStringCellValue());
                System.out.println("status: " + row.getCell(5).getStringCellValue());
                System.out.println("--------------------------------------");

            }

            redirectAttributes.addFlashAttribute("message", "anda berhasil upload file '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
        }

        return "redirect:/materi/excel/uploadStatus";
    }

    @GetMapping("list")
    public String showListTable(Model model) {
        model.addAttribute("materies", materiRepository.findAll());
        return "materi/list";
    }

    @GetMapping("/form")
    public String materiBaru(Materi m) {
        return "materi/form";
    }

    @PostMapping("/form")
    public String prosesForm(@ModelAttribute @Valid Materi materi, BindingResult hasilValidasi) {

        if (hasilValidasi.hasErrors()) {
            return "/materi/form";
        }

        materiRepository.save(materi);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        materiRepository.delete(id);
        return "redirect:/materi/list";
    }

    @GetMapping("/form/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("materi", materiRepository.getMateriById(id));
        return "materi/form";
    }

    @ModelAttribute("daftarAccount")
    public Iterable<Account> daftarAccount() {
        return accountRepository.findAllAdmin();
    }

}
