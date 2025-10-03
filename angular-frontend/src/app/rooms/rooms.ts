import { Component } from '@angular/core';
import { Pagination } from '../pagination/pagination';
import { Roomresult } from '../roomresult/roomresult';
import { Roomsearch } from '../roomsearch/roomsearch';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api } from '../service/api';

@Component({
  selector: 'app-rooms',
  imports: [Pagination, Roomresult,  CommonModule, FormsModule],
  templateUrl: './rooms.html',
  styleUrl: './rooms.css',
})
export class Rooms {
  rooms: any[] = [];
  filteredRooms: any[] = [];
  roomTypes: string[] = [];
  selectedRoomType: string = '';
  currentPage: number = 1;
  roomsPerPage: number = 8;
  error: any = null;

  imageBaseUrl = Api.IMAGE_BASE_URL;

  constructor(private api: Api) {}

  ngOnInit(): void {
    this.fetchRooms();
    this.fetchRoomTypes();
  }

  showError(msg: string): void {
    this.error = msg;
    setTimeout(() => {
      this.error = null;
    }, 5000);
  }

  // Fetch all rooms from the API
  fetchRooms() {
    this.api.getAllRooms().subscribe({
      next: (response: any) => {
        this.rooms = response.rooms;
        this.filteredRooms = response.rooms;
      },
      error: (err) => {
        this.showError(err?.error?.message || 'Error fetching rooms:' + err);
      },
    });
  }

  // Fetch room types from the API
  fetchRoomTypes() {
    this.api.getRoomTypes().subscribe({
      next: (types: string[]) => {
        this.roomTypes = types;
      },
      error: (err) => {
        this.showError(err?.error?.message || 'Error fetching room Types:' + err);
      },
    });
  }

  // Handle the search result passed from RoomsearchComponent
  handleSearchResult(results: any[]) {
    this.rooms = results;
    this.filteredRooms = results;
  }

  // Handle changes to room type filter
  handleRoomTypeChange(event: any) {
    const selectedType = event.target.value;
    this.selectedRoomType = selectedType;
    this.filterRooms(selectedType);
  }

  // Filter rooms by type
  filterRooms(type: string) {
    if (type === '') {
      this.filteredRooms = this.rooms;
    } else {
      this.filteredRooms = this.rooms.filter((room) => room.type === type);
    }
    this.currentPage = 1;
  }

  // Pagination logic
  get indexOfLastRoom() {
    return this.currentPage * this.roomsPerPage;
  }

  get indexOfFirstRoom() {
    return this.indexOfLastRoom - this.roomsPerPage;
  }

  get currentRooms() {
    return this.filteredRooms.slice(this.indexOfFirstRoom, this.indexOfLastRoom);
  }

  // Pagination function to change page
  paginate(pageNumber: number) {
    this.currentPage = pageNumber;
  }
}
