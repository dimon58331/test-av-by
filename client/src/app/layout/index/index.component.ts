import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {User} from "../../models/User";
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";
import {PageEvent} from "@angular/material/paginator";



@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css'],
})
export class IndexComponent implements OnInit{
  isPostsLoaded = false;
  isUserLoaded = false;
  isSearchByFilteredParameters = false;

  filteredPosts: Post[];
  currentPosts: Post[];
  user: User;

  totalFilteredPostsCount: number;
  currentTotalPostsCount: number;
  page: number = 0;
  size: number = 1;

  currentHttpParameters: Map<string, Array<number>>;
  filteredHttpParameters: Map<string, Array<number>>;


  isPaginationSearch = false;

  constructor(private postService: PostService,
              private userService: UserService,
              private notificationService: NotificationService,
              private imageService: ImageUploadService) {
    // @ts-ignore
    this.user = null;

    // @ts-ignore
    this.currentPosts = null;
    // @ts-ignore
    this.filteredPosts = null;

    // @ts-ignore
    this.totalFilteredPostsCount = null;
    // @ts-ignore
    this.currentTotalPostsCount = null;

    this.filteredHttpParameters = new Map<string, Array<number>>;
    this.currentHttpParameters = new Map<string, Array<number>>;
    console.log("check constructor index.component");
  }

  ngOnInit(): void {
    console.log("check ngOnInit index.component");
    this.loadPostsByParameters(this.currentHttpParameters);
    this.userService.getCurrentUser()
      .subscribe(value => {
        console.log(value);
        this.user = value;
        this.isUserLoaded = true;
      });
  }

  toNextOrPreviousPage(event: PageEvent) {

    this.page = event.pageIndex;
    this.size = event.pageSize;

    console.log('currentHttpParameters');
    console.log(this.currentHttpParameters);
    this.loadPostsByParameters(this.currentHttpParameters);
  }

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
    if (this.isSearchByFilteredParameters) {
      httpParameters.set("page", [0]);
      httpParameters.set("size", [1]);

      this.filteredHttpParameters = httpParameters;
    } else {
      httpParameters.set("page", [this.page]);
      httpParameters.set("size", [this.size]);
    }

    this.postService.getAllPostsByParameters(httpParameters)
      .subscribe(value => {
        if (this.isSearchByFilteredParameters) {
          this.filteredPosts = value.content;
          this.totalFilteredPostsCount = value.totalElements;

          this.isSearchByFilteredParameters = false;
        } else {
          console.log(value);
          this.currentPosts = value.content;
          this.currentTotalPostsCount = value.totalElements;
          // this.totalFilteredPostsCount = value.totalElements;

          this.isPostsLoaded = true;

          this.getImagesToPosts(this.currentPosts);
        }

      }, error => {
        console.log(error);
        if (this.isSearchByFilteredParameters) {
          // @ts-ignore
          this.filteredPosts = null;
          this.totalFilteredPostsCount = 0;

          this.isSearchByFilteredParameters = false;
        } else {
          // @ts-ignore
          this.currentPosts = null;
          this.currentTotalPostsCount = 0;
        }
      });
  }

  loadFilteredPostsToPosts() {
    console.log("loadFilteredPostsToPosts");

    this.currentPosts = this.filteredPosts;
    if (this.currentPosts) {
      this.getImagesToPosts(this.currentPosts);
    }

    this.currentTotalPostsCount = this.totalFilteredPostsCount;
    this.currentHttpParameters = this.filteredHttpParameters;
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
