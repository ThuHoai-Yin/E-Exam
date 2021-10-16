<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet" />
		<link rel="preload" href="assets/exam.png" as="image" />
	</head>
	<body>
		<div class="absolute top-0 bottom-0 left-0 right-0 flex justify-center items-center">
			<form action="home" method="post" class="py-2 px-6 bg-gray-50 self-center flex items-center" style="border-radius: 5rem">
				<img class="w-10" src="assets/exam.png" alt="Exam icon" />
				<input
					class="bg-transparent h-16 w-96 font-semibold text-2xl outline-none text-center"
					placeholder="Enter exam code"
					type="text"
					name="testCode"
				/>
				<button type="submit" class="rounded-full w-10 h-10 font-extrabold bg-blue-500 inline-flex items-center justify-center">
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
