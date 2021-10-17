<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>${requestScope.Detail}</title>
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/background.jpg" as="image" />
    </head>
    <body>
        <div class="w-screen h-screen flex justify-center items-center">
            <div class="flex flex-col items-center gap-6">
                <div class="flex">
                    <p class="font-extrabold text-blue-700 text-5xl border-r-dark-200 border-r-2 pr-6">${requestScope.Code}</p>
                    <div class="flex flex-col pl-6">
                        <p class="font-bold text-gray-600 text-5xl">${requestScope.Detail}</p>
                        <p class="font-light text-gray-500">${requestScope.Msg}</p>
                    </div>
                </div>
                <a href="home" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"> Take Me Home </a>
            </div>
        </div>
    </body>
</html>
