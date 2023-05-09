import {TransportModel} from "./TransportModel";

export interface Transport {
  id?: number;
  transportModel: TransportModel;
  releaseYear: number;
  engineCapacity: number;
  enginePower: number;
  color: string;
}
