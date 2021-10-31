<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>View Exam</title>
        <link href="static/styles.css" rel="stylesheet" />
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/exam.png" as="image" />
    </head>

    <body>
        <jsp:include page="header.jsp" />
        <c:set var="records" value="${requestScope.records}" />
        <main class="pt-16 flex flex-col items-center justify-center mb-10">
            <div>
                <div class="mt-10 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8 mb-4">
                    <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
                        <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
                            <table class="table-auto divide-y divide-gray-200" style="min-width: 70rem;">
                                <thead class="bg-gray-50">
                                <th scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Record ID</th>
                                <th scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Student ID</th>
                                <th scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Full Name</th>
                                <th scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Exam Date</th>
                                <th scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Date Submitted</th>
                                <th scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Correct Answers</th>
                                <th scope="col"
                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                    Total Answers</th>
                                <th scope="col"></th>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <c:if test="${not empty records}">
                                        <c:forEach var="record" items="${records}">
                                            <tr <c:if test="${record.getDateSubmitted() != null}">
                                                    class="text-green-600"
                                                </c:if>
                                                <c:if test="${record.getDateSubmitted() == null}">
                                                    class="text-yellow-600"
                                                </c:if>    
                                                >
                                                <c:set var="temp" value="${record.getExamDate()}"/>
                                                <td class="px-6 py-4 whitespace-nowrap">${record.getRecordID()}</td>
                                                <td class="px-6 py-4 whitespace-nowrap">${record.getStudentID()}</td>
                                                <td class="px-6 py-4 whitespace-nowrap">${record.getStudentFullname()}</td>
                                                <td class="px-6 py-4 whitespace-nowrap"><%=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(pageContext.getAttribute("temp"))%></td>                                                
                                                <c:set var="temp" value="${record.getDateSubmitted()}"/>
                                                <td class="px-6 py-4 whitespace-nowrap"><%
                                                    Object time = pageContext.getAttribute("temp");
                                                    if (time == null) {
                                                        out.println("-");
                                                    } else {
                                                        out.println(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format((Timestamp) time));
                                                    }
                                                    %></td>
                                                <c:remove var="temp" scope="page"/>
                                                <td class="px-6 py-4 whitespace-nowrap">${record.getNumOfCorrectAnswers()}</td>
                                                <td class="px-6 py-4 whitespace-nowrap">${record.getNumOfAnswers()}</td>
                                                <td class="px-6 py-4 whitespace-nowrap flex justify-end gap-x-3">
                                                    <a                                                    
                                                        <c:if test="${record.getDateSubmitted() != null}">
                                                            class="cursor-pointer text-lg text-yellow-500 hover:text-yellow-700" 
                                                            href="viewRecord?recordID=<c:out value="${record.getRecordID()}" />&examCode=<c:out value="${param.examCode}" />"
                                                        </c:if>
                                                        <c:if test="${record.getDateSubmitted() == null}">
                                                            class="cursor-not-allowed text-lg text-gray-500"        
                                                            onclick="return false;"
                                                        </c:if>

                                                        >View</a>
                                                    <a class="cursor-pointer text-lg text-red-500 hover:text-red-700"
                                                       onclick="removeRecord(<c:out value="${record.getRecordID()}"/>,
                                                       <c:out value="'${record.getStudentFullname()}'"/>)">Remove</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty records}">
                                        <tr>
                                            <td class="py-4 whitespace-nowrap text-center text-xl font-medium text-gray-500 uppercase"
                                                colspan="8">No Data</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <a href="manageExam" class="self-start" id="redirect">
                    <button type="submit" form="redirect"
                            class="w-28 p-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-red-600 bg-red-50 border-red-200 hover:bg-red-100 active:bg-red-200 focus:ring-red-300">Back</button>
                </a> 
            </div>
            <div class="fixed hidden inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto flex flex-col justify-center items-center h-full w-full"
                 id="remove-modal">
                <div class="relative p-5 border w-96 shadow-lg rounded-md bg-white">
                    <div class="text-center">
                        <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100">
                            <svg class="h-6 w-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                                 xmlns="http://www.w3.org/2000/svg">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                            </svg>
                        </div>
                        <h3 class="text-lg leading-6 font-medium text-gray-900">Warning!</h3>
                        <div class="mt-2 px-7 py-3">
                            <p class="text-sm text-gray-500" id="remove-msg"></p>
                        </div>
                        <form action="viewExam" method="post"
                              class="items-center px-4 py-3 flex flex-1 gap-5 justify-center">
                            <input type="hidden" name="action" value="remove" />
                            <input type="hidden" id="remove-recordID" name="recordID" />
                            <input type="hidden" name="examCode" value="${param.examCode}" />
                            <button type="submit" onclick="{
                                        document.getElementById('remove-modal').style.display = 'none';
                                    }"
                                    class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-red-600 bg-red-50 border-red-200 hover:bg-red-100 active:bg-red-200 focus:ring-red-300">
                                OK
                            </button>
                            <button form="" onclick="{
                                        document.getElementById('remove-modal').style.display = 'none';
                                    }"
                                    class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-gray-600 bg-gray-50 border-gray-200 hover:bg-gray-100 active:bg-gray-200 focus:ring-gray-300">
                                Close
                            </button>
                        </form>
                    </div>
                </div>
            </div>           
        </main>      
        <script>
            function removeRecord(id, studentFullname) {
                document.getElementById('remove-msg').innerHTML = 'Do you want to remove the record of <br/>"' + studentFullname + '"?';
                document.getElementById('remove-modal').style.display = 'flex';
                document.getElementById('remove-recordID').value = id;
            }
        </script>
    </body>

</html>