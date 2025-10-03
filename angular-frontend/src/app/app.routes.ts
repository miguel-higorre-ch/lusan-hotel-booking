import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { Profile } from './profile/profile';
import { Editprofile } from './editprofile/editprofile';
import { Guard } from './service/guard';
import { Home } from './home/home';
import { Rooms } from './rooms/rooms';
import { Findbooking } from './findbooking/findbooking';
import { Roomdetails } from './roomdetails/roomdetails';
import { PaymentPage } from './payment/payment-page/payment-page';
import { PaymentSuccess } from './payment/payment-success/payment-success';
import { PaymentFailure } from './payment/payment-failure/payment-failure';
import { AdminHome } from './admin/admin-home/admin-home';
import { ManageRooms } from './admin/manage-rooms/manage-rooms';
import { AddRoom } from './admin/add-room/add-room';
import { EditRoom } from './admin/edit-room/edit-room';
import { ManageBookings } from './admin/manage-bookings/manage-bookings';
import { UpdateBooking } from './admin/update-booking/update-booking';
import { AdminRegister } from './admin/admin-register/admin-register';
import { About } from './about/about';
import { Testimonials } from './testimonials/testimonials';
import { Gallery } from './gallery/gallery';

export const routes: Routes = [
    {path: 'login', component: Login},
    {path: 'register', component: Register},
    {path: 'profile', component: Profile, canActivate: [Guard]},
    {path: 'edit-profile', component: Editprofile, canActivate: [Guard]},
    {path: 'home', component: Home, data: {animation: 'home'}},
    {path: 'rooms', component: Rooms, data: {animation: 'rooms'}},
    {path: 'find-booking', component: Findbooking, canActivate: [Guard], data: {animation: 'find-booking'}},
    {path: 'room-details/:id', component: Roomdetails, canActivate: [Guard]},
    {path: 'about', component: About, data: {animation: 'about'}},
    {path: 'testimonials', component: Testimonials, data: {animation: 'testimonials'}},
    {path: 'gallery', component: Gallery, data: {animation: 'gallery'}},
    
    //PAYMENT ROUTES
    {path: 'payment/:bookingReference/:amount', component:PaymentPage, canActivate: [Guard]},
    {path: 'payment-success/:bookingReference', component:PaymentSuccess, canActivate: [Guard]},
    {path: 'payment-failure/:bookingReference', component:PaymentFailure, canActivate: [Guard]},

    //ADMIN ROUTES
    {path: 'admin', component: AdminHome,  canActivate: [Guard], data: {requiresAdmin: true, animation: 'admin'}},
    {path: 'admin/manage-rooms', component: ManageRooms, canActivate: [Guard], data: {requiresAdmin: true}},
    {path: 'admin/add-room', component: AddRoom, canActivate: [Guard], data: {requiresAdmin: true}},
    {path: 'admin/edit-room/:id', component: EditRoom, canActivate: [Guard], data: {requiresAdmin: true}},

    {path: 'admin/manage-bookings', component: ManageBookings, canActivate: [Guard], data: {requiresAdmin: true}},
    {path: 'admin/edit-booking/:bookingCode', component: UpdateBooking, canActivate: [Guard], data: {requiresAdmin: true}},
    {path: 'admin/admin-register', component: AdminRegister, canActivate: [Guard], data: {requiresAdmin: true}},

    {path: '**', redirectTo: '/home'},
    
];
