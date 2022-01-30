import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Gameevent } from 'src/app/models/gameevent';
import { GameeventService } from 'src/app/services/gameevent.service';

@Component({
  selector: 'app-gameeventdetails',
  templateUrl: './gameeventdetails.component.html',
  styleUrls: ['./gameeventdetails.component.css']
})
export class GameeventdetailsComponent implements OnInit {
  gameEvent: Gameevent = new Gameevent();
  id: number = 0;
  guestNames = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private gameEventSvc: GameeventService,
  ) { }

  ngOnInit(): void {
    let idStr = this.route.snapshot.paramMap.get('eventId');
    if (idStr) {
      this.id = Number.parseInt(idStr);
    }
    this.load();
    this.loadGuests();
  }

  load() {
    this.gameEventSvc.show(this.id).subscribe({
      next: (g) => {
        this.gameEvent = g;
      },
      error: (fail) => {
        console.error("error in GameEventDetails Component.load... Here the error: " + fail);
      }
    });
  }

  loadGuests() {
    console.log("in loadGuests");
    console.log(this.gameEvent.guests);
    if (this.gameEvent.guests) {
      this.guestNames = this.gameEvent.guests.map(x => x.username).join(", ");
      console.log(this.guestNames);
    }
  }

}
