import { Component } from '@angular/core';
import { Api } from '../service/api';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-findbooking',
  imports: [CommonModule, FormsModule],
  templateUrl: './findbooking.html',
  styleUrl: '../admin/admin-forms.css'
})
export class Findbooking {

  constructor(private api: Api){}
  
  confirmationCode: string = '';
  bookingDetails: any = null;
  error: any = null;

  imageBaseUrl = Api.IMAGE_BASE_URL;

  handleSearch(){
    if (!this.confirmationCode.trim()) {
      this.showError("Please enter the booking confirmation Code");
      return;
    }

    this.api.getBookingByReference(this.confirmationCode).subscribe({
      next: (res) => {
        this.bookingDetails = res.booking;
      },
      error: (err) => {
        this.showError(err?.error.message || "Error fetching booking details")
      },
    })
  }

  showError(err: any): void{
    console.log(err)
    this.error = err;
    setTimeout(() => {
      this.error = ''
    }, 4000)
  }

}
