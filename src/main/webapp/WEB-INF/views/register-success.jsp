<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<section class="panel message-panel"><div class="success-icon">✓</div><span class="eyebrow">ĐĂNG KÝ HOÀN TẤT</span><h1>Chào mừng thành viên mới!</h1><p>Hồ sơ khách hàng và tài khoản đã được lưu thành công.</p><p class="code">Mã khách hàng: <strong><c:out value="${customerCode}"/></strong></p><a class="button primary" href="${pageContext.request.contextPath}/login">Đến trang đăng nhập</a></section>
<%@ include file="footer.jspf" %>
