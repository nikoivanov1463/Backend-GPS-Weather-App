tailwind.config = {
    theme: {
        extend: {
            colors: {
                'flutter-blue': '#02569B',
                'flutter-light-blue': '#0175C2',
                'flutter-dark-blue': '#013747',
            }
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // Password toggle functionality
    const toggleNewPassword = document.getElementById('toggleNewPassword');
    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const newPasswordInput = document.getElementById('newPassword');
    const confirmPasswordInput = document.getElementById('confirmPassword');

    toggleNewPassword.addEventListener('click', function() {
        if (newPasswordInput.type === 'password') {
            newPasswordInput.type = 'text';
            toggleNewPassword.classList.replace('fa-eye-slash', 'fa-eye');
        } else {
            newPasswordInput.type = 'password';
            toggleNewPassword.classList.replace('fa-eye', 'fa-eye-slash');
        }
    });

    toggleConfirmPassword.addEventListener('click', function() {
        if (confirmPasswordInput.type === 'password') {
            confirmPasswordInput.type = 'text';
            toggleConfirmPassword.classList.replace('fa-eye-slash', 'fa-eye');
        } else {
            confirmPasswordInput.type = 'password';
            toggleConfirmPassword.classList.replace('fa-eye', 'fa-eye-slash');
        }
    });

    // Password validation
    // newPasswordInput.addEventListener('input', validatePassword);
    // confirmPasswordInput.addEventListener('input', validatePassword);

    // function validatePassword() {
    //     const newPassword = newPasswordInput.value;
    //     const confirmPassword = confirmPasswordInput.value;
    //     const newPasswordError = document.getElementById('newPasswordError');
    //     const newPasswordSuccess = document.getElementById('newPasswordSuccess');
    //     const confirmPasswordError = document.getElementById('confirmPasswordError');
    //     const confirmPasswordSuccess = document.getElementById('confirmPasswordSuccess');
    //
    //     // Validate new password
    //     if (newPassword.length > 0 && newPassword.length < 8) {
    //         newPasswordError.style.display = 'block';
    //         newPasswordSuccess.style.display = 'none';
    //     } else if (newPassword.length >= 8) {
    //         newPasswordError.style.display = 'none';
    //         newPasswordSuccess.style.display = 'block';
    //     } else {
    //         newPasswordError.style.display = 'none';
    //         newPasswordSuccess.style.display = 'none';
    //     }
    //
    //     // Validate password confirmation
    //     if (confirmPassword.length > 0 && newPassword !== confirmPassword) {
    //         confirmPasswordError.style.display = 'block';
    //         confirmPasswordSuccess.style.display = 'none';
    //     } else if (confirmPassword.length > 0 && newPassword === confirmPassword) {
    //         confirmPasswordError.style.display = 'none';
    //         confirmPasswordSuccess.style.display = 'block';
    //     } else {
    //         confirmPasswordError.style.display = 'none';
    //         confirmPasswordSuccess.style.display = 'none';
    //     }
    // }

    // Form submission
    // document.getElementById('resetForm').addEventListener('submit', function(e) {
    //     const newPassword = newPasswordInput.value;
    //     const confirmPassword = confirmPasswordInput.value;
    //
    //     // Final validation
    //     if (newPassword.length < 8) {
    //         document.getElementById('newPasswordError').style.display = 'block';
    //         return;
    //     }
    //
    //     if (newPassword !== confirmPassword) {
    //         document.getElementById('confirmPasswordError').style.display = 'block';
    //         return;
    //     }
    //
    //     // Simulate form submission success
    //     document.getElementById('resetForm').classList.add('hidden');
    //     document.getElementById('successMessage').classList.remove('hidden');
    //
    //     // Animate progress bar to complete
    //     document.querySelector('.h-full').classList.add('w-full');
    // });
});