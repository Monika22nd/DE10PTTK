<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><a class="back" href="${pageContext.request.contextPath}/reports">← Các loại báo cáo</a><span class="eyebrow">MODULE 02 · MANAGEMENT STAFF</span><h1>Thống kê mặt hàng theo doanh thu</h1><p>Chọn khoảng ngày để tổng hợp các giao dịch bán hoàn tất.</p></div>
<section class="panel filter-panel">
  <form method="get" action="${pageContext.request.contextPath}/reports/items" class="filter-form">
    <div class="field"><label for="startDate">Ngày bắt đầu *</label><input id="startDate" name="startDate" type="date" required value="${fn:escapeXml(startDate)}"></div>
    <div class="field"><label for="endDate">Ngày kết thúc *</label><input id="endDate" name="endDate" type="date" required value="${fn:escapeXml(endDate)}"></div>
    <button class="button primary" type="submit">Xem thống kê</button>
  </form>
  <c:if test="${not empty error}"><div class="alert" role="alert"><c:out value="${error}"/></div></c:if>
</section>
<c:if test="${searched}"><section class="panel table-panel"><div class="table-heading"><div><span class="eyebrow">KẾT QUẢ</span><h2>Danh sách mặt hàng</h2></div><span class="count"><c:out value="${fn:length(rows)}"/> mặt hàng</span></div>
  <c:choose><c:when test="${empty rows}"><div class="empty">Không có mặt hàng phát sinh doanh thu trong khoảng ngày đã chọn.</div></c:when>
  <c:otherwise><div class="table-scroll"><table><thead><tr><th>STT</th><th>Mã mặt hàng</th><th>Tên mặt hàng</th><th class="num">Số lượng bán</th><th class="num">Tổng doanh thu</th><th></th></tr></thead><tbody>
    <c:forEach var="row" items="${rows}" varStatus="loop"><tr><td><c:out value="${loop.count}"/></td><td class="mono"><c:out value="${row.itemCode}"/></td><td><c:out value="${row.itemName}"/></td><td class="num"><c:out value="${row.totalQuantitySold}"/></td><td class="num amount"><fmt:formatNumber value="${row.totalRevenue}" pattern="#,##0"/> ₫</td><td><a class="row-link" href="${pageContext.request.contextPath}/reports/items/detail?itemId=${row.id}&amp;startDate=${fn:escapeXml(startDate)}&amp;endDate=${fn:escapeXml(endDate)}">Chi tiết →</a></td></tr></c:forEach>
  </tbody></table></div></c:otherwise></c:choose>
</section></c:if>
<%@ include file="footer.jspf" %>
