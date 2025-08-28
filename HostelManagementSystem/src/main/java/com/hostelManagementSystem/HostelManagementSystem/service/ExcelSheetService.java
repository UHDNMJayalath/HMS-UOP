package com.hostelManagementSystem.HostelManagementSystem.service;

import com.hostelManagementSystem.HostelManagementSystem.entity.Student;
import com.hostelManagementSystem.HostelManagementSystem.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ExcelSheetService {

    @Autowired
    private StudentRepository studentRepo;

    private static final String UPLOAD_DIR = "uploads/excelSheets";
    private static final String PROCEED_DIR = "uploads/proceededExcelSheets";

    public ExcelSheetService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public void saveExcelData(String filename){


        File file = new File(UPLOAD_DIR + File.separator + filename);

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)){



            Sheet sheet = workbook.getSheetAt(0);
            List<Student> studentList = new ArrayList<>();

            for (int i = 4; i <= sheet.getLastRowNum(); i++){
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Student student = new Student();

                student.setStudentId(this.getCellValue(row.getCell(1)));
                student.setIndexNo(this.getCellValue(row.getCell(2)));
                student.setName(this.getCellValue(row.getCell(3)));
                student.setLName(this.getCellValue(row.getCell(5)));
                student.setInitials(this.getCellValue(row.getCell(6)));
                student.setFullName(this.getCellValue(row.getCell(7)));
                student.setALDistrict(this.getCellValue(row.getCell(8)));
                student.setSex(this.getCellValue(row.getCell(9)));
                student.setZScore(this.getCellValue(row.getCell(10)));
                student.setMedium(this.getCellValue(row.getCell(11)));
                student.setNIC(this.getCellValue(row.getCell(12)));
                student.setADD1(this.getCellValue(row.getCell(13)));
                student.setADD2(this.getCellValue(row.getCell(14)));
                student.setADD3(this.getCellValue(row.getCell(15)));
                student.setAddress(this.getCellValue(row.getCell(16)));
                student.setEmail(this.getCellValue(row.getCell(17)));
                student.setPhoneNo1(this.getCellValue(row.getCell(18)));
                student.setPhoneNo2(this.getCellValue(row.getCell(19)));
                student.setGenEngMarks(this.getCellValue(row.getCell(20)));
                student.setIntake(this.getCellValue(row.getCell(21)));
                student.setDateOfEnrollment(this.getCellValue(row.getCell(22)));

                studentList.add(student);
            }
            studentRepo.saveAll(studentList);
            this.moveFileToProcessed(file);
        }
        catch (Exception e){
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    public String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // If you expect integer-like values, remove decimal part
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Or format the date
                } else {
                    return String.valueOf((long) cell.getNumericCellValue()); // integer
                    // Or String.valueOf(cell.getNumericCellValue()); for decimal
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "Error";
            default:
                return "Error";
        }
    }

    private void moveFileToProcessed(File file) throws IOException {
        Path sourcePath = file.toPath();
        Path targetPath = Paths.get(PROCEED_DIR + File.separator + file.getName());

        Files.createDirectories(Paths.get(PROCEED_DIR));
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

}
