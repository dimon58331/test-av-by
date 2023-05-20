import {Component, OnInit} from '@angular/core';
import {TransportService} from "../../service/transport.service";
import {TransportBrand} from "../../models/transport/TransportBrand";
import {TransportModel} from "../../models/transport/TransportModel";
import {GenerationTransport} from "../../models/transport/GenerationTransport";

const PAGE = 0;
const SIZE = 25;

@Component({
  selector: 'app-search-filter',
  templateUrl: './search-filter.component.html',
  styleUrls: ['./search-filter.component.css']
})
export class SearchFilterComponent implements OnInit{
  transportBrands: TransportBrand[];
  transportModels: TransportModel[];
  generationsTransport: GenerationTransport[];

  isTransportBrandsLoaded = false;
  isTransportModelsLoaded = false;
  isGenerationsTransportLoaded = false;

  constructor(private transportService: TransportService) {
    // @ts-ignore
    this.transportBrands = null;
    // @ts-ignore
    this.transportModels = null;
    // @ts-ignore
    this.generationsTransport = null;
  }

  makeIsTransportModelsLoadedFalse() {
    this.isTransportModelsLoaded = false;
    // @ts-ignore
    this.transportModels = null;
  }

  makeIsGenerationsTransportLoadedFalse() {
    this.isGenerationsTransportLoaded = false;
    // @ts-ignore
    this.generationsTransport = null;
  }

  findTransportModelsByTransportBrandId(modelId: number){
    this.transportService.getTransportModelsByTransportBrandId(PAGE, SIZE, modelId).subscribe(value => {
      this.transportModels = value.content;
      console.log(this.transportModels);
      this.isTransportModelsLoaded = true;
    })
  }

  findGenerationsTransportByTransportModelId(generationId: number) {
    this.transportService.getGenerationsTransportByTransportModelId(PAGE, SIZE, generationId)
      .subscribe(value => {
        this.generationsTransport = value.content;
        console.log(this.generationsTransport);
        this.isGenerationsTransportLoaded = true;
      }, error => {
        console.log(error);
      })
  }

  ngOnInit(): void {
    this.transportService.getTransportBrands(PAGE, SIZE)
      .subscribe(value => {
        this.transportBrands = value.content;
        this.isTransportBrandsLoaded = true;
        console.log(this.transportBrands);
      })
  }
}
