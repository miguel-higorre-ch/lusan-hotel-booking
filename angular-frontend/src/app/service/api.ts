import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import CryptoJS from 'crypto-js';

@Injectable({
  providedIn: 'root'
})
export class Api {

  private static BASE_URL = 'http://localhost:7070/api';
  public static IMAGE_BASE_URL = 'http://localhost:7070';
  private static ENCRYPTION_KEY = 'lusan-encryption-key';
  
  constructor(private http: HttpClient) { }

  // Encryption and save token or role to localstorage
  encryptAndSaveToStorage(key: string, value: string) : void{
    const encryptValue = CryptoJS.AES.encrypt(
      value, 
      Api.ENCRYPTION_KEY
    ).toString();
    localStorage.setItem(key, encryptValue);
  }


  // retrieve from localstorage and decrypt
  private getFromStorageAndDecrypt(key: string) : string | null{
    try {
      const encryptedValue = localStorage.getItem(key);
      if (!encryptedValue) return null
      return CryptoJS.AES.decrypt(
        encryptedValue, 
        Api.ENCRYPTION_KEY
      ).toString(CryptoJS.enc.Utf8);
    } catch (error) {
      return null;
    }
  }

  // clear authentication data
  private clearAuth(): void{
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  
  }

  private getHeader(): HttpHeaders {
    const token = this.getFromStorageAndDecrypt('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  // AUTH Api methods
  registerUser(body: any): Observable<any> {
    return this.http.post(`${Api.BASE_URL}/auth/register`, body);
  }

  loginUser(body: any): Observable<any> {
    return this.http.post(`${Api.BASE_URL}/auth/login`, body);
  }

  // USER Api methods
  myProfile(): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/users/account`, { 
      headers: this.getHeader(), 
    });
  }

  myBookings(): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/users/bookings`, { 
      headers: this.getHeader(), 
    });
  }

  deleteAccount(): Observable<any> {
    return this.http.delete(`${Api.BASE_URL}/users/delete`, { 
      headers: this.getHeader(), 
    });
  }

  // Rooms Api methods
  addRoom(formData: any): Observable<any> {
    return this.http.post(`${Api.BASE_URL}/rooms/add`, formData,{
      headers: this.getHeader(),
    });
  }

  updateRoom(formData: any): Observable<any> {
    return this.http.put(`${Api.BASE_URL}/rooms/update`, formData, {
      headers: this.getHeader(),
    });
  }

  getAvailableRooms(
    checkInDate: string,
    checkOutDate: string,
    roomType: string
  ): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/rooms/available`, {
      params: { checkInDate, checkOutDate, roomType},
    });
  }

  getRoomTypes(): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/rooms/types`);
  }

  getAllRooms(): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/rooms/all`);
  }

  getRoomById(roomId: string): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/rooms/${roomId}`);
  }
  
  deleteRoom(roomId: string): Observable<any> {
    return this.http.delete(`${Api.BASE_URL}/rooms/delete/${roomId}`, {
      headers: this.getHeader(),
    });
  }

  // Bookings Api methods
  bookRoom(booking: any): Observable<any> {
    return this.http.post(`${Api.BASE_URL}/bookings/add`, booking, {
      headers: this.getHeader(),
    });
  }

  getAllBookings(): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/bookings/all`, {
      headers: this.getHeader(),
    });
  }

  updateBooking(booking: any): Observable<any> {
    return this.http.put(`${Api.BASE_URL}/bookings/update`, booking, {
      headers: this.getHeader(),
    });
  }

  getBookingByReference(bookingCode: string): Observable<any> {
    return this.http.get(`${Api.BASE_URL}/bookings/${bookingCode}`);
  }

  // Payment Api methods
  proceedForPayment(body: any): Observable<any> {
    return this.http.post(`${Api.BASE_URL}/payments/pay`, body, {
      headers: this.getHeader(),
    });
  }

  updateBookingPayment(body: any): Observable<any> {
    return this.http.put(`${Api.BASE_URL}/payments/update`, body, {
      headers: this.getHeader(),
    });
  }


  // Authentication checker
  logout(): void {
    this.clearAuth();
  }

  isAuthenticated(): boolean {
    const token = this.getFromStorageAndDecrypt('token');
    return !!token;
  }

  isAdmin(): boolean {
    const role = this.getFromStorageAndDecrypt('role');
    return role === 'ADMIN';
  }

  isCustomer(): boolean {
    const role = this.getFromStorageAndDecrypt('role');
    return role === 'CUSTOMER';
  }

}
