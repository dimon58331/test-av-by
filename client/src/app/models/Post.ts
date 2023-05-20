import {User} from "./User";

export interface Post{
  id: number;
  price: number;
  title?: string;
  caption?: string;
  user?: User;
  image?: File;
}
