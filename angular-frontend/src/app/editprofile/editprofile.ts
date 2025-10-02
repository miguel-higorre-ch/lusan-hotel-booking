import { Component } from '@angular/core';
import { Api } from '../service/api';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';


@Component({
  selector: 'app-editprofile',
  imports: [CommonModule],
  templateUrl: './editprofile.html',
  styleUrl: './editprofile.css'
})
export class Editprofile {

user: any = null;
  error: any = null;

  constructor(private api: Api, private router: Router) {}

  ngOnInit(): void {
    this.fetchUserProfile();
  }

  // Fetch user profile on component initialization
  fetchUserProfile(): void {
    this.api.myProfile().subscribe({
      next: (response: any) => {
        this.user = response.user;
        console.log(this.user); // Optional, for debugging
      },
      error: (err) => {
        this.showError(err?.error?.message || 'Error fetching user profile');
      },
    });
  }

  showError(message: string) {
    this.error = message;
    setTimeout(() => {
      this.error = null;
    }, 4000);
  }

  // Handle account deletion
  handleDeleteProfile(): void {
    if (
      !window.confirm(
        'Are you sure you want to delete your account? If you delete your account, you will lose access to your profile and booking history.'
      )
    ) {
      return;
    }

    this.api.deleteAccount().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.showError(err?.error?.message || 'Error Deleting account');
      },
    });
  }

}
