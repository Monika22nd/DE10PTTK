<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<section class="hero">
  <div><span class="eyebrow">HỆ THỐNG SIÊU THỊ TRỰC TUYẾN</span><h1>Hai chức năng, một luồng xử lý rõ ràng.</h1>
  <p>Bản web JSP minh họa đúng hai module của đề số 10: khách hàng đăng ký thành viên và nhân viên quản lý xem thống kê mặt hàng theo doanh thu.</p>
  <div class="actions"><a class="button primary" href="${pageContext.request.contextPath}/register">Đăng ký thành viên</a><a class="button secondary" href="${pageContext.request.contextPath}/reports">Xem báo cáo</a></div></div>
  <div class="hero-card"><span>01 / MODULE</span><strong>Member<br>Registration</strong><hr><span>02 / MODULE</span><strong>Revenue<br>Statistics</strong></div>
</section>
<section class="cards two">
  <article class="card"><div class="card-index">01</div><h2>Register as Member 6</h2><p>Nhập thông tin cá nhân, kiểm tra dữ liệu và thông tin trùng, sau đó lưu hồ sơ và tài khoản trong cùng giao dịch CSDL.</p><a href="${pageContext.request.contextPath}/register">Mở biểu mẫu <span aria-hidden="true">→</span></a></article>
  <article class="card"><div class="card-index">02</div><h2>View Item Statistics by Revenue 6</h2><p>Chọn khoảng ngày, xem mặt hàng theo doanh thu giảm dần, mở chi tiết và danh sách các giao dịch bán liên quan.</p><a href="${pageContext.request.contextPath}/reports">Mở thống kê <span aria-hidden="true">→</span></a></article>
</section>
<%@ include file="footer.jspf" %>
