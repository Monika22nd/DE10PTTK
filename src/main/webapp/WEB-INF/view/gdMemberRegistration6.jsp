<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><a class="back" href="${pageContext.request.contextPath}/">← Trang chủ</a><span class="eyebrow">MODULE 1 · CUSTOMER</span><h1>Đăng ký thành viên</h1><p>Nhập đầy đủ thông tin. Các trường có dấu * là bắt buộc.</p></div>
<section class="panel form-panel">
  <c:if test="${not empty errors.form}"><div class="alert" role="alert"><c:out value="${errors.form}"/></div></c:if>
  <c:if test="${not empty errors and empty errors.form}"><div class="alert" role="alert">Thông tin chưa hợp lệ, vui lòng kiểm tra các trường được đánh dấu.</div></c:if>
  <form method="post" action="${pageContext.request.contextPath}/register" novalidate>
    <input type="hidden" name="csrf" value="${csrf}">
    <div class="form-grid">
      <div class="field"><label for="fullName">Họ và tên *</label><input id="fullName" name="fullName" maxlength="120" value="${fn:escapeXml(customer.fullName)}"><small class="error"><c:out value="${errors.fullName}"/></small></div>
      <div class="field"><label for="dateOfBirth">Ngày sinh *</label><input id="dateOfBirth" name="dateOfBirth" type="date" value="${fn:escapeXml(empty dateOfBirthText ? customer.dateOfBirth : dateOfBirthText)}"><small class="error"><c:out value="${errors.dateOfBirth}"/></small></div>
      <div class="field wide"><label for="address">Địa chỉ *</label><input id="address" name="address" maxlength="255" value="${fn:escapeXml(customer.address)}"><small class="error"><c:out value="${errors.address}"/></small></div>
      <div class="field"><label for="phoneNumber">Số điện thoại *</label><input id="phoneNumber" name="phoneNumber" maxlength="11" inputmode="numeric" value="${fn:escapeXml(customer.phoneNumber)}"><small class="error"><c:out value="${errors.phoneNumber}"/></small></div>
      <div class="field"><label for="email">Email *</label><input id="email" name="email" type="email" maxlength="254" value="${fn:escapeXml(customer.email)}"><small class="error"><c:out value="${errors.email}"/></small></div>
      <div class="field wide"><label for="username">Tên đăng nhập *</label><input id="username" name="username" maxlength="30" autocomplete="username" value="${fn:escapeXml(customer.username)}"><small class="error"><c:out value="${errors.username}"/></small></div>
      <div class="field"><label for="password">Mật khẩu *</label><input id="password" name="password" type="password" maxlength="72" autocomplete="new-password"><small class="error"><c:out value="${errors.password}"/></small></div>
      <div class="field"><label for="confirmPassword">Nhập lại mật khẩu *</label><input id="confirmPassword" name="confirmPassword" type="password" maxlength="72" autocomplete="new-password"><small class="error"><c:out value="${errors.confirmPassword}"/></small></div>
    </div>
    <div class="form-actions"><a class="button secondary" href="${pageContext.request.contextPath}/">Hủy</a><button class="button primary" type="submit">Thêm</button></div>
  </form>
</section>
<%@ include file="footer.jspf" %>
