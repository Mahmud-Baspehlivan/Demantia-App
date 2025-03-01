# Demantia App API Documentation

## Base URL

`http://[your-server-host]:[port]`

## Authentication

All API endpoints are protected with basic authentication.

- Username: `demantia`
- Password: `demantia123`

Add these credentials to your Flutter app's HTTP requests as Basic Auth.

## Question API

### Get All Questions

- **GET** `/api/questions`
- **Response**: Array of Question objects

### Get Question by ID

- **GET** `/api/questions/{id}`
- **Response**: Question object or 404 if not found

### Get Questions by Category

- **GET** `/api/questions/category/{category}`
- **Response**: Array of Question objects

### Create New Question

- **POST** `/api/questions`
- **Body**: Question object (without ID)
- **Response**: Created question ID

### Update Question

- **PUT** `/api/questions/{id}`
- **Body**: Question object
- **Response**: Updated question ID

### Delete Question

- **DELETE** `/api/questions/{id}`
- **Response**: Deleted question ID

## Test Response API

### Get Test Response by ID

- **GET** `/api/test-responses/{id}`
- **Response**: TestResponse object or 404 if not found

### Get Test Responses by Session ID

- **GET** `/api/test-responses/session/{sessionId}`
- **Response**: Array of TestResponse objects

### Get Test Responses by Patient ID

- **GET** `/api/test-responses/patient/{patientId}`
- **Response**: Array of TestResponse objects

### Create Test Response

- **POST** `/api/test-responses`
- **Body**: TestResponse object (without ID)
- **Response**: Created test response ID

### Update Test Response

- **PUT** `/api/test-responses/{id}`
- **Body**: TestResponse object
- **Response**: Updated test response ID

### Delete Test Response

- **DELETE** `/api/test-responses/{id}`
- **Response**: Deleted test response ID

## Test Result API

### Get Test Result by ID

- **GET** `/api/test-results/{id}`
- **Response**: TestResult object or 404 if not found

### Get All Test Results

- **GET** `/api/test-results`
- **Response**: Array of TestResult objects

### Get Test Results by Patient ID

- **GET** `/api/test-results/patient/{patientId}`
- **Response**: Array of TestResult objects

### Get Test Result by Session ID

- **GET** `/api/test-results/session/{sessionId}`
- **Response**: TestResult object or 404 if not found

### Create Test Result

- **POST** `/api/test-results`
- **Body**: TestResult object (without ID)
- **Response**: Created test result ID

### Update Test Result

- **PUT** `/api/test-results/{id}`
- **Body**: TestResult object
- **Response**: Updated test result ID

### Delete Test Result

- **DELETE** `/api/test-results/{id}`
- **Response**: Deleted test result ID

## Data Models

### Question
