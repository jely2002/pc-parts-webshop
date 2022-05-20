# PC Parts webshop
This is a simple webshop made in Angular and Spring Boot with JPA. As course work for the University of Applied Sciences Leiden.

It has no order process, but contains product and product info management. As well as a working user authentication system and cart.
The webshop is styled using the material design principles.

The database has been made as generic as possible, with almost everything being configurable.
Categories, products, product properties and more.

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
- `cp .env.example .env`
- `docker compose up --build`

The front-end can be started using the following commands (starting from root):
- Change the environment.ts and environment.prod.ts to point their apiUrl to: `http://localhost:3000`.
- `cd frontend`
- `npm install`
- `npm run start`

The front-end can then be found on `localhost:4200`.

## Screenshots
All screenshots except for the 'Profile' are made while logged in to an administrator account. Making the website show 'delete', 'add category' and 'add product' buttons.

### Home / categories
The homepage that shows all the available categories.

![categories](https://user-images.githubusercontent.com/20154900/169577519-a6594207-3039-487d-bdf1-17617cca6be1.png)

### Products
A list view of products belonging to a category with its price, description and properties.

![products](https://user-images.githubusercontent.com/20154900/169577695-61ed3d71-a5ee-476c-aac1-9966fcb343c5.png)

### Cart
The cart, where amounts can be changed.

![cart](https://user-images.githubusercontent.com/20154900/169577715-4db5048a-beea-492d-8238-e55091aef480.png)

### Profile
User profile, here a user can logout, delete their account or change details.

![profile](https://user-images.githubusercontent.com/20154900/169577780-fb85286d-7153-4587-be8d-318c83acc01b.png)

## Datamodel
![datamodel](https://user-images.githubusercontent.com/20154900/169577852-f512c6a6-8305-46d4-9ed1-0f408e6a8c83.png)


