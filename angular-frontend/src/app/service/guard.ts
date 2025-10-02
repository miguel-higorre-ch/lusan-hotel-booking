import { Injectable } from '@angular/core';
import { Api } from './api';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class Guard implements CanActivate {
  
  constructor(private api: Api, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot): boolean{
      const requiresAdmin = route.data['requiresAdmin'] || false;

      if(requiresAdmin){
        if(this.api.isAdmin()){
          return true; // allow access for admin if the user is an admin
        } else {
          this.router.navigate(['/login'], {
            queryParams: {returnUrl: state.url}
          });
          return false; // deny access
        }

      } else{
        if(this.api.isAuthenticated()){
          return true; // allow access
        } else{
          this.router.navigate(['/login'], {
            queryParams: {returnUrl: state.url}
          });
          return false; // deny access
        }
      }
    
  }
}
