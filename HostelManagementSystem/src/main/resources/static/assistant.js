// Step 2: Apply filter
    function applyFilter() {

        console.log("Function connected");

        const criteriaType = document.querySelector('input[name="criteriaType"]:checked').value;
        const distanceThreshold = document.getElementById('distanceThreshold').value;
        const studentCount = document.getElementById('studentCount').value;

        if (criteriaType === 'distance' && !distanceThreshold) {
            showMessage('Please enter distance threshold', 'error');
            return;
        }

        if (criteriaType === 'count' && !studentCount) {
            showMessage('Please enter number of students', 'error');
            return;
        }

    // Call backend to filter students
            fetch('/api/filter-students', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    faculty: document.getElementById('facultyId').value,
                    intake: document.getElementById('intakeId').value,
                    gender: document.getElementById('gender').value,
                    criteriaType: criteriaType,
                    distanceThreshold: distanceThreshold,
                    studentCount: studentCount
                })
            })
            .then(response => response.json())
            .then(data => {
                document.getElementById('filteredCount').classList.remove('hidden');
                document.getElementById('filteredStudentCount').textContent = data.count;
                filteredStudents = data.students;
                showMessage('Filter applied successfully', 'success');
            })
            .catch(error => {
                showMessage('Error applying filter', 'error');
                console.error('Error:', error);
            });

    }

// Step 3: Show hostel tab
    function showHostelTab(tabName) {
        // Hide all tabs
        document.querySelectorAll('.hostel-content').forEach(tab => {
            tab.classList.add('hidden');
        });

        // Remove active class from all tab buttons
        document.querySelectorAll('.hostel-tab').forEach(btn => {
            btn.classList.remove('border-blue-500', 'text-blue-600');
            btn.classList.add('border-transparent', 'text-gray-500');
        });

        // Show selected tab
        document.getElementById(tabName).classList.remove('hidden');

        // Add active class to selected tab button
        const tabBtn = document.getElementById('tab-' + tabName.split('-')[0]);
        tabBtn.classList.add('border-blue-500', 'text-blue-600');
        tabBtn.classList.remove('border-transparent', 'text-gray-500');
    }

    // Step 4: Select hostel
    function selectHostel(hostelId, hostelName) {
        if (filteredStudents.length === 0) {
            showMessage('Please apply filter first to see eligible students', 'error');
            return;
        }

        selectedHostel = { id: hostelId, name: hostelName };
        document.getElementById('selectedHostelName').textContent = hostelName;
        document.getElementById('allocationSection').classList.remove('hidden');

        // Get hostel capacity from backend
        fetch(`/api/hostel-capacity/${hostelId}`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('availableSpaces').textContent = data.availableSpaces;
            document.getElementById('remainingSpaces').textContent = data.availableSpaces;
            loadStudentList();
        });
    }

// Load and display student list
    function loadStudentList() {
        const container = document.getElementById('studentList');
        container.innerHTML = '';

        filteredStudents.forEach(student => {
            const div = document.createElement('div');
            div.className = 'flex items-center p-4 border-b border-gray-200 hover:bg-gray-50';
            div.innerHTML = `
                <input type="checkbox" class="student-checkbox mr-4" value="${student.studentId}"
                       onchange="updateSelection('${student.studentId}')">
                <div class="flex-1 grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                        <p class="font-medium text-gray-900">${student.studentId}</p>
                        <p class="text-sm text-gray-600">${student.name}</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Distance</p>
                        <p class="font-medium text-gray-900">${student.distance} km</p>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Faculty</p>
                        <p class="font-medium text-gray-900">${student.faculty}</p>
                    </div>
                </div>
            `;
            container.appendChild(div);
        });
    }

// Sort student list
    function sortStudentList() {
        const sortBy = document.getElementById('sortBy').value;

        if (sortBy === 'distance') {
            filteredStudents.sort((a, b) => b.distance - a.distance);
        } else if (sortBy === 'studentId') {
            filteredStudents.sort((a, b) => a.studentId.localeCompare(b.studentId));
        }

        loadStudentList();
    }

// Update selection
    function updateSelection(regNo) {
        const checkbox = document.querySelector(`input[value="${regNo}"]`);
        const availableSpaces = parseInt(document.getElementById('availableSpaces').textContent);

        if (checkbox.checked) {
            if (selectedStudents.size >= availableSpaces) {
                checkbox.checked = false;
                showMessage('Cannot select more students than available spaces', 'error');
                return;
            }
            selectedStudents.add(regNo);
        } else {
            selectedStudents.delete(regNo);
        }

        updateSelectionDisplay();
    }

    // Toggle select all
    function toggleSelectAll() {
        const selectAllCheckbox = document.getElementById('selectAll');
        const studentCheckboxes = document.querySelectorAll('.student-checkbox');
        const availableSpaces = parseInt(document.getElementById('availableSpaces').textContent);

        if (selectAllCheckbox.checked) {
            selectedStudents.clear();
            studentCheckboxes.forEach((checkbox, index) => {
                if (index < availableSpaces) {
                    checkbox.checked = true;
                    selectedStudents.add(checkbox.value);
                }
            });
        } else {
            studentCheckboxes.forEach(checkbox => {
                checkbox.checked = false;
            });
            selectedStudents.clear();
        }

        updateSelectionDisplay();
    }

    // Update selection display
    function updateSelectionDisplay() {
        const selectedCount = selectedStudents.size;
        const availableSpaces = parseInt(document.getElementById('availableSpaces').textContent);
        const remaining = availableSpaces - selectedCount;

        document.getElementById('selectedCount').textContent = selectedCount;
        document.getElementById('remainingSpaces').textContent = remaining;

        // Enable/disable submit button
        const submitBtn = document.getElementById('submitBtn');
        submitBtn.disabled = selectedCount === 0;

        if (selectedCount > 0) {
            submitBtn.classList.remove('disabled:bg-gray-300');
            submitBtn.classList.add('bg-blue-600', 'hover:bg-blue-700');
        }
    }

    // Step 5: Submit allocation
    function submitAllocation() {
        if (selectedStudents.size === 0) {
            showMessage('Please select at least one student', 'error');
            return;
        }

        const academicYear = document.getElementById('academicYear').value;


        const allocationData = {
            hostelId: selectedHostel.id,
            students: Array.from(selectedStudents),
            academicYear: academicYear
        };

        fetch('/api/allocate-students', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(allocationData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showMessage(`Successfully allocated ${selectedStudents.size} students to ${selectedHostel.name}`, 'success');

                // Remove allocated students from the list
                filteredStudents = filteredStudents.filter(student =>
                    !selectedStudents.has(student.regNo)
                );

                // Update available spaces
                const currentAvailable = parseInt(document.getElementById('availableSpaces').textContent);
                const newAvailable = currentAvailable - selectedStudents.size;
                document.getElementById('availableSpaces').textContent = newAvailable;
                document.getElementById('remainingSpaces').textContent = newAvailable;

                // Clear selection
                selectedStudents.clear();
                document.getElementById('selectedCount').textContent = '0';
                document.getElementById('selectAll').checked = false;

                // Reload student list
                removeAllocated();
                //loadStudentList();

                // Disable submit button
                document.getElementById('submitBtn').disabled = true;
            } else {
                showMessage('Error allocating students: ' + data.message, 'error');
            }
        })
        .catch(error => {
            showMessage('Error allocating students', 'error');
            console.error('Error:', error);
        });
    }

function removeAllocated() {
    const academicYear = document.getElementById('academicYear').value;

    // If no hostel selected yet, do nothing
    if (!selectedHostel || !selectedHostel.id) {
        console.warn("Hostel not selected yet.");
        return;
    }

    // API: get allocated students for the selected hostel + academic year
    fetch(`/api/allocated-students?hostelId=${selectedHostel.id}&year=${academicYear}`)
        .then(response => response.json())
        .then(allocatedList => {

            // allocatedList should be something like: ["S1234", "S5678"]

            // Remove allocated students from filteredStudents
            filteredStudents = filteredStudents.filter(student =>
                !allocatedList.includes(student.studentId)
            );

            // Reload the visible list after filtering
            loadStudentList();
        })
        .catch(error => {
            console.error("Error loading allocated students:", error);
        });
}
