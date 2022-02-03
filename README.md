# PC Parts webshop
This is a simple webshop made in Angular and Spring Boot with JPA. As course work for the University of Applied Sciences Leiden.

It has no order process, but contains product and product info management. As well as a working user authentication system and cart.
The webshop is styled using the material design principles.

## Technologies
### Front-end
- Angular
- Angular Material Design
- CSS3

### Back-end
- Spring Boot
- MySQL
- JPA / Hibernate
- JSON Web Token

## Build process
1. Database design
2. Front-end design
3. Back-end creation
4. Front-end creation

Time spent: ~20 hours

## Run locally
To run this locally docker and NodeJS is required.

The back-end can be started using the following commands (starting from root):
- `cd backend`
- `docker compose up --build`

The front-end can be started using the following commands (starting from root):
- Change the environment.ts  and environment.prod.ts to point their apiUrl to: `http://localhost:3000`.
- `cd frontend`
- `npm run start`

The front-end can then be found on `localhost:4200`.

