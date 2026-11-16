<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><span class="eyebrow">MANAGEMENT STAFF</span><h1>Xin chào, <c:out value="${sessionScope.user.fullName}"/></h1><p>Giao diện chính của nhân viên quản lý.</p></div>
<section class="cards two">
  <a class="card card-link" href="${pageContext.request.contextPath}/reports"><div class="card-index">01</div><h2>Xem báo cáo thống kê</h2><p>Thống kê mặt hàng, nhà cung cấp và doanh thu.</p></a>
</section>
<%@ include file="footer.jspf" %>
