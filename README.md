# Spring Batch Mastery Course 🚀

دوره‌ی جامع **Spring Batch** با تمرکز بر طراحی و پیاده‌سازی پردازش‌های دسته‌ای (Batch Processing) در سطح Enterprise  
این دوره شما را برای کار با داده‌های حجیم، Jobهای قابل اطمینان و سیستم‌های واقعی بانکی و سازمانی آماده می‌کند.

---

## 🎯 هدف دوره
آموزش عمیق Spring Batch از مفاهیم پایه تا مباحث پیشرفته مانند:
- Fault Tolerance
- Parallel Processing
- Monitoring
- Performance Tuning
- Enterprise Best Practices

---

## 🧩 سرفصل‌های دوره

### 1️⃣ مقدمه و مفاهیم پایه Spring Batch
- Spring Batch چیست و چه مسائلی را حل می‌کند
- معماری کلی: `Job`، `Step`، `JobRepository`، `JobLauncher`
- تفاوت Batch Processing و Stream Processing

**تمرین:**  
ایجاد یک پروژه Spring Boot با `spring-boot-starter-batch` و اجرای یک Job خالی.

---

### 2️⃣ Job و Step
- ساختار Job و Step
- Tasklet-based Step vs Chunk-oriented Step
- `JobParameters` و مفاهیم `JobInstance` / `JobExecution` / `StepExecution`

**تمرین:**  
تعریف یک Job با دو Step:
- Step اول: Tasklet برای چاپ متن
- Step دوم: Chunk ساده

---

### 3️⃣ ItemReader / ItemProcessor / ItemWriter
- Readerها:
    - `FlatFileItemReader`
    - `JdbcCursorItemReader`
    - `JpaPagingItemReader`
    - `StaxEventItemReader`
- Processor و منطق تجاری
- Writerها:
    - `FlatFileItemWriter`
    - `JdbcBatchItemWriter`
    - `JpaItemWriter`
- مفهوم Chunk و Commit Interval

**تمرین:**  
خواندن داده از CSV، پردازش و ذخیره در دیتابیس.

---

### 4️⃣ JobRepository و Metadata Tables
- `JobRepository`، `JobLauncher`، `JobExplorer`
- ساخت جداول metadata و تنظیم datasource
- Restartability و نقش metadata

**تمرین:**  
پیکربندی JobRepository با H2 یا PostgreSQL و بررسی جداول `BATCH_JOB_EXECUTION` و سایر جداول.

---

### 5️⃣ Retry، Skip و Fault Tolerance
- پیکربندی:
    - `skip`
    - `retry`
    - `SkipPolicy`
    - `RetryPolicy`
- تفاوت Skip / Retry / Fail
- مدیریت Exceptionها

**تمرین:**  
پیاده‌سازی Job با:
- Retry برای خطاهای موقتی
- Skip برای خطاهای دائمی

---

### 6️⃣ Transaction Management و Isolation
- مدیریت تراکنش در Chunk Processing
- اثر `commit-interval` روی rollback
- Isolation level و locking در دیتابیس

**تمرین:**  
بررسی داده‌های commit شده و rollback شده هنگام بروز Exception.

---

### 7️⃣ Scopeها: `@StepScope` و `@JobScope`
- تفاوت Scopeها در Spring Batch
- استفاده از JobParameters در Reader/Writer/Processor

**تمرین:**  
خواندن مسیر فایل از JobParameters در یک Reader با `@StepScope`.

---

### 8️⃣ Listenerها، Validatorها و Lifecycle Hooks
- `JobExecutionListener`
- `StepExecutionListener`
- `ItemReadListener` / `ItemWriteListener`
- `JobParametersValidator`

**تمرین:**  
لاگ‌گیری شروع و پایان Job و زمان اجرای Stepها.

---

### 9️⃣ Flow، Decision و Conditional Steps
- Flow و JobBuilder
- Split و Parallel Flow
- `JobExecutionDecider`

**تمرین:**  
اجرای مسیرهای متفاوت Job بر اساس مقدار پارامتر ورودی.

---

### 🔟 Parallelism، Partitioning و Scalability
- Multi-threaded Step
- Local & Remote Partitioning
- Remote Chunking
- ملاحظات همزمانی

**تمرین:**  
تقسیم یک فایل بزرگ به N پارت و پردازش موازی آن‌ها.

---

### 1️⃣1️⃣ Remote Chunking و Spring Cloud Integration
- معماری Remote Chunking
- استفاده از Kafka یا RabbitMQ

**تمرین (پیشرفته):**  
نمونه ساده Remote Chunking با صف پیام.

---

### 1️⃣2️⃣ Monitoring و Management
- `JobExplorer`
- Logging و Metrics
- Spring Boot Actuator

**تمرین:**  
نمایش لیست Jobها و وضعیت اجرای آن‌ها.

---

### 1️⃣3️⃣ Testing در Spring Batch
- Unit Test با JUnit و Mockito
- Integration Test با `@SpringBatchTest`
- Testcontainers

**تمرین:**  
اجرای Job در تست و اعتبارسنجی دیتابیس.

---

### 1️⃣4️⃣ Performance Tuning و Best Practices
- انتخاب commit-interval مناسب
- JDBC Batch و Prepared Statements
- Memory Management
- Tuning Thread Pool و Datasource

**تمرین:**  
مقایسه Performance برای Chunk Sizeهای مختلف.

---

### 1️⃣5️⃣ Error Handling در Production
- Retry با Backoff و Exponential Retry
- Dead Letter Queue (DLQ)
- Idempotent Writers

**تمرین:**  
پیاده‌سازی Exponential Backoff و ارسال خطاها به DLQ.

---

### 1️⃣6️⃣ Migration و Versioning
- تفاوت نسخه‌های Spring Batch
- مهاجرت از Jobهای قدیمی

---

### 1️⃣7️⃣ مباحث تکمیلی و اکوسیستم
- Spring Cloud Data Flow
- آشنایی با Hadoop و Spark

---

### 1️⃣8️⃣ پروژه نهایی (Capstone Project)
پیاده‌سازی یک Pipeline کامل:
1. خواندن میلیون‌ها رکورد از CSV یا دیتابیس
2. پردازش و تبدیل داده
3. نوشتن Batch در دیتابیس
4. مدیریت خطا (Skip / Retry / DLQ)
5. Partitioning و Parallel Processing
6. Testing و Monitoring

---

## 🌟 ارزش افزوده دوره
- تسلط بر Batch Processing در سیستم‌های Enterprise
- آمادگی برای پروژه‌های بانکی، بیمه و تجارت الکترونیک
- درک عمیق Fault Tolerance و Restartability
- تجربه عملی با Spring Batch و Spring Boot

---

## 🎓 دستاوردهای دانشجویان
پس از پایان دوره، دانشجو قادر خواهد بود:
- Jobهای کامل Spring Batch طراحی و اجرا کند
- Reader / Processor / Writer را بهینه‌سازی کند
- Jobهای شرطی و چندمرحله‌ای بسازد
- Jobها را مانیتور و مدیریت کند
- پردازش‌ها را مقیاس‌پذیر و Parallel کند
- Jobها را در محیط واقعی تست و اجرا کند

---

## 👥 این دوره مناسب چه کسانی است؟
- توسعه‌دهندگان Java آشنا با Spring Boot
- علاقه‌مندان به ETL و پردازش داده‌های حجیم
- افرادی که قصد فعالیت در سیستم‌های Enterprise دارند

---

## 🚫 این دوره مناسب چه کسانی نیست؟
- مبتدیان Java یا Spring Boot
- علاقه‌مندان صرفاً به Frontend یا CRUD ساده
- کسانی که به Batch Processing علاقه ندارند

---

## ✅ جمع‌بندی
این دوره شما را از یک توسعه‌دهنده معمولی Spring Boot به یک **متخصص Spring Batch در سطح Enterprise** تبدیل می‌کند؛  
مهارتی کمیاب و بسیار ارزشمند در بازار کار حرفه‌ای.

---

📌 **Enterprise-ready | Scalable | Reliable Batch Processing**
