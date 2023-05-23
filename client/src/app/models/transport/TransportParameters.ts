import {GenerationTransport} from "./GenerationTransport";

export interface TransportParameters {
  id: number;
  bodyType: string;
  transmissionType: string;
  typeEngine: string;
  enginePower: number;
  releaseYear: number;
  generationTransport?: GenerationTransport;
}
