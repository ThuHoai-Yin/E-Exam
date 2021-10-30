<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/background.jpg" as="image" />
        <link rel="preload" href="assets/logo.png" as="image" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
    </head>
    <body class="bg-cover bg-fixed" style="background-image: url('assets/background.jpg')">
        <c:set var="user" value="${sessionScope.user}" />
        <c:set var="path" value="${pageContext.request.servletPath}"/>
        <nav class="bg-gray-100 px-8 fixed w-screen z-10">
            <div class="relative flex items-center justify-between h-16">
                <div class="flex-1 flex items-center justify-start h-full">
                    <div class="flex-shrink-0 flex items-center">
                        <img class="block h-8 w-auto" src="assets/logo.png" alt="Logo" />
                        <a class="font-sans text-3xl px-3">E-Exam</a>
                    </div>
                    <div class="pl-12">
                        <div class="flex space-x-4 h-full">
                            <a href="home" class="w-32 text-center py-2 text-base font-medium border-b-2 
                               <c:if test="${path == '/home.jsp'}">font-semibold border-purple-600</c:if>">Home</a>
                            <c:if test="${user.getRole().canManageBank()}">
                                <a href="manageBank" class="w-32 text-center py-2 text-base font-medium border-b-2 
                                   <c:if test="${path == '/manageBank.jsp' || path == '/createBank.jsp'|| path == '/viewBank.jsp' || path == '/editBank.jsp'}">font-semibold border-purple-600</c:if>">Bank</a>
                            </c:if>
                            <c:if test="${user.getRole().canManageExam()}">
                                <a href="manageExam" class="w-32 text-center py-2 text-base font-medium border-b-2 
                                   <c:if test="${path == '/manageExam.jsp' || path == '/viewExam.jsp' || path == '/viewRecord.jsp'}">font-semibold border-purple-600</c:if>">Exam</a>
                            </c:if>    
                            <c:if test="${user.getRole().canManageAccount()}">
                                <a href="manageAccount" class="w-32 text-center py-2 text-base font-medium border-b-2 
                                   <c:if test="${path == '/manageAccount.jsp'}">font-semibold border-purple-600</c:if>">Account</a>
                            </c:if>

                        </div>
                    </div>
                </div>
                <div class="absolute right-0 flex items-center pr-2">
                    <div class="ml-3 relative">
                        <button class="text-base font-semibold" id="dropdown-btn" onclick="toggleDropdown()">${user.getFullname()}</button>
                        <div
                            class="
                            hidden
                            origin-top-right
                            absolute
                            right-0
                            mt-2
                            w-48
                            rounded-md
                            shadow-lg
                            py-1
                            bg-white
                            ring-1 ring-black ring-opacity-5
                            focus:outline-none
                            "
                            id="dropdown"
                            >
                            <a href="javascript:void(0);" onclick="{
                                        document.getElementById('change-modal').style.display = 'flex';
                                    }" class="block px-4 py-2 text-sm text-gray-700 hover:bg-indigo-500 hover:text-white">Change Password</a>
                            <a href="logout" class="block px-4 py-2 text-sm text-gray-700 hover:bg-indigo-500 hover:text-white">Sign out</a>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
        <div class="fixed hidden inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto flex flex-col z-10 justify-center items-center h-full w-full"
             id="change-modal">
            <div class="relative p-5 border shadow-lg rounded-md bg-white w-96">
                <form action="accountInfo" method="post" onsubmit ="return verifyPassword()" class="flex flex-col gap-y-5 items-center">
                    <input type="hidden" name="backURL" value="${pageContext.request.request.getAttribute('javax.servlet.forward.request_uri')}" />

                    <label class="block w-full" for="passwordIn">
                        <span class="text-gray-700">Password</span>
                        <input class="form-input mt-1 block w-full" type="password" name="password" id="examNameIn" required/>
                    </label>


                    <label class="block w-full" for="rePasswordIn">
                        <span class="text-gray-700">Re Password</span>
                        <input class="form-input mt-1 block w-full" type="password" id="rePasswordIn" required/>
                    </label>


                    <div class="items-center px-4 py-3 flex flex-1 gap-5 justify-center">
                        <button
                            class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-green-600 bg-green-50 border-green-200 hover:bg-green-100 active:bg-green-200 focus:ring-green-300"
                            type="submit">Change</button>
                        <button form="" onclick="{
                                    document.getElementById('change-modal').style.display = 'none';
                                }"
                                class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-gray-600 bg-gray-50 border-gray-200 hover:bg-gray-100 active:bg-gray-200 focus:ring-gray-300">Close</button>
                    </div>
                </form>
            </div>
        </div>
        <script>
            window.onclick = function (event) {
                if (!event.target.matches('#dropdown-btn')) {
                    document.getElementById('dropdown').style.display = 'none';
                }
            };
            function verifyPassword() {
                return document.getElementById('passwordIn').value === document.getElementById('rePasswordIn').value;
            }
            function toggleDropdown() {
                var el = document.getElementById('dropdown');
                console.log(el.style.display);
                el.style.display = el.style.display === 'block' ? 'none' : 'block';
            }
        </script>
    </body>
</html>
