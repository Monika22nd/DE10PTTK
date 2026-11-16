<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><a class="back" href="${pageContext.request.contextPath}/reports">← Các loại thống kê</a><span class="eyebrow">MODULE 2 · MANAGEMENT STAFF</span><h1>Thống kê mặt hàng theo doanh thu</h1><p>Chọn ngày bắt đầu và ngày kết thúc rồi nhấn Xem thống kê.</p></div>
<section class="panel filter-panel">
  <form method="get" action="${pageContext.request.contextPath}/reports/items" class="filter-form" novalidate>
    <div class="field"><label for="startDate">Ngày bắt đầu *</label><input id="startDate" name="startDate" type="date" value="${fn:escapeXml(startDate)}"></div>
    <div class="field"><label for="endDate">Ngày kết thúc *</label><input id="endDate" name="endDate" type="date" value="${fn:escapeXml(endDate)}"></div>
    <button class="button primary" type="submit">Xem thống kê</button>
  </form>
  <c:if test="${not empty error}"><div class="alert" role="alert"><c:out value="${error}"/></div></c:if>
</section>
<c:if test="${searched}"><section class="panel table-panel"><div class="table-heading"><div><span class="eyebrow">KẾT QUẢ</span><h2>Mặt hàng theo doanh thu giảm dần</h2></div><span class="count"><c:out value="${fn:length(statistics)}"/> mặt hàng</span></div>
  <c:choose><c:when test="${empty statistics}"><div class="empty">Không có dữ liệu thống kê trong khoảng ngày đã chọn.</div></c:when>
  <c:otherwise><div class="table-scroll"><table><thead><tr><th>STT</th><th>Mã mặt hàng</th><th>Tên mặt hàng</th><th>Loại</th><th class="num">Số lượng bán</th><th class="num">Tổng doanh thu</th><th></th></tr></thead><tbody>
    <c:forEach var="s" items="${statistics}" varStatus="loop"><tr><td><c:out value="${loop.count}"/></td><td class="mono"><c:out value="${s.itemCode}"/></td><td><c:out value="${s.name}"/><c:if test="${s.status ne 'ACTIVE'}"> <span class="pill">Ngừng bán</span></c:if></td><td><c:out value="${s.category.name}"/></td><td class="num"><c:out value="${s.totalQuantity}"/></td><td class="num amount"><fmt:formatNumber value="${s.totalRevenue}" pattern="#,##0"/> ₫</td><td><a class="row-link" href="${pageContext.request.contextPath}/reports/items/detail?itemId=${s.id}&amp;startDate=${fn:escapeXml(startDate)}&amp;endDate=${fn:escapeXml(endDate)}">Chi tiết →</a></td></tr></c:forEach>
  </tbody></table></div></c:otherwise></c:choose>
</section></c:if>
<%@ include file="footer.jspf" %>
