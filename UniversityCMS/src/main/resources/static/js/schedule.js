document.getElementById('course').addEventListener('change',
	function() {
		const courseId = this.value;
		const startDate = document.getElementById('lessonStart').value;
		const endDate = document.getElementById('lessonEnd').value;
		const url = '/schedules/course/' + courseId
			+ '/teachers?startDate=' + encodeURIComponent(startDate)
			+ '&endDate=' + encodeURIComponent(endDate);

		fetch(url)
			.then(response => {
				if (!response.ok) {
					throw new Error('Network response was not ok ' + response.statusText);
				}
				return response.json();
			})
			.then(data => {
				const teacherSelect = document.getElementById('teacher');
				teacherSelect.innerHTML = '';
				if (data.length === 0) {
					const option = document.createElement('option');
					option.text = 'No teachers available';
					option.disabled = true;
					option.selected = true;
					teacherSelect.appendChild(option);
				} else {
					data.forEach(function(teacher) {
						const option = document.createElement('option');
						option.value = teacher.userId;
						if (!teacher.available) {
							option.text = teacher.userId + " | " 
								+ teacher.firstName + ' ' + teacher.lastName + ' (BUSY)';
							option.style.color = 'gray';
							option.disabled = true;
						} else {
							option.text = teacher.userId + " | " 
								+ teacher.firstName + ' ' + teacher.lastName;
						}
						teacherSelect.appendChild(option);
					});
				}
			})
			.catch(error => {
				console.error('Error fetching teachers:', error);
				alert('There was a problem retrieving the teachers. Please choose lesson time.');
			});
	});

function setLessonDuration(minutes) {
	const startInput = document.getElementById('lessonStart');
	const endInput = document.getElementById('lessonEnd');
	const startTime = new Date(startInput.value);

	if (isNaN(startTime.getTime())) {
		return alert('Please select a valid start time first.');
	}

	const endTime = new Date(startTime.getTime() + minutes * 60000);

	const offset = startTime.getTimezoneOffset() * 60000;
	const localEndTime = new Date(endTime.getTime() - offset);

	endInput.value = localEndTime.toISOString().slice(0, 16);
	endInput.dispatchEvent(new Event('change'));
}

document.addEventListener('DOMContentLoaded', function() {
	const startTime = document.getElementById('lessonStart');
	const endTime = document.getElementById('lessonEnd');
	const classroom = document.getElementById('classroom');
	const teacherSelect = document.getElementById('teacher');
	const createButton = document.querySelector('button[type="submit"]');

	// Disable the create button initially
	createButton.disabled = true;

	function checkTimeFields() {
		if (startTime.value && endTime.value) {
			fetchClassrooms(startTime.value, endTime.value);
		} else {
			classroom.disabled = true;
			createButton.disabled = true;
		}
	}

	startTime.addEventListener('change', checkTimeFields);
	endTime.addEventListener('change', checkTimeFields);

	function fetchClassrooms(startDate, endDate) {

		fetch(`/schedules/getClassrooms?startDate=${startDate}&endDate=${endDate}`)
			.then(response => response.json())
			.then(classrooms => {
				updateClassroomOptions(classrooms);
				classroom.disabled = false;
				validateForm();
			})
			.catch(error => {
				console.error('Error fetching classrooms:', error);
				alert('There was a problem retrieving the classrooms. Please try again later.');
				createButton.disabled = true;
			});
	}

	function updateClassroomOptions(classrooms) {
		classroom.innerHTML = '';

		classrooms.forEach(cls => {
			const option = document.createElement('option');
			option.value = cls.classroomId;

			if (cls.occupied) {
				option.textContent = `${cls.number} - Occupied: 
					${cls.courseName}, ${cls.teacher.firstName} 
					${cls.teacher.lastName}, ${formatDate(cls.startTime)} 
					to ${formatDate(cls.endTime)}`;
				option.classList.add('text-danger');
				option.disabled = true;
			} else {
				option.textContent = `${cls.number} - Available`;
			}

			classroom.appendChild(option);
		});

		validateForm();
	}

	function validateForm() {
		const allFieldsValid = startTime.value && endTime.value
			&& teacherSelect.value && !teacherSelect.disabled
			&& classroom.value && !classroom.disabled;
		createButton.disabled = !allFieldsValid;
	}

	function formatDate(dateTime) {
		const date = new Date(dateTime);
		return date.toLocaleString('ru-RU', { dateStyle: 'short', timeStyle: 'short' });
	}
});
