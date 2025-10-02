import { Component } from '@angular/core';
import { Api } from '../service/api';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: '../auth.css',
})
export class Register {
  constructor(private api: Api, private router: Router) {}

  formData: any = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: '',
  };
  error: any = null;

  handleSubmit() {
    if (
      !this.formData.firstName ||
      !this.formData.lastName ||
      !this.formData.email ||
      !this.formData.phoneNumber ||
      !this.formData.password
    ) {
      this.showError('Please fill in all fields');
      return;
    }
    this.api.registerUser(this.formData).subscribe({
      next: (resp: any) => {
        this;
      },
      error: (error: any) => {
        this.showError(error?.error?.message || error.message || 'Error Registering User' + error);
        
      },
    });
  }

  showError(msg: string): void {
    this.error = msg;
    setTimeout(() => {
      this.error = null;
    }, 4000);
  }
}
