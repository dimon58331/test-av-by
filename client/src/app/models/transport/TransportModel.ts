import {TransportBrand} from "./TransportBrand";

export interface TransportModel {
  id: number;
  modelName: string;
  transportBrand?: TransportBrand;
}
