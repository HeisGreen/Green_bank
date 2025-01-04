package com.chidoscode.green_bank.service.impl;

import com.chidoscode.green_bank.dto.EmailDetails;
import com.chidoscode.green_bank.entity.Transaction;
import com.chidoscode.green_bank.entity.User;
import com.chidoscode.green_bank.repository.TransactionRepository;
import com.chidoscode.green_bank.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStament {

    private TransactionRepository transactionRepository;

    private UserRepository userRepository;

    private EmailService emailService;

    private static final String FILE = "/Users/owner/Documents/MyStatement.pdf";
    /**
     * retrieve a list of transactions withing a given range
     * generate a pdf file of these transactions
     * send the file via email
     */

    public List<Transaction> generateStatements(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {

        LocalDate start =LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end =LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
        String address = user.getAddress();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting Size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document,outputStream);
        document.open();

        PdfPTable bankTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Green's Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.GREEN);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("23, maple road, Victoria Island, Lagos."));
        bankAddress.setBorder(0);
        bankTable.addCell(bankName);
        bankTable.addCell(bankAddress);

        PdfPTable statementTable = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date : " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date : " + endDate));
        stopDate.setBorder(0);
        PdfPCell customer = new PdfPCell(new Phrase("CUSTOMER NAME : " + customerName));
        customer.setBorder(0);
        PdfPCell spacer = new PdfPCell();
        spacer.setBorder(0);
        PdfPCell customerAddress = new PdfPCell(new Phrase("Customer Address : " + address));
        customerAddress.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBorder(0);
        date.setBackgroundColor(BaseColor.GREEN);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBorder(0);
        transactionType.setBackgroundColor(BaseColor.GREEN);
        PdfPCell amount = new PdfPCell(new Phrase("TRANSCACTION AMOUNT"));
        amount.setBorder(0);
        amount.setBackgroundColor(BaseColor.GREEN);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBorder(0);
        status.setBackgroundColor(BaseColor.GREEN);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(amount);
        transactionTable.addCell(status);

        transactionList.forEach(transaction -> {
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getCreatedAt().toString())));
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getTransactionType())));
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getAmount().toString())));
            transactionTable.addCell(new PdfPCell(new Phrase(transaction.getStatus())));
        });

        statementTable.addCell(customerInfo);
        statementTable.addCell(statement);
        statementTable.addCell(endDate);
        statementTable.addCell(customerName);
        statementTable.addCell(spacer);
        statementTable.addCell(customerAddress);

        document.add(bankTable);
        document.add(statementTable);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested account Statment attached below")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }
}
