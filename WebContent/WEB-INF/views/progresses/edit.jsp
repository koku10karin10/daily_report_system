<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="../layout/app.jsp">
    <c:param name="content">
       <form method="POST" action="<c:url value='/progresses/update' />">
        <table id ="progress_list">
            <tbody>
                <tr>
                    <th>進捗度</th>
                    <th>サブゴール</th>
                    <th>期日</th>

                </tr>

                <c:forEach var="progress" items="${pl}" varStatus="status">
                    <tr class="row${status.count%2}">
                        <td><c:out value="${progress.phase}" /></td>
                        <td><input type="text" name="step${progress.phase}" value="${progress.step}" /></td>
                        <td><input type="date" name="limit${progress.phase}" value="${progress.limit}" /></td>
                    </tr>
                        <input type="hidden" name="pId${progress.phase}" value="${progress.id}"/>
                </c:forEach>
            </tbody>
        </table>

        <input type="hidden" name="_token" value="${_token}" />
        <button type="submit">更新する</button>
        </form>

        <p><a href="<c:url value = '/progresses/index' />">戻る</a></p>
    </c:param>
</c:import>