<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<section class="panel login-panel"><span class="eyebrow">TÀI KHOẢN HỆ THỐNG</span><h1>Đăng nhập</h1><p>Nhân viên quản lý đăng nhập để xem báo cáo mặt hàng.</p>
  <c:if test="${not empty error}"><div class="alert" role="alert"><c:out value="${error}"/></div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/login">
    <input type="hidden" name="csrf" value="${csrf}">
    <div class="field"><label for="username">Tên đăng nhập</label><input id="username" name="username" required autocomplete="username" value="${fn:escapeXml(username)}"></div>
    <div class="field"><label for="password">Mật khẩu</label><input id="password" name="password" type="password" required autocomplete="current-password"></div>
    <button class="button primary full" type="submit">Đăng nhập</button>
  </form>
  <c:if test="${demoMode}"><div class="demo-hint">Tài khoản quản lý dữ liệu mẫu: <code>manager6</code> / <code>Manager@123</code></div></c:if>
</section>
<%@ include file="footer.jspf" %>
