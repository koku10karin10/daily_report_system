<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="../layout/app.jsp">
    <c:param name="content">
     <c:if test = "${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
     <c:if test="${errors != null}">
        <div id ="flush_error">

        <c:forEach var="error" items="${errors}">
            <c:out value="${error}" /><br />
        </c:forEach>
        </div>
    </c:if>
        <h2 id="project"><c:out value="${project_name}" /></h2>
        <h1 id="project">現在のプロジェクトの進捗度：<c:out value="${percent}" />％</h1>
        <div id="graph">
        <c:forEach var="je" items="${joinedEmployees}">
          <c:choose>
            <c:when test="${je.projectFinish == false}">
                <div id="false">
                    <p><c:out value="${je.name}" /></p>
                </div>
            </c:when>
            <c:otherwise>
                <div id="true">
                    <p><c:out value="${je.name}" /></p>
                </div>
            </c:otherwise>
          </c:choose>
        </c:forEach>
        </div>

        <form action="<c:url value='/progresses/finish' />" method="POST">
        <table id ="progress_list">
            <tbody>
                <tr>
                    <th>進捗度</th>
                    <th>サブゴール</th>
                    <th>期日</th>
                    <th></th>
                </tr>
                <c:forEach var="progress" items="${progresses}" varStatus="status">
                  <c:choose>
                    <c:when test="${progress.finish == false}">
                    <tr class="progressFalse">
                        <td><c:out value="${progress.phase}" /></td>
                        <td><c:out value="${progress.step}" /></td>
                        <td><c:out value="${progress.limit}" /></td>
                        <td><button type="submit" name="finish" value="${progress.id}" >完了</button></td>
                    </tr>

                    </c:when>
                    <c:otherwise>
                    <tr class="progressTrue">
                        <td><c:out value="${progress.phase}" /></td>
                        <td><c:out value="${progress.step}" /></td>
                        <td><c:out value="${progress.limit}" /></td>
                        <td><button type="submit" name="finish" value="${progress.id}" >未完了に戻す</button></td>
                    </tr>

                    </c:otherwise>
                  </c:choose>
                </c:forEach>
            </tbody>
        </table>
        </form>
        <p><a href="<c:url value = '/progresses/edit' />">進捗管理内容を編集する</a>
        <p><a href="<c:url value = '/' />">Topに戻る</a></p>
    </c:param>
</c:import>
