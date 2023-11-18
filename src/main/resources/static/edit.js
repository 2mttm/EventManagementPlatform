function toggleEdit(button) {
    var row = button.parentNode.parentNode.parentNode;
    var editableFields = row.querySelectorAll('.editable');
    editableFields.forEach(function(field) {
        field.contentEditable = true;
        field.classList.add('editing');
    });
    button.style.display = 'none';
    row.querySelector('button[onclick="saveChanges(this)"]').style.display = 'inline-block';
    row.querySelector('button[type="button"][onclick="cancelEdit(this)"]').style.display = 'inline-block';
}

function saveChanges(button) {
    var row = button.parentNode.parentNode;
    var editableFields = row.querySelectorAll('.editable');
    editableFields.forEach(function(field) {
        field.contentEditable = false;
        field.classList.remove('editing');
        // Update the text of the editable field into the corresponding hidden input value
        var fieldName = field.getAttribute('th:text');
        row.querySelector('input[name="' + fieldName + '"]').value = field.innerText.trim();
    });
    button.style.display = 'none';
    row.querySelector('button[onclick="toggleEdit(this)"]').style.display = 'inline-block';
    row.querySelector('button[type="button"][onclick="cancelEdit(this)"]').style.display = 'none';
    // Submit the form here if needed
//    row.querySelector('editForm').submit();
}

function cancelEdit(button) {
    var row = button.parentNode.parentNode;
    var editableFields = row.querySelectorAll('.editable');
    editableFields.forEach(function(field) {
        field.contentEditable = false;
        field.classList.remove('editing');
        // Reset the field values to initial values if needed
    });
    button.style.display = 'none';
    row.querySelector('button[onclick="toggleEdit(this)"]').style.display = 'inline-block';
    row.querySelector('button[type="button"][onclick="saveChanges(this)"]').style.display = 'none';
}
