import { Component } from '@angular/core';
import { Roomsearch } from '../roomsearch/roomsearch';
import { Roomresult } from '../roomresult/roomresult';

@Component({
  selector: 'app-home',
  imports: [Roomsearch, Roomresult],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  searchResults: any[] = []; // store the results of the search room

  // handle the result coming from the search room
  handleSearchResult(results:any){
    this.searchResults = results;
  }

}
