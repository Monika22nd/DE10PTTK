# Siêu thị trực tuyến 6 — Đề 10 — B23DCDT076

Web Java (JSP/Servlet, mô hình MVC trên Jakarta EE / Tomcat 10.1) cài đặt **hai module** của đề số 10:

1. **Register as Member 6** — khách hàng chọn *Đăng ký thành viên* → nhập thông tin → nhấn *Thêm* → hệ thống kiểm tra và lưu vào CSDL.
2. **View Item Statistics by Revenue 6** — nhân viên quản lý chọn *Xem báo cáo thống kê* → *Thống kê mặt hàng theo doanh thu* → chọn ngày bắt đầu, ngày kết thúc → xem danh sách → nhấn một mặt hàng để xem chi tiết và danh sách giao dịch liên quan.

## Cấu trúc project (model – dao – controller – view)

```text
DE10PTTK/
├── .github/workflows/build.yml       # CI: kiểm thử, WAR, ảnh giao diện
├── build.gradle.kts                   # Cấu hình Gradle và runLocal
├── gradlew / gradlew.bat              # Gradle wrapper
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/DevServer6.java    # Tomcat nhúng
│   │   │   ├── controller/           # Servlet, kiểm tra phiên và biểu mẫu
│   │   │   │   ├── MemberRegistrationServlet6.java
│   │   │   │   ├── ItemStatisticsServlet6.java
│   │   │   │   ├── LoginServlet6.java / LogoutServlet6.java
│   │   │   │   └── HomeServlet6.java / ManagerHomeServlet6.java / WebUtil6.java
│   │   │   ├── dao/                  # JDBC, giao dịch và truy vấn
│   │   │   │   ├── CustomerDAO6.java / UserDAO6.java
│   │   │   │   ├── ItemStatisticsDAO6.java / TransactionDAO6.java
│   │   │   │   └── DAO6.java / Database6.java
│   │   │   └── model/                # User6, Customer6, Item6, Transaction6,
│   │   │                             # ItemStatistics6 và các lớp liên quan
│   │   ├── resources/schema.sql      # 12 bảng CSDL và các khóa ngoại
│   │   └── webapp/
│   │       ├── assets/               # CSS
│   │       ├── index.jsp
│   │       └── WEB-INF/
│   │           ├── view/             # 8 trang JSP + header/footer
│   │           └── web.xml           # Ánh xạ Servlet và URL
│   └── test/java/dao/                # 4 nhóm kiểm thử JUnit, 17 bài test
└── tools/screenshots.js              # Chụp giao diện trong CI
```

`ItemStatistics6` là kết quả truy vấn kế thừa `Item6`, **không có bảng thống kê riêng**.
Mã triển khai lưu tài khoản và hồ sơ trong `tblUser6` và `tblCustomer6`, **không có** `AccountDAO6`, `RegistrationService6` hoặc `UnitOfWork6` riêng. Giao dịch đăng ký nằm trong `CustomerDAO6`.

## Chạy thử

Yêu cầu JDK 17 hoặc 21.

```bash
./gradlew.bat runLocal      # Windows
bash gradlew runLocal       # Linux/macOS
```

Mở http://localhost:8090/ . Chế độ chạy thử dùng H2 (file `data/supermarket6v2.mv.db`) và nạp dữ liệu mẫu:

| Tài khoản | Mật khẩu | Vai trò |
|---|---|---|
| manager6 | Manager@123 | Nhân viên quản lý (xem thống kê) |
| khachhang6 | Customer@123 | Khách hàng thành viên |

Dữ liệu mẫu có 1 phiếu nhập (không tính doanh thu), 2 hóa đơn tại quầy, 2 đơn trực tuyến (1 đơn PENDING không tính) và 1 mặt hàng đã ngừng kinh doanh vẫn xem được lịch sử. Chọn khoảng ngày từ 30 ngày trước đến hôm nay để thấy báo cáo.

Kiểm thử và đóng gói: `bash gradlew clean test war` → `build/libs/*.war` (triển khai lên Tomcat 10.1).
Muốn dùng MySQL: thêm driver MySQL vào `build.gradle.kts` và đặt biến môi trường `SUPERMARKET_DB_URL`.

Nếu project nằm trong đường dẫn Windows có dấu tiếng Việt và `gradlew.bat`/JUnit báo lỗi classpath, ánh xạ tạm thư mục sang ổ đĩa có đường dẫn ASCII (chọn ký tự ổ còn trống):

```powershell
subst X: 'C:\duong-dan-den-project\DE10PTTK'
Set-Location X:\
& 'C:\Program Files\Java\jdk-21\bin\java.exe' -classpath '.\gradle\wrapper\gradle-wrapper.jar' org.gradle.wrapper.GradleWrapperMain clean test war
Set-Location C:\
subst X: /D
```

Thay `C:\duong-dan-den-project\DE10PTTK` bằng đường dẫn thật. Chỉ gỡ ổ `X:` sau khi đã rời thư mục đó. Cách đơn giản hơn là sao chép project vào một đường dẫn không dấu trước khi chạy.

## Quy tắc nghiệp vụ đã cài đặt

- Đăng ký: bắt buộc đủ trường; ngày sinh không lớn hơn hôm nay; SĐT 10–11 số bắt đầu bằng 0; email đúng định dạng; tên đăng nhập 4–30 ký tự; mật khẩu 8–72 ký tự và khớp ô nhập lại; không trùng tên đăng nhập/email/SĐT. Mật khẩu được băm BCrypt. `tblUser6` và `tblCustomer6` được ghi trong **một giao dịch**, lỗi thì rollback.
- Thống kê: chỉ tính giao dịch **bán** (`tblCounterSale6`, `tblOnlineOrder6`) có trạng thái `COMPLETED`, ngày trong [ngày bắt đầu, ngày kết thúc] (tính trọn ngày kết thúc); doanh thu = Σ số lượng × đơn giá trên dòng giao dịch; sắp xếp doanh thu giảm dần, bằng nhau thì theo mã mặt hàng tăng dần. Chỉ đọc, không ghi dữ liệu.

## CI

GitHub Actions (`.github/workflows/build.yml`) chạy `gradlew clean test war`, khởi động ứng dụng và chụp ảnh các giao diện (artifact `screenshots`).
