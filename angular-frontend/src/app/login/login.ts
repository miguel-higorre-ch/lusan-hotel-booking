import { Component } from '@angular/core';
import { Api } from '../service/api';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: '../auth.css'
})
export class Login {

  constructor(private api: Api, private router: Router) {}

  formData: any = {
    email: '',
    password: ''
  };
  error: any = null;

  handleSubmit() {
    if (!this.formData.email || !this.formData.password) {
      this.showError('Please fill in all fields');
      return;
    }

    this.api.loginUser(this.formData).subscribe({
      next: (resp: any) => {
        if(resp.status === 200){
          this.api.encryptAndSaveToStorage('token', resp.token);
          this.api.encryptAndSaveToStorage('role', resp.role);
          this.router.navigate(['/home']);
        }
      },
      error: (error: any) => {
        this.showError(error?.error?.message || error.message || 'Error Logging In' + error);
      }
    });
  }


    showError(msg: string): void {
    this.error = msg;
    setTimeout(() => {
      this.error = null;
    }, 4000);
  }

}
