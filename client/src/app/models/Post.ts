import {User} from "./User";
import {TransportParameters} from "./transport/TransportParameters";

export interface Post{
  id: number;
  price: number;
  title?: string;
  caption?: string;
  user?: User;
  transportParameters?: TransportParameters;
  image?: File;
}
