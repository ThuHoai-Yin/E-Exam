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
        <div class="w-screen h-screen flex justify-center">
            <div class="p-8 bg-gray-50 self-center rounded-3xl flex items-center">
                <form action="admin" method="post" class="float-left w-96 h-full flex flex-col gap-y-3">         
                    <p class="text-center text-xl font-semibold">Login as Admin User</p>
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
                        >Login</button>            
                </form>
            </div>
        </div>
    </body>
</html>
