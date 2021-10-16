<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Dashboard</title>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
    </head>
    <body>
        <c:set var="auth" value="${sessionScope.Auth}" />
        <c:set var="test" value="${sessionScope.Test}" />
        <jsp:include page="includes/header.jsp" />
        <p
            id="countdown"
            class="fixed z-20 w-36 top-0 h-16 left-1/2 font-extrabold text-4xl text-center text-blue-800"
            style="transform: translate(-50%, 0%); line-height: 4rem"
            >
            0:00
        </p>
        <main class="pt-16 flex flex-col items-center">
            <c:if test="${not empty test}">
                <form action="exam" method="post" class="my-10 w-1/2 flex flex-col items-center gap-y-3">
                    <c:forEach var="question" items="${test.getQuestions()}" varStatus="qCount">
                        <div class="bg-white rounded w-full p-10">
                            <div class="flex flex-1">
                                <div>
                                    <span class="rounded-full w-9 h-9 text-center bg-blue-600 text-white inline-block mr-6" style="line-height: 2.25rem">
                                        ${qCount.count}
                                    </span>
                                </div>
                                <p class="whitespace-pre-line font-bold text-xl pb-3">${question.getContent()}</p>
                                <span class="ml-auto font-normal text-md text-blue-700">${question.getMark()} points</span>
                            </div>
                            <div class="pt-4 px-6 flex flex-col gap-y-3 border-t-gray-500 border-t-2">
                                <c:forEach var="answer" items="${question.getAnswers()}" varStatus="aCount">
                                    <div>
                                        <label class="inline-flex items-center w-full" for="q${qCount.count}-a${aCount.count}"
                                               ><input
                                                class="form-checkbox h-5 w-5 text-blue-600"
                                                type="checkbox"
                                                id="q${qCount.count}-a${aCount.count}"
                                                name="answerID-${answer.getAnswerID()}"
                                                />
                                            <span class="ml-2 text-gray-700">${answer.getContent()}</span>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                    <button type="submit" class="w-32 h-12 bg-green-500 text-white font-semibold text-xl rounded-sm">Submit</button>
                </form>
            </c:if>
        </main>
        <script>
            const endTime = Date.parse('<c:out value="${test.getExamEndTime()}"/>');
            let counter = 0;
            setInterval(function () {
                let milliseconds = endTime - new Date();
                let seconds = milliseconds / 1000;
                let mins = seconds / 60;
                seconds %= 60;
                document.getElementById('countdown').innerHTML = Math.trunc(mins) + ':' + (seconds < 10 ? '0' : '') + Math.trunc(seconds);
                if (counter-- === 0) {
                    let http = new XMLHttpRequest();
                    http.open('GET', 'ping', true);
                    http.send();
                    counter = 30;
                }
            }, 1000);
        </script>
    </body>
</html>
