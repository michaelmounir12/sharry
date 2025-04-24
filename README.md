# 📸 Sharry - Image Sharing API

Sharry is a RESTful Image Sharing API built with **Spring Boot**, **MySQL**, and **JPA**. Users can register and authenticate, upload and view images stored in an **S3 bucket**, set image privacy, and apply transformations to their images.

---

## 🚀 Features

- 🔐 User registration and authentication (JWT-based)
- 📤 Upload images to AWS S3
- 🖼️ Retrieve images by ID or all public images
- 🔧 Apply transformations (resize, crop, grayscale, etc.)
- 🕶️ Set image privacy (public or private)
- 💾 Data persistence with MySQL and JPA

---

## 📂 Tech Stack

- **Java** + **Spring Boot**
- **MySQL** + **JPA/Hibernate**
- **AWS S3** for image storage
- **Spring Security** for authentication

---

## 📌 API Endpoints

### 🔐 Authentication Controller

#### `POST /sharry/register`
Register a new user.

**Request Body**:
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

---

#### `POST /sharry/authenticate`
Authenticate an existing user and receive a JWT token.

**Request Body**:
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response**:
```json
{
  "token": "jwt-token-here"
}
```

---

### 🖼️ Image Controller

#### `GET /sharry/images`
Get all **public** images or the user's **private** images (requires auth).

**Headers**:  
`Authorization: Bearer <JWT>`

---

#### `POST /sharry/images`
Upload a new image.

**Headers**:  
`Authorization: Bearer <JWT>`

**Form-Data**:
- `file`: image file (PNG, JPG, etc.)
- `title`: image title
- `privacy`: `public` or `private`

---

#### `POST /sharry/images/{id}/transform`
Apply a transformation to the image.

**Headers**:  
`Authorization: Bearer <JWT>`

**Request Body**:
```json
{
  "type": "resize", 
  "params": {
    "width": 200,
    "height": 200
  }
}
```

Supported transformation types:
- `resize`
- `crop`
- `grayscale`
- `rotate`

---

#### `POST /sharry/images/{id}/setPrivacy`
Update the privacy setting of an image.

**Headers**:  
`Authorization: Bearer <JWT>`

**Request Body**:
```json
{
  "privacy": "private"
}
```

---

#### `GET /sharry/images/{id}`
Retrieve an image by ID.  
If the image is private, only the owner can access it.

**Headers (if private)**:  
`Authorization: Bearer <JWT>`

---

## 🛠️ Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/sharry.git
   cd sharry
   ```

2. Configure your `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/sharry
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   aws.s3.bucket.name=your-bucket-name
   jwt.secret=your-secret-key
   ```

3. Build and run the app:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🧪 Testing

You can use [Postman](https://www.postman.com/) or [cURL](https://curl.se/) to test the endpoints. Make sure to register and authenticate to get a token before calling protected endpoints.

---

## ⚙️ Ignore Sensitive Files

To prevent accidental exposure of sensitive information:

1. Add to `.gitignore`:
   ```
   src/main/resources/application.properties
   ```

2. Stop tracking the file (if already committed):
   ```bash
   git rm --cached src/main/resources/application.properties
   git commit -m "Ignore application.properties"
   ```

3. Create a safe `application.properties.example` with placeholder values.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
