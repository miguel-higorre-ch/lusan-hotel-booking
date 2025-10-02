import { Component } from '@angular/core';

import { RouterLink, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-payment-failure',
  imports: [],
  templateUrl: './payment-failure.html',
  styleUrl: './payment-failure.css',
})
export class PaymentFailure {
  bookingReference: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.bookingReference = this.route.snapshot.paramMap.get('bookingReference') || '';
  }
}
