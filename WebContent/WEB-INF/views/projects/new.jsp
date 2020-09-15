<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test = "${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <c:if test="${errors != null}">
            <div id ="flush_error">
                入力内容にエラーがあります。<br />
            <c:forEach var="error" items="${errors}">
                <c:out value="${error}" /><br />
            </c:forEach>
            </div>
        </c:if>
        <h2>従業員　一覧</h2>
        <form method="POST" action="<c:url value='/projects/create' />">
        <table id ="employee_list">
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <th>氏名</th>
                    <th>プロジェクトに加える</th>
                </tr>
                <c:forEach var="employee" items="${employees}" varStatus="status">
                    <tr class="row${status.count%2}">
                        <td><c:out value="${employee.code}" /></td>
                        <td><c:out value="${employee.name}" /></td>
                        <td>
                        <input type="checkbox" name="cBox" value="${employee.code}">
                        </td>
                </c:forEach>

            </tbody>
        </table>
        <p>
           プロジェクト名：<input type="text" name="project_name">
        </p>
        <p>
        <input type ="hidden" name="_token" value="${_token}" />
        <input type="submit" value="プロジェクトを作成する" >
        </p>
        </form>
        <div id="pagination">
            (全${employees_count}件)<br />
           <c:forEach var="i" begin="1" end="${((employees_count - 1) / 15) + 1}">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='/employees/index?page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
           </c:forEach>
        </div>
    </c:param>
</c:import>
