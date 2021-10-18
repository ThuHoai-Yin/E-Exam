<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>${requestScope.Msg}</title>
		<link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
		<link rel="preload" href="assets/background.jpg" as="image" />
	</head>
	<body>
		<div class="w-screen h-screen flex flex-col justify-center items-center gap-6">
			<svg xmlns="http://www.w3.org/2000/svg" class="w-60 stroke-current text-green-400" fill="none" viewBox="0 0 20 20">
				<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
			</svg>
			<div>
				<p class="font-bold text-gray-600 text-5xl text-center">${requestScope.Msg}</p>
				<p class="font-light text-gray-500 text-xl text-center mt-2">${requestScope.Detail}</p>
			</div>
			<a href="home" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"> Take Me Home </a>
		</div>
	</body>
</html>
