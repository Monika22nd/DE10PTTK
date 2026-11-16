<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="page-heading"><a class="back" href="${pageContext.request.contextPath}/reports/items?startDate=${fn:escapeXml(startDate)}&amp;endDate=${fn:escapeXml(endDate)}">← Quay lại danh sách</a><span class="eyebrow">CHI TIẾT MẶT HÀNG</span><h1><c:out value="${item.name}"/></h1><p>Mã <span class="mono"><c:out value="${item.itemCode}"/></span> · Từ ngày <c:out value="${startDate}"/> đến ngày <c:out value="${endDate}"/></p></div>
<section class="cards three metrics"><article class="card"><span class="eyebrow">SỐ LƯỢNG BÁN</span><strong><c:out value="${item.totalQuantity}"/></strong></article><article class="card"><span class="eyebrow">TỔNG DOANH THU</span><strong><fmt:formatNumber value="${item.totalRevenue}" pattern="#,##0"/> ₫</strong></article><article class="card"><span class="eyebrow">GIÁ BÁN HIỆN TẠI</span><strong><fmt:formatNumber value="${item.sellingPrice}" pattern="#,##0"/> ₫</strong></article></section>
<section class="panel details-panel"><h2>Thông tin mặt hàng</h2><dl>
  <div><dt>Loại mặt hàng</dt><dd><c:out value="${item.category.name}"/></dd></div>
  <div><dt>Đơn vị tính</dt><dd><c:out value="${item.unit}"/></dd></div>
  <div><dt>Trạng thái</dt><dd><c:out value="${item.status eq 'ACTIVE' ? 'Đang kinh doanh' : 'Ngừng kinh doanh'}"/></dd></div>
  <div><dt>Mô tả</dt><dd><c:out value="${item.description}"/></dd></div></dl></section>
<section class="panel table-panel"><div class="table-heading"><div><span class="eyebrow">GIAO DỊCH LIÊN QUAN</span><h2>Danh sách giao dịch bán</h2></div><span class="count"><c:out value="${fn:length(transactions)}"/> giao dịch</span></div>
  <c:if test="${not empty warning}"><div class="alert" role="alert"><c:out value="${warning}"/></div></c:if>
  <c:choose><c:when test="${empty transactions}"><div class="empty">Không có giao dịch bán trong khoảng ngày này.</div></c:when>
  <c:otherwise><div class="table-scroll"><table><thead><tr><th>STT</th><th>Mã giao dịch</th><th>Ngày giao dịch</th><th>Loại</th><th class="num">Số lượng</th><th class="num">Đơn giá</th><th class="num">Thành tiền</th></tr></thead><tbody>
    <c:forEach var="t" items="${transactions}" varStatus="loop"><c:forEach var="d" items="${t.details}"><tr><td><c:out value="${loop.count}"/></td><td class="mono"><c:out value="${t.transactionCode}"/></td><td><c:out value="${t.transactionDateText}"/></td><td><c:out value="${t.typeName}"/></td><td class="num"><c:out value="${d.quantity}"/></td><td class="num"><fmt:formatNumber value="${d.unitPrice}" pattern="#,##0"/> ₫</td><td class="num amount"><fmt:formatNumber value="${d.amount}" pattern="#,##0"/> ₫</td></tr></c:forEach></c:forEach>
  </tbody></table></div></c:otherwise></c:choose>
</section>
<%@ include file="footer.jspf" %>
