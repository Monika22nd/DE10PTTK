<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><a class="back" href="${pageContext.request.contextPath}/">← Trang chủ</a><span class="eyebrow">MANAGEMENT STAFF</span><h1>Báo cáo thống kê</h1><p>Chọn loại báo cáo cần xem.</p></div>
<section class="cards three">
  <a class="card card-link" href="${pageContext.request.contextPath}/reports/items"><div class="card-index">01</div><h2>Mặt hàng theo doanh thu</h2><p>Số lượng bán, doanh thu và giao dịch liên quan trong khoảng ngày.</p><span class="pill active">Có sẵn trong đề</span></a>
  <article class="card muted"><div class="card-index">02</div><h2>Nhà cung cấp</h2><p>Loại thống kê có trong toàn hệ thống; nằm ngoài hai module của bài này.</p><span class="pill">Chưa triển khai</span></article>
  <article class="card muted"><div class="card-index">03</div><h2>Doanh thu tổng hợp</h2><p>Loại thống kê có trong toàn hệ thống; nằm ngoài hai module của bài này.</p><span class="pill">Chưa triển khai</span></article>
</section>
<%@ include file="footer.jspf" %>
