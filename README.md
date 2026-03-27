# Hotel Booking Lusan

A full-stack hotel reservation platform built with Spring Boot and Angular. The application supports customer self-service booking, admin inventory management, JWT-based authentication, Stripe-powered payments, email notifications, and OpenAPI documentation.

## Highlights

- Customer registration, login, profile management, and booking history
- Room catalog with availability search, room details, and image uploads
- Booking creation with generated booking references and payment follow-up
- Stripe payment intent flow with success and failure handling
- Admin dashboard for rooms, bookings, and admin user registration
- Swagger UI and OpenAPI docs for backend endpoint exploration

## Tech Stack

| Layer | Technology |
| --- | --- |
| Frontend | Angular 20, TypeScript, RxJS, ngx-stripe |
| Backend | Spring Boot 3.5, Java 21, Spring Security, Spring Data JPA |
| Database | MySQL |
| Auth | JWT |
| Payments | Stripe |
| Notifications | Spring Mail |
| API Docs | springdoc-openapi / Swagger UI |

## Repository Structure

```text
hotel-booking-lusan/
|-- backend/              Spring Boot REST API
|-- angular-frontend/     Angular client application
`-- README.md             Project documentation
```

## Product Areas

### Customer experience

- Browse rooms and filter by stay dates and room type
- View room details before booking
- Create bookings as an authenticated customer
- Retrieve bookings by reference
- Complete payment from the Stripe checkout page
- Review booking history in the profile area

### Admin experience

- Access protected admin routes
- Add, edit, and delete rooms
- Review all bookings
- Update booking and payment statuses
- Register additional admin accounts

## Backend Overview

Base URL: `http://localhost:7070`

Key endpoint groups:

- `/api/auth` for registration and login
- `/api/users` for account details, profile updates, and booking history
- `/api/rooms` for room listing, room search, availability, and admin room management
- `/api/bookings` for booking creation, lookup, and admin booking updates
- `/api/payments` for Stripe payment intent creation and payment status updates
- `/images/**` for uploaded room images
- `/swagger-ui.html` and `/api-docs` for API documentation

Role model:

- `CUSTOMER` can register, log in, manage their account, and create bookings
- `ADMIN` can manage rooms, bookings, users, and admin-only screens

## Frontend Overview

Default client URL: `http://localhost:4200`

Implemented routes include:

- Public pages: `home`, `rooms`, `about`, `gallery`, `testimonials`
- Auth pages: `login`, `register`
- Customer pages: `profile`, `edit-profile`, `find-booking`, `room-details/:id`
- Payment pages: `payment/:bookingReference/:amount`, `payment-success/:bookingReference`, `payment-failure/:bookingReference`
- Admin pages under `/admin`

## Local Setup

### Prerequisites

- Java 21
- Node.js 20+ and npm
- MySQL 8+
- A Stripe account with test keys
- An SMTP account for email delivery

### 1. Configure the backend

The backend reads configuration from `backend/src/main/resources/application.properties` and supports loading values from a local `.env` file through:

```properties
spring.config.import=optional:file:.env[.properties]
```

Create a `.env` file in the `backend/` directory with values similar to:

```properties
SERVER_PORT=7070
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/hotel_booking
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=replace_with_a_long_random_secret

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@example.com
MAIL_PASSWORD=your_app_password

STRIPE_PUBLIC_KEY=pk_test_your_key
STRIPE_SECRET_KEY=sk_test_your_key

FILE_UPLOAD_DIR=uploads/products
```

Notes:

- `spring.jpa.hibernate.ddl-auto=update` is enabled, so schema changes are applied automatically at startup.
- Uploaded room images are served from `/images/**`.
- Swagger UI is enabled by default.

### 2. Start the backend

From `backend/`:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

### 3. Install frontend dependencies

From `angular-frontend/`:

```bash
npm install
```

### 4. Start the frontend

From `angular-frontend/`:

```bash
npm start
```

Then open `http://localhost:4200`.

## Environment and Integration Notes

- The Angular API service currently targets `http://localhost:7070/api`.
- Image rendering in the frontend currently targets `http://localhost:7070`.
- The backend booking service currently generates payment links pointing to `http://localhost:4200/payment/...`.
- The payment page currently loads a hardcoded Stripe test publishable key in the frontend component.

If you change local ports or deploy to another environment, update the relevant frontend and backend constants before release.

## API Quick Reference

| Area | Method | Endpoint | Purpose |
| --- | --- | --- | --- |
| Auth | `POST` | `/api/auth/register` | Register a customer or admin |
| Auth | `POST` | `/api/auth/login` | Authenticate and receive JWT |
| Users | `GET` | `/api/users/account` | Get current user profile |
| Users | `GET` | `/api/users/bookings` | Get current user booking history |
| Rooms | `GET` | `/api/rooms/all` | List rooms |
| Rooms | `GET` | `/api/rooms/available` | Search available rooms |
| Rooms | `POST` | `/api/rooms/add` | Add room as admin |
| Bookings | `POST` | `/api/bookings/add` | Create booking |
| Bookings | `GET` | `/api/bookings/{reference}` | Find booking by reference |
| Payments | `POST` | `/api/payments/pay` | Create Stripe payment intent |
| Payments | `PUT` | `/api/payments/update` | Persist payment result |

## Quality Checks

Verified locally on March 27, 2026:

- Backend: `.\mvnw.cmd test` passed
- Frontend: `npm run build` failed due to Angular production budget limits

Current frontend build issue:

- Initial bundle exceeds the configured `500 kB` warning budget and triggers a production build failure because an inlined Google Fonts asset exceeds the `8 kB` component-style budget.

## Known Implementation Notes

- The root README has been updated to match the current codebase, but the frontend-specific `angular-frontend/README.md` still contains the default Angular scaffold content.
- `SecurityFilter` permits `/api/rooms/**` and `/api/bookings/**` at the URL layer, while method-level annotations still enforce admin/customer rules for protected operations.
- CORS is currently configured with `allowedOrigins("*")`, which is convenient for local development but should be tightened for production deployments.

## Next Improvements

- Move frontend API and Stripe configuration into environment files
- Replace hardcoded local URLs with environment-aware values
- Reduce Angular bundle size or relax budgets to restore production builds
- Add dedicated backend and frontend test coverage beyond the current smoke test
- Add deployment instructions for staging and production environments

## License

No license file is currently included in the repository.
