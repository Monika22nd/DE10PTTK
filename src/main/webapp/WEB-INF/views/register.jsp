<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><a class="back" href="${pageContext.request.contextPath}/">← Trang chủ</a><span class="eyebrow">MODULE 01 · CUSTOMER</span><h1>Đăng ký thành viên</h1><p>Vui lòng điền đầy đủ thông tin. Các trường có dấu * là bắt buộc.</p></div>
<section class="panel form-panel">
  <c:if test="${not empty errors.form}"><div class="alert" role="alert"><c:out value="${errors.form}"/></div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/register" novalidate>
    <input type="hidden" name="csrf" value="${csrf}">
    <div class="form-grid">
      <div class="field"><label for="fullName">Họ và tên *</label><input id="fullName" name="fullName" maxlength="120" required autocomplete="name" value="${fn:escapeXml(form.fullName)}"><small class="error"><c:out value="${errors.fullName}"/></small></div>
      <div class="field"><label for="dateOfBirth">Ngày sinh *</label><input id="dateOfBirth" name="dateOfBirth" type="date" required value="${fn:escapeXml(form.dateOfBirth)}"><small class="error"><c:out value="${errors.dateOfBirth}"/></small></div>
      <div class="field wide"><label for="address">Địa chỉ *</label><input id="address" name="address" maxlength="255" required autocomplete="street-address" value="${fn:escapeXml(form.address)}"><small class="error"><c:out value="${errors.address}"/></small></div>
      <div class="field"><label for="phoneNumber">Số điện thoại *</label><input id="phoneNumber" name="phoneNumber" maxlength="20" required inputmode="numeric" autocomplete="tel" value="${fn:escapeXml(form.phoneNumber)}"><small class="error"><c:out value="${errors.phoneNumber}"/></small></div>
      <div class="field"><label for="email">Email *</label><input id="email" name="email" type="email" maxlength="254" required autocomplete="email" value="${fn:escapeXml(form.email)}"><small class="error"><c:out value="${errors.email}"/></small></div>
      <div class="field wide"><label for="username">Tên đăng nhập *</label><input id="username" name="username" maxlength="30" required autocomplete="username" value="${fn:escapeXml(form.username)}"><small class="error"><c:out value="${errors.username}"/></small></div>
      <div class="field"><label for="password">Mật khẩu *</label><input id="password" name="password" type="password" minlength="8" maxlength="72" required autocomplete="new-password"><small class="error"><c:out value="${errors.password}"/></small></div>
      <div class="field"><label for="confirmPassword">Nhập lại mật khẩu *</label><input id="confirmPassword" name="confirmPassword" type="password" required autocomplete="new-password"><small class="error"><c:out value="${errors.confirmPassword}"/></small></div>
    </div>
    <div class="form-actions"><a class="button secondary" href="${pageContext.request.contextPath}/">Hủy</a><button class="button primary" type="submit">Thêm thành viên</button></div>
  </form>
</section>
<%@ include file="footer.jspf" %>
