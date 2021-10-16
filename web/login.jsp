<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet">
        <link rel="preload" href="assets/background.jpg" as="image">
        <link rel="preload" href="assets/teacher.png" as="image">
        <link rel="preload" href="assets/student.png" as="image">
    </head>
    <body class="bg-cover bg-fixed" style="background-image: url('assets/background.jpg')">
        <c:set var="errMsg" value="${requestScope.errMsg}"/>
        <c:set var="lastUser" value="${requestScope.lastUser}"/>
        <c:set var="userRole" value="${requestScope.userRole == null ? 'teacher' : requestScope.userRole}"/>
        <div class="w-screen h-screen flex justify-center">
            <div class="p-8 bg-gray-50 self-center rounded-3xl flex items-center">
                <form action="login" method="post" class="float-left w-96 h-full flex flex-col gap-y-3 mr-8">          
                    <p class="text-center text-xl font-semibold">Sign In</p>
                    <input type="hidden" name="lastURI" value="${pageContext.request.requestURI}"/>
                    <div>
                        <label class="text-sm font-medium <c:if test="${not empty errMsg}">text-red-700</c:if>" for="usernameInput">Username
                            <span class="text-xs">${errMsg}</span>
                        </label>
                        <input
                            class="w-full p-2.5 border border-gray-400 bg-gray-50 
                            focus:outline-none focus:ring-2 focus:ring-blue-600 
                            focus:border-transparent rounded-lg"
                            type="text"
                            id="usernameInput"
                            name="username"
                            value="${lastUser}"
                            required
                            />
                    </div>
                    <div>
                        <label class="text-sm font-medium <c:if test="${not empty errMsg}">text-red-700</c:if>" for="passwordInput">Password                            
                            <span class="text-xs">${errMsg}</span>
                        </label><br />
                        <input
                            class="w-full p-2.5 border border-gray-400 bg-gray-50 
                            focus:outline-none focus:ring-2 focus:ring-blue-600 
                            focus:border-transparent rounded-lg"
                            type="password"
                            id="passwordInput"
                            name="password"
                            required
                            />
                    </div>
                    <button
                        type="submit"
                        name="action"
                        value="login"
                        class="text-xl text-gray-100 font-semibold bg-gradient-to-r 
                        from-blue-500 to-purple-500 text-light-50 rounded-3xl 
                        w-full h-12 hover:from-blue-600 hover:to-purple-600"
                        >Login</button
                    >         
                    <input id="rIn" type="hidden" name="role" value="${userRole}"/>              
                </form>
                <div class="float-right w-80 h-full flex items-center flex-col gap-y-4">
                    <img id="rImg" class="object-contain w-36 h-36" src="assets/teacher.png"/>
                    <div class="flex justify-evenly w-full">
                        <button onclick="toggleRole(this, document.getElementById('sBtn'))" id="tBtn">Teacher</button>
                        <button onclick="toggleRole(this, document.getElementById('tBtn'))" id="sBtn">Student</button>
                    </div>
                </div>
            </div>
        </div>
        <script>
            const activeClass = 'bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-xl border-transparent border-2';
            const inActiveClass = 'bg-white-500 hover:bg-blue-200 text-blue-500 font-bold py-2 px-4 rounded-xl border-blue-400 border-2';
            function updateStyle() {
                switch (rIn.value) {
                    case 'teacher':
                        document.getElementById('rImg').src = 'assets/teacher.png';
                        document.getElementById('tBtn').className = activeClass;
                        document.getElementById('sBtn').className = inActiveClass;
                        break;
                    case 'student':
                        document.getElementById('rImg').src = 'assets/student.png';
                        document.getElementById('tBtn').className = inActiveClass;
                        document.getElementById('sBtn').className = activeClass;
                        break;
                }
            }
            function toggleRole(cur, tar) {
                if ((cur.id === 'tBtn') === (rIn.value === 'teacher'))
                    return;
                rIn.value = rIn.value === 'teacher' ? 'student' : 'teacher';
                updateStyle();
            }
            updateStyle();
        </script>
    </body>
</html>
