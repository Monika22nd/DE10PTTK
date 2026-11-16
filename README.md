# Supermarket JSP — đề 10, B23DCDT076

Web Java/JSP/Servlet cho đúng **hai module** trong bài phân tích–thiết kế:

1. `Register as Member 6`: khách hàng nhập hồ sơ → kiểm tra bắt buộc, định dạng, mật khẩu, trùng username/email/điện thoại → lưu `User6`, `Customer6`, `Account6` trong **một JDBC transaction**; lỗi thì rollback.
2. `View Item Statistics by Revenue 6`: nhân viên quản lý đăng nhập → chọn báo cáo → chọn ngày bắt đầu/kết thúc → xem số lượng và doanh thu theo mặt hàng, giảm dần → chọn mặt hàng để xem chi tiết và các giao dịch liên quan.

Giao diện là JSP/Jakarta Tags 3.0; Servlet là Controller; `RegistrationService6` và `ItemStatisticsService6` là nghiệp vụ. `UserDAO6`, `CustomerDAO6`, `AccountDAO6`, `ItemDAO6` và `TransactionDetailDAO6` phụ trách truy xuất JDBC; `UnitOfWork6` giữ ba bản ghi đăng ký trong một giao dịch. Cách tách này bám lớp boundary–control/service–DAO/entity trong báo cáo. Tomcat 10.1/Jakarta Servlet 6.0 yêu cầu dùng package `jakarta.*`, không phải `javax.*`.

## Chạy trên máy

Yêu cầu JDK 17 hoặc 21. Project chạy độc lập, **không mở bằng Android Studio như project Android**. Mở thư mục này trong IntelliJ IDEA hoặc VS Code, hay chạy PowerShell tại thư mục project:

```powershell
.\gradlew.bat runLocal
```

Mở [http://localhost:8090/](http://localhost:8090/). `runLocal` chỉ lắng nghe `127.0.0.1` và bật dữ liệu mẫu để demo. H2 tạo file ở `data/supermarket6.mv.db` trong thư mục đang chạy. Có thể đổi cổng bằng biến môi trường `SUPERMARKET_PORT`.

Tài khoản nhân viên quản lý mẫu: `manager6` / `Manager@123`. Dữ liệu mẫu có hai mặt hàng, hai giao dịch bán **COMPLETED** và một đơn **PENDING** cố ý không được tính vào thống kê. Chọn khoảng ngày gồm hôm nay và hai ngày trước để thấy báo cáo.

`runLocal` không phải cấu hình production. Khi đóng gói WAR và triển khai vào Tomcat riêng, dữ liệu mẫu **không tự bật**, nên phải tạo tài khoản nhân viên quản lý qua quy trình quản trị của hệ thống trước khi đăng nhập xem báo cáo. Thay đổi mật khẩu mẫu nếu bạn bật chế độ demo trong môi trường có người khác truy cập.

## Đóng gói WAR

```powershell
.\gradlew.bat clean test war
```

File kết quả: `build/libs/SupermarketJsp_10B23DCDT076-1.0.0.war`. Triển khai trên Apache Tomcat 10.1, Java 17+. Để app ở đường dẫn gốc `/`, đổi tên WAR thành `ROOT.war` khi đưa vào thư mục `webapps` của Tomcat. Dùng Gradle 8.7 với JDK **17 hoặc 21**, không dùng JVM 25 để chạy Gradle.

## Các trang và lớp tương ứng

| URL | JSP | Controller/Service |
|---|---|---|
| `/register` | `register.jsp` | `RegisterServlet` → `RegistrationService6` → `UnitOfWork6` + ba DAO |
| `/register/success` | `register-success.jsp` | `RegisterSuccessServlet` |
| `/login` | `login.jsp` | `LoginServlet` → `AuthService6` |
| `/reports` | `reports.jsp` | `ReportsServlet` (chỉ `MANAGEMENT_STAFF`) |
| `/reports/items` | `item-statistics.jsp` | `ItemStatisticsServlet` → `ItemStatisticsService6.list()` → `TransactionDetailDAO6` |
| `/reports/items/detail` | `item-detail.jsp` | `ItemDetailServlet` → `ItemStatisticsService6.detail()` / `transactions()` → `ItemDAO6` / `TransactionDetailDAO6` |

Các loại báo cáo nhà cung cấp và doanh thu tổng hợp hiện chỉ hiển thị ở menu vì nằm ngoài hai module được yêu cầu; không có nút giả dẫn tới trang trống.

## Quy tắc thống kê

- Chỉ lấy `tblTransaction6.status='COMPLETED'` và `transactionType IN ('COUNTER_SALE','ONLINE_ORDER')`.
- Khoảng ngày bao gồm trọn cả ngày bắt đầu và ngày kết thúc.
- `totalQuantitySold = SUM(quantity)`; `totalRevenue = SUM(quantity × unitPrice)` của **dòng giao dịch**, không nhân với giá bán hiện tại.
- Sắp xếp `totalRevenue DESC`, sau đó mã mặt hàng tăng dần; không ghi dữ liệu mới khi xem báo cáo.
- Chi tiết mặt hàng có thể từ chối nếu mặt hàng đã chuyển sang trạng thái không còn `ACTIVE`.

## Giới hạn và bảo vệ cơ bản

Đây là bản demo học phần, không phải hệ thống siêu thị đầy đủ. Nó có prepared statements, hash mật khẩu BCrypt, session/role check, CSRF token cho form POST và escape dữ liệu khi hiển thị. Chưa có quản trị sản phẩm/giao dịch, reset mật khẩu, rate limit, HTTPS, connection pool hay quy trình tạo tài khoản nhân viên production. H2 được dùng để chạy ngay mà không phải cấu hình MySQL; nếu lớp bắt buộc MySQL, cần thêm MySQL driver và chỉnh DDL/kết nối trước khi triển khai.

Kiểm thử: `.\gradlew.bat test` xác nhận đăng ký, chống trùng, lọc đơn chờ và tính doanh thu/chi tiết giao dịch.

**Lưu ý Windows:** một số bản Gradle/JDK không nạp được class khi đường dẫn project chứa ký tự có dấu tiếng Việt. Nếu gặp `ClassNotFoundException` dù mã đã biên dịch, đặt project ở đường dẫn ngắn không dấu hoặc ánh xạ tạm một ký tự ổ đĩa bằng `subst`, chạy lại lệnh rồi gỡ ánh xạ. Lần kiểm tra của project này trên đường dẫn không dấu đã chạy thành công `clean test war`.

Sơ đồ thiết kế là mô hình logic; ở bản triển khai, JSP tương ứng với lớp `Page6`, Servlet là Controller và một số DAO truy vấn trực tiếp bảng giao dịch bằng phép JOIN nên không phải mọi lớp trên sơ đồ có một file Java cùng tên. Chỉ hai module nêu ở đầu README đã được cài đặt.
