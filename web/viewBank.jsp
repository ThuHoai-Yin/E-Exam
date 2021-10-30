<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>View Bank</title>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
    </head>

    <body>
        <c:set var="bank" value="${requestScope.bank}" />
        <jsp:include page="header.jsp" />
        <main class="pt-16 flex flex-col items-center mb-6">            
            <div>
                <c:if test="${not empty bank}">
                    <div class="mt-10 w-full py-5 px-10 bg-white rounded mb-3 flex justify-between">
                        <div>
                            <p class="font-semibold text-2xl">${bank.getBankName()}</p> 
                        </div>
                        <div class="mr-0 ml-auto w-40 relative">
                            <p class="font-normal text-md text-blue-700 text-right absolute bottom-0 right-0">
                                Total points: <span class="bg-blue-600 px-2 py-1 ml-1 rounded-md text-white">${bank.getQuestions().stream().flatMap(e -> e.getAnswers().stream()).filter(e -> e.isSelected()).count()}</span>
                            </p>
                        </div>
                    </div>
                    <div class="mb-3 flex flex-col items-center gap-y-3"
                         style="width: 60rem">
                        <c:forEach var="question" items="${bank.getQuestions()}" varStatus="qCount">
                            <div class="bg-white rounded w-full p-10">
                                <div class="flex flex-1 justify-between">
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
                                    <div class="w-36 relative">
                                        <p class="font-normal text-md text-blue-700 text-right absolute bottom-0">
                                            Choose ${question.getMaxChoose()} answer${question.getMaxChoose() > 1 ? 's' : ''}
                                        </p>
                                    </div>
                                </div>
                                <div class="pt-4 px-6 flex flex-col gap-y-2 border-t-gray-500 border-t-2">
                                    <c:forEach var="answer" items="${question.getAnswers()}">
                                        <div>
                                            <label class="inline-flex items-center w-full p-2 rounded-md">
                                                       <input
                                                           class="form-checkbox p-3"
                                                           type="checkbox"
                                                           readonly onclick="return false;"
                                                       <c:if test="${answer.isSelected()}">checked</c:if>
                                                           />
                                                       <span class="ml-4 text-gray-700">${answer.getContent()}</span>
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>            
                <a href="manageBank" class="self-start" id="redirect">
                    <button type="submit" form="redirect"
                            class="w-28 p-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-red-600 bg-red-50 border-red-200 hover:bg-red-100 active:bg-red-200 focus:ring-red-300">Back</button>
                </a> 
            </div>
        </main>
    </body>
</html>