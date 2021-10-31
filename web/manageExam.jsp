<%@page import="java.sql.Timestamp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Manage Exam</title>
        <link href="static/styles.css" rel="stylesheet" />
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/exam.png" as="image" />
    </head>

    <body>
        <jsp:include page="header.jsp" />
        <c:set var="exams" value="${requestScope.exams}" />
        <c:set var="banks" value="${requestScope.banks}" />
        <main class="pt-16 flex flex-col items-center justify-center">
            <div class="mt-10 -my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
                <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
                    <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
                        <table class="table-auto divide-y divide-gray-200" style="min-width: 70rem;">
                            <thead class="bg-gray-50">
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Exam Code</th>
                             <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Exam Name</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Open Date</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Close Date</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Num of Questions</th>
                            <th scope="col"
                                class="py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Duration (Sec)</th>
                            <th scope="col"></th>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:if test="${not empty exams}">
                                    <c:forEach var="exam" items="${exams}">
                                        <tr>
                                            <c:set var="temp" value="${exam.getOpenDate()}"/>                                            
                                            <td class="px-6 py-4 whitespace-nowrap">${exam.getExamCode()}</td>                        
                                            <td class="px-6 py-4 whitespace-nowrap">${exam.getExamName()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap"><%
                                                Object time = pageContext.getAttribute("temp");
                                                if (time == null) {
                                                    out.println("-");
                                                } else {
                                                    out.println(new SimpleDateFormat("dd-MM-yyyy HH:mm").format((Timestamp) time));
                                                }
                                                %></td>
                                                <c:set var="temp" value="${exam.getCloseDate()}"/>   
                                            <td class="px-6 py-4 whitespace-nowrap"><%
                                                time = pageContext.getAttribute("temp");
                                                if (time == null) {
                                                    out.println("-");
                                                } else {
                                                    out.println(new SimpleDateFormat("dd-MM-yyyy HH:mm").format((Timestamp) time));
                                                }
                                                %></td>
                                                <c:remove var="temp" scope="page"/>
                                            <td class="px-6 py-4 whitespace-nowrap">${exam.getQuestions().size()}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap">${exam.getDuration()}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap flex justify-end gap-x-3">
                                                <a class="w-16 text-right cursor-pointer text-lg text-green-500 hover:text-green-700"
                                                   onclick="{
                                                               navigator.clipboard.writeText(<c:out value="'${exam.getExamCode()}'" />);
                                                               this.innerHTML = 'Copied';
                                                               setTimeout(function (el) {
                                                                   el.innerHTML = 'Copy';
                                                               }, 5000, this);
                                                           }">Copy</a>
                                                <a class="cursor-pointer text-lg text-yellow-500 hover:text-yellow-700"
                                                   href="viewExam?examCode=<c:out value="${exam.getExamCode()}" />">View</a>
                                                <a class="cursor-pointer text-lg text-red-500 hover:text-red-700"
                                                   onclick="removeExam(<c:out value="'${exam.getExamCode()}'"/>)">Remove</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty exams}">
                                    <tr>    
                                        <td class="py-4 whitespace-nowrap text-center text-xl font-medium text-gray-500 uppercase"
                                            colspan="6">No Data</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>                     
            <button onclick="{
                        document.getElementById('add-modal').style.display = 'flex';
                    }"
                    class="p-2 rounded-full bg-white mt-2 hover:opacity-80">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                </svg>
            </button>
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
                        <form action="manageExam" method="post"
                              class="items-center px-4 py-3 flex flex-1 gap-5 justify-center">
                            <input type="hidden" name="action" value="remove" />
                            <input type="hidden" id="remove-examCode" name="examCode" />
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
            <div class="fixed hidden inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto flex flex-col justify-center items-center h-full w-full"
                 id="add-modal">
                <div class="relative p-5 border shadow-lg rounded-md bg-white w-96">
                    <form action="manageExam" method="post" class="flex flex-col gap-y-5 items-center">
                        <input type="hidden" name="action" value="add" />
                        <label class="block w-full" for="bankIDIn">
                            <span class="text-gray-700">Bank</span>
                            <select class="form-select mt-1 block w-full" name="bankID" id="bankIDIn"
                                    onchange="selectBank(this)">
                                <option disabled selected value> -- select an option -- </option>
                                <c:forEach var="bank" items="${banks}">
                                    <option value="${bank.getBankID()}">${bank.getBankName()} -
                                        ${bank.getCreatorFullName()}</option>
                                    </c:forEach>
                            </select>
                        </label>

                        <label class="block w-full" for="examNameIn">
                            <span class="text-gray-700">Exam Name</span>
                            <input class="form-input mt-1 block w-full" type="text" name="examName"
                                   placeholder="JP113 Progress Test 1" id="examNameIn" required/>
                        </label>


                        <label class="block w-full" for="openDateIn">
                            <span class="text-gray-700">Open Date</span>
                            <input class="form-input mt-1 block w-full" type="text" name="openDate" 
                                   pattern="^([0-1]?[0-9]|2[0-3]):[0-5][0-9] ([0][1-9]|[1|2][0-9]|[3][0|1])\/([0][1-9]|[1][0-2])\/[0-9]{4}$"
                                   title="Ex: 00:00 01/01/2000"
                                   placeholder="00:00 01/01/2000    " id="openDateIn" />
                        </label>

                        <label class="block w-full" for="closeDateIn">
                            <span class="text-gray-700">Close Date</span>
                            <input class="form-input mt-1 block w-full" type="text" name="closeDate"
                                   pattern="^([0-1]?[0-9]|2[0-3]):[0-5][0-9] ([0][1-9]|[1|2][0-9]|[3][0|1])\/([0][1-9]|[1][0-2])\/[0-9]{4}$"
                                   title="Ex: 00:00 01/01/2000"
                                   placeholder="00:00 01/01/2000" id="closeDateIn" />
                        </label>

                        <label class="block w-full" for="numOfQuestionsIn">
                            <span class="text-gray-700">Number Of Questions</span>
                            <input class="form-input mt-1 block w-full" type="number" name="numOfQuestions" min="1"
                                   placeholder="10" id="numOfQuestionsIn" required/>
                        </label>

                        <label class="block w-full" for="durationIn">
                            <span class="text-gray-700">Duration (sec)</span>
                            <input class="form-input mt-1 block w-full" type="number" name="duration" min="30"
                                   max="10800" placeholder="600" id="durationIn" required/>
                        </label>

                        <div class="items-center px-4 py-3 flex flex-1 gap-5 justify-center">
                            <button
                                class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-green-600 bg-green-50 border-green-200 hover:bg-green-100 active:bg-green-200 focus:ring-green-300"
                                type="submit">Add</button>
                            <button form="" onclick="{
                                        document.getElementById('add-modal').style.display = 'none';
                                    }"
                                    class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-gray-600 bg-gray-50 border-gray-200 hover:bg-gray-100 active:bg-gray-200 focus:ring-gray-300">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
        <script>
            var dict = {};
            <c:forEach var="bank" items="${banks}">
            dict[<c:out value="${bank.getBankID()}" />] = <c:out value="${bank.getQuestions().size()}" />
            </c:forEach>

            function selectBank(el) {
                document.getElementById('numOfQuestionsIn').max = dict[el.options[el.selectedIndex].value];
                for (let bank of b)
                    if (bank.bankID === value) {
                        document.getElementById('numOfQuestionsIn').max = bank.questions.length;
                        return;
                    }
            }
            function removeExam(examCode) {
                document.getElementById('remove-msg').innerHTML = 'Do you want to remove "' + examCode + '"?<br/>This action also removes all related records';
                document.getElementById('remove-modal').style.display = 'flex';
                document.getElementById('remove-examCode').value = examCode;
            }
        </script>
    </body>

</html>