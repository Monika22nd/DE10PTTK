<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><a class="back" href="${pageContext.request.contextPath}/manager">← Trang quản lý</a><span class="eyebrow">BÁO CÁO THỐNG KÊ</span><h1>Chọn loại thống kê</h1><p>Chọn một loại báo cáo cần xem.</p></div>
<section class="cards three">
  <a class="card card-link" href="${pageContext.request.contextPath}/reports/items"><div class="card-index">01</div><h2>Thống kê mặt hàng theo doanh thu</h2><p>Số lượng bán, doanh thu và giao dịch liên quan trong khoảng ngày.</p></a>
  <article class="card muted"><div class="card-index">02</div><h2>Thống kê nhà cung cấp</h2><p>Ngoài phạm vi hai module của bài.</p><span class="pill">Chưa cài đặt</span></article>
  <article class="card muted"><div class="card-index">03</div><h2>Thống kê doanh thu</h2><p>Ngoài phạm vi hai module của bài.</p><span class="pill">Chưa cài đặt</span></article>
</section>
<%@ include file="footer.jspf" %>
