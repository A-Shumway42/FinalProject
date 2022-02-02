import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Gameevent } from 'src/app/models/gameevent';
import { User } from 'src/app/models/user';
import { Comment } from 'src/app/models/comment';
import { AuthService } from 'src/app/services/auth.service';
import { GameeventService } from 'src/app/services/gameevent.service';
import { UserService } from 'src/app/services/user.service';
import { CommentService } from 'src/app/services/comment.service';
import { Address } from 'src/app/models/address';

@Component({
  selector: 'app-gameeventdetails',
  templateUrl: './gameeventdetails.component.html',
  styleUrls: ['./gameeventdetails.component.css'],
})
export class GameeventdetailsComponent implements OnInit {
  gameEvent: Gameevent = new Gameevent();
  id: number = 0;
  guestNames = '';
  tags = '';
  loggedInUser: User = new User();
  isLoggedIn = false;
  games = '';
  newComment: Comment = new Comment();
  isHost = false;
  alreadyJoined = false;
  beginEdit = false;
  editGameEvent: Gameevent = new Gameevent();
  userAddressess: Address[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private gameEventSvc: GameeventService,
    private auth: AuthService,
    private userSvc: UserService,
    private commentSvc: CommentService
  ) {}

  ngOnInit(): void {
    let idStr = this.route.snapshot.paramMap.get('eventId');
    if (idStr) {
      this.id = Number.parseInt(idStr);
    }
    this.load();
    this.loadUser();
  }

  load() {
    this.gameEventSvc.show(this.id).subscribe({
      next: (g) => {
        this.gameEvent = g;
        if (!this.gameEvent.enabled) {
          this.router.navigateByUrl('/oof');
        }
        this.loadTags();
        this.loadGames();
        this.loadGuests();
      },
      error: (fail) => {
        console.error(
          'error in GameEventDetails Component.load... Here the error: ' + fail
        );
      },
    });
  }

  checkJoined() {
    if (this.gameEvent.guests) {
      for (const g of this.gameEvent.guests) {
        if (g.id === this.loggedInUser.id) {
          this.alreadyJoined = true;
        }
      }
    }
  }

  loadGuests() {
    this.guestNames = '';
    if (this.gameEvent.guests) {
      this.guestNames = this.gameEvent.guests.map((x) => x.username).join(', ');
    }
  }

  loadTags() {
    this.tags = '';
    if (this.gameEvent.eventTags) {
      this.tags = this.gameEvent.eventTags.map((x) => x.name).join(', ');
    }
  }

  loadGames() {
    this.games = '';
    if (this.gameEvent.games) {
      this.games = this.gameEvent.games.map((x) => x.name).join(', ');
    }
  }

  loadUser() {
    let id = this.auth.getCurrentUserId();
    if (id > 0) {
      this.userSvc.show(id).subscribe({
        next: (u) => {
          this.loggedInUser = u;
          this.checkLogin();
        },
        error: (fail) => {
          console.error(
            'error in game event details component... loadUser(): ' + fail
          );
        },
      });
    }
  }

  checkLogin() {
    this.isLoggedIn = this.auth.checkLogin();
    this.checkHost();
    this.checkJoined();
  }

  createComment(c: Comment) {
    if (this.gameEvent.id) {
      this.commentSvc.create(c, this.gameEvent.id).subscribe({
        next: () => {
          this.load();
          this.newComment = new Comment();
        },
        error: (f) => {
          console.error('error adding comment to game event: ' + f);
        },
      });
    }
  }

  checkHost() {
    if (this.loggedInUser.id === this.gameEvent.host.id) {
      this.isHost = true;
    } else this.isHost = false;
  }

  joinEvent(gId: number, uId: number) {
    this.gameEventSvc.joinGameEvent(gId, uId).subscribe({
      next: (g) => {
        this.gameEvent = g;
        this.load();
        this.alreadyJoined = true;
      },
      error: (f) => {
        console.error('error joining game event' + f);
      },
    });
  }

  leaveEvent(gId: number, uId: number) {
    this.gameEventSvc.leaveGameEvent(gId, uId).subscribe({
      next: (g) => {
        this.gameEvent = g;
        this.alreadyJoined = false;
        this.load();
      },
      error: (f) => {
        console.error('error joining game event' + f);
      },
    });
  }

  deleteComment(comment: Comment) {
    if (comment.id && this.gameEvent.id){
    this.commentSvc.destroy(comment.id, this.gameEvent.id).subscribe({
      next: () => {
        this.load();
      },
      error: (f) => {
        console.error('error deleting comment' + f);
      }
    })
  }
}

updateGameEvent(event: Gameevent) {
  this.gameEventSvc.update(event, this.id).subscribe({
    next: (g) => {
      this.gameEvent = g;
      this.load();
      this.editGameEvent = new Gameevent();
    },
    error: (f) => {
      console.error('error updating game: ' + f)
    }
  });
}

loadUpdateEvent() {
  this.editGameEvent = Object.assign({}, this.gameEvent);
}

deleteEvent(id: number) {
  this.gameEventSvc.destroy(id);
  this.router.navigateByUrl('/profile/' + this.loggedInUser.id)
}

}
