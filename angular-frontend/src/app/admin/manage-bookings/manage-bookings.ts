import { Component } from '@angular/core';
import { Api } from '../../service/api';
import { Router } from '@angular/router';
import { Pagination } from '../../pagination/pagination';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-manage-bookings',
  imports: [CommonModule, FormsModule, Pagination],
  templateUrl: './manage-bookings.html',
  styleUrl: './manage-bookings.css'
})
export class ManageBookings {


  
  bookings: any[] = []; // Store all bookings
  filteredBookings: any[] = []; // Store filtered bookings based on search term
  searchTerm: string = ''; // Search term for filtering bookings
  currentPage: number = 1; // Current page for pagination
  bookingsPerPage: number = 10; // Number of bookings per page
  error:any =null;

  constructor(private api: Api, private router: Router) {}

  ngOnInit(): void {
    this.fetchBookings();
  }

  // Fetch bookings data from the API
  fetchBookings(): void {
    this.api.getAllBookings().subscribe({
      next: (response: any) => {
        this.bookings = response.bookings || []; // Set bookings or an empty array if no data
        this.filteredBookings = this.bookings; // Initially, filtered bookings are the same as all bookings
      },
      error: (error) => {
        this.error('Error fetching bookings:', error.message);
      }
  });
  }

  // Update filtered bookings based on the search term
  handleSearchChange(): void {
    if (!this.searchTerm) {
      this.filteredBookings = this.bookings; // If no search term, show all bookings
    } else {
      this.filteredBookings = this.bookings.filter((booking) =>
        booking.bookingReference?.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }

    this.currentPage = 1; // Reset to the first page when search term changes
  }

  // Handle page changes for pagination (this is the handler for the paginate event)
  onPageChange(pageNumber: number): void {
    this.currentPage = pageNumber;
  }

  // Get bookings for the current page
  get currentBookings(): any[] {
    const indexOfLastBooking = this.currentPage * this.bookingsPerPage;
    const indexOfFirstBooking = indexOfLastBooking - this.bookingsPerPage;
    return this.filteredBookings.slice(indexOfFirstBooking, indexOfLastBooking);
  }

  // Navigate to the booking management page
  manageBooking(bookingReference: string): void {
    this.router.navigate([`/admin/edit-booking/${bookingReference}`]);
  }

}
