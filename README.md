# FinTechEase - Bank Application

FinTechEase is a backend application built with Spring Boot to simulate banking functionalities. The app allows users to create accounts, transfer money, check balances, generate bank statements, and receive alerts for transactions. It integrates with MySQL for data storage and uses JavaMail for email notifications.

## Features

- **Create Account**: Allows users to create a new bank account with personal and contact details.
- **Account Confirmation**: Sends a confirmation email containing the account number and details after account creation.
- **Transaction Operations**:
  - **Credit and Debit**: Users can deposit and withdraw funds.
  - **Money Transfer**: Transfer money between accounts.
  - **Transaction Alerts**: Credit and debit alerts sent via email.
- **Balance and Name Enquiry**: Users can check their account balance and verify account holder names.
- **Generate Bank Statements**: Generate bank statements for a specified date range and send them via email.
- **API Documentation**: Provides Swagger documentation for easy testing and exploration of APIs.

## Technologies Used

- **Backend**: Spring Boot, Spring Data JPA, Lombok
- **Database**: MySQL
- **Email**: JavaMail for transaction alerts and account confirmation emails.
- **API Documentation**: Swagger for API testing and documentation
- **PDF Generation**: iText Core for generating downloadable bank statements

## Endpoints

### User Endpoints:

- **Create Account**:  
  `POST http://localhost:8080/api/user/createUser`

- **Balance Enquiry**:  
  `GET http://localhost:8080/api/user/balanceEnquiry`

- **Name Enquiry**:  
  `GET http://localhost:8080/api/user/nameEnquiry`

- **Credit Account**:  
  `POST http://localhost:8080/api/user/credit`

- **Debit Account**:  
  `POST http://localhost:8080/api/user/debit`

- **Transfer Money**:  
  `POST http://localhost:8080/api/user/transfer`

- **Generate Bank Statement**:  
  `GET http://localhost:8080/bankStatement?accountNumber={accountNumber}&startDate={startDate}&endDate={endDate}`

### Example Bank Statement Generation:
```bash
GET http://localhost:8080/bankStatement?accountNumber=2025700024&startDate=2025-01-03&endDate=2025-01-03
