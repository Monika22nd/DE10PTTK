<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<section class="panel message-panel"><div class="success-icon">✓</div><span class="eyebrow">ĐĂNG KÝ THÀNH CÔNG</span><h1>Chào mừng thành viên mới!</h1>
  <p>Hồ sơ của <strong><c:out value="${customer.fullName}"/></strong> đã được lưu.</p>
  <p class="code">Mã khách hàng: <strong><c:out value="${customer.customerCode}"/></strong> · Tên đăng nhập: <strong><c:out value="${customer.username}"/></strong></p>
  <a class="button primary" href="${pageContext.request.contextPath}/">OK</a></section>
<%@ include file="footer.jspf" %>
