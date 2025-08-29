# Bajaj Finserv Health | Qualifier 1 | JAVA — Spring Boot Solution

This app runs **automatically on startup**:
1. Calls the **generateWebhook** API with your details.
2. Receives a **webhook URL** and an **accessToken (JWT)**.
3. Determines which SQL question you got (based on last two digits of `regNo`).
4. Computes a **final SQL query** (placeholder in code — replace with your real solution).
5. Submits the query to the **returned webhook URL** using the JWT in the `Authorization` header.

---

## ✅ Quick Start

1. **Install JDK 17** and **Maven 3.9+**.
2. Edit `src/main/resources/application.yml` → set your `name`, `regNo`, and `email`.
3. Build:
   ```bash
   mvn -q -e -U -DskipTests clean package
   ```
4. Run:
   ```bash
   java -jar target/bfh-java-qualifier-1.0.0.jar
   ```

> The app will execute the flow automatically. No controller/endpoint is exposed.

---

## 🔧 How the flow works

- **On startup**: `StartupRunner` triggers the workflow.
- **Generate webhook**: `WebhookClient.generateWebhook(...)` posts to:
  ```
  https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA
  ```
  Body:
  ```json
  { "name": "...", "regNo": "...", "email": "..." }
  ```
  Response is deserialized into `GenerateWebhookResponse`:
  ```json
  { "webhook": "<URL>", "accessToken": "<JWT or token>" }
  ```

- **Pick your SQL**: `SqlSolver.solve(regNo)` checks the **last two digits** of `regNo`:
  - Odd → Question 1 (replace with your actual final SQL).
  - Even → Question 2 (replace with your actual final SQL).

- **Submit**: `WebhookClient.submitFinalQuery(...)` posts to the **returned `webhook` URL**, or falls back to `submission.fallbackWebhook` if missing.

Payload:
```json
{ "finalQuery": "YOUR_SQL_QUERY_HERE" }
```

Header:
```
Authorization: <accessToken>
Content-Type: application/json
```

---

## 🧩 Where to put your real SQL answer?

Open `SqlSolver.java` and replace the placeholders in `solve(...)`. Keep it a **single final SQL query string** (as required).

---

## 📝 Packaging for submission

- Commit your code to a **public GitHub repo**.
- Attach the **built JAR** from `target/bfh-java-qualifier-1.0.0.jar`.
- Provide a **raw downloadable** link to the JAR in your repo (e.g., GitHub Releases or direct `/raw/` link).
- Submit both via the provided Microsoft Forms link.