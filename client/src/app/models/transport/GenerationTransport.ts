import {TransportModel} from "./TransportModel";

export interface GenerationTransport {
  id: number;
  generationName: string;
  startReleaseYear: number;
  endReleaseYear: number;
  image?: File;
  transportModel?: TransportModel;
}
