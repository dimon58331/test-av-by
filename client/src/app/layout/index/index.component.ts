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
  filteredPosts: Post[];
  posts: Post[];
  user: User;
  totalPostsCount: number;
  totalPostsCountForPagination: number;
  page: number = 0;
  size: number = 1;
  httpParameters: Map<string, Array<number>>
  isPaginationSearch = false;

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
    // @ts-ignore
    this.totalPostsCount = null;
    // @ts-ignore
    this.totalPostsCountForPagination = null;
    this.httpParameters = new Map<string, Array<number>>;
    console.log("check constructor index.component");
  }

  ngOnInit(): void {
    console.log("check ngOnInit index.component");
    this.loadAllPosts(this.page, this.size);
    this.userService.getCurrentUser()
      .subscribe(value => {
        console.log(value);
        this.user = value;
        this.isUserLoaded = true;
      });
  }

  setPageAndSize(event: PageEvent) {
    console.log(event.pageIndex + ', pageIndex');
    console.log(event.pageSize + ', pageSize');
    this.page = event.pageIndex;
    this.size = event.pageSize;
    this.isPaginationSearch = true;
    this.loadPostsByParameters(this.httpParameters);
    //this.loadAllPosts(this.page, this.size);
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
    // @ts-ignore
    this.filteredPosts = null;
    console.log("before set page and size")
    console.log(httpParameters);

    httpParameters.set("page", [this.page]);
    httpParameters.set("size", [this.size]);

    this.httpParameters = httpParameters;

    console.log("before do get request")
    console.log(httpParameters);
    this.postService.getAllPostsByParameters(httpParameters)
      .subscribe(value => {
        console.log("loadPostsByParameters success");
        console.log(value);
        this.filteredPosts = value.content;
        this.totalPostsCount = value.totalElements;
        console.log(this.filteredPosts);
        console.log("httpParameters");
        console.log(httpParameters);
        if (this.isPaginationSearch) {
          this.loadFilteredPostsToPosts();
          this.isPaginationSearch = false;
        }
      }, error => {
        console.log("loadPostsByParameters error");
        console.log(error);
        console.log(this.filteredPosts);
        console.log("httpParameters");
        console.log(httpParameters);
        // @ts-ignore
        this.filteredPosts = null;
        this.totalPostsCount = 0;
      });
  }

  loadAllPosts(page: number, size: number) {
    this.postService.getAllPosts(page, size)
      .subscribe(value => {
        console.log("Value: ");
        console.log(value);
        this.totalPostsCount = value.totalElements;
        this.totalPostsCountForPagination = value.totalElements;
        this.posts = value.content;
        this.filteredPosts = value.content;
        console.log(this.posts);
        this.getImagesToPosts(this.posts);
        this.isPostsLoaded = true;
        }, error => {
        console.error(error);
      });
  }

  loadFilteredPostsToPosts() {
    console.log("loadFilteredPostsToPosts");
    this.posts = this.filteredPosts;
    if (this.posts) {
      this.getImagesToPosts(this.posts);
    }
    this.totalPostsCountForPagination = this.totalPostsCount;
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
