import { Component } from '@angular/core';
import {NotificationService} from "./service/notification.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Test-av-by';
  constructor(private notificationService: NotificationService) {
    if (window.sessionStorage.getItem('reloadAfterPageLoad') === 'true'){
      if (window.sessionStorage.getItem('notification-message')){
        let message = window.sessionStorage.getItem('notification-message');
        // @ts-ignore
        this.notificationService.showSnackBar(message);
        window.sessionStorage.removeItem('notification-message');
      }
      window.sessionStorage.setItem('reloadAfterPageLoad', 'false');
    }
  }
}
