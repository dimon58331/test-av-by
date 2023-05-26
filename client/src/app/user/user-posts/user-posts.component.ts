import { Component } from '@angular/core';
import {PostService} from "../../service/post.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";
import {UserService} from "../../service/user.service";
import {Post} from "../../models/Post";
import {User} from "../../models/User";
import {IndexComponent} from "../../layout/index/index.component";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-user-posts',
  templateUrl: './user-posts.component.html',
  styleUrls: ['./user-posts.component.css']
})
export class UserPostsComponent {
  //@ts-ignore
  posts: Post[];
  //@ts-ignore
  user: User;
  isPostsLoaded = false;
  isUserLoaded = false;
  httpParameters: Map<string, Array<number>>;
  size: number = 1;
  page: number = 0;
  totalPostsCount: number = 0;
  constructor(private postService: PostService,
              private notificationService: NotificationService,
              private imageService: ImageUploadService,
              private userService: UserService) {
    this.httpParameters = new Map<string, Array<number>>();
    this.getPostsForCurrentUser();

    this.userService.getCurrentUser()
      .subscribe(currentUser => {
        this.user = currentUser;
        this.isUserLoaded = true;
        console.log(this.user);
      });
  }

  getPostsForCurrentUser() {
    this.httpParameters.set('page', [this.page]).set('size', [this.size]);
    this.postService.getPostsForCurrentPerson(this.httpParameters)
      .subscribe(value => {
        this.posts = value.content;
        this.totalPostsCount = value.totalElements;

        this.getImagesToPosts(this.posts);
        this.isPostsLoaded = true;
        console.log(this.posts);
      });
  }

  getImagesToPosts(posts: Post[]): void {
    posts.forEach(post => {
      // @ts-ignore
      this.imageService.getPostImage(post.id)
        .subscribe(value => {
          post.image = value.imageBytes;
        })
    });
  }

  toNextOrPreviousPage(event: PageEvent) {

    this.page = event.pageIndex;
    this.size = event.pageSize;

    this.getPostsForCurrentUser();
  }

  formatImage(img: any): any {
    if (img == null){
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  // likePost(postId: number | undefined, postIndex: number): void {
  //   const post = this.posts[postIndex];
  //   console.log(post);
  //
  //   if (!post.likedUsers?.includes(this.user.username)){
  //     // @ts-ignore
  //     this.postService.likePost(postId, this.user.username)
  //       .subscribe(() => {
  //         post.likedUsers?.push(this.user.username);
  //         this.notificationService.showSnackBar("Liked!");
  //       });
  //   } else {
  //     // @ts-ignore
  //     this.postService.likePost(postId, this.user.username)
  //       .subscribe(() => {
  //         const index = post.likedUsers?.indexOf(this.user.username, 0);
  //         // @ts-ignore
  //         if (index > -1){
  //           // @ts-ignore
  //           post.likedUsers?.splice(index, 1);
  //         }
  //       });
  //   }
  // }
}
