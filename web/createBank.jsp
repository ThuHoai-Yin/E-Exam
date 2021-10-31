<%@page contentType="text/html" pageEncoding="UTF-8"%> <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Create Bank</title>
        <link href="static/styles.css" rel="stylesheet">
        <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
        <link href="https://unpkg.com/@tailwindcss/custom-forms@0.2.1/dist/custom-forms.min.css" rel="stylesheet" />
        <link rel="preload" href="assets/exam.png" as="image" />
    </head>
    <body>
        <c:set var="user" value="${sessionScope.user}" />
        <jsp:include page="header.jsp" />    
        <main class="pt-16 flex flex-col items-center">
            <form action="createBank" method="post" id="form" method="post" class="my-10 flex flex-col items-center">
                <div class="w-full p-5 bg-white flex flex-col gap-y-3 mb-3">
                    <h1 class="font-semibold text-center text-xl">Create New Bank</h1>
                    <input type="txt" name="bankName" placeholder="Enter bank name" class="mt-2 form-input w-full text-center" required/>
                    <input type="txt" name="courseName" placeholder="Enter course name" class="mt-2 form-input w-full text-center" required/>
                </div>
                <div id="question-container" class="question flex flex-col gap-y-3" style="width: 60rem">                    
                </div>
                <div class="flex flex-1 justify-center w-full relative mt-3">
                    <a href="manageBank" class="absolute left-0" id="redirect">
                        <button type="submit" form="redirect"
                                class="w-28 p-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-red-600 bg-red-50 border-red-200 hover:bg-red-100 active:bg-red-200 focus:ring-red-300">Back</button>
                    </a>   
                    <button onclick="addQuestion(this)" type="button"
                            class="p-2 rounded-full bg-white hover:opacity-80">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                        </svg>
                    </button>   
                    <button type="submit"
                            class="absolute right-0 w-28 p-2 rounded-md text-sm font-medium border shadow focus:outline-none focus:ring transition text-green-600 bg-green-50 border-green-200 hover:bg-green-100 active:bg-green-200 focus:ring-green-300">Submit</button>
                </div>
            </form>
            <div>
                <div class="hidden bg-white rounded w-full p-10 pb-3 flex flex-col items-center relative" id="question-template">
                    <div class="flex flex-1 w-full">
                        <div>
                            <span
                                class="num rounded-full w-9 h-9 text-center bg-blue-600 text-white inline-block mr-6"
                                style="line-height: 2.25rem">
                                1
                            </span>
                        </div>
                        <textarea class="h-24 resize-none form-textarea font-bold w-full mb-1 mr-14" placeholder="Enter question content" required></textarea>
                    </div>
                    <div class="answer-container pt-4 pl-6 pr-3 flex flex-col gap-y-3 border-t-gray-500 border-t-2 w-full">                       
                    </div>
                    <button type="button"
                            onclick="addAnswer(this)"
                            class="p-1 rounded-full bg-transparent hover:opacity-80">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                        </svg>
                    </button>
                    <div class="absolute -right-12 top-0 bottom-0 flex items-center">
                        <button onclick="removeQuestion(this)" type="button"
                                class="p-2 rounded-full bg-white hover:opacity-80">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                </div>
                <div class="hidden inline-flex items-center w-full" id="answer-template">
                    <input
                        class="form-checkbox h-5 w-5 text-blue-600"
                        onchange=""
                        type="checkbox"
                        />
                    <input type="txt" class="form-input text-gray-700 ml-2 w-full" placeholder="Enter answer content" required/>
                    <button onclick="removeAnswer(this)"
                            type="button"
                            class="p-1 rounded-full bg-transparent hover:opacity-80">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>   
            </div>
        </main>
        <script>
            var counter = 0;

            function addAnswer(btn) {
                let parentId = btn.parentElement.getElementsByTagName('textarea')[0].getAttribute('name').split('.')[1];
                let target = btn.parentElement.getElementsByClassName('answer-container')[0];
                let childId = makeid(4);
                let el = document.getElementById('answer-template').cloneNode(true);
                el.id = '';
                el.style.display = 'flex';
                el.querySelector('input[type=txt]').setAttribute('name', 'answer.' + parentId + '.' + childId);
                el.querySelector('input[type=checkbox]').setAttribute('name', 'correct.' + parentId + '.' + childId);
                target.appendChild(el);
            }
            function addQuestion() {
                counter++;
                let el = document.getElementById('question-template').cloneNode(true);
                el.id = '';
                el.style.display = 'flex';
                el.getElementsByTagName('textarea')[0].setAttribute('name', 'question.' + makeid(4));
                el.getElementsByClassName('num')[0].innerHTML = counter;
                document.getElementById('question-container').appendChild(el);
            }
            function removeAnswer(btn) {
                btn.parentElement.remove();
            }
            function removeQuestion(btn) {
                let target = btn.parentElement.parentElement;
                let value = parseInt(target.getElementsByClassName('num')[0].innerHTML);
                target.remove();
                counter--;
                for (let el of document.querySelectorAll('#question-container>div')) {
                    let elNum = el.getElementsByClassName('num')[0];
                    let cValue = parseInt(elNum.innerHTML);
                    if (cValue > value)
                        elNum.innerHTML = cValue - 1;
                }
            }
            function makeid(length) {
                var result = '';
                var characters = 'abcdefghijklmnopqrstuvwxyz0123456789';
                var charactersLength = characters.length;
                for (var i = 0; i < length; i++) {
                    result += characters.charAt(Math.floor(Math.random() *
                            charactersLength));
                }
                return result;
            }
        </script>
    </body>
</html>
