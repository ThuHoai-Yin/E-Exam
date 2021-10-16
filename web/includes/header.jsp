<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
		<link rel="preload" href="assets/background.jpg" as="image" />
		<link rel="preload" href="assets/logo.png" as="image" />
	</head>
	<body class="bg-cover bg-fixed" style="background-image: url('assets/background.jpg')">
		<c:set var="userInfo" value="${sessionScope.UserFullName}" />
		<c:set var="auth" value="${sessionScope.Auth}" />
		<nav class="bg-gray-100 px-8 fixed w-screen z-10">
			<div class="relative flex items-center justify-between h-16">
				<div class="flex-1 flex items-center justify-start h-full">
					<div class="flex-shrink-0 flex items-center">
						<img class="block h-8 w-auto" src="assets/logo.png" alt="Logo" />
						<a class="font-sans text-3xl px-3">E-Exam</a>
					</div>
					<c:if test="${auth.getRole() == 'teacher'}">
						<div class="pl-12">
							<div class="flex space-x-4 h-full">
								<a href="dashboard.jsp" class="w-32 text-center py-2 text-base font-semibold border-b-2 border-purple-600">Home</a>
								<a href="bank.jsp" class="w-32 text-center py-2 text-base font-medium border-b-2">Bank</a>
								<a href="test.jsp" class="w-32 text-center py-2 text-base font-medium border-b-2">Test</a>
							</div>
						</div>
					</c:if>
				</div>
				<div class="absolute right-0 flex items-center pr-2">
					<div class="ml-3 relative">
						<button class="text-base font-semibold" id="dropdown-btn" onclick="toggleDropdown()">${userInfo}</button>
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
							<a href="logout" class="block px-4 py-2 text-sm text-gray-700">Sign out</a>
						</div>
					</div>
				</div>
			</div>
		</nav>
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
