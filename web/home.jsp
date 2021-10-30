<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Home</title>
        <link href="static/styles.css" rel="stylesheet">
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/exam.png" as="image" />
        <link rel="preload" href="assets/examl.png" as="image" />
        <link rel="preload" href="assets/bank.png" as="image" />
        <link rel="preload" href="assets/account.png" as="image" />
    </head>
    <body>
        <c:set var="user" value="${sessionScope.user}" />
        <jsp:include page="header.jsp" />
        <div class="absolute top-0 bottom-0 left-0 right-0 flex justify-center items-center">
            <c:if test="${user.getRole().canTakeExam()}">
                <form action="home" method="post" class="py-2 px-6 bg-gray-50 self-center flex items-center" style="border-radius: 5rem">
                    <img class="w-10" src="assets/exam.png" alt="Exam icon" />
                    <input
                        class="bg-transparent h-16 w-96 font-semibold text-2xl outline-none text-center"
                        placeholder="Enter exam code"
                        type="text"
                        name="examCode"
                        />
                    <button type="submit" class="rounded-full w-10 h-10 font-extrabold bg-indigo-500 inline-flex items-center justify-center">
                        <i
                            style="
                            border: solid #fff;
                            border-width: 0 3px 3px 0;
                            display: inline-block;
                            padding: 3px;
                            transform: translate(-2.5px, 0px) rotate(-45deg);
                            -webkit-transform: translate(-2.5px, 0px) rotate(-45deg);
                            "
                            ></i>
                    </button>
                </form>
            </c:if>
            <div class="flex gap-x-6">                
                <c:if test="${user.getRole().canManageBank()}">
                    <a href="manageBank">
                        <button class="w-72 h-72 rounded-3xl bg-white p-6 border border-solid border-indigo-500 hover:bg-indigo-500 hover:text-white active:bg-indigo-600 font-bold uppercase px-8 py-3 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all text-xl duration-150">
                            <img src="assets/bank.png" class="h-40 mx-auto"/> 
                            <h2>Manage Bank</h2>
                        </button>
                    </a>
                </c:if>
                
                <c:if test="${user.getRole().canManageExam()}">
                    <a href="manageExam">
                        <button class="w-72 h-72 rounded-3xl bg-white p-6 border border-solid border-indigo-500 hover:bg-indigo-500 hover:text-white active:bg-indigo-600 font-bold uppercase px-8 py-3 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all text-xl duration-150">
                            <img src="assets/examl.png" class="h-40 mx-auto"/> 
                            <h2 class="text-bold text-xl">Manage Exam</h2>
                        </button>
                    </a>
                </c:if>
                
                <c:if test="${user.getRole().canManageAccount()}">
                    <a href="manageAccount">
                        <button class="w-72 h-72 rounded-3xl bg-white p-6 border border-solid border-indigo-500 hover:bg-indigo-500 hover:text-white active:bg-indigo-600 font-bold uppercase px-8 py-3 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all text-xl duration-150">
                            <img src="assets/account.png" class="h-40 mx-auto"/> 
                            <h2 class="text-bold text-xl">Manage Account</h2>
                        </button>
                    </a>
                </c:if>
            </div>
        </div>
    </div>
    <script>
        window.onclick = function (event) {
            if (!event.target.matches('#dropdown-btn')) {
                document.getElementById('dropdown').style.display = 'none';
            }
        };
        function toggleDropdown() {
            var el = document.getElementById('dropdown');
            console.log(el.style.display);
            el.style.display = el.style.display === 'block' ? 'none' : 'block';
        }
    </script>
</body>
</html>
