<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Manage Bank</title>
        <link href="static/styles.css" rel="stylesheet" />
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/exam.png" as="image" />
    </head>

    <body>
        <jsp:include page="header.jsp" />
        <c:set var="user" value="${sessionScope.user}" />
        <c:set var="banks" value="${requestScope.banks}" />
        <main class="pt-16 flex flex-col items-center justify-center">
            <div class="mt-10 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
                <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
                    <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
                        <table class="table-auto divide-y divide-gray-200" style="min-width: 70rem;">
                            <thead class="bg-gray-50">
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                ID</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Name</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Course</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Creator</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Num of Questions</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Date Created</th>
                            <th scope="col"></th>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:if test="${not empty banks}">
                                    <c:forEach var="bank" items="${banks}">
                                        <tr>
                                            <c:set var="temp" value="${bank.getDateCreated()}"/>
                                            <td class="px-6 py-4 whitespace-nowrap">${bank.getBankID()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap">${bank.getBankName()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap">${bank.getCourseName()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap">${bank.getCreatorFullName()}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap">${bank.getQuestions().size()}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap"><%=new SimpleDateFormat("dd-MM-yyyy hh:mm").format(pageContext.getAttribute("temp"))%></td>
                                            <c:remove var="temp" scope="page"/>
                                            <td class="px-6 py-4 whitespace-nowrap flex justify-end gap-x-3">
                                                <a <c:if test="${bank.getCreatorID() == user.getUserID()}">
                                                        class="cursor-pointer text-lg text-red-500 hover:text-red-700"
                                                        onclick="removeBank(<c:out value="'${bank.getBankID()}'"/>, <c:out value="'${bank.getBankName()}'"/>)"                                                       
                                                    </c:if>
                                                    <c:if test="${bank.getCreatorID() != user.getUserID()}">
                                                        class="cursor-not-allowed text-lg text-gray-500"                                                    
                                                    </c:if>
                                                    >Remove</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty banks}">
                                    <tr>
                                        <td class="py-4 whitespace-nowrap text-center text-xl font-medium text-gray-500 uppercase"
                                            colspan="7">No Data</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <a href="createBank">
                <button onclick="addExam()" class="p-2 rounded-full bg-white mt-2 hover:opacity-80">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                    </svg>
                </button>
            </a>
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
                        <form action="manageBank" method="post"
                              class="items-center px-4 py-3 flex flex-1 gap-5 justify-center">
                            <input type="hidden" name="action" value="remove" />
                            <input type="hidden" id="remove-bankID" name="bankID" />
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
            <script>
                function removeBank(id, name) {
                    document.getElementById('remove-msg').innerHTML = 'Do you want to remove "' + name + '"?<br/>This action may removes some related questions';
                    document.getElementById('remove-modal').style.display = 'flex';
                    document.getElementById('remove-bankID').value = id;
                }
            </script>
    </body>
</html>