<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>${requestScope.msg}</title>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/background.jpg" as="image" />
    </head>
    <body>
        <div class="w-screen h-screen flex flex-col justify-center items-center gap-6">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-60 stroke-current text-red-400" fill="none" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <div>
                <p class="font-bold text-gray-600 text-5xl text-center">${requestScope.msg}</p>
                <p class="font-light text-gray-500 text-xl text-center mt-2">${requestScope.detail}</p>
            </div>
            <a href="${requestScope.backURL}" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded">Take Me Back</a>
        </div>
    </body>
</html>
