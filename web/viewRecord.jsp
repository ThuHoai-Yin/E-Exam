<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>View Record</title>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
    </head>

    <body>
        <c:set var="selected" value="${requestScope.selected}" />
        <c:set var="questions" value="${requestScope.questions}" />
        <jsp:include page="header.jsp" />
        <main class="pt-16 flex flex-col items-center">            
            <div>
                <c:if test="${not empty questions}">
                    <div class="mt-10 mb-3 flex flex-col items-center gap-y-3"
                         style="width: 60rem">
                        <c:forEach var="question" items="${questions}" varStatus="qCount">
                            <div class="bg-white rounded w-full p-10">
                                <div class="flex flex-1">
                                    <div class="flex flex-1">
                                        <div>
                                            <span
                                                class="rounded-full w-9 h-9 text-center bg-blue-600 text-white inline-block mr-6"
                                                style="line-height: 2.25rem">
                                                ${qCount.count}
                                            </span>
                                        </div>
                                        <p class="font-bold text-xl pb-3">${fn:replace(question.getContent(), '\\n',
                                                                            '<br />')}</p>
                                    </div>
                                    <div class="mr-0 ml-auto w-36 relative">
                                        <p class="font-normal text-md text-blue-700 text-right absolute bottom-0">
                                            Choose ${question.getMaxChoose()} answer${question.getMaxChoose() > 1 ? 's' : ''}
                                        </p>
                                    </div>
                                </div>
                                <div class="pt-4 px-6 flex flex-col gap-y-2 border-t-gray-500 border-t-2">
                                    <c:forEach var="answer" items="${question.getAnswers()}">
                                        <div>
                                            <c:set var="temp" value="${selected.contains(answer.getAnswerID())}"/>
                                            <label class="inline-flex items-center w-full p-2 rounded-md
                                                   <c:if test="${temp && answer.isSelected()}">bg-green-400</c:if>                                               
                                                   <c:if test="${!temp && answer.isSelected()}">bg-gray-400</c:if>                                               
                                                   <c:if test="${temp && !answer.isSelected()}">bg-red-400</c:if>">
                                                       <input
                                                           class="form-checkbox h-5 w-5 text-gray-600"
                                                           type="checkbox"
                                                           readonly onclick="return false;"
                                                       <c:if test="${temp}">checked</c:if>
                                                           />
                                                       <span class="ml-2 text-gray-700">${answer.getContent()}</span>
                                            </label>
                                            <c:remove var="temp" scope="page"/>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>            
                <a href="viewExam?examCode=${param.examCode}" class="self-start" id="redirect">
                    <button type="submit" form="redirect"
                            class="w-28 p-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-red-600 bg-red-50 border-red-200 hover:bg-red-100 active:bg-red-200 focus:ring-red-300">Back</button>
                </a> 
            </div>
        </main>
    </body>
</html>