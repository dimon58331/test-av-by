import {Component, OnInit} from '@angular/core';
import {TokenStorageService} from "../../service/token-storage.service";
import {PostService} from "../../service/post.service";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";
import {UserService} from "../../service/user.service";
import {User} from "../../models/User";
import {Post} from "../../models/Post";
import {AddPostComponent} from "../add-post/add-post.component";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{
  isUserDataLoaded = false;
  // @ts-ignore
  user: User;
  // @ts-ignore
  post: Post = {};
  // @ts-ignore
  selectedFile: File;
  previewImgURL: any;

  constructor(private tokenService: TokenStorageService,
              private postService: PostService,
              private dialog: MatDialog,
              private notificationService: NotificationService,
              private imageService: ImageUploadService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getCurrentUser()
      .subscribe(data=>{
        this.user = data;
        this.isUserDataLoaded = true;
      });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(this.selectedFile);
    reader.onload = () => {
      this.previewImgURL = reader.result;
    };
  }

  // openEditDialog(): void {
  //   const dialogUserEditConfig = new MatDialogConfig();
  //   dialogUserEditConfig.width = '500px';
  //   dialogUserEditConfig.data = {
  //     user: this.user
  //   }
  //   this.dialog.open(EditUserComponent, dialogUserEditConfig);
  // }
  //
  openAddPostDialog(): void {
    const dialogCreatePostConfig = new MatDialogConfig();
    dialogCreatePostConfig.width = '500px';
    dialogCreatePostConfig.data = {
      post: this.post
    }
    this.dialog.open(AddPostComponent, dialogCreatePostConfig);
  }

  formatImage(img: any): any {
    if (img == null){
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  // onUpload(): void {
  //   if (this.selectedFile != null){
  //     this.imageService.uploadImageToPerson(this.selectedFile)
  //       .subscribe(()=>{
  //         sessionStorage.setItem('reloadAfterPageLoad', 'true');
  //         sessionStorage.setItem('notification-message', 'Profile image updated successfully!');
  //         window.location.reload();
  //       });
  //   }
  // }
}
