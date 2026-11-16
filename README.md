# Siêu thị trực tuyến 6 — Đề 10 — B23DCDT076

Web Java (JSP/Servlet, mô hình MVC trên Jakarta EE / Tomcat 10.1) cài đặt **hai module** của đề số 10:

1. **Register as Member 6** — khách hàng chọn *Đăng ký thành viên* → nhập thông tin → nhấn *Thêm* → hệ thống kiểm tra và lưu vào CSDL.
2. **View Item Statistics by Revenue 6** — nhân viên quản lý chọn *Xem báo cáo thống kê* → *Thống kê mặt hàng theo doanh thu* → chọn ngày bắt đầu, ngày kết thúc → xem danh sách → nhấn một mặt hàng để xem chi tiết và danh sách giao dịch liên quan.

## Cấu trúc project (theo giáo trình: model – dao – view, thêm controller cho MVC)

```
src/main/java/
  model/       Lớp thực thể: User6, Customer6, Employee6, ManagementStaff6, ..., Item6, ItemCategory6,
               Transaction6, TransactionDetail6, CounterSale6, OnlineOrder6, GoodsReceipt6, Delivery6,
               ItemStatistics6 (kế thừa Item6)
  dao/         DAO6 (lớp gốc giữ Connection), UserDAO6, CustomerDAO6, ItemStatisticsDAO6, TransactionDAO6,
               Database6 (kết nối, tạo bảng, dữ liệu mẫu)
  controller/  Servlet (Controller): HomeServlet6, LoginServlet6, LogoutServlet6, MemberRegistrationServlet6,
               ManagerHomeServlet6, ItemStatisticsServlet6, AppContextListener6, WebUtil6
  app/         DevServer6 (Tomcat nhúng để chạy thử)
src/main/webapp/WEB-INF/view/   gdHome6.jsp, gdLogin6.jsp, gdMemberRegistration6.jsp, gdRegistrationSuccess6.jsp,
                                gdManagerHome6.jsp, gdStatisticsMenu6.jsp, gdItemStatistics6.jsp, gdItemDetail6.jsp
src/main/resources/schema.sql   CSDL toàn hệ thống (12 bảng tbl...6)
src/test/java/dao/              Kiểm thử đơn vị JUnit 5 cho từng lớp DAO
```

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

## Quy tắc nghiệp vụ đã cài đặt

- Đăng ký: bắt buộc đủ trường; ngày sinh không lớn hơn hôm nay; SĐT 10–11 số bắt đầu bằng 0; email đúng định dạng; tên đăng nhập 4–30 ký tự; mật khẩu 8–72 ký tự và khớp ô nhập lại; không trùng tên đăng nhập/email/SĐT. Mật khẩu được băm BCrypt. `tblUser6` và `tblCustomer6` được ghi trong **một giao dịch**, lỗi thì rollback.
- Thống kê: chỉ tính giao dịch **bán** (`tblCounterSale6`, `tblOnlineOrder6`) có trạng thái `COMPLETED`, ngày trong [ngày bắt đầu, ngày kết thúc] (tính trọn ngày kết thúc); doanh thu = Σ số lượng × đơn giá trên dòng giao dịch; sắp xếp doanh thu giảm dần, bằng nhau thì theo mã mặt hàng tăng dần. Chỉ đọc, không ghi dữ liệu.

## CI

GitHub Actions (`.github/workflows/build.yml`) chạy `gradlew clean test war`, khởi động ứng dụng và chụp ảnh các giao diện (artifact `screenshots`).
