import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {User} from "../../models/User";
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit{
  isPostsLoaded = false;
  isUserLoaded = false;
  filteredPosts :Post[];
  posts: Post[];
  user: User;
  constructor(private postService: PostService,
              private userService: UserService,
              private notificationService: NotificationService,
              private imageService: ImageUploadService) {
    // @ts-ignore
    this.user = null;
    // @ts-ignore
    this.posts = null;
    // @ts-ignore
    this.filteredPosts = null;
    console.log("check constructor index.component");
  }

  ngOnInit(): void {
    console.log("check ngOnInit index.component");
    this.loadAllPosts();
    this.userService.getCurrentUser()
      .subscribe(value => {
        console.log(value);
        this.user = value;
        this.isUserLoaded = true;
      });
  }
  //
  getImagesToPosts(posts: Post[]): void {
    posts.forEach(post => {
      // @ts-ignore
      this.imageService.getPostImage(post.id)
        .subscribe(value => {
          console.log(value);
          post.image = value.imageBytes;
        }, error => {
          console.log(error);
        });
    });
  }

  loadPostsByParameters(httpParameters: Map<string, Array<number>>){
    // @ts-ignore
    this.filteredPosts = null;
    console.log("before do get request")
    console.log(httpParameters);
    this.postService.getAllPostsByParameters(httpParameters)
      .subscribe(value => {
        console.log("loadPostsByParameters success");
        this.filteredPosts = value.content;
        console.log(this.filteredPosts);
        console.log("httpParameters");
        console.log(httpParameters);
      }, error => {
        console.log("loadPostsByParameters error");
        console.log(error);
        console.log(this.filteredPosts);
        console.log("httpParameters");
        console.log(httpParameters);
        // @ts-ignore
        this.filteredPosts = null;
      });
  }

  loadAllPosts() {
    this.postService.getAllPosts(0, 25)
      .subscribe(value => {
          this.posts = value.content;
          this.filteredPosts = value.content;
          console.log(this.posts);
          this.getImagesToPosts(this.posts);
          this.isPostsLoaded = true;
        }, error => {
          console.error(error);
        }
      );
  }

  loadFilteredPostsToPosts() {
    this.posts = this.filteredPosts;
    if (this.posts) {
      this.getImagesToPosts(this.posts);
    }
  }

  //
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
  //
  //
  formatImage(img: any): any {
    if (img == null){
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }
}
