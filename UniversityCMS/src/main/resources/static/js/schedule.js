document.addEventListener('DOMContentLoaded', function() {
	const startTime = document.getElementById('lessonStart');
	const endTime = document.getElementById('lessonEnd');
	const courseSelect = document.getElementById('course');
	const teacherSelect = document.getElementById('teacher');
	const classroomSelect = document.getElementById('classroom');
	const createButton = document.querySelector('button[type="submit"]');

	// Initial setup
	if (scheduleId == 0) {
		disableElements([createButton, courseSelect, teacherSelect, classroomSelect]);
	} else {
		// Editing mode: fetch and populate data based on the existing values
		checkTimeFields();
	}

	// Event Listeners
	startTime.addEventListener('change', () => checkTimeFields());
	endTime.addEventListener('change', () => checkTimeFields());
	courseSelect.addEventListener('change', handleCourseChange);

	// Functions
	function disableElements(elements) {
		elements.forEach(el => el.disabled = true);
	}

	function checkTimeFields() {
		if (startTime.value && endTime.value) {
			if (new Date(startTime.value) > new Date(endTime.value)) {
				alert('End time cannot be before start time. Please try again.');
				disableElements([courseSelect, classroomSelect, teacherSelect, createButton]);
			} else {
				// Fetch courses when time fields are set correctly
				fetchCourses().then(() => {
					const selectedCourseId = courseSelect.dataset.selected;
					if (selectedCourseId) {
						courseSelect.value = selectedCourseId;
						fetchTeachers(selectedCourseId, startTime.value, endTime.value);
					}
				});

				fetchClassrooms(startTime.value, endTime.value); // Fetch classrooms on every change
				courseSelect.disabled = false;
				classroomSelect.disabled = false;
			}
		} else {
			disableElements([classroomSelect, teacherSelect, createButton]);
		}
	}

	function handleCourseChange() {
		const courseId = courseSelect.value;
		const startDate = startTime.value;
		const endDate = endTime.value;

		if (courseId) {
			fetchTeachers(courseId, startDate, endDate);
			teacherSelect.disabled = false;
		} else {
			teacherSelect.disabled = true;
			teacherSelect.innerHTML = '<option value="" selected disabled>Select a teacher</option>';
		}
		validateForm();
	}

	function fetchCourses() {
		const url = '/courses/get-courses-list';

		return fetch(url)
			.then(handleResponse)
			.then(updateCoursesOptions)
			.catch(handleError('courses'));
	}

	function updateCoursesOptions(data) {
		courseSelect.innerHTML = '';
		const selectedCourseId = courseSelect.dataset.selected;
		
		addOption(courseSelect, 'Choose a course', '', false, !selectedCourseId); // Default option

		if (data.length === 0) {
			addOption(courseSelect, 'No courses available', '', true, false);
		} else {
			data.forEach(course => {
				const text = course.courseName;
				const value = course.courseId;
				const isSelected = value === selectedCourseId;
				addOption(courseSelect, text, value, false, isSelected);
			});
		}
	}

	function fetchTeachers(courseId, startDate, endDate) {
		const url = `/schedules/course/${courseId}/teachers?startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`;

		fetch(url)
			.then(handleResponse)
			.then(updateTeacherOptions)
			.catch(handleError('teachers'));
	}

	function updateTeacherOptions(data) {
		teacherSelect.innerHTML = '';
		const selectedTeacherId = teacherSelect.dataset.selected;

		if (data.length === 0) {
			addOption(teacherSelect, 'No teachers available', '', true, false);
			return;
		}

		data.forEach(teacher => {
			const value = String(teacher.userId);
			const isAssignedToCurrentSchedule = value === String(selectedTeacherId);
			const isAvailable = isAssignedToCurrentSchedule || teacher.available;
			const text = `${teacher.userId} | ${teacher.firstName} ${teacher.lastName}`;
			const displayText = isAvailable ? text : `${text} (BUSY)`;
			const isDisabled = !isAvailable;
			const isSelected = isAssignedToCurrentSchedule;
			const textColor = isDisabled ? 'gray' : 'black';

			addOption(teacherSelect, displayText, value, isDisabled, isSelected, true, textColor);
		});
	}

	function fetchClassrooms(startDate, endDate) {
		const url = `/schedules/getClassrooms?startDate=${startDate}&endDate=${endDate}&scheduleId=${scheduleId}`;

		fetch(url)
			.then(handleResponse)
			.then(updateClassroomOptions)
			.catch(handleError('classrooms'));
	}

	function updateClassroomOptions(classrooms) {
		classroomSelect.innerHTML = '';
		const selectedClassroomId = classroomSelect.dataset.selected;

		classrooms.forEach(cls => {
			const text = cls.occupied ? `${cls.number} - Occupied: ${cls.courseName}, 
                ${cls.teacher.firstName} ${cls.teacher.lastName},
                ${formatDate(cls.startTime)} to ${formatDate(cls.endTime)}`
				: `${cls.number} - Available`;
			const value = cls.classroomId;
			const isDisabled = cls.occupied;
			const isSelected = value === selectedClassroomId;
			addOption(classroomSelect, text, value, isDisabled, isSelected, true, cls.occupied ? 'red' : 'black');
		});

		validateForm();
	}

	function handleResponse(response) {
		if (!response.ok) throw new Error('Network response was not ok ' + response.statusText);
		return response.json();
	}

	function handleError(entity) {
		return error => {
			console.error(`Error fetching ${entity}:`, error);
			alert(`There was a problem retrieving the ${entity}. Please try again later.`);
			createButton.disabled = true;
		};
	}

	function addOption(selectElement, text, value, isDisabled, isSelected, isEnabled = true, color = 'black') {
		const option = document.createElement('option');
		option.text = text;
		option.value = value;
		option.disabled = isDisabled;
		option.selected = isSelected;
		option.style.color = color;
		if (isEnabled) selectElement.appendChild(option);
	}

	function validateForm() {
		const isValid = startTime.value && endTime.value && teacherSelect.value && !teacherSelect.disabled && classroomSelect.value && !classroomSelect.disabled;
		createButton.disabled = !isValid;
	}

	function formatDate(dateTime) {
		const date = new Date(dateTime);
		return date.toLocaleString('ru-RU', { dateStyle: 'short', timeStyle: 'short' });
	}
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
