<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Take Exam</title>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
    </head>

    <body>
        <c:set var="user" value="${sessionScope.user}" />
        <c:set var="exam" value="${sessionScope.exam}" />
        <jsp:include page="header.jsp" />
        <p id="countdown"
           class="fixed z-20 w-36 top-0 h-16 left-1/2 font-extrabold text-4xl text-center text-blue-800"
           style="transform: translate(-50%, 0%); line-height: 4rem">
            0:00
        </p>
        <main class="pt-16 flex flex-col items-center">
            <c:if test="${not empty exam}">
                <form action="takeExam" method="post" class="my-10 flex flex-col items-center gap-y-3"
                      style="width: 60rem">
                    <c:forEach var="question" items="${exam.getQuestions()}" varStatus="qCount">
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
                                    <p class="font-normal text-md text-blue-700 text-right absolute bottom-0 right-0">
                                        Choose ${question.getMaxChoose()} answer${question.getMaxChoose() > 1 ? 's' : ''}
                                    </p>
                                </div>
                            </div>
                            <div id="answer-container-${qCount.count}"
                                 class="pt-4 px-6 flex flex-col gap-y-3 border-t-gray-500 border-t-2">
                                <c:forEach var="answer" items="${question.getAnswers()}">
                                    <div>
                                        <label class="inline-flex items-center w-full">
                                            <input
                                                class="form-checkbox p-3 text-blue-600"
                                                onchange="checkboxOnChange(
                                                                document.querySelectorAll('#answer-container-<c:out value="${qCount.count}" /> input:checked'),
                                                                document.querySelectorAll('#answer-container-<c:out value="${qCount.count}" /> input:not(:checked)'), this,
                                                <c:out value="${question.getMaxChoose()}" />)"
                                                type="checkbox"
                                                name="answer.${answer.getAnswerID()}" />
                                            <span class="ml-4 text-gray-700">${answer.getContent()}</span>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                    <button type="submit" id="submitBtn"
                            class="w-32 h-12 rounded-md text-xl font-medium border-0 focus:outline-none focus:ring transition text-white bg-blue-500 hover:bg-blue-600 active:bg-blue-700 focus:ring-blue-300">Submit</button>
                </form>
            </c:if>
        </main>
        <script>
            const endTime = Date.parse('<c:out value="${exam.getExamEndTime()}"/>');
            setInterval(function () {
                let milliseconds = endTime - new Date();
                if (milliseconds < 0) {
                    document.getElementById('submitBtn').click();
                }
                let seconds = milliseconds / 1000;
                let mins = seconds / 60;
                seconds %= 60;

                document.getElementById('countdown').innerHTML = Math.trunc(mins) + ':' + (seconds < 10 ? '0' : '') + Math.trunc(seconds);
            }, 1000); 
                       
            function checkboxOnChange(checkedlist, uncheckedlist, el, maxchoose) {
                let b = (checkedlist.length + (el.checked ? 1 : 0)) > maxchoose;
                uncheckedlist.forEach((element) => {
                    if (element !== el) {
                        element.disabled = b;
                        element.style.backgroundColor = b ? 'rgb(209, 213, 219)' : '';
                    }
                });
            }
        </script>
    </body>

</html>