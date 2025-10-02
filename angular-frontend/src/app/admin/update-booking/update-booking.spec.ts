import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateBooking } from './update-booking';

describe('UpdateBooking', () => {
  let component: UpdateBooking;
  let fixture: ComponentFixture<UpdateBooking>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateBooking]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateBooking);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
