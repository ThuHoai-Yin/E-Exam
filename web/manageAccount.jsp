<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Manage Account</title>
        <link href="static/styles.css" rel="stylesheet" />
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/exam.png" as="image" />
    </head>

    <body>
        <c:set var="user" value="${sessionScope.user}" />
        <jsp:include page="header.jsp" />
        <c:set var="accounts" value="${requestScope.accounts}" />
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
                                Username</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Full Name</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Email</th>
                            <th scope="col"
                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Role</th>
                            <th scope="col"></th>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:if test="${not empty accounts}">
                                    <c:forEach var="acc" items="${accounts}">
                                        <tr>
                                            <td class="px-6 py-4 whitespace-nowrap">${acc.getUserID()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap">${acc.getUsername()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap">${acc.getFullname()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap">${acc.getEmail()}</td>
                                            <td class="px-6 py-4 whitespace-nowrap">${acc.getRole().getRoleName()}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap flex justify-end gap-x-3">
                                                <a class="cursor-pointer text-lg text-blue-500 hover:text-blue-700"
                                                   onclick="editAccount(<c:out value=" ${acc.getUserID()}" />,
                                                   <c:out value="'${acc.getUsername()}'" />,
                                                   <c:out value="'${acc.getFullname()}'" />,
                                                   <c:out value="'${acc.getEmail()}'" />,
                                                   <c:out value="'${acc.getRole().getRoleName()}'" />)">Edit</a>
                                                <a class="cursor-pointer text-lg text-red-500 hover:text-red-700"
                                                   onclick="removeAccount(<c:out value=" ${acc.getUserID()}" />,
                                                   <c:out value="'${acc.getUsername()}'" />)">Remove</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty accounts}">
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
            <button onclick="addAccount()" class="p-2 rounded-full bg-white mt-2 hover:opacity-80">
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
                        <form action="manageAccount" method="post"
                              class="items-center px-4 py-3 flex flex-1 gap-5 justify-center">
                            <input type="hidden" name="action" value="remove" />
                            <input type="hidden" id="remove-userID" name="userID" />
                            <button type="submit" onclick="{
                                                                            document.getElementById('remove-modal').style.display = 'none'
                                                                        }"
                                    class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-red-600 bg-red-50 border-red-200 hover:bg-red-100 active:bg-red-200 focus:ring-red-300">
                                OK
                            </button>
                            <button form="" onclick="{
                                                                            document.getElementById('remove-modal').style.display = 'none'
                                                                        }"
                                    class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-gray-600 bg-gray-50 border-gray-200 hover:bg-gray-100 active:bg-gray-200 focus:ring-gray-300">
                                Close
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="fixed hidden inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto flex flex-col justify-center items-center h-full w-full"
                 id="addoredit-modal">
                <div class="relative p-5 border shadow-lg rounded-md bg-white w-96">
                    <form action="manageAccount" method="post" class="flex flex-col gap-y-5 items-center">
                        <input type="hidden" name="action" value="add" />
                        <input type="hidden" name="userID" />

                        <label class="block w-full" for="usernameIn">
                            <span class="text-gray-700">Username</span>
                            <input class="form-input mt-1 block w-full" type="text" name="username" id="usernameIn"
                                   required />
                        </label>

                        <label class="block w-full" for="passwordIn">
                            <span id="passwordSpan" class="text-gray-700">New Password</span>
                            <input class="form-input mt-1 block w-full" type="password" name="password"
                                   id="passwordIn" required /></label>

                        <label class="block w-full" for="fullNameIn">
                            <span class="text-gray-700">Full Name</span>
                            <input class="form-input mt-1 block w-full" type="text" name="fullName" id="fullNameIn"
                                   required /></label>

                        <label class="block w-full" for="emailIn">
                            <span class="text-gray-700">Email</span>
                            <input class="form-input mt-1 block w-full" type="email" name="email" id="emailIn"
                                   required /></label>

                        <label class="block w-full" for="roleNameIn">
                            <span class="text-gray-700">Role</span>
                            <select class="form-select mt-1 block w-full" name="roleName" id="roleNameIn" required>
                                <c:forEach var="role" items="${requestScope.roles}">
                                    <option value="${role.getRoleName()}">${role.getRoleName()}</option>
                                </c:forEach>
                            </select>
                        </label>

                        <div class="items-center px-4 py-3 flex flex-1 gap-5 justify-center">
                            <button
                                class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-green-600 bg-green-50 border-green-200 hover:bg-green-100 active:bg-green-200 focus:ring-green-300"
                                type="submit">Add</button>
                            <button form="" onclick="{
                                                                            document.getElementById('addoredit-modal').style.display = 'none'
                                                                        }"
                                    class="w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-gray-600 bg-gray-50 border-gray-200 hover:bg-gray-100 active:bg-gray-200 focus:ring-gray-300">Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
        <script>
            function addAccount() {
                document.querySelector('#addoredit-modal input[type=hidden]').value = 'add';

                var userNameIn = document.getElementById('usernameIn');
                userNameIn.value = '';
                userNameIn.readOnly = false;

                document.getElementById('passwordSpan').innerHTML = 'Password';
                document.getElementById('passwordIn').required = true;
                document.getElementById('fullNameIn').value = '';
                document.getElementById('emailIn').value = '';

                var btn = document.querySelector('#addoredit-modal button[type=submit]');
                btn.innerHTML = 'Add'
                btn.className = 'w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-green-600 bg-green-50 border-green-200 hover:bg-green-100 active:bg-green-200 focus:ring-green-300'
                document.getElementById('addoredit-modal').style.display = 'flex';
            }
            function editAccount(id, username, fullName, email, selectedRole) {
                document.querySelector('#addoredit-modal input[name=userID]').value = id;
                document.querySelector('#addoredit-modal input[type=hidden]').value = 'edit';

                var userNameIn = document.getElementById('usernameIn');
                userNameIn.value = username;
                userNameIn.readOnly = true;

                document.getElementById('passwordSpan').innerHTML = 'New Password';
                document.getElementById('passwordIn').required = false;
                document.getElementById('fullNameIn').value = fullName;
                document.getElementById('emailIn').value = email;

                for (const el of document.querySelectorAll('#roleNameIn > option')) {
                    if (el.value === selectedRole)
                        el.selected = true;
                }

                var btn = document.querySelector('#addoredit-modal button[type=submit]');
                btn.innerHTML = 'Edit'
                btn.className = 'w-28 py-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-blue-600 bg-blue-50 border-blue-200 hover:bg-blue-100 active:bg-blue-200 focus:ring-blue-300'
                document.getElementById('addoredit-modal').style.display = 'flex';
            }
            function removeAccount(id, username) {
                document.getElementById('remove-msg').innerHTML = 'Do you want to remove "' + username + '"?<br/>This action also removes all related information'
                document.getElementById('remove-modal').style.display = 'flex';
                document.getElementById('remove-userID').value = id;
            }
        </script>
    </body>

</html>